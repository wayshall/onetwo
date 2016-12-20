# onetw-poi
基于poi，对操作excel的简单封装。

## 使用 ##
    
        String templatePath = "c:/excel_template.xml";
        Map<String, Object> context = new HashMap<>();
        TemplateGenerator g = ExcelGenerators.createExcelGenerator(templatePath, context);
        g.write("c:/excel_generated.xls");
## xml 模板语法 ##
    <?xml version="1.0" encoding="UTF-8"?>
    <template name="报名客户资料" autoSizeColumn="true" >
    <rows>
        <row span="2">
            <fields>
                <field value="'报名客户资料报表'" colspan="4" rowspan="2" style="alignment:ALIGN_CENTER" font="boldweight:BOLDWEIGHT_BOLD"/>
            </fields>
        </row>
        <row name="element" type="iterator" datasource="#data" renderHeader="true" fieldHeaderFont="boldweight:BOLDWEIGHT_BOLD;color:COLOR_RED"> 
            <fields>
                <field name="id" label="主键"/>
                <field name="cardNo" label="卡号" />
                <field name="cardPwd" label="密码" />
                <field name="#f.formatDate(startTime, 'yy:MM:dd')" label="开始时间"/>
            </fields>
        </row>
    </rows>
    </template>
