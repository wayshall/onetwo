<#if __this__.component.hasToolbarMenu()>
	<div class="well">
		<div class="btn-group">
			<a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#">
				&nbsp;&nbsp;&nbsp;操&nbsp;&nbsp;作&nbsp;&nbsp;&nbsp;
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<#list __this__.component.menus as menu>
				<li>
					<@jfishui component=menu data=__this__.data/>
				</li>
				</#list>
			</ul>
		</div>
	</div>
</#if>
	