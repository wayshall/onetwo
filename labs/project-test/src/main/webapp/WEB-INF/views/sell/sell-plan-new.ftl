
<@extends>
	<@override name="title">
		新建 SellPlan
	</@override>
	
	<@override name="main-content">
	<@form.form modelAttribute="sellPlan" action="${siteConfig.baseURL}/sell/sellplan" method="post">
		
<div class="page-header">
<h2>
新建 SellPlan
</h2>
</div>
		<table class="table table-bordered table-striped">
		
			<tr>
				<td>remark </td>
				<td>
				<@form.input path="remark"/>
				<@form.errors path="remark" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>param </td>
				<td>
				<@form.input path="param"/>
				<@form.errors path="param" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>templetNo </td>
				<td>
				<@form.input path="templetNo"/>
				<@form.errors path="templetNo" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>beginIcLseq </td>
				<td>
				<@form.input path="beginIcLseq"/>
				<@form.errors path="beginIcLseq" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>length </td>
				<td>
				<@form.input path="length"/>
				<@form.errors path="length" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>lengthBak </td>
				<td>
				<@form.input path="lengthBak"/>
				<@form.errors path="lengthBak" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>preAddValue </td>
				<td>
				<@form.input path="preAddValue"/>
				<@form.errors path="preAddValue" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>agentIssueId </td>
				<td>
				<@form.input path="agentIssueId"/>
				<@form.errors path="agentIssueId" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>providerNo </td>
				<td>
				<@form.input path="providerNo"/>
				<@form.errors path="providerNo" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>corpCode </td>
				<td>
				<@form.input path="corpCode"/>
				<@form.errors path="corpCode" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>corpCardExp </td>
				<td>
				<@form.input path="corpCardExp"/>
				<@form.errors path="corpCardExp" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>issueDate </td>
				<td>
				<@form.input path="issueDate"/>
				<@form.errors path="issueDate" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>printType </td>
				<td>
				<@form.input path="printType"/>
				<@form.errors path="printType" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>washType </td>
				<td>
				<@form.input path="washType"/>
				<@form.errors path="washType" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>cityNo </td>
				<td>
				<@form.input path="cityNo"/>
				<@form.errors path="cityNo" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>channel </td>
				<td>
				<@form.input path="channel"/>
				<@form.errors path="channel" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>abnormal </td>
				<td>
				<@form.input path="abnormal"/>
				<@form.errors path="abnormal" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>operId </td>
				<td>
				<@form.input path="operId"/>
				<@form.errors path="operId" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>createTime </td>
				<td>
				<@form.input path="createTime"/>
				<@form.errors path="createTime" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>checkTime </td>
				<td>
				<@form.input path="checkTime"/>
				<@form.errors path="checkTime" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>checkOperId </td>
				<td>
				<@form.input path="checkOperId"/>
				<@form.errors path="checkOperId" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>clipId </td>
				<td>
				<@form.input path="clipId"/>
				<@form.errors path="clipId" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>preInit </td>
				<td>
				<@form.input path="preInit"/>
				<@form.errors path="preInit" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>clipBatch </td>
				<td>
				<@form.input path="clipBatch"/>
				<@form.errors path="clipBatch" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>issueStatus </td>
				<td>
				<@form.input path="issueStatus"/>
				<@form.errors path="issueStatus" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>updTime </td>
				<td>
				<@form.input path="updTime"/>
				<@form.errors path="updTime" cssClass="error"/>
				</td>
			</tr>
		
		<tr>
			<td colspan="2">
			<input name="" type="submit" value="保存" class="btn btn-primary"/>
			<a href="${siteConfig.baseURL}/sell/sellplan" class="btn">返回</a>
			</td>
		</tr>
		</table>
	</@form.form>
	</@override> 
</@extends>