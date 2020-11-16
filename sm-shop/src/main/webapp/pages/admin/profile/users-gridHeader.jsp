<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page session="false" %>	

{title:"<s:message code="label.entity.id" text="Id"/>", name:"userId",canFilter:true},
{title:"<s:message code="label.generic.username" text="Username"/>", name:"username",canFilter:true},
{title:"<s:message code="label.generic.email" text="Email"/>", name:"email",canFilter:true},
{title:"<s:message code="label.customer.datecreated" text="Date"/>", name:"date",canFilter:false},
{title:"<s:message code="label.entity.active" text="Active"/>", name:"active", canFilter:true}
<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
,
{title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false,canSort:false, canReorder:false}
</sec:authorize> 