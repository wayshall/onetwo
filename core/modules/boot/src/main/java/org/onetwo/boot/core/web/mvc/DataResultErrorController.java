package org.onetwo.boot.core.web.mvc;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.common.utils.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 把spring默认的错误格式转为jfish约定的格式
 * @author wayshall
 * <br/>
 */
//@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class DataResultErrorController extends BasicErrorController {
	
	public DataResultErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
		super(errorAttributes, errorProperties);
	}

	public DataResultErrorController(ErrorAttributes errorAttributes,
			ErrorProperties errorProperties,
			List<ErrorViewResolver> errorViewResolvers) {
		super(errorAttributes, errorProperties, errorViewResolvers);
	}

	@RequestMapping
	@ResponseBody
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
//		Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
		Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
		HttpStatus status = getStatus(request);
		SimpleDataResult<?> dr = DataResults.error(StringUtils.emptyIfNull(body.get("message")))
											.code(StringUtils.emptyIfNull(body.get("error")))
											.data(StringUtils.emptyIfNull(body.get("path")))
											.build();
		return new ResponseEntity<Map<String, Object>>(ReflectUtils.toMap(dr), status);
	}
}
