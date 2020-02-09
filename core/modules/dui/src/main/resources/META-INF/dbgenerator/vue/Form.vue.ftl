<#import "../webadmin/helper.ftl" as helper>

<#assign hasSelectType=false/>
<#assign dataFormName="dataForm"/>
<#assign apiName="${table.propertyName}Api"/>
<#assign formComponentName="${table.propertyName}Form"/>
<#assign moduleName="${_globalConfig.getModuleName()}"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign formFields=DUIEntityMeta.formFields/>
<template>
  <div v-loading="dataLoading">
    <el-form
      ref="${dataFormName}"
      :rules="rules"
      :model="dataModel"
      label-position="right"
      label-width="40%"
      style="width: 60%; margin-left:50px;">
   <#list formFields as field>
     <@helper.makeVueFormField field=field/>
   </#list>
    </el-form>
    <div class="formButton">
      <el-button type="primary" @click="handleSave" :loading="savingLoading">保存</el-button>
    </div>
  </div>
</template>

<script>
import * as ${apiName} from '@/api/${vueModuleName}/${apiName}'
//  import { exchangeLinebreak } from '@/filters'

export default {
  name: '${_tableContext.className}Form',
  components: {
  },
  props: {
    statusMode: {
      type: String,
      required: true
    },
    visible: {
      type: Boolean,
      default: false
    },
    dataId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      rules: {
<#list formFields as field>
    <#if field.notnull>
        ${field.name}: [
          { required: true, trigger: 'blur', message: '${(field.label)!field.column.commentName}是必须的' }
        ]<#if field?counter != formFields.size()>,</#if>
    </#if>
</#list>
      },
      dataModel: this.initDataModel(),
      savingLoading: false,
      dataLoading: false
    }
  },
  watch: {
    dataId: function(newDataId) {
      // console.log('newDataId:' + newDataId)
      if (this.dataId) {
        this.getData()
      } else {
        this.dataModel = this.initDataModel()
      }
    }
  },
  mounted: function() {
    this.getData()
  },
  methods: {
    getData() {
      if (!this.dataId) {
        return
      }
      this.dataLoading = true
      ${apiName}.get(this.dataId).then(res => {
        this.dataModel = res.data.data || this.initDataModel()
      }).finally(() => {
        this.dataLoading = false
      })
    },
    // 初始化dataModel
    initDataModel() {
      return {
  <#list DUIEntityMeta.formFields as field>
    <#if !field.column.primaryKey>
      <#if field.column.isFileType()>
        ${field.column.javaName}File: null,
      <#else>
        ${field.column.javaName}: '',
      </#if>
    </#if>
  </#list>
        ${table.primaryKey.javaName}: null
      }
    },
    handleSave() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.savingLoading = true
          const methodName = `handle${'$'}{this.statusMode}Data`
          const p = this[methodName]()
          p.then(res => {
            this.$nextTick(() => {
              // this.getList()
              this.$emit('finishHandle', {
                statusMode: this.statusMode,
                resposne: res
              })
              this.savingLoading = false
              // 保存后清除表单并重新加载数据
              this.$refs.dataForm.resetFields()
              this.getData()
            })
          }).catch(err => {
            console.log(`execute ${'$'}{methodName} error: ${'$'}{err}`)
            this.$emit('finishHandle', {
              statusMode: this.statusMode,
              error: err
            })
            this.savingLoading = false
          })
        } else {
          console.log('validate error!')
          return false
        }
      })
    },
<#if !DUIEntityMeta.editableEntity>
    handleAddData() {
      return ${apiName}.add(this.dataModel)
    },
</#if>
    handleEditData() {
      console.log('edit data.....')
      this.dataModel.${idName} = this.dataId
      return ${apiName}.update(this.dataModel)
    }
  }
}
</script>
<style scoped>
.formButton {
  text-align: right;
}
</style>

