# onetwo-poi
基于poi，对操作excel的简单封装。

## maven ##
```xml

<dependency>
    <groupId>org.onetwo4j</groupId>
    <artifactId>onetwo-poi</artifactId>
    <version>4.8.0-SNAPSHOT</version>
</dependency>   

```

## 使用 ##
### 定义xml模板
excel_template.xml:   
```xml

<?xml version="1.0" encoding="UTF-8"?>
<template name="用户卡列表" columnWidth="2:30">
	<rows>
		<row span="2" fieldStyle="alignment:ALIGN_CENTER;verticalAlignment:VERTICAL_CENTER;" fieldFont="boldweight:BOLDWEIGHT_BOLD">
			<fields>
				<field value="'报名客户资料'" rowspan="2" colspan="4"/>
			</fields>
		</row>
		<row renderHeader="true" 
		      fieldHeaderStyle="alignment:ALIGN_CENTER;verticalAlignment:VERTICAL_CENTER;" fieldHeaderFont="boldweight:BOLDWEIGHT_BOLD"
		      name="element" type="iterator" datasource="#cardList" fieldFont="boldweight:BOLDWEIGHT_NORMAL;color:COLOR_RED"> 
			<fields>
				<field label="主键" name="id" dataType="java.lang.String"/>
				<field label="卡号" name="cardNo"/>
				<field label="卡密码" name="cardPwd" />
			</fields>
		</row>
	</rows>
</template>

```
- id如果是一个比较大的长整型数字的时候，导出excel的时候会变成科学记数的形式数字，这时可以用dataType指定为string类型，这个单元格就会设为string类型

### java代码

创建Java类Card：   

```Java

public class CardEntity {
	
	protected Long id;
	protected String cardNo;
	protected String cardPwd;
	
	//...getter and setter
}

```

使用TemplateGenerator生成excel：   
```Java

        
		List<CardEntity> cardList = LangOps.generateList(10, i->{
			CardEntity card = new CardEntity();
			card.setId(Long.valueOf(i));
			card.setCardNo("card_no_"+i);
			card.setCardPwd("password"+i);
			card.setStartTime(new Date());
			return card;
		});
		Map<String, Object> context = new HashMap<>();
		context.put("cardList", cardList);
		TemplateGenerator g = ExcelGenerators.createExcelGenerator("c:/excel_template.xml", context);
		String path = "c:/excel_generated.xls";
		g.write(path);

```

### 其它特性
[url链接](https://github.com/wayshall/onetwo/issues/74)
[动态列支持](https://github.com/wayshall/onetwo/issues/78)

### 与spring mvc集成

[zifish](https://github.com/wayshall/onetwo) 提供了一个与spring mvc的集成实现，可通过简单的配置即可启用集成

```yaml
jfish.poi.exportView.enabled = true # 默认即为true
```

启用后：

1、编写xml模板放到项目的 META-INF/resources/excel-view/ 目录，比如excel_template.xml

​		模板编写参考[定义xml模板](#定义xml模板)

2、编写普通的spring mvc controller，并返回ModelAndView对象，其中ModelAndView的view路径为: excel_template，并把需要在template里用到的数据put到model里，大概代码如下：

```java
@RequestMapping(path="export", method=RequestMethod.GET)
public ModelAndView export(){ 
  List<CardData> cardList = userService.findList();

  return pluginMv("excel_template", 
                  "cardList", cardList,
                  // fileName 为导出的文件名称
                  "fileName", StringUtils.defaultValue(fileName, "人员列表"));
}
```

3、直接访问controller的url地址并加上jfxls后缀即可导出文件，如：http://localhost:8080/export.jfxls



### 读取excel为Java对象

excel模板如下：   
![excel_test.jpg](doc/image/excel_test.jpg)

用poi模块读取此模板的数据为Java对象代码如下：

```Java
String path = "excel_template_path.xls";
List<CardEntity> cardList = WorkbookReaderFactory.createWorkbookReader(
                                            CardEntity.class, //每一行excel数据映射的对象
                                            1, //数据行的索引，若第一行为标题行，第二行开始为数据行，则此参数为1 
											"主键", //excel标题列名
											"id", //对应的JavaBean(此处为CardEntity)的属性名,下面的参数如此类推
											"卡号", "cardNo", 
											"密码", "cardPwd")
											.readFirstSheet(path); // 读取第一个excel表格
		
```

### 流式读取api


4.7.3 后增加了流式api读取excel

```Java
@Data
public class ImportBatchVO {
    String title;
    List<DetailImportData> dataList;
}


ImportBatchVO batch = new ImportBatchVO();
WorkbookReaderFactory.streamReader()
		.readSheet(0) //读取第一个表格
			//读取第1行作为标题
			.row(0).onData((row, index) -> {
				batch.setTitle(row.getString(1));
			})
			////读取第2行到结束
			.row(1).toEnd().onData((row, index) -> {
                 if (StringUtils.isBlank(row.getString(0))) {
                     // 如果该行的第一列为空，则视为空行，忽略处理
                     return ;
                 }
				DetailImportData detail = new DetailImportData();
				detail.setRealName(row.getString(0));
				detail.setUserName(row.getString(1));
				detail.setFee(row.getCellValue(2, BigDecimal.class));
				batch.dataList.add(detail);
			})
		.endSheet()
		.from(dataFile);//从哪个数据文件读取
```

## 直接使用excel文档作为模版生成新的excel文件

## 使用模板生成新excel
可以使用excel文档作为模板，生成新的excel文件。
excel文档里需要替换的地方，使用${varName}表达式替换即可。
比如：
```Java
    String templateName = "D:/excel-template.xlsx";
    String generatedPath = "D:/excel-template-generated.xlsx";
    
    ExcelTemplateEngineer g = new DefaultExcelTemplateEngineer();
    g.generate(templateName, generatedPath, new ETemplateContext(){
        {
            put("year", TheFunction.getInstance().formatDateByPattern("yyyy", new Date()));
            put("now", NiceDate.Now());
            put("datalist", list);
            put("lineCount", 30);
            put("busCount", 300);
            put("totalLabel", "合计");
        }
    });
```

### 模板+数据文件批量生成excel
某些场景下，需要使用一个数据文件的每一行数据作为模板的内容替换生成一个excel，此时可以使用批量生成
```Java
SimpleBatchExceGenerator g = SimpleBatchExceGenerator.create()
                                                    .templateFilePath("模板文件")
                                                    .dataFilePath("数据文件")
                                                    .dataFileTitleRowIndex(1) // 标题行，标题用于模板替换时作为变量名，默认为0，即第一行
                                                    .dataFileDataStartRowIndex(3) // 数据开始行，默认为1，即第二行
                                                    .outDirPath(outDir) // 输出目录
                                                    .dataFileSheetIndex(5) // 读取excel的文档的第几个sheet作为数据文档，默认为0，即第一个
                                                    .keyName("槽段编号"); // 主键列，用于判断数据行是否为空行，也可以为空
g.generate(3);
```














