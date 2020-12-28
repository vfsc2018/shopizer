<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	



	{title:"<s:message code="label.entity.id" text="Id"/>", name:"transactionId", width:50},
    {title:"<s:message code="label.generic.transactionDate" text="transactionDate"/>", name:"transactionDate", width:120},
    {title:"<s:message code="label.entity.transactionType" text="transactionType"/>", name:"transactionType", width:100,canFilter:false},
    {title:"<s:message code="label.generic.paymentType" text="paymentType"/>", name:"paymentType", width:100,canFilter:false},
    {title:"<s:message code="label.generic.amount" text="transactionAmount"/>", name:"transactionAmount", width:60, align: "right",canFilter:false},
    {title:"<s:message code="label.entity.details" text="transactionDetails"/>", name:"transactionDetails"},
    