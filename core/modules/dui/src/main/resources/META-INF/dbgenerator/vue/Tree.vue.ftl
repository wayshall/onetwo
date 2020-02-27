<#import "../webadmin/helper.ftl" as helper>

<#assign apiName="${table.propertyName}Api"/>
<#assign formComponentName="${table.propertyName}Form"/>
<#assign moduleName="${_globalConfig.getModuleName()}"/>
<#assign searchableFields=DUIEntityMeta.searchableFields/>
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-container>
          <el-header>
            ${DUIEntityMeta.label}管理
            <el-dropdown @command="handleDropMenu">
              <i class="el-icon-setting" style="margin-right: 15px"></i>
              <el-dropdown-menu slot="dropdown">
              </el-dropdown-menu>
            </el-dropdown>
          </el-header>
          <el-main>
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
          </el-main>
        </el-container>
      </el-col>
      <el-col :span="16">
        <el-container>
          <el-header>{{ detailTitle }}</el-header>
          <el-main>
            <${table.horizontalBarName}
              :${DUIEntityMeta.treeGrid.parentField.horizontalBarName}="queryModel.${DUIEntityMeta.treeGrid.parentField.name}"
              @batchDeleted="onNodeDatasBatchDeleted"/>
          </el-main>
        </el-container>
      </el-col>
    </el-row>

    <el-dialog
      title="机构部门管理"
      :visible.sync="dataForm.visible"
      :close-on-click-modal="false"
      :before-close="handleNodeDataFormClose">
      <department-form
        ref="departmentForm"
        :status-mode="dataForm.status"
        :data-id="dataForm.dataId"/>
    </el-dialog>

  </div>
</template>

<script>
import * as ${apiName} from '@/api/${vueModuleName}/${apiName}'
import ${_tableContext.propertyName} from './${_tableContext.propertyName}'
<#if DUIEntityMeta.treeGrid.isCascadeOnRightStyle()==true>
import ${_tableContext.propertyName}Form from './${_tableContext.propertyName}Form'
</#if>

export default {
  name: '${_tableContext.className}Tree',
  components: {
<#if DUIEntityMeta.treeGrid.isCascadeOnRightStyle()==true>
    ${_tableContext.propertyName}Form,
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
      currentNodedata: null
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

