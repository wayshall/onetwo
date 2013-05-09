<#import "[jdoc]/lib/import_libs.ftl" as import/>

<#assign form = JspTaglibs["http://www.springframework.org/tags/form"]/>
<#assign spring = JspTaglibs["http://www.springframework.org/tags"]/>

<!DOCTYPE html>
<html lang="zh">
	<head>
	    <meta charset="utf-8">
	    <title>jfish java文档</title>
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="description" content="">
	    <meta name="author" content="">
					
		<@import.bootstrap/>
		
     <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
    </style>
    
		<@define name="jsscript"></@define>
		
	</head>
	<body>	
  
	<div class="container-fluid">
      <div class="row-fluid">
        <div class="span3">
          <div class="well sidebar-nav">
            <ul class="nav nav-list">
            <#list classDocs as clsDoc>
              <li class="nav-header"><a href="${pluginConfig.baseURL}/class?name=${clsDoc.fullName}">${clsDoc.name}</a></li>
              <#list clsDoc.methods as mdoc>
              	<li><a href="${pluginConfig.baseURL}/method?name=${mdoc.fullName}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:black">${mdoc.name}</span></a></li>
              </#list>
            </#list>
            
            <!--
              <li class="nav-header">Sidebar</li>
              <li class="active"><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li class="nav-header">Sidebar</li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li class="nav-header">Sidebar</li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              -->
            </ul>
          </div><!--/.well -->
        </div><!--/span-->
        
        <div class="span9">
		<@define name="main-content"></@define>
		</div>
		
	</div>
</div>
      <hr>

      <footer class="footer">
        <p>&copy; 基于jfish构建  2012.10</p>
      </footer>
      
	</body>
</html>
