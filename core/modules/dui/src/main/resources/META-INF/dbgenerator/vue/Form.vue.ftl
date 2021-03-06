<#import "../webadmin/helper.ftl" as helper>

<#assign hasSelectType=false/>
<#assign dataFormName="dataForm"/>
<#assign apiName="${table.propertyName}Api"/>
<#assign formComponentName="${table.propertyName}Form"/>
<#assign moduleName="${_globalConfig.getModuleName()}"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign formFields=DUIEntityMeta.formFields/>
<#assign selectableFields=DUIEntityMeta.selectableFields/>
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
<#if DUIEntityMeta.detailPageEditable>
    <div class="formButton">
      <el-button type="primary" @click="handleSave" :loading="savingLoading">保存</el-button>
    </div>
</#if>
  </div>
</template>

<script>
import * as ${apiName} from '@/api/${vueModuleName}/${apiName}'
//  import { exchangeLinebreak } from '@/filters'

export default {
  name: '${DUIEntityMeta.componentName}Form',
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
<#if DUIEntityMeta.childEntity>
    ${DUIEntityMeta.refParentField}: {
      type: String,
      required: true
    },
</#if>
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
    <#else>
        ${field.name}: [
          { required: false }
        ]<#if field?counter != formFields.size()>,</#if>
    </#if>
</#list>
      },
      dataModel: this.initDataModel(),
      savingLoading: false,
      dataLoading: false
    }
  },
  computed: {
<#if selectableFields.isEmpty()==false>
  <#list selectableFields as field>
    ${field.name}Datas() {
      if (this.dataModel.${field.name}) {
        return [{ value: this.dataModel.${field.name}, label: this.dataModel.${field.listField} }]
      }
      return []
    }<#if field?is_last==false>,</#if>
  </#list>
</#if>
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
    <#if field.isIdField()==false>
      <#if field.input.isFileType()>
        ${field.name}File: null,
      <#else>
        ${field.name}: '',
      </#if>
    </#if>
  </#list>
        ${table.primaryKey.javaName}: null
      }
    },
<#if DUIEntityMeta.detailPageEditable>
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
</#if>
<#if !DUIEntityMeta.editableEntity>
    handleAddData() {
<#if DUIEntityMeta.childEntity>
      this.dataModel.${DUIEntityMeta.refParentField} = this.${DUIEntityMeta.refParentField}
</#if>
      return ${apiName}.add(this.dataModel)
    },
</#if>
    handleEditData() {
      console.log('edit data.....')
<#if DUIEntityMeta.childEntity>
      this.dataModel.${DUIEntityMeta.refParentField} = this.${DUIEntityMeta.refParentField}
</#if>
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

