<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<layout:extends>	

 <layout:override name="main-content">
 	<c:choose>
		<c:when test="${helper.login}">
				已经登录：${helper.currentUserLogin.userName} <a data-method="post" href="${siteConfig.baseURL}/logout" data-confirm="确定要注销？">注销</a>
		</c:when>
		<c:otherwise>
			<form:form modelAttribute="login" action="${siteConfig.baseURL}/login" method="post" cssClass="form-horizontal">
				<fieldset>
   			 	<legend>请先登录 ${message}</legend>
   			 	
   			 	<div class="control-group">
      				<label class="control-label" for="userName">用户名：</label>
			      <div class="controls">
					<form:input id="userName" path="userName" cssClass="input-xlarge"/>
			        <p class="help-block">
			        	字母，数字，汉字皆可
			        	<form:errors path="userName" cssClass="error"/>
			        </p>
			      </div>
			    </div>
			    
   			 	<div class="control-group">
      				<label class="control-label" for="userPassword">密码：</label>
			      <div class="controls">
					<form:password path="userPassword" cssClass="input-xlarge"/>
			        <p class="help-block">
			        	<form:errors path="userName" cssClass="error"/>
			        </p>
			      </div>
			    </div>
			    
			    <div class="form-actions">
		            <button type="submit" class="btn-large btn-primary">&nbsp;&nbsp;登&nbsp;&nbsp;录&nbsp;&nbsp;</button>
		          </div>
		          
				</fieldset>
			</form:form>
				
			</c:otherwise>
	</c:choose>
</layout:override>
</layout:extends>
