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
                    'url': "<c:url value="/admin/vouchers/save.html"/>",
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
                
                
        		
/*         	    $.ajax({
        	        url : post_url,
        	        type: request_method,
        	        data : form_data,
        			contentType: false,
        			cache: false,
        			processData:false,
	    			success: function(data){
	    					if(data.response.status==0 || data.response.status ==9999) {
	    						alert("Thanh cong");	
	    					}else{
	    						alert(data.response.statusMessage);
	    					}
	    			   		},
	    			error: function(xhr, textStatus, errorThrown) {
	    				  	alert('error ' + errorThrown);
	    			}
        	    }); */
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
                     		 <s:message code="label.title.voucher.edit" text="Edit voucher"/>: <c:out value="${voucher.id}" />
                     		 <br>
                       </div>       
                  </div>
           </h3>
		   <br/>
	<button  type="button" id ="btVoucherCode" class="btn btn-medium btn-primary" ><s:message code="button.label.voucherCode" text="Voucher code"/></button>
	
	
         <form:form method="POST" id="FormBuildBill" modelAttribute="voucher" >
                <form:errors path="*" cssClass="alert alert-error" element="div" />
	                <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
	                <div id="store.error" class="alert alert-error" style="display:none;"><s:message code="message.error" text="An error occured"/></div>
					
					<input name="id" id="id" type="hidden" value="<c:out value="${voucher.id}"/>">
					
					<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.code" text="Code"/></label>	 
					                  <div class="controls"> 
					                        <form:input  cssClass="input-large highlight" path="code"/>      														
					                   </div>
					           </div>       				
      				</div>
					<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.description" text="description"/></label>	 
					                  <div class="controls"> 
					                        <form:input  cssClass="input-large highlight" path="description"/>      														
					                   </div>
					           </div>       				
      				</div>
					<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.point" text="point"/></label>	 
					                  <div class="controls"> 
					                        <form:input  cssClass="small" path="point"/>      														
					                   </div>
					           </div>       				
      				</div>
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.percent" text="percent"/></label>	 
					                  <div class="controls"> 
					                        <form:input  cssClass="small" path="percent"/>      														
					                   </div>
					           </div>       				
      				</div>
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.discount" text="discount"/></label>	 
					                  <div class="controls"> 
					                        <form:input  cssClass="small" path="discount"/>      														
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
					                  <label><s:message code="label.entity.startDate" text="start date"/></label>	 
					                  <div class="controls"> 
					                        <form:input id="startDate" cssClass="small" path="startDate" data-date-format="<%=com.salesmanager.core.business.constants.Constants.DEFAULT_DATE_FORMAT%>"/>      
											<script type="text/javascript">
												$('#startDate').datepicker();
											</script>    
					                   </div>
					           </div>       				
      				</div>      				      				
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.endDate" text="end date"/></label>	 
					                  <div class="controls"> 
					                        <form:input id="endDate" cssClass="small" path="endDate" data-date-format="<%=com.salesmanager.core.business.constants.Constants.DEFAULT_DATE_FORMAT%>"/>      
											<script type="text/javascript">
												$('#endDate').datepicker();
											</script>    
					                   </div>
					           </div>       				
      				</div>

      				      				
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.generic.weekDays" text="weekDays"/></label>	 
					                  <div class="controls"> 
					                        <form:input  cssClass="input-large highlight" path="weekDays"/>      														
					                   </div>
					           </div>       				
      				</div>
      				
      				<div class="span8">
							<div class="control-group">
				                  <label><s:message code="label.generic.dayOfMonth" text="dayOfMonth"/></label>	 
				                  <div class="controls"> 
				                        <form:input  cssClass="small" path="dayOfMonth"/>      														
				                   </div>
				           </div>       				
      				</div>
      				<div class="span8">
							<div class="control-group">
				                  <label><s:message code="label.entity.startTime" text="startTime"/></label>	 
				                  <div class="controls"> 
				                        <form:input  cssClass="small" path="startTime"/>      														
				                   </div>
				           </div>       				
      				</div>
      				<div class="span8">
							<div class="control-group">
				                  <label><s:message code="label.entity.endTime" text="endTime"/></label>	 
				                  <div class="controls"> 
				                        <form:input  cssClass="small" path="endTime"/>      														
				                   </div>
				           </div>       				
      				</div>
      				
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.approved" text="approved"/></label>	 
					                  <div class="controls"> 
					                        <form:input id="approved" cssClass="input-large highlight" path="approved" data-date-format="<%=com.salesmanager.core.business.constants.Constants.DEFAULT_DATE_FORMAT%>"/>      
											<script type="text/javascript">
												$('#approved').datepicker();
											</script>    
					                   </div>
					           </div>       				
      				</div>    				
      				
      				<div class="span8">
							<div class="control-group">
				                  <label><s:message code="label.entity.manager" text="manager"/></label>	 
				                  <div class="controls"> 
				                        <form:input  cssClass="input-large highlight" path="manager"/>      														
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
<script>				
$(document).ready(function(){ 				
		$("#btVoucherCode").click(function() {
			 location.href="<c:url value="/admin/voucherCodes/list.html" />?voucherId=" + <c:out value="${voucher.id}" />;
		}); 
});
</script>	
