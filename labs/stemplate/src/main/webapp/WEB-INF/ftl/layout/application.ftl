<#import "/lib/spring.ftl" as s/>
<#import ":fmtag:/lib/html-helper.ftl" as h/>
<#assign form = JspTaglibs["http://www.springframework.org/tags/form"]/>
<#assign spring = JspTaglibs["http://www.springframework.org/tags"]/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title> 
			<@define name="title">jfish app </@define> 
		</title>
		
		<@h.include_js src="jquery"/>
		<@h.include_js src="table" plugin="fmtag"/>
		
	</head>
	<body>
		
	</body>
</html>
