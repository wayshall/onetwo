<#assign requestPath="/${vueModuleName}/${_tableContext.propertyName}"/>
<#assign idName="${table.primaryKey.javaName}"/>
import request from '@/utils/request'
<#if DUIEntityMeta?? && DUIEntityMeta.editableEntity>

export function get(${idName}) {
  return request.get(`${requestPath}/${'$'}{${idName}}.json`)
}

export function update(data) {
  return request.post(`${requestPath}/${'$'}{data.${idName}}`, data<#if DUIEntityMeta.hasFileField()==true>, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }</#if>)
}
<#else>
import qs from 'qs'

export function getList(page, queryFormModel) {
  const params = Object.assign({
    page: page.pageNo,
    pageSize: page.pageSize
  }, queryFormModel || {})
  return request.get('${requestPath}.json', {
    params
  })
}

export function get(${idName}) {
  return request.get(`${requestPath}/${'$'}{${idName}}.json`)
}

export function update(data) {
  return request.put(`${requestPath}/${'$'}{data.${idName}}`, data<#if DUIEntityMeta.hasFileField()==true>, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }</#if>)
}

<#if DUIEntityMeta.isTree()==true>
export function getTree() {
  return request.get('${requestPath}Tree')
}

export function loadTree(data) {
  return request.get('${requestPath}LoadTree', {
    params: data
  })
}

</#if>
export function add(data) {
  return request.post('${requestPath}.json', data<#if DUIEntityMeta.hasFileField()==true>, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }</#if>)
}

export function remove(${idName}s) {
  return request.post('${requestPath}.json', qs.stringify(
    {
      _method: 'delete',
      ${idName}s: ${idName}s
    },
    { arrayFormat: 'repeat' }
  ))
}
</#if>
