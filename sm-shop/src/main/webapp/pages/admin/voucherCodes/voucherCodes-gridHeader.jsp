<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	

	{title:"<s:message code="label.entity.id" text="Id"/>", name:"id", width:50},
	{title:"<s:message code="label.entity.voucherId" text="voucherId"/>", name:"voucherId", width:50},
	{title:"<s:message code="label.entity.code" text="Code"/>", name:"code"},
    {title:"<s:message code="label.entity.index" text="Index"/>", name:"index", width:120},
    {title:"<s:message code="label.entity.customerId " text="customerId "/>", name:"customerId ", width:120},
    {title:"<s:message code="label.entity.used" text="used"/>", name:"used", width:120},
    {title:"<s:message code="label.entity.used" text="redeem"/>", name:"redeem", width:120},
    {title:"<s:message code="label.entity.orderId" text="orderId"/>", name:"orderId", width:100,canFilter:false},
    {title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false,canSort:false, canReorder:false}
    