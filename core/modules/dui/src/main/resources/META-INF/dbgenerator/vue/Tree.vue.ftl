<#import "../webadmin/helper.ftl" as helper>

<#assign apiName="${table.propertyName}Api"/>
<#assign formComponentName="${table.propertyName}Form"/>
<#assign moduleName="${_globalConfig.getModuleName()}"/>
<#assign searchableFields=DUIEntityMeta.searchableFields/>
<template>
  <el-container style="height: 500px; border: 1px solid #eee">

    <el-aside
      width="200px"
      style="margin-top:20px;background-color: rgb(238, 241, 246)">
      <el-tree
        :data="treeList"
        @node-click="handleNodeClick"
        node-key="${table.primaryKey.propertyName}"
        lazy
      <#if DUIEntityMeta.treeGrid.isCascadeOnRightStyle()==true>
        :render-content="renderTreeNodeButton"
      </#if>
        :load="loadTree"
        :expand-on-click-node="false"
        :default-expand-all="false">
      </el-tree>
    </el-aside>

    <el-container>
      <el-main>
      <#if DUIEntityMeta.treeGrid.isCascadeOnRightStyle()==true>
        <${DUIEntityMeta.treeGrid.cascadeEntityMeta.table.horizontalBarName}
          :${DUIEntityMeta.treeGrid.cascadeFieldBarName}="currentNodedata.${table.primaryKey.propertyName}"/>
      <#else>
        <${table.horizontalBarName}
          :${DUIEntityMeta.treeGrid.parentField.horizontalBarName}="queryModel.${DUIEntityMeta.treeGrid.parentField.name}"
          @batchDeleted="onNodeDatasBatchDeleted"/>
      </#if>
      </el-main>
    </el-container>

    <el-dialog
      title="${DUIEntityMeta.label}编辑"
      :visible.sync="dataForm.visible"
      :close-on-click-modal="false"
      :before-close="handleNodeDataFormClose">
      <${DUIEntityMeta.table.horizontalBarName}-form
        ref="${DUIEntityMeta.table.propertyName}Form"
        :status-mode="dataForm.status"
        :data-id="dataForm.dataId"/>
    </el-dialog>

  </el-container>
</template>

<script>
import * as ${apiName} from '@/api/${vueModuleName}/${apiName}'
import ${_tableContext.propertyName} from './${_tableContext.propertyName}'
import ${_tableContext.propertyName}Form from './${_tableContext.propertyName}Form'
<#if DUIEntityMeta.treeGrid.isCascadeOnRightStyle()==true>
import ${DUIEntityMeta.treeGrid.cascadeEntityMeta.table.propertyName} from './${DUIEntityMeta.treeGrid.cascadeEntityMeta.table.propertyName}'
</#if>

export default {
  name: '${_tableContext.className}Tree',
  components: {
    ${_tableContext.propertyName}Form,
<#if DUIEntityMeta.treeGrid.isCascadeOnRightStyle()==true>
    ${DUIEntityMeta.treeGrid.cascadeEntityMeta.table.propertyName},
</#if>
    ${_tableContext.propertyName}
  },
  data() {
    return {
      detailTitle: '数据列表',
      treeList: [],
      queryModel: {
        ${DUIEntityMeta.treeGrid.parentField.name}: ''
      },
<#if DUIEntityMeta.treeGrid.isCascadeOnRightStyle()==true>
      dataForm: {
        status: '',
        dataId: '',
        row: null,
        visible: false
      },
</#if>
      currentNodedata: {
        ${table.primaryKey.propertyName}: ''
      }
    }
  },
  mounted: function() {
    // 非懒加载
    // this.getTree()
  },
  methods: {
    handleDropMenu(cmd) {
    },
    onNodeDatasBatchDeleted(ids) {
      this.currentNodedata.children = this.currentNodedata.children.filter(e => {
        return ids.findIndex(id => id === e.${table.primaryKey.propertyName}) === -1
      })
    },
    // 非懒加载
    getTree() {
      ${apiName}.getTree().then(res => {
        this.treeList = res.bizData
      })
    },
    loadTree(node, resolve) {
      if (node.level === 0) {
        this.queryModel.${DUIEntityMeta.treeGrid.parentField.name} = '${DUIEntityMeta.treeGrid.rootId}'
        ${apiName}.loadTree(this.queryModel).then(res => {
          resolve(res.bizData)
        })
      } else {
        this.queryModel.${DUIEntityMeta.treeGrid.parentField.name} = node.data.${table.primaryKey.propertyName}
        ${apiName}.loadTree(this.queryModel).then(res => {
          resolve(res.bizData)
        })
      }
    },
    handleNodeClick(data) {
      this.currentNodedata = data
      this.queryModel.${DUIEntityMeta.treeGrid.parentField.name} = data.${table.primaryKey.propertyName}
    },
<#if DUIEntityMeta.treeGrid.isCascadeOnRightStyle()==true>
    renderTreeNodeButton(h, { node, data, store }) {
      return (
        <span>
          <span>{node.label} </span>
          <span>
            <el-button size='mini' type='text' on-click={ (e) => this.handleEdit(e, data) }>查看</el-button>
            <el-button size='mini' type='text' on-click={ (e) => this.handleAdd(e) }>添加子节点</el-button>
          </span>
        </span>
      )
    },
    handleNodeDataFormClose() {
      // 清除验证信息
      this.$refs.${_tableContext.propertyName}Form.$refs.dataForm.resetFields()
      this.dataForm.visible = false
      return true
    },
    handleEdit(e, row) {
      this.dataForm.status = 'Edit'
      this.dataForm.visible = true
      this.dataForm.row = row
      this.dataForm.dataId = row.id
      e.stopPropagation()
    },
    handleAdd(e) {
      this.dataForm.status = 'Add'
      this.dataForm.visible = true
      this.dataForm.row = {}
      this.dataForm.dataId = ''
      e.stopPropagation()
    },
</#if>
    handleViewDetail(data) {
    }
  }
}
</script>
<style lang="scss" scoped>
.el-header  {
  text-align: center;
  line-height: 60px;
  background-color: #B3C0D1;
  color: #333;
}
.el-main {
  background-color: #E9EEF3;
}
.el-footer {
  @extend .el-header;
  background-color: #E9EEF3;
}
</style>

