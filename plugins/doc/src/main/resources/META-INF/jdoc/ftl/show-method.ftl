<@extends>
	
	<@override name="main-content">
	
	<#if methodDoc??>
        
          <div class="hero-unit">
            <h2>${methodDoc.name} </h2>
            	<br/>接口文档
          </div>
          
         <div>
         	<h2>接口地址</h2>
         	<p>${methodDoc.url}.json</p>
         	
         	<h2>http方法</h2>
         	<p>${methodDoc.httpMethod}</p>
         	
         	<h2>接口实现规范</h2>
         	<p>${methodDoc.description}</p>
         	
         	
         	<h2>参数</h2>
         	<p>
         		<table class="table table-bordered table-striped">
         			<thead>
         			<tr>
         				<td>参数名</td>
         				<td>必填</td>
         				<td>类型</td>
         				<td>说明</td>
         			</tr>
         			</thead>
         			<tbody>
         		<#list methodDoc.params as param>
         			<tr>
         				<td>${param.name}</td>
         				<td>${param.required?string("true", "false")}</td>
         				<td>
         				<#if param.typeLink>
         					<a href="${pluginConfig.baseURL}/class?name=${param.typeName}">
         				</#if>
         				${param.typeName}
         				<#if param.typeLink>
         					</a>
         				</#if>
         				</td>
         				<td>${param.description}</td>
         			</tr>
         		</#list>
         			</tbody>
         		
         		</table>
         	</p>
         	
         	<h2>返回值</h2>
         	<p>${methodDoc.returnDoc.type?html}</p>
         	<p>data列数据类型: 
         	<#list methodDoc.returnDoc.dataTypes as dt>
         		<a href="${pluginConfig.baseURL}/class?name=${dt}">${dt?html}</a><br/>
         	</#list>
         	</p>
         	<p>${methodDoc.returnDoc.description}</p>
         	
         	<!--
         	<p>
         		<table class="table table-bordered table-striped">
         			<thead>
         			<tr>
         				<td>参数名</td>
         				<td>必填</td>
         				<td>类型</td>
         				<td>说明</td>
         			</tr>
         			</thead>
         			<tbody>
         		
         			</tbody>
         		
         		</table>
         	</p>
         	 -->
         	
         	<h2>错误代码</h2>
         	<p>
         		<table class="table table-bordered table-striped">
         			<thead>
         			<tr>
         				<td>错误代码</td>
         				<td>错误消息</td>
         				<td>备注</td>
         			</tr>
         			</thead>
         			<tbody>
         		<#list methodDoc.errorCodes as code>
         			<tr>
         				<td>${code.code}</td>
         				<td>${code.message}</td>
         				<td>${code.memo}</td>
         			</tr>
         		</#list>
         			</tbody>
         		
         		</table>
         	</p>
         	
         	
         </div><!--/span-->
    <#else>
    	没有找到该方法[${name}]的文档
    </#if>
	</@override>
</@extends>