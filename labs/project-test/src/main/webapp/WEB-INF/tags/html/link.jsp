<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<a data-method="${__tag__LinkTag.dataMethod}" data-confirm="${not empty __tag__LinkTag.dataConfirm?__tag__LinkTag.dataConfirm:'确定要提交此操作？'}" href="${__tag__LinkTag.href}" ${__tag__LinkTag.attributesHtml}> ${__tag__LinkTag.label}</a> 