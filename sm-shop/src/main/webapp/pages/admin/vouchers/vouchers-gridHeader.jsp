<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	

    {title:"<s:message code="label.entity.id" text="id"/>", name:"id", width:50},
    {title:"<s:message code="label.entity.code" text="code"/>", name:"code"},
	{title:"<s:message code="label.entity.blocked" text="blocked"/>", name:"blocked", type:"boolean"},
    {title:"<s:message code="label.entity.approved" text="approved"/>", name:"approved", type:"boolean"},
    {title:"<s:message code="label.entity.startDate" text="startDate"/>", name:"startDate", width:90},
    {title:"<s:message code="label.entity.endDate" text="endDate"/>", name:"endDate", width:90},
    {title:"<s:message code="label.entity.voucher" text="voucher"/>", name:"voucher"},
    {title:"<s:message code="label.entity.product" text="product"/>", name:"product"},
    {title:"<s:message code="label.entity.manager" text="manager"/>", name:"manager"},
    {title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false,canSort:false, canReorder:false}
    