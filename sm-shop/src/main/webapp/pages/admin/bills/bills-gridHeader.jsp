<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	



	{title:"<s:message code="label.entity.id" text="Id"/>", name:"id"},
	{title:"<s:message code="label.entity.orderId" text="orderId"/>", name:"orderId"},
    {title:"<s:message code="label.order.date" text="Date"/>", name:"date", canFilter:false},
    
    {title:"<s:message code="label.order.total" text="Total"/>", name:"total", canFilter:false},
    {title:"<s:message code="label.order.customer" text="Customer"/>", name:"customer", canFilter:false},
    {title:"<s:message code="label.order.telephone" text="Telephone"/>", name:"telephone", canFilter:false},
    {title:"<s:message code="label.order.address" text="Address"/>", name:"address", canFilter:false},
    
    {title:"<s:message code="label.entity.status" text="Status"/>", name:"status"},
    {title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false,canSort:false, canReorder:false}
    
