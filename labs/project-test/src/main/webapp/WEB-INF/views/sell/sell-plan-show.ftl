<@extends>
	<@override name="title">
SellPlan ${sellPlan.id}
	</@override>
	
	<@override name="main-content">
		
<div class="page-header">
<h2>
SellPlan ${sellPlan.id} 
</h2>
</div>

		<table class="table table-bordered table-striped">
			<tr>
				<td  style="width:160px">planId </td>
				<td>
				${sellPlan.planId?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">remark </td>
				<td>
				${sellPlan.remark?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">param </td>
				<td>
				${sellPlan.param?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">templetNo </td>
				<td>
				${sellPlan.templetNo?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">beginIcLseq </td>
				<td>
				${sellPlan.beginIcLseq?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">length </td>
				<td>
				${sellPlan.length?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">lengthBak </td>
				<td>
				${sellPlan.lengthBak?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">preAddValue </td>
				<td>
				${sellPlan.preAddValue?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">agentIssueId </td>
				<td>
				${sellPlan.agentIssueId?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">providerNo </td>
				<td>
				${sellPlan.providerNo?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">corpCode </td>
				<td>
				${sellPlan.corpCode?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">corpCardExp </td>
				<td>
				${sellPlan.corpCardExp?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">issueDate </td>
				<td>
				${sellPlan.issueDate?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">printType </td>
				<td>
				${sellPlan.printType?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">washType </td>
				<td>
				${sellPlan.washType?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">cityNo </td>
				<td>
				${sellPlan.cityNo?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">channel </td>
				<td>
				${sellPlan.channel?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">abnormal </td>
				<td>
				${sellPlan.abnormal?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">operId </td>
				<td>
				${sellPlan.operId?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">createTime </td>
				<td>
				${sellPlan.createTime?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">checkTime </td>
				<td>
				${sellPlan.checkTime?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">checkOperId </td>
				<td>
				${sellPlan.checkOperId?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">clipId </td>
				<td>
				${sellPlan.clipId?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">preInit </td>
				<td>
				${sellPlan.preInit?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">clipBatch </td>
				<td>
				${sellPlan.clipBatch?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">issueStatus </td>
				<td>
				${sellPlan.issueStatus?html}
				</td>
			</tr>
			<tr>
				<td  style="width:160px">updTime </td>
				<td>
				${sellPlan.updTime?html}
				</td>
			</tr>
		<tr>
			<td colspan="2">
			<a href="${siteConfig.baseURL}/sell/sellplan/${sellPlan.planId}/edit" class="btn btn-primary">编辑</a>
			<a href="${siteConfig.baseURL}/sell/sellplan" class="btn">返回</a>
			</td>
		</tr>
		
		</table>
	</@override> 
</@extends>