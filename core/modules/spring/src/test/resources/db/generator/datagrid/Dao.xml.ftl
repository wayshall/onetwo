<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign entityPackage="com.yooyo.zhiyetong.${_globalConfig.getModuleName()}.entity"/>
<#assign mapperPackage="com.yooyo.zhiyetong.${_globalConfig.getModuleName()}.mapper"/>
<#assign daoPackage="com.yooyo.zhiyetong.${_globalConfig.getModuleName()}.dao"/>
<#assign daoClassName="${_tableContext.className}Dao"/>
<#assign daoPropertyName="${_tableContext.propertyName}Dao"/>
<#assign entityClassName="${_tableContext.className}ExtEntity"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>
<#assign mapperClassName="${_tableContext.className}Mapper"/>
<#assign mapperPropertyName="${_tableContext.propertyName}Mapper"/>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${daoPackage}.${daoClassName}" >
  <resultMap id="${_tableContext.propertyName}Ext" type="${entityPackage}.${entityClassName}" extends="${mapperPackage}.BaseResultMap">
    <result column="test" property="test" jdbcType="VARCHAR" />
  </resultMap>
  
  
  <select id="findPage"  resultMap="${_tableContext.propertyName}Ext">
       select 
            tb.* from ${table.name} tb
        where tb.${table.primaryKey.name}= ${'#'}{${idName}}
        <if test="${idName}!=null">
                and est.${table.primaryKey.name} like ${'#'}{idName}
        </if>
  </select>
  
</mapper>