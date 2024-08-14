package org.onetwo.boot.module.poi;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.onetwo.boot.module.poi.annotation.ExcelEntity;
import org.onetwo.boot.module.poi.annotation.ExcelExportable;
import org.onetwo.common.date.NiceDate;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.reflect.ClassIntroManager;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseType;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.common.web.utils.WebHolder;
import org.onetwo.ext.poi.excel.generator.ExcelGenerators;
import org.onetwo.ext.poi.excel.generator.FieldModel;
import org.onetwo.ext.poi.excel.generator.PoiExcelGenerator;
import org.onetwo.ext.poi.excel.generator.RowModel;
import org.onetwo.ext.poi.excel.generator.TemplateModel;
import org.onetwo.ext.poi.excel.generator.TemplateRowTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.google.common.collect.Maps;

public class PoiExcelHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

	@Autowired
	private PoiProperties poiProperties;
    private String dataSourceName = "datas";
	
	@PostConstruct
	public void init() {
		MediaType meidaType = MediaType.valueOf(poiProperties.getContentType());
		setSupportedMediaTypes(Arrays.asList(meidaType));
	}

	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		throw new BaseException("not support read");
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		HttpServletRequest request = WebHolder.getRequest().orElse(null);
		if (request!=null) {
			ResponseType type =RequestUtils.getResponseType(request);
			if (type!=ResponseType.JFXLS) {
				return false;
			}
		}
		Type componentType = ReflectUtils.getGenricType(clazz, 0, null);
		if (componentType==null) {
			return false;
		}
		Intro<?> intro = ClassIntroManager.getInstance().getIntro((Class<?>)componentType);
		ExcelEntity excelData = intro.getAnnotationWithSupers(ExcelEntity.class);
		return excelData!=null;
	}

	private Intro<?> getAndCheckComponentType(Type type) {
		Type componentType = ReflectUtils.getGenricType(type, 0, null);
		if (componentType==null) {
			throw new BaseException("componentType not found!");
		}
		Intro<?> intro = ClassIntroManager.getInstance().getIntro((Class<?>)componentType);
		return intro;
	}
	
	private List<ExcelExportableData> getExcelExportableDatas(Intro<?> intro) {
		List<ExcelExportableData> exportableFields = intro.getFieldsByAnnotation(ExcelExportable.class)
														  .stream().map(field -> {
															  ExcelExportable fieldAnno = field.getAnnotation(ExcelExportable.class);
															  ExcelExportableData fieldData = new ExcelExportableData();
															  fieldData.setName(field.getName());
															  fieldData.setSort(fieldAnno.sort());
															  fieldData.setLabel(fieldAnno.label());
															  return fieldData;
														  }).collect(Collectors.toList());
		List<ExcelExportableData> exportableProps = intro.getPropertyDescriptorsByAnnotation(ExcelExportable.class)
														.stream().map(prop -> {
															  ExcelExportable fieldAnno = prop.getReadMethod().getAnnotation(ExcelExportable.class);
															  ExcelExportableData fieldData = new ExcelExportableData();
															  fieldData.setName(prop.getName());
															  fieldData.setSort(fieldAnno.sort());
															  fieldData.setLabel(fieldAnno.label());
															  return fieldData;
														}).collect(Collectors.toList());
		if (!exportableProps.isEmpty()) {
			exportableFields.addAll(exportableProps);
		}
		return exportableFields;
	}
	@Override
	protected void writeInternal(Object returnValue, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		Intro<?> intro = getAndCheckComponentType(type);
		ExcelEntity excelData = intro.getAnnotationWithSupers(ExcelEntity.class);
		if (excelData==null) {
			throw new ServiceException("can not found component type with @ExcelData");
		}
		
		List<ExcelExportableData> exportableFields = getExcelExportableDatas(intro);
		if (exportableFields.isEmpty()) {
			throw new ServiceException("@ExcelField field not found!");
		}
		
		exportableFields.sort(Comparator.comparingInt(e -> e.getSort()));
		Map<String, Object> context = Maps.newHashMap();
		TemplateModel tempalte = createTemplateModel(excelData, exportableFields);
		List<?> dataList = null;
		if (returnValue instanceof List) {
			dataList = (List<?>) returnValue;
		} else if (returnValue instanceof Page) {
			Page<?> page = (Page<?>) returnValue;
			dataList = page.getResult();
		} else {
			throw new ServiceException("not support type: " + type);
		}
		if (LangUtils.isEmpty(dataList)) {
			throw new ServiceException("export data can not be empty!");
		}
		if (dataList.size()>poiProperties.getMaxCountLimit()) {
			throw new ServiceException("export data is too big, count: " + dataList.size());
		}
		context.put(dataSourceName, dataList);
		

		HttpServletResponse response = WebHolder.getResponse().orElse(null);
		if (response!=null) {
			ResponseUtils.configDownloadHeaders(response, excelData.name() + "-" + NiceDate.New().formatAsDateTime() + ".xlsx");
		}
		
		PoiExcelGenerator generator = ExcelGenerators.createExcelGenerator(tempalte, context);
		OutputStream out = null;
		try {
			out = outputMessage.getBody();
			generator.write(out);
		} catch (Exception e) {
			throw new ServiceException("导出excel失败：" + e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		throw new BaseException("not support read");
	}

	private TemplateModel createTemplateModel(ExcelEntity excelData, List<ExcelExportableData> exportableFields) {
		TemplateModel template = new TemplateModel();
		template.setName(excelData.name());
		
		RowModel iteratorRow = new RowModel();
		iteratorRow.setRowType(TemplateRowTypes.ITERATOR);
		iteratorRow.setRenderHeader(true);
		iteratorRow.setFieldHeaderStyle("alignment:ALIGN_CENTER;verticalAlignment:VERTICAL_CENTER;");
		iteratorRow.setFieldHeaderFont("boldweight:BOLDWEIGHT_BOLD;fontHeightInPoints:13");
		iteratorRow.setFieldFont("boldweight:BOLDWEIGHT_NORMAL;");
		iteratorRow.setDatasource("#"+dataSourceName);
		iteratorRow.setName("element");
		exportableFields.forEach(field -> {
			FieldModel itField = new FieldModel();
			itField.setLabel(field.getLabel());
			itField.setName(field.getName());
			iteratorRow.addField(itField);
		});
		template.addRow(iteratorRow);
		return template;
	}

}
