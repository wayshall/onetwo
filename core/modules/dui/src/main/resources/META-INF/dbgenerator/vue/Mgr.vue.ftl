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
      :list-api="listApi"
      :query-form-model="queryFormModel"
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
    <#else>
      <el-table-column align="center" label="${(field.label)!''}" prop="${field.listField}" <#if field?counter != DUIEntityMeta.listableFields.size()>width="100"</#if>/>
    </#if>
  </#list>
    </layout-table>

    <el-dialog
      title="${DUIEntityMeta.label}管理"
      :visible.sync="dataForm.visible"
      :close-on-click-modal="false"
      :before-close="handleClose">
      <el-tabs
        type="border-card"
        v-model="currentTabName">
        <el-tab-pane
          label="${DUIEntityMeta.label}编辑"
          name="dataFormTab">
          <${table.propertyName}-form
            :status-mode="dataForm.status"
            :data-id="dataForm.dataId"
            @finishHandle="on${_tableContext.className}Finish"/>
        </el-tab-pane>
      <#list DUIEntityMeta.editableEntities as editableEntity>
        <el-tab-pane label="${editableEntity.label}" v-if="dataForm.status === 'Edit'">
          <${editableEntity.table.horizontalBarName}-form
            :status-mode="dataForm.status"
            :data-id="dataForm.row['${editableEntity.cascadeField}'] || ''"/>
        </el-tab-pane>
      </#list>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import * as ${apiName} from '@/api/${vueModuleName}/${apiName}'
import ${formComponentName} from './${formComponentName}'
<#list DUIEntityMeta.editableEntities as editableEntity>
import ${editableEntity.table.propertyName}Form from './${editableEntity.table.propertyName}Form'
</#list>

export default {
  name: '${_tableContext.className}',
  components: {
<#list DUIEntityMeta.editableEntities as editableEntity>
    ${editableEntity.table.propertyName}Form,
</#list>
    ${formComponentName}
  },
  data() {
    return {
      queryFormModel: {
  <#list searchableFields as field>
    <#if !field.column.primaryKey>
        ${field.column.javaName}: '',
    </#if>
  </#list>
        ${table.primaryKey.javaName}: null
      },
      dataForm: {
        status: '',
        dataId: '',
        row: null,
        visible: false
      },
      refreshTable: false,
      currentTabName: 'dataFormTab',
      operations: [
        { action: 'edit', text: '编辑', handler: this.handleEdit }
      ]
    }
  },
  mounted: function() {
  },
  methods: {
    handleClose() {
      // 清除验证信息
      // this.$refs.dataForm.resetFields()
      this.dataForm.visible = false
      return true
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
    },
    handleAdd() {
      this.dataForm.status = 'Add'
      this.dataForm.visible = true
      this.dataForm.row = {}
      this.dataForm.dataId = ''
      this.currentTabName = 'dataFormTab'
    },
    handleEdit(row) {
      this.dataForm.status = 'Edit'
      this.dataForm.visible = true
      this.dataForm.row = row
      this.dataForm.dataId = row.${table.primaryKey.javaName}
      this.currentTabName = 'dataFormTab'
    }
  }
}
</script>

<style lang="scss">
.text-wrapper {
  white-space: pre-wrap;
}
</style>

