<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	

	{title:"<s:message code="label.entity.id" text="Id"/>", name:"id", width:50},
	{title:"<s:message code="label.entity.blocked" text="blocked"/>", name:"blocked", width:50},
    {title:"<s:message code="label.entity.approved" text="approved"/>", name:"approved", width:120},
    {title:"<s:message code="label.entity.startDate" text="startDate"/>", name:"startDate", width:120},
    {title:"<s:message code="label.entity.endDate" text="endDate"/>", name:"endDate", width:120},
    {title:"<s:message code="label.entity.expire" text="expire"/>", name:"expire", width:120},
    {title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false,canSort:false, canReorder:false}
    