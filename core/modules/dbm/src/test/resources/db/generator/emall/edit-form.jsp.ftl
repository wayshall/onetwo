<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.propertyName}"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/pages/common/taglibs.jsp" %>

    <#list table.columns as column>
        <#if !column.primaryKey>
            <tr>
                <td class="titletd" >
                    <#if !column.nullable><span class="required" style="color:red;" aria-required="true"> * </span></#if>
                    ${(column.comments[0])!''}：
                </td>
                <td class="contd">
                    <form:input path="${column.javaName}" cssClass="form-control required" placeholder="${(column.comments[0])!''}"/>
                    <form:errors path="${column.javaName}" cssClass="label label-danger"/>
                </td>
            </tr>
       </#if>
    </#list>