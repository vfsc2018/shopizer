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

<script>

    $(function() {

		$("#btSaveBill").click(function() {
			$( "#FormBuildBill" ).submit();
		});

    });
 
    $.fn.serializeObject = function() {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
    
    
    
    
    
    $(document).ready(function() {

        // process the form
        	$("#FormBuildBill").submit(function(event){
        	    event.preventDefault(); 
        	    var post_url = $(this).attr("action"); 
                var data = $(this).serializeObject();
                $.ajax({
                    'type': 'POST',
                    'url': "<c:url value="/admin/vouchercodes/save.html"/>",
                    'contentType': 'application/json',
                    'data': JSON.stringify(data),
                    'dataType': 'json',
                    'success': function(result) {
                       var response = result.response;
                       var status = response.status;
                       if(status==0 || status ==9999) {
                            alert("Cap nhat thanh cong");
                       } else { 
                            alert(response.statusMessage);
                       }
    
                    }
                });
                
           
        	});

    });
    
</script>

<div class="tabbable">

		<jsp:include page="/common/adminTabs.jsp" />
					
		<div class="tab-content">

  		<div class="tab-pane active" id="order-section">

		<div class="sm-ui-component">	


		<h3>
					<div class="control-group">
                      <div class="controls">
                     		 <s:message code="label.title.voucherCode.edit" text="Edit voucher"/>: <c:out value="${voucherCode.id}" />
                     		 <br>
                       </div>       
                  </div>
           </h3>
		   <br/>

         <form:form method="POST" id="FormBuildBill" modelAttribute="voucherCode" >
                <form:errors path="*" cssClass="alert alert-error" element="div" />
	                <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
	                <div id="store.error" class="alert alert-error" style="display:none;"><s:message code="message.error" text="An error occured"/></div>
					
					<input name="id" id="id" type="hidden" value="<c:out value="${voucherCode.id}"/>">
					<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.id" text="Id"/></label>	 
					                  <div class="controls"> 
					                        <c:out value="${voucherCode.id}" />      														
					                   </div>
					           </div>       				
      				</div>
					<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.voucherId" text="VoucherId"/></label>	 
					                  <div class="controls"> 
					                        <c:out value="${voucherCode.voucherId}" />      															
					                   </div>
					           </div>       				
      				</div>      				
      				
					<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.code" text="Code"/></label>	 
					                  <div class="controls"> 
					                        <c:out value="${voucherCode.code}" />      															
					                   </div>
					           </div>       				
      				</div>
      				
					<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.securecode" text="securecode"/></label>	 
					                  <div class="controls"> 
					                        <c:out value="${voucherCode.securecode}" />  												
					                   </div>
					           </div>       				
      				</div>
      				
      				
					<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.blocked" text="blocked"/></label>	 
					                  <div class="controls"> 
					                  	
					                  	<form:select path="blocked">
												<form:option value="1" label="Yes"/>
												<form:option value="0" label="No"/>
										</form:select>
										  													
					                   </div>
					           </div>       				
      				</div>
      				
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.blockMessage" text="blockMessage"/></label>	 
					                  <div class="controls"> 
					                        <form:input  cssClass="small" path="blockMessage"/>      	  															
					                   </div>
					           </div>       				
      				</div>
      				
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.customerId" text="customerId"/></label>	 
					                  <div class="controls"> 
					                        <c:out value="${voucherCode.customerId}" />  															
					                   </div>
					           </div>       				
      				</div>
      				      				
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.generic.used" text="used"/></label>	 
					                  <div class="controls"> 
					                        <c:out value="${voucherCode.used}" />  														
					                   </div>
					           </div>       				
      				</div>
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.generic.index" text="index"/></label>	 
					                  <div class="controls"> 
					                        <c:out value="${voucherCode.index}" />  														
					                   </div>
					           </div>       				
      				</div>      				
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.generic.redeem" text="redeem"/></label>	 
					                  <div class="controls"> 
					                        <c:out value="${voucherCode.redeem}" />  														
					                   </div>
					           </div>       				
      				</div>
      				<div class="span8">
							<div class="control-group">
				                  <label><s:message code="label.entity.orderId" text="orderId"/></label>	 
				                  <div class="controls"> 
				                       <c:out value="${voucherCode.orderId}" />  		    														
				                   </div>
				           </div>       				
      				</div>
		            <br/>   
		            <div class="span8">
			              <div class="form-actions">
			              		<button  type="button" id ="btSaveBill" class="btn btn-medium btn-primary" ><s:message code="button.label.submit" text="Save"/></button>	              		
			      		  </div>
		      		</div> 
            <br/>   
   		</form:form>       

      </div>
	 </div>
  </div>
</div>

