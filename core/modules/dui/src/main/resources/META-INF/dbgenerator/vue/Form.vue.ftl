<#import "../webadmin/helper.ftl" as helper>

<#assign hasFileType=false/>
<#assign hasSelectType=false/>
<#assign dataFormName="dataForm"/>
<#assign apiName="${table.propertyName}Api"/>
<#assign formComponentName="${table.propertyName}Form"/>
<#assign moduleName="${_globalConfig.getModuleName()}"/>
<#assign idName="${table.primaryKey.javaName}"/>
<template>
  <div v-loading="dataLoading">
    <el-form
      ref="${dataFormName}"
      :rules="rules"
      :model="dataModel"
      label-position="right"
      label-width="40%"
      style="width: 60%; margin-left:50px;">
   <#list DUIEntityMeta.formFields as field>
      <el-form-item
        label="${(field.label)!''}"
        prop="${field.column.javaName}">
      <#if field.select??>
        <dui-select
          v-model="dataModel.${field.column.javaName}"
          entity="${DUIEntityMeta.name}"
          field="${field.name}"/>
        <#assign hasSelectType=true/>
      <#elseif field.column.mapping.isNumberType()==true>
        <el-input-number
          v-model="dataModel.${field.column.javaName}"
          :min="1" :max="10"
          label="${(field.label)!''}"
          :disabled="${field.formDisabledValue}"
          <#if field.column.mapping.isSqlFloat()==true> :precision="2"</#if>/>
      <#elseif field.column.mapping.isSqlTimestamp()==true>
        <el-date-picker
          v-model="dataModel.${field.column.javaName}"
          type="datetime"
          placeholder="选择日期时间">
        </el-date-picker>
      <#elseif field.column.mapping.isSqlTime()==true>
        <el-time-picker
          v-model="dataModel.${field.column.javaName}"
          placeholder="选择时间>
       </el-time-picker>
      <#elseif field.column.mapping.isSqlDate()==true>
        <el-date-picker
          v-model="dataModel.${field.column.javaName}"
          type="date"
          placeholder="选择日期">
        </el-date-picker>
      <#elseif field.column.mapping.isBooleanType()==true>
        <el-switch
          v-model="dataModel.${field.column.javaName}"
          active-color="#13ce66"
          inactive-color="#ff4949">
        </el-switch>
      <#elseif field.column.isFileType()==true>
        <file-input v-model="dataModel.${field.column.javaName}File"/>
        <#assign hasFileType=true/>
      <#elseif field.column.isAssociationType()==true>
        <el-input v-model="dataModel.${field.column.javaName}" placeholder="请输入${(field.label)!''}"/>
      <#else>
        <el-input
          v-model="dataModel.${field.column.javaName}"
          type="${field.input.typeName}"
          placeholder="请输入${(field.label)!''}"/>
      </#if>
      </el-form-item>
  </#list>
    </el-form>
    <div class="formButton">
      <el-button type="primary" @click="handleSave" :loading="savingLoading">保存</el-button>
    </div>
  </div>
</template>

<script>
import * as ${apiName} from '@/api/${vueModuleName}/${apiName}'
<#if hasFileType>
import fileInput from '@/components/xui/fileInput'
</#if>
//  import { exchangeLinebreak } from '@/filters'

export default {
  name: '${_tableContext.className}Form',
  components: {
<#if hasFileType>
    fileInput,
</#if>
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
<#list DUIEntityMeta.formFields as field>
    <#if field.notnull && !field.column.primaryKey>
        ${field.name}: [
          { required: true, trigger: 'blur', message: '${(field.label)!field.column.commentName}是必须的' }
        ],
    </#if>
</#list>
        ${table.primaryKey.javaName}: [
        ]
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

