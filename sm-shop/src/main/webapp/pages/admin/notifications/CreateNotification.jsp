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

		$("#btSave").one('click', function() {
			$( "#FromCreate" ).submit();
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
        	$("#FromCreate").submit(function(event){
				if($("#message").val()=="" || $("#topic").val()=="") {
					alert('Please create new notification and type value of Topic & Message');
					return false;
				}
        	    event.preventDefault(); 
        	    var post_url = $(this).attr("action"); 
                var data = $(this).serializeObject();
                $.ajax({
                    'type': 'POST',
                    'url': "<c:url value="/admin/notifications/save.html"/>",
                    'contentType': 'application/json',
                    'data': JSON.stringify(data),
                    'dataType': 'json',
                    'success': function(result) {
                       var response = result.response;
                       var status = response.status;
                       if(status==0 || status ==9999) {
                            alert("Tao thong bao thanh cong");
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
                     		 <s:message code="label.notification.create" text="Create notification"/>
                       </div>       
                  </div>
           </h3>
		   <br/>

         <form:form method="POST" id="FromCreate" modelAttribute="notification" >
					<input name="id" id="id" type="hidden" value="0">  				
					
	

					<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.notification.customerId" text="customerId"/></label>	 
					                  <div class="controls"> 
					 						<form:input  cssClass="input-small" path="customerId"/>               															
					                   </div>
					           </div>       				
                      </div>
                      <div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.customer.topic" text="Topic"/></label>	 
					                  <div class="controls"> 
					                  		<form:input maxlength="65"  cssClass="input-large highlight" path="topic"/>          															
					                   </div>
					           </div>       				
      				</div>
                      <div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.notification.message" text="Message"/></label>	 
					                  <div class="controls"> 
					                  		<form:input maxlength="240" cssClass="input-large highlight" path="message"/>          															
					                   </div>
					           </div>       				
      				</div>
            <br/>   
            <div class="span8">
	            <div class="form-actions">
	              	<button  type="button" id ="btSave" class="btn btn-medium btn-primary" ><s:message code="button.label.submit2" text="Send"/></button>	              		
                
                </div>
      		</div> 
            <br/> 
            <br/>   
   		</form:form>       

      </div>
	 </div>
  </div>
</div>
