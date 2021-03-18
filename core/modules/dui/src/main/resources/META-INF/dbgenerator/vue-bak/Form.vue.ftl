<#import "../webadmin/helper.ftl" as helper>

<#assign hasFileType=false/>
<#assign hasSelectType=false/>
<#assign dataFormName="dataForm"/>
<#assign apiName="${table.propertyName}Api"/>
<#assign formComponentName="${table.propertyName}Form"/>
<#assign moduleName="${_globalConfig.getModuleName()}"/>
<template>
  <el-dialog :title="title" :visible.sync="visible" :close-on-click-modal="false" :before-close="handleClose">
    <el-form ref="${dataFormName}" :rules="rules" :model="dataModel" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
   <#list UIClassMeta.formFields as field>
      <el-form-item label="${(field.label)!''}" prop="${field.column.javaName}">
      <#if field.select??>
        <dui-select v-model="dataModel.${field.column.javaName}" entity="${UIClassMeta.name}" field="${field.name}" label-field="${field.select.labelField}" value-field="${field.select.valueField}"/>
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
        <el-input v-model="dataModel.${field.column.javaName}" placeholder="请输入${(field.label)!''}"/>
      </#if>
      </el-form-item>
  </#list>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visibleStatus = false" :loading="savingLoading">取消</el-button>
      <el-button type="primary" @click="handleSave" :loading="savingLoading">保存</el-button>
    </div>
  </el-dialog>
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
    dataModel: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      rules: {
<#list table.columns as column>
    <#if !column.nullable && !column.primaryKey>
        ${column.javaName}: [
          { required: true, trigger: 'blur', message: '${(column.commentName)!''}是必须的' }
        ],
    </#if>
</#list>
        ${table.primaryKey.javaName}: [
        ]
      },
<#list table.columns as column>
    <#if column.isDictType()>
      ${column.javaName}Options: [
        <#list column.dictData as opt>
        { label: '${opt.label}', value: '${opt.value}' },
        </#list>
        { label: '请选择', value: '' }
      ],
    <#elseif column.isFileType()==true>
    </#if>
</#list>
      titleText: {
        Add: '新增[${(table.comments[0])!''}]',
        Edit: '编辑[${(table.comments[0])!''}]'
      },
      savingLoading: false
    }
  },
  computed: {
    title: function() {
      return this.titleText[this.statusMode]
    },
    visibleStatus: {
      get: function() {
        return this.visible
      },
      set: function(value) {
        // 更新父组件属性
        this.$emit('update:visible', value)
      }
    }
  },
  methods: {
<#list table.columns as column>
    <#if column.isFileType()>
    </#if>
</#list>
    handleClose() {
      // 清除验证信息
      this.$refs.dataForm.resetFields()
      this.visibleStatus = false
      return true
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
              this.visibleStatus = false
              this.savingLoading = false
            })
          }).catch(err => {
            console.log(`execute ${'$'}{methodName} error: ${'$'}{err}`)
            this.$emit('finishHandle', {
              statusMode: this.statusMode,
              error: err
            })
            this.visibleStatus = false
            this.savingLoading = false
          })
        } else {
          console.log('validate error!')
          return false
        }
      })
    },
    handleAddData() {
      return ${apiName}.add(this.dataModel)
    },
    handleEditData() {
      console.log('edit data.....')
      return ${apiName}.update(this.dataModel)
    }
  }
}
</script>

