<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page session="false" %>				
				


	<script type="text/javascript">
	

	
	$(function(){
		if($("#adminName").val()=="") {
			$('.btn').addClass('disabled');
		}

	    
	    
	});
	
	
	$(document).ready(function() {
		
		
	    //reset password link
	    $('a[href="#resetPassword"]').click(function(){
	    	var customerId = this.id;
			$('#confirmModal').modal();
		});
		
	    //set credentials link
	    $('a[href="#setCredentials"]').click(function(){
	    	var customerId = this.id;
			$('#credentialsModal').modal();
		});		
		
		
	});
	
	function validateCode() {
		$('#checkCodeStatus').html('<img src="<c:url value="/resources/img/ajax-loader.gif" />');
		$('#checkCodeStatus').show();
		var adminName = $("#adminName").val();
		var id = $("#id").val();
		checkCode(adminName,id,'<c:url value="/admin/users/checkUserCode.html" />');
	}
	
	function callBackCheckCode(msg,code) {
		
		if(code==0) {
			$('.btn').removeClass('disabled');
		}
		if(code==9999) {

			$('#checkCodeStatus').html('<font color="green"><s:message code="message.code.available" text="This code is available"/></font>');
			$('#checkCodeStatus').show();
			$('.btn').removeClass('disabled');
		}
		if(code==9998) {

			$('#checkCodeStatus').html('<font color="red"><s:message code="message.code.exist" text="This code already exist"/></font>');
			$('#checkCodeStatus').show();
			$('.btn').addClass('disabled');
		}
		
	}
	

	
	
	
	

	function resetCustomerPassword(username){
			$('#customerError').hide();
			$('#customerSuccess').hide();
			$('#confirmationInnerBox').showLoading({
	                'indicatorZIndex' : 1000001,
	                'overlayZIndex': 1000000
			})
			$.ajax({
			  type: 'POST',
			  url: '<c:url value="/admin/users/resetPasswordForUsers.html"/>',
			  data: 'username=' + username,
			  dataType: 'json',
			  success: function(response){
				   $('#confirmationInnerBox').hideLoading();
				   $('#confirmModal').modal('hide');
					var status = isc.XMLTools.selectObjects(response, "/response/status");
					if(status==0 || status ==9999) {
						
						$('#customerSuccess').html('<s:message code="message.password.reset" text="Password has been reset" />');
						$('#customerSuccess').show();
						
					} else {
						$('#customerError').html('<s:message code="message.error" text="An error occured" />');
						$('#customerError').show();
					}


			  
			  },
			  error: function(xhr, textStatus, errorThrown) {
			  	//alert('error ' + errorThrown);
			  	$('#confirmationInnerBox').hideLoading();
				$('#confirmModal').modal('hide');
			  	$('#customerError').html('<s:message code="message.error" text="An error occured" />');
				$('#customerError').show();
			  }
			  
			});
	}


	function setCredentials(customerId, userName, password){
		$('#customerError').hide();
		$('#customerSuccess').hide();
		$('#crConfirmationInnerBox').showLoading({
	            'indicatorZIndex' : 1000001,
	            'overlayZIndex': 1000000
		})
		$.ajax({
		  type: 'POST',
		  url: '<c:url value="/admin/users/setCredentials.html"/>',
		  data: 'customerId=' + customerId + '&userName=' + userName + '&password=' + password,
		  dataType: 'json',
		  success: function(response){
			   $('#crConfirmationInnerBox').hideLoading();
			   $('#confirmModal').modal('hide');
				var status = isc.XMLTools.selectObjects(response, "/response/status");
				if(status==0 || status ==9999) {
					
					$('#customerSuccess').html('<s:message code="message.credentials.reset" text="Credentials have been changed" />');
					$('#customerSuccess').show();
					
				} else {
					$('#customerError').html('<s:message code="message.error" text="An error occured" />');
					$('#customerError').show();
				}


		  
		  },
		  error: function(xhr, textStatus, errorThrown) {
		  	//alert('error ' + errorThrown);
		  	$('#confirmationInnerBox').hideLoading();
			$('#confirmModal').modal('hide');
		  	$('#customerError').html('<s:message code="message.error" text="An error occured" />');
			$('#customerError').show();
		  }
		  
		});
	}

	</script>



<div class="tabbable">

					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">

    					<div class="tab-pane active" id="catalogue-section">



								<div class="sm-ui-component">	
								
								
				<h3>
					<c:choose>
						<c:when test="${user.id!=null && user.id>0}">
								<s:message code="label.user.edituser" text="Edit user" /> <c:out value="${user.adminName}"/>
						</c:when>
						<c:otherwise>
								<s:message code="label.user.createuser" text="Create user" />
						</c:otherwise>
					</c:choose>
					
				</h3>	
				<br/>
				
				
				
				<c:if test="${user.id!=null && user.id>0}">
				<div class="btn-group" style="z-index:400000;">
                    <button class="btn btn-info dropdown-toggle" data-toggle="dropdown"><s:message code="label.generic.moreoptions" text="More options"/> ... <span class="caret"></span></button>
                     <ul class="dropdown-menu">
				    	<li><a id="${user.id}" href="#resetPassword"><s:message code="button.label.resetpassword" text="Reset password" /></a></li>
				    	<li><a id="${user.id}" href="#setCredentials"><s:message code="button.label.setcredentials" text="Set credentials" /></a></li>
                     </ul>
                </div><!-- /btn-group -->
			    <br/>
				</c:if>
				
					
					
					
				<c:url var="userSave" value="/admin/users/save.html"/>
					

				<form:form method="POST" modelAttribute="user" action="${userSave}">

      							
      				<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
								

                  <div class="control-group">
                        <label><s:message code="label.generic.username" text="User name"/></label>
	                        <div class="controls">
	                        		<form:input cssClass="input-large highlight" path="adminName" onblur="validateCode()"/>
	                                <span class="help-inline"><div id="checkCodeStatus" style="display:none;"></div><form:errors path="adminName" cssClass="error" /></span>
	                        </div>
                  </div>
                  
                  <div class="control-group">
                      <label><s:message code="label.store.title" text="Store"/> </label>
                      <div class="controls">
                          <form:select cssClass="" items="${stores}" itemValue="id" itemLabel="code" path="merchantStore.id"/>
                              <span class="help-inline"><form:errors path="merchantStore" cssClass="error" /></span>
                      </div>
                  </div>

                  <div class="control-group">
	                  <label><s:message code="label.user.email" text="Email"/></label>
	                  <div class="controls">
                   		  <form:input cssClass="input-large highlight" path="adminEmail"/>
                             <span class="help-inline"><form:errors path="adminEmail" cssClass="error" /></span>
	                  </div>
	       		  </div>

	       		  <c:if test="${user.id==null || user.id==0}">
                  <div class="control-group">
	                  <label><s:message code="label.generic.password" text="Password"/></label>
	                  <div class="controls">
                   		  <form:password cssClass="input-large highlight" path="adminPassword"/>
                             <span class="help-inline"><form:errors path="adminPassword" cssClass="error" /></span>
	                  </div>

	       		  </div>
                  </c:if>

                  
                  <div class="control-group">
                      <label><s:message code="label.generic.firstname" text="First name"/> </label>
                      <div class="controls">
                          <form:input cssClass="input-large" path="firstName"/>
                              <span class="help-inline"><form:errors path="firstName" cssClass="error" /></span>
                      </div>
                  </div>

                  <div class="control-group">
                      <label><s:message code="label.generic.lastname" text="Last name"/> </label>
                      <div class="controls">
                          <form:input cssClass="input-large" path="lastName"/>
                              <span class="help-inline"><form:errors path="lastName" cssClass="error" /></span>
                      </div>
                  </div>
                  
                  <div class="control-group">
                      <label><s:message code="label.defaultlanguage" text="Default language"/> </label>
                      <div class="controls">
                          <form:select cssClass="" items="${languages}" itemValue="id" itemLabel="code" path="defaultLanguage.id"/>
                              <span class="help-inline"><form:errors path="defaultLanguage" cssClass="error" /></span>
                      </div>
                  </div>
                  
                  <sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
                  <div class="control-group">
	                        <label><s:message code="label.groups.title" text="Groups"/></label>
	                        <div class="controls">
	                        	<form:checkboxes cssClass="highlight" items="${groups}" itemValue="id" itemLabel="groupName" path="groups" delimiter="<br/>" /> 
	                            <span class="help-inline"><form:errors path="groups" cssClass="error" /></span>
	                        </div>
	              </div>
	              </sec:authorize>
                  
                  
                  <sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
                  <div class="control-group">
                        	<label><strong></strong><s:message code="label.entity.active" text="Active"/></strong></label>
                        	<div class="controls">
                                    <form:checkbox path="active" />
                        	</div>
                  </div>
                  </sec:authorize>
                  
                  <div class="control-group">
                      <label><s:message code="security.question1" text="Question 1"/> </label>
                      <div class="controls">
                          <form:select cssClass="" items="${questions}" itemValue="label" itemLabel="label" path="question1"/>
                          <form:input cssClass="input-large" path="answer1"/>
                              <span class="help-inline"><form:errors path="answer1" cssClass="error" /></span>
                      </div>
                  </div>
                  
                  <div class="control-group">
                      <label><s:message code="security.question1" text="Question 2"/> </label>
                      <div class="controls">
                      	  <form:select cssClass="" items="${questions}" itemValue="label" itemLabel="label" path="question2"/>
                          <form:input cssClass="input-large" path="answer2"/>
                              <span class="help-inline"><form:errors path="answer2" cssClass="error" /></span>
                      </div>
                  </div>
                  
                  <div class="control-group">
                      <label><s:message code="security.question3" text="Question 3"/> </label>
                      <div class="controls">
                      	  <form:select cssClass="" items="${questions}" itemValue="label" itemLabel="label" path="question3"/>
                          <form:input cssClass="input-large" path="answer3"/>
                              <span class="help-inline"><form:errors path="answer3" cssClass="error" /></span>
                      </div>
                  </div>

                  <form:hidden path="id"/>
                  <c:if test="${user.id!=null && user.id>0}">
                  <form:hidden path="adminPassword"/>
                  </c:if>
			
			      <div class="form-actions">

                  		<div class="pull-right">
                  			<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  		</div>

            	 </div>

            	 </form:form>
	      			     
      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>		  
				
				
				
				

<div id="confirmModal"  class="modal hide" style="z-index:600000" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <span id="confirmationInnerBox">
  <div class="modal-header">
          <button type="button" class="close close-modal" data-dismiss="modal" aria-hidden="true">X</button>
          <h3 id="modalTitle"><s:message code="label.generic.confirm" text="Please confirm!" /></h3>
  </div>
  <div class="modal-body">
           <p id="modalMessage">
				<s:message code="label.customer.resetpasswor.confirm" text="Are you sure you want to reset the customer password?" />
           </p>
  </div>  
  <div class="modal-footer">
  
  		   <button class="btn btn-primary" aria-hidden="true"
	  		   	onClick="resetCustomerPassword( $('#adminName').val() );" >
	  		   	<s:message  code="button.label.ok" text="-" />
	  	   </button>
  		   	
  		   	
           <button class="btn cancel-modal" data-dismiss="modal" aria-hidden="true"><s:message code="button.label.cancel" text="Cancel" /></button>
           <button class="btn btn-success close-modal" id="closeModal" data-dismiss="modal" aria-hidden="true" style="display:none;"><s:message code="button.label.close" text="Close" /></button>
  </div>
  </span>
</div>


<div id="credentialsModal"  class="modal hide" style="z-index:650000" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <span id="crConfirmationInnerBox">
  <div class="modal-header">
          <button type="button" class="close close-modal" data-dismiss="modal" aria-hidden="true">X</button>
          <h3 id="modalTitle"><s:message code="button.label.setcredentials" text="Set credentials" /></h3>
  </div>
  <div class="modal-body">

           <p id="modalMessage">
           		    <label>
           		    	<s:message code="label.generic.username" text="User Name"/>
           		    </label>&nbsp;<input type="text" id="crUserName" name="crUserName" class="input-small">            		    
           </p>
           <p id="modalMessage">
                    <label>
           		    	<s:message code="label.generic.password" text="Password"/>
           		    </label>&nbsp;<input type="text" id="crPassword" name="crPassworde" class="input-small"> 
           </p>
  </div>  
  <div class="modal-footer">
  
  		   <button class="btn btn-primary" aria-hidden="true"
	  		   	onClick="setCredentials( $('#customerId').val(), $('#crUserName').val(), $('#crPassword').val() );" >
	  		   	<s:message  code="button.label.submit2" text="Submit" />
	  	   </button>
  		   	
  		   	
           <button class="btn cancel-modal" data-dismiss="modal" aria-hidden="true"><s:message code="button.label.cancel" text="Cancel" /></button>
           <button class="btn btn-success close-modal" id="closeModal" data-dismiss="modal" aria-hidden="true" style="display:none;"><s:message code="button.label.close" text="Close" /></button>
  </div>
  </span>
</div>

				