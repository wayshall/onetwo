<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>
<#assign entityPackage="${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity"/>
<#assign mapperPackage="${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.mapper"/>
<#assign daoPackage="${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.dao"/>
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
  <resultMap id="${_tableContext.propertyName}Ext" type="${entityPackage}.${entityClassName}" extends="${mapperPackage}.${mapperClassName}.BaseResultMap">
    <result column="test" property="test" jdbcType="VARCHAR" />
  </resultMap>
  
  
  <select id="findPage"  resultMap="${_tableContext.propertyName}Ext">
       select  
    <#list table.columns as column> 
        <#if column.primaryKey>
            tb.${column.name}
        <#else>
            , tb.${column.name}
        </#if>
    </#list>
        from 
            ${table.name} tb
        <if test="_parameter != null" >
          <include refid="Example_Where_Clause" />
        </if>
        <if test="orderByClause != null" >
          order by ${'$'}{orderByClause}
        </if>
  </select>


  
  <select id="findById" resultMap="${_tableContext.propertyName}Ext" parameterType="${table.primaryKey.javaType.name}" >
    select 
        <#list table.columns as column> 
            <#if column.primaryKey>
                tb.${column.name}
            <#else>
                , tb.${column.name}
            </#if>
        </#list>
    from 
        ${table.name} tb
    where tb.${table.primaryKey.name} = ${'#'}{${idName}}
  </select>
  
  <sql id="Example_Where_Clause" >
    <where >
      <foreach collection="oredCriteria" item="criteria" separator="or" >
        <if test="criteria.valid" >
          <trim prefix="(" suffix=")" prefixOverrides="and" >
            <foreach collection="criteria.criteria" item="criterion" >
              <choose >
                <when test="criterion.noValue" >
                  and tb.${'$'}{criterion.condition}
                </when>
                <when test="criterion.singleValue" >
                  and tb.${'$'}{criterion.condition} ${'#'}{criterion.value}
                </when>
                <when test="criterion.betweenValue" >
                  and tb.${'$'}{criterion.condition} ${'#'}{criterion.value} and ${'#'}{criterion.secondValue}
                </when>
                <when test="criterion.listValue" >
                  and tb.${'$'}{criterion.condition}
                  <foreach collection="criterion.value" item="listItem" open="(" close=")" separator="," >
                    ${'#'}{listItem}
                  </foreach>
                </when>
              </choose>
            </foreach>
          </trim>
        </if>
      </foreach>
    </where>
  </sql>
  
</mapper>