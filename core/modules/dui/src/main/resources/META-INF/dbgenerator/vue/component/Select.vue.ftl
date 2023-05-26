<#assign apiName="${table.propertyName}Api"/>

<template>
  <el-select
    v-model="selectedValue"
    @change="change"
    placeholder="请选择${(DUIEntityMeta.label)!''}"
    :filterable="false"
    :remote="false"
    :remote-method="search"
    :clearable="true"
    :multiple="multiple">
    <el-option
      v-for="item in dataList"
      :key="item[valueField]"
      :label="item[labelField]"
      :value="item[valueField]">
    </el-option>
  </el-select>
</template>

<script>
import * as ${apiName} from '@/api/${vueModuleName}/${apiName}'

export default {
  name: '${DUIEntityMeta.componentName}Select',
  model: {
    prop: 'selectedProperty',
    event: 'change'
  },
  props: {
    selectedProperty: {
      type: [String, Boolean, Number, Array],
      required: false,
      default: null
    },
    labelField: {
      type: String,
      default: 'name',
      required: false
    },
    valueField: {
      type: String,
      default: 'id',
      required: false
    },
    multiple: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data() {
    return {
      dataList: [],
      dataPage: {
        page: 1,
        pageSize: 10,
        pagination: false
      }
    }
  },
  computed: {
    selectedValue: {
      get: function() {
        return this.selectedProperty
      },
      set: function(val) {
        this.$emit('change:selectedProperty', val)
      }
    }
  },
  watch: {
    selectedProperty(newValue, oldValue) {
      // console.log('OrgMgrUserSelect newValue: ' + newValue + ', oldValue: ' + oldValue)
      // this.loadDataList()
    }
  },
  mounted: function() {
    this.loadDataList()
  },
  created() {
  },
  updated() {
  },
  methods: {
    loadDataList() {
      const queryModel = {
      }
      ${apiName}.getList(this.dataPage, queryModel).then(res => {
        this.dataList = res.bizData
        this.loadSelectedItem()
      })
    },
    loadSelectedItem() {
      let selectedvalueList = []
      if (this.multiple) {
        selectedvalueList = this.selectedValue
      } else {
        selectedvalueList.push(this.selectedValue)
      }
      if (!selectedvalueList || selectedvalueList.length < 1) {
        return
      }
      selectedvalueList.forEach(selected => {
        if (!this.dataList.find(e => e[this.valueField] === selected[this.valueField])) {
          ${apiName}.get(selected[this.valueField]).then(res => {
            if (res.data.success) {
              this.dataList.unshift(res.bizData)
            }
          })
        }
      })
    },
    // throttle节流
    search: this.$lodash.throttle(query => {
      if (!query || !query.trim()) {
        console.log('${DUIEntityMeta.componentName}Select ignore search: ', query)
        return
      }
      console.log('search: ', query)
      const queryModel = {
      }
      ${apiName}.getList(this.dataPage, queryModel).then(res => {
        this.dataList = res.bizData.result
      })
    }, 500),
    change(val) {
      console.log('${DUIEntityMeta.componentName}Select emit change: ', val)
      this.$emit('change', val)
    }
  }
}
</script>

<style lang="scss">
.depart-select {
  position: relative;
}
.text-wrapper {
  white-space: pre-wrap;
}
</style>
