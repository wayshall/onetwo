<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.propertyName}"/>
<#assign modulePath="/${_globalConfig.getModuleName()}/${_tableContext.propertyName}"/>
<#assign pagePath="${_tableContext.tableNameWithoutPrefix?replace('_', '-')?lower_case}"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/pages/common/taglibs.jsp" %>


<layout:extends parentPage="/WEB-INF/layout/application.jsp">
    <layout:override name="title">
        ${(table.comments[0])!''}编辑
    </layout:override>

    <layout:override name="body">
        
         <div class="portlet light" id="sample_1_wrapper">

        <form:form modelAttribute="${_tableContext.propertyName}" method="put" action="${"$"}{basePath}/manager${requestPath}/${"$"}{${_tableContext.propertyName}.${table.primaryKey.javaName}}" cssClass="form-horizontal">
            <table>
            
                <%@ include file="${pagePath}-edit-form.jsp" %>
            
            </table>
            
            <div style="padding-left:100px;" class="form-actions">
                <div class="row">
                    <div class="col-md-offset-3 col-md-9">
                        <button id="submit" class="btn blue" type="submit">提交</button>
                        <button class="btn yellow" id="close" type="button" onclick="history.back();">返回</button>
                    </div>
                </div>
            </div>
         </form:form>
        
        </div>
        
    </layout:override>
</layout:extends>

