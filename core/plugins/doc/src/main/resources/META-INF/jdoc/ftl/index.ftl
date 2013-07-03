<@extends>
	
	<@override name="main-content">
	
        
          <div class="hero-unit">
            <h1>帮助文档</h1>
            <p>你看到的这个是一个自动生成的帮助文档</p>
          </div>
          
          <#assign rowCount = 0>
          <#list methodDocs as methodDoc>
          	
          	<#if rowCount%3==0>
          	<div class="row-fluid">
          	</#if>
          	
      		<div class="span4">
              <h2>${methodDoc.name}</h2>
              <p>${methodDoc.description}</p>
              <p><a class="btn" href="#">查看详细 &raquo;</a></p>
            </div><!--/span-->
            
          	<#if rowCount%3==0>
          	</div>
          	</#if>
          	
          	<#assign rowCount = rowCount+1>
          	
          </#list>
           
      
	</@override>
</@extends>