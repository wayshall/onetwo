<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.propertyName}"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/pages/common/taglibs.jsp" %>



<layout:extends parentPage="/WEB-INF/layout/application.jsp">
    <layout:override name="title">
                    用户列表
    </layout:override>

    <layout:override name="body">
        <div class="row">
            <div class="col-md-2 col-xs-2 ">
                 <a oid="" href="${"$"}{basePath}/manager${requestPath}/new" class="btn btn-sm green-meadow contentAdd" ><i class="fa fa-plus"></i> 新增</a>
            </div>
        </div>
        
        <div class="table-container">
            <table class="table table-striped table-bordered table-hover dataTable no-footer" id="sample_1" role="grid" aria-describedby="sample_1_info">
                <thead>
                    <tr role="row">
                        <#list table.columns as column>
                            <#if !column.primaryKey>
                            <th>${(column.comments[0])!column.javaName}</th>
                            </#if>
                        </#list>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${"$"}{page.result}" varStatus="i">
                        <tr class="${"$"}{i.index%2==0?'even':'odd' }" role="row">
                            <#list table.columns as column>
                                <#if !column.primaryKey>
                                <th>${"$"}{item.${column.javaName}}</th>
                                </#if>
                            </#list>

                            <td>
                            <a  class="btn green-sharp btn-large contentAdd" href="${"$"}{basePath}/manager/${requestPath}/${"$"}{item.${table.primaryKey.javaName}}/edit">编辑</a>
                            <button class="btn green-sharp btn-large" data-toggle="confirmation" data-original-title="确定删除?"
                                    data-on-confirm="deleteConfirm" data-id="${"$"}{item.${table.primaryKey.javaName}}">删除</button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${"$"}{fn:length(page.result) == 0 }">
                        <tr>
                            <td align="center" colspan="17">没有数据</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
        <sys:pagination page="${"$"}{page}"/>
        
        
    </layout:override>
</layout:extends>
<script>
    function deleteConfirm(){
        var id = $(this).attr('data-id');
        bootHelper.deleteItem({
            url: '${"$"}{basePath}/manager/${requestPath}',
            data: {
                ids: id
            },
            removeEl: $(this).parents('tr')
        });
    }
</script>
