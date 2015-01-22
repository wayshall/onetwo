
	<@widget.formField name="code" label="字典类型编码" readOnly=dictionary.id!=null/>
	<@widget.formField name="name" label="名称"/>
	<@widget.formField name="value" label="字典值"/>
	<@widget.formField name="valid" label="是否有效" type="select" value="value" provider="dictionary" items="BOOL" itemValue="value"/>
	<@widget.formField name="sort" label="sort" label="排序"/>
	<@widget.formField name="remark" type="textarea" label="备注" />
	<@widget.formField name="" type="submit" label="提交"/>
	<@widget.formField name="" type="button" value="${pluginConfig.baseURL}/data/dictionary-type" label="返回"/>
