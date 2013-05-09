<div id="user_sidebar" class="user-sidebar">
	<div class="u-s-bd">
	
		<dl>
			<dt>
				后台管理
			</dt>
			<#list menuData?keys as key>
				<dd class="menu_on"><a href="${siteConfig.baseURL}${menuData[key]}">- ${key}</a></dd>
			</#list>
		</dl>
		
	</div>
	<div class="u-s-ft"></div>
</div>
<style>
	.menu_on {
		background-color: red;
	}
</style>
<script>
	jQuery(function($) {
		var menus = $('#user_sidebar dd a');
		var matchs = false;
		
		menus.each(function() {
			if(this.href == location.toString()) {
				$(this).css('color', '#F18022');
				matchs = true;
				return false;
			}
		});
		
		if(!matchs) {
			menus.each(function() {
				if(this.href.indexOf(location.toString().replace(/!\w+\.do.*/, '')) != -1) {
					$(this).css('color', '#F18022');
					return false;
				}
			});
		}
	});
</script>