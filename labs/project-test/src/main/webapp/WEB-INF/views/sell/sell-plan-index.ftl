<@extends>
	<@override name="title">
		SellPlan列表
	</@override>
	
	<@override name="main-content">
		<@override name="grid_toolbar">
			<li>
			<a href="${siteConfig.baseURL}/sell/sellplan/new"> 新 建  </a>
			</li>
			<li>
			<a data-method="delete" class="dg-toolbar-button-delete" data-confirm="确定要批量删除这些数据？" href="${siteConfig.baseURL}/sell/sellplan"> 批量删除  </a>
			</li>
		</@override>
		
		<@grid name="page" 
				title="SellPlan列表"
				cssClass="tableStyle" 
				cssStyle="width:760px" 
				action=":qstr">
			<@field name="ids" label="全选" type="checkbox" value="id" cssStyle="width:60px;text-align:center;">
			</@field>
			
			<@field name="planId" label="planId" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="remark" label="remark" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="param" label="param" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="templetNo" label="templetNo" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="beginIcLseq" label="beginIcLseq" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="length" label="length" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="lengthBak" label="lengthBak" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="preAddValue" label="preAddValue" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="agentIssueId" label="agentIssueId" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="providerNo" label="providerNo" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="corpCode" label="corpCode" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="corpCardExp" label="corpCardExp" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="issueDate" label="issueDate" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="printType" label="printType" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="washType" label="washType" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="cityNo" label="cityNo" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="channel" label="channel" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="abnormal" label="abnormal" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="operId" label="operId" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="createTime" label="createTime" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="checkTime" label="checkTime" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="checkOperId" label="checkOperId" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="clipId" label="clipId" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="preInit" label="preInit" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="clipBatch" label="clipBatch" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="issueStatus" label="issueStatus" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="updTime" label="updTime" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			
			<@field name="operation" label="操作" cssStyle="text-align:center;" autoRender="false">
				<a href="${siteConfig.baseURL}/sell/sellplan/${__entity__.planId}/edit">编辑</a>
			</@field>
		</@grid>
		
	</@override> 
</@extends>