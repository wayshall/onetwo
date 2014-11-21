
<@widget.formField name="appCode" type="hidden"/>	
<@widget.formField name="name" label="角色名称 "/>
<@widget.formField name="code" label="角色代码 " type="select" items=roleCodes itemLabel="name" itemValue="value"  title="商户和充值类型一般分配给合作商户的用户使用" />
<@widget.formField name="status" type="select" label="状态" items=roleStatusList itemLabel="label" itemValue="value" showable=role.id??/>
<@widget.formField name="remark" type="textarea" label="备注 "/>

<@widget.formField name="" type="submit" label="提交"/>
<@widget.formField name="" type="button" value="${pluginConfig.baseURL}/app/role?appCode=${role.appCode }" label="返回"/>