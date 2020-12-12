<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	



    {title:"<s:message code="label.entity.id" text="Id"/>", name:"id", width:50},
    {title:"<s:message code="label.entity.time" text="Time"/>", name:"date", canFilter:false},
    {title:"<s:message code="label.entity.customer" text="Customer"/>", name:"customer"},
    
    {title:"<s:message code="label.notification.message" text="Message"/>", name:"message"},
    {title:"<s:message code="label.customer.topic" text="Topic"/>", name:"topic"},
    {title:"<s:message code="label.generic.read" text="Read"/>", name:"read", type:"boolean", canFilter:false},
    {title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false,canSort:false, canReorder:false}
    
