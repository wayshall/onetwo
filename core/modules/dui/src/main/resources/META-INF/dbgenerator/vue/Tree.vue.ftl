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
              node-key="id"
              lazy
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
            <${table.horizontalBarName} :${DUIEntityMeta.treeGrid.parentField.horizontalBarName}="queryModel.${DUIEntityMeta.treeGrid.parentField.name}"/>
          </el-main>
        </el-container>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import * as ${apiName} from '@/api/${vueModuleName}/${apiName}'
import ${_tableContext.propertyName} from './${_tableContext.propertyName}'

export default {
  name: '${_tableContext.className}Tree',
  components: {
    ${_tableContext.propertyName}
  },
  data() {
    return {
      detailTitle: '数据列表',
      treeList: [],
      queryModel: {
        ${DUIEntityMeta.treeGrid.parentField.name}: ''
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
      this.queryModel.${DUIEntityMeta.treeGrid.parentField.name} = data.${table.primaryKey.propertyName}
    },
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

