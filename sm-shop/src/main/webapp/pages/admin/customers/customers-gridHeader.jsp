<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	



		{title:"<s:message code="label.entity.id" text="Id"/>", name:"id", width:40},
		{title:"<s:message code="label.customer.name" text="Customer"/>", name:"firstName"},
		{title:"<s:message code="label.generic.phone" text="Phone"/>", name:"phone"},
		{title:"<s:message code="label.generic.address" text="Address"/>", name:"address"},
		{title:"<s:message code="label.customer.datecreated" text="Date"/>", name:"date"},
		{title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false}