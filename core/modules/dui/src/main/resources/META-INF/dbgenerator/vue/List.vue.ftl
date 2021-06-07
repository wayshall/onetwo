<#import "../webadmin/helper.ftl" as helper>

<#assign apiName="${table.propertyName}Api"/>
<#assign formComponentName="${table.propertyName}Form"/>
<#assign moduleName="${_globalConfig.getModuleName()}"/>
<#assign searchableFields=DUIEntityMeta.searchableFields/>
<template>
  <div class="app-container">

    <layout-table
      ref="listTable"
      id-property="${table.primaryKey.javaName}"
      @batchDeleted="onBatchDeleted"
      :list-api="listApi"
<#if searchableFields.isEmpty()==false || DUIEntityMeta.isTree()==true>
      :query-form-model="queryFormModel"
</#if>
      :refresh.sync="refreshTable"
      :delete-api="deleteApi"
      :operations="operations">
  <#if searchableFields.isEmpty()==false>
      <template slot="queryForm">
    <#list searchableFields as field>
        <@helper.makeVueFormField field=field modelPrefix="queryFormModel" spaces="  "/>
    </#list>
      </template>
  </#if>

      <template slot="toolbar">
        <el-button type="primary" icon="el-icon-edit" @click="handleAdd">
          添加${(DUIEntityMeta.label)!''}
        </el-button>
      </template>

      <el-table-column align="center" width="80" type="selection"/>
  <#list DUIEntityMeta.listableFields as field>
    <#if field.column.isDateType()>
      <el-table-column align="center" label="${(field.label)!''}" <#if field?counter != DUIEntityMeta.listableFields.size()>width="100"</#if>>
        <template slot-scope="scope">
          <span>{{ scope.row.${field.column.javaName} | formatDateInMillis }}</span>
        </template>
      </el-table-column>
    <#elseif field.input.isFileType()==true>
      <image-popover-column prop="${field.listField}" label="${(field.label)!''}"/>
    <#else>
      <el-table-column align="center" label="${(field.label)!''}" prop="${field.listField}" <#if field?counter != DUIEntityMeta.listableFields.size()>width="100"</#if>/>
    </#if>
  </#list>
    </layout-table>

  </div>
</template>

<script>
import * as ${apiName} from '@/api/${vueModuleName}/${apiName}'

export default {
  name: '${_tableContext.className}List',
  components: {
  },
  data() {
    return {
<#if searchableFields.isEmpty()==false || DUIEntityMeta.isTree()==true>
      queryFormModel: {
  <#if DUIEntityMeta.isTree()==true>
        ${DUIEntityMeta.treeGrid.parentField.name}: '',
  </#if>
  <#list searchableFields as field>
    <#if !field.column.primaryKey>
        ${field.column.javaName}: '',
    </#if>
  </#list>
        ${table.primaryKey.javaName}: null
      },
</#if>
      dataForm: {
        status: '',
        dataId: '',
        row: null,
        visible: false
      },
      refreshTable: false,
      currentTabName: 'dataFormTab',
      operations: null
    }
  },
<#if DUIEntityMeta.isTree()==true>
  watch: {
    ${DUIEntityMeta.treeGrid.parentField.name}: function(newValue) {
      this.queryFormModel.${DUIEntityMeta.treeGrid.parentField.name} = newValue
      this.refreshTable = true
    }
  },
</#if>
  mounted: function() {
  },
  methods: {
    handleClose() {
      // 清除验证信息
      this.$refs.${table.propertyName}Form.$refs.dataForm.resetFields()
      this.dataForm.visible = false
      return true
    },
    onBatchDeleted(ids) {
      this.$emit('batchDeleted', ids)
    },
    on${_tableContext.className}Finish() {
      this.refreshTable = true
      this.dataForm.visible = false
    },
    listApi: ${apiName}.getList,
    deleteApi: ${apiName}.remove,
    // 操作菜单处理，根据command分派到不同方法
    handleAction(data) {
      const command = data.action
      if (command === 'edit') {
        this.handleEdit(data.row)
      } else if (command === 'delete') {
        this.$refs.listTable.handleDelete([data.row.${table.primaryKey.javaName}])
      }
    }
  }
}
</script>

<style lang="scss">
.text-wrapper {
  white-space: pre-wrap;
}
</style>

