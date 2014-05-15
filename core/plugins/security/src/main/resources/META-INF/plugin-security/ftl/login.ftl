

<!DOCTYPE html>
<html lang="zh">
	<head>
	    <meta charset="utf-8">
	    <title>
			登录
	    </title>
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="description" content="">
	    <meta name="author" content="">
		
		<script type="text/javascript">

 		if(parent && parent.window.mainPage){
 			parent.location.href = "${siteConfig.ssoLoginUrl}&${helper.queryString}";
 		}else{
 			location.href = "${siteConfig.ssoLoginUrl}&${helper.queryString}"; 
 		} 
		
		</script>
		
	</head>
	<body>	
	</body>
	
</html>
