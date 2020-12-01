<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	



	{title:"<s:message code="label.entity.id" text="Id"/>", name:"orderId", width:50},
    {title:"<s:message code="label.order.date" text="Date"/>", name:"date"},
    {title:"<s:message code="label.purchased" text="Purchased"/>", name:"purchased",type:"boolean", canFilter:false},
    {title:"<s:message code="label.customer.name" text="Customer"/>", name:"customer"},
    {title:"<s:message code="label.order.totals" text="Total"/>", name:"amount", align: "right", canFilter:false},
    {title:"<s:message code="label.entity.fromDate" text="From date"/>", name:"startDate"},
    {title:"<s:message code="label.entity.toDate" text="To date"/>", name:"endDate"},
    {title:"<s:message code="label.generic.phone" text="Phone"/>", name:"phone", align: "right"},
    {title:"<s:message code="label.generic.address" text="Address"/>", name:"address"},
    {title:"<s:message code="label.entity.status" text="Status"/>", name:"status"},
    {title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false,canSort:false, canReorder:false}
    
