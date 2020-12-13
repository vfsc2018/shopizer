<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
<%@ page session="false" %>				
				

<link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
<script src="<c:url value="/resources/js/ckeditor/ckeditor.js" />"></script>
<script src="<c:url value="/resources/js/jquery.formatCurrency-1.4.0.js" />"></script>
<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>
<script src="<c:url value="/resources/js/json2.js" />"></script>
<script src="<c:url value="/resources/js/adminFunctions.js" />"></script>

<script src="<c:url value="/resources/js/jquery.showLoading.min.js" />"></script>
<link href="<c:url value="/resources/css/showLoading.css" />" rel="stylesheet">	


<div class="tabbable">

		<jsp:include page="/common/adminTabs.jsp" />
					
		<div class="tab-content">

  		<div class="tab-pane active" id="order-section">

		<div class="sm-ui-component">	
		   		<h3><s:message code="label.detail.notification.title" text="Detail" /> (<c:out value="${notification.id}" />)</h3>	
		   		<br/>

					
      				<div class="control-group">
      							<div class="row-fluid">
					                  <Strong><s:message code="label.entity.orderId" text="Order"/>:</Strong>	 
					                  <c:out value="${notification.order.id}" /> 														
					           </div>
      				</div>
      				
      				<div class="control-group">
      							<div class="row-fluid">
					                  <Strong><s:message code="label.entity.time" text="Time"/>:</Strong>	 
					                  <c:out value="${notification.auditSection.dateCreated}" />
					           </div>
      				</div>
      				      				
      				<div class="control-group">
      							<div class="row-fluid">
					                  <Strong><s:message code="label.entity.customer" text="Customer"/>:</Strong>	 
					                  <c:out value="${notification.customer.billing.firstName}" />
					           </div>
      				</div>
      				      				
      				<div class="control-group">
      							<div class="row-fluid">
					                  <Strong><s:message code="label.customer.topic" text="Topic"/>:</Strong>
					                  <div><c:out value="${notification.topic}" /></div>
					           </div>
      				</div>   
      				<div class="control-group">
      							<div class="row-fluid">
					                  <Strong><s:message code="label.notification.message" text="ID"/>:</Strong>
					                  <div>	 
					                  <c:out value="${notification.message}" />
					                  </div>
					           </div>
      				</div>  
      				

      </div>
	 </div>
  </div>
</div>

