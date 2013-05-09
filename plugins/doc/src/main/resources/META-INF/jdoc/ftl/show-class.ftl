<@extends>
	
	<@override name="main-content">
	
	<#if classDoc??>
        
          <div class="hero-unit">
            <h2>${classDoc.fullName} </h2>
            	<br/>文档
          </div>
          
         <div>
         	<h2>接口地址</h2>
         	<p>${classDoc.description}</p>
         	
         	
         	<h2>字段</h2>
         	<p>
         		<table class="table table-bordered table-striped">
         			<thead>
         			<tr>
         				<td>字段名</td>
         				<td>必填</td>
         				<td>类型</td>
         				<td>说明</td>
         			</tr>
         			</thead>
         			<tbody>
         		
        <#list classDoc.fields as field>
         			<tr>
         				<td>${field.name}</td>
         				<td>${field.required?string("true", "false")}</td>
         				<td>
         				<#if field.typeLink>
         					<a href="${pluginConfig.baseURL}/class?name=${field.typeName}">
         				</#if>
         				${field.typeName}
         				<#if field.typeLink>
         					</a>
         				</#if>
         				</td>
         				<td>${field.description}</td>
         			</tr>
         </#list>
         			</tbody>
         		
         		</table>
         	</p>
         	
         </div><!--/span-->
    <#else>
    	没有找到该类[${name}]的文档
    </#if>
	</@override>
</@extends>