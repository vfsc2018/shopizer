<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	



	{title:"<s:message code="label.entity.id" text="Id"/>", name:"orderId"},
	{title:"<s:message code="label.product.sku" text="Sku"/>", name:"sku"},
    {title:"<s:message code="label.product.name" text="Product name"/>", name:"productName"},
    {title:"<s:message code="label.order.date" text="Date"/>", name:"date", canFilter:false},
    {title:"<s:message code="label.entity.status" text="Status"/>", name:"status"},
    {title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false,canSort:false, canReorder:false}
    
