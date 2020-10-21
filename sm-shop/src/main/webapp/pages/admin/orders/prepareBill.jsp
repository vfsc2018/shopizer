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

function displayErrorMessage(message) {
	
}


function getZones(listDiv, textDiv, countryCode, defaultValue){
	$.ajax({
	  type: 'POST',
	  url: '<c:url value="/shop/reference/provinces.html"/>',
	  data: 'countryCode=' + countryCode + '&lang=${requestScope.LANGUAGE.code}',
	  dataType: 'json',
	  success: function(response){
			var status = response.response.status;
			var data = response.response.data;
			//console.log(status);
			if((status==0 || status ==9999) && data) {
				//console.log(data);
				if(data && data.length>0) {
					$(listDiv).show();  
					$(textDiv).hide();
					$(listDiv).addItems(listDiv, data, defaultValue);		
				} else {
					$(listDiv).hide();             
					$(textDiv).show();
					if(defaultValue!=null || defaultValue !='') {
						$(textDiv).val(defaultValue);
					}
				}
			} else {
				$(listDiv).hide();             
				$(textDiv).show();
			}
	  },
	    error: function(xhr, textStatus, errorThrown) {
	  	alert('error ' + errorThrown);
	  	$('.sm').hideLoading();
	  }

	});
	
}


$.fn.addItems = function(div, data, defaultValue) {
	//console.log('Populating div ' + div + ' defaultValue ' + defaultValue);
	var selector = div + ' > option';
	var defaultExist = false;
    $(selector).remove();
        return this.each(function() {
            var list = this;
            $.each(data, function(index, itemData) {
            	//console.log(itemData.code + ' ' + defaultValue);
            	if(itemData.code==defaultValue) {
            		defaultExist = true;
            	}
                var option = new Option(itemData.name, itemData.code);
                list.add(option);
            });
            if(defaultExist && (defaultValue!=null && defaultValue!='')) {
           	 	$(div).val(defaultValue);
            }
     });
};

function listTransactions(orderId){

	$.ajax({
		  type: 'GET',
		  url: '<c:url value="/admin/orders/listTransactions.html"/>?id=' + orderId,
		  dataType: 'json',
		  success: function(response){
				var status = response.response.status;
				var data = response.response.data;
				console.log(status);
				if((status==0 || status ==9999) && data) {
					//console.log(data);
					$('#transactionsModal').modal();
					var transactions = data;
					//console.log(transactions);
					for(i=0;i<transactions.length;i++) {
						var tr = '<tr><td>' + transactions[i].transactionId + '</td><td>' + transactions[i].transactionDate + '</td><td>' + transactions[i].transactionType + '</td><td>' + transactions[i].transactionAmount + '</td><td>' + JSON.stringify(transactions[i].transactionDetails) + '</td>';
						$('#transactionList').append(tr);
					}
				} 
		  },
		    error: function(xhr, textStatus, errorThrown) {
		  	alert('error ' + errorThrown);
		  	$('.sm').hideLoading();
		  }
	
	});
}

function sendInvoice(orderId){

	$.ajax({
		  type: 'GET',
		  url: '<c:url value="/admin/orders/sendInvoice.html"/>?id=' + orderId,
		  dataType: 'json',
		  success: function(response){
				var status = response.response.status;
				var data = response.response.data;
				//console.log(status);
				if(status==0 || status ==9999) {
					$(".alert-success").show();
				} else {
					$(".alert-error").show();
				}
				$('.sm').hideLoading();
		  },
		    error: function(xhr, textStatus, errorThrown) {
		  	alert('error ' + errorThrown);
		  	$('.sm').hideLoading();
		  }
	
	});
}

function updateStatus(orderId){

	$.ajax({
		  type: 'GET',
		  url: '<c:url value="/admin/orders/updateStatus.html"/>?id=' + orderId,
		  dataType: 'json',
		  success: function(response){
				var status = response.response.status;
				var data = response.response.data;
				//console.log(status);
				if(status==0 || status ==9999) {
					$(".alert-success").show();
				} else {
					$(".alert-error").show();
				}
				$('.sm').hideLoading();
		  },
		    error: function(xhr, textStatus, errorThrown) {
		  	alert('error ' + errorThrown);
		  	$('.sm').hideLoading();
		  }
	
	});
}

function resetMessages() {
	$(".alert-error").hide();
	$(".alert-success").hide();
}


function sendDownloadEmail(orderId){

	$.ajax({
		  type: 'GET',
		  url: '<c:url value="/admin/orders/sendDownloadEmail.html"/>?id=' + orderId,
		  dataType: 'json',
		  success: function(response){
				var status = response.response.status;
				var data = response.response.data;
				//console.log(status);
				if(status==0 || status ==9999) {
					$(".alert-success").show();
				} else {
					$(".alert-error").show();
				}
				$('.sm').hideLoading();
		  },
		    error: function(xhr, textStatus, errorThrown) {
		  	alert('error ' + errorThrown);
		  	$('.sm').hideLoading();
		  }
	
	});
}

function captureOrder(orderId){
	$.ajax({
		  type: 'POST',
		  url: '<c:url value="/admin/orders/captureOrder.html"/>?id=' + orderId,
		  dataType: 'json',
		  success: function(response){
				var status = response.response.status;
				var data = response.response.data;
				//console.log(status);
				if(status==0 || status ==9999) {
					$(".alert-success").show();
					window.location='<c:url value="/admin/orders/editOrder.html" />?id=' + orderId;
				} else {
					$(".alert-error").show();
				}
				$('.sm').hideLoading();
				$('#captureAction').removeClass('disabled');
		  },
		    error: function(xhr, textStatus, errorThrown) {
		  	alert('error ' + errorThrown);
		  	$('.sm').hideLoading();
		  }
	
	});
}

	$(document).ready(function(){ 
	
		$("#refundAction").click(function() {
			resetMessages();
			$('#refundModal').modal();
		}); 
		
		$("#transactionsAction").click(function() {
			resetMessages();
			listTransactions('<c:out value="${order.order.id}"/>');
		});
		
		$("#sendInvoiceAction").click(function() {
			resetMessages();
			$('.sm').showLoading();
			sendInvoice('<c:out value="${order.order.id}"/>');
		});
		
		$("#updateStatusAction").click(function() {
			resetMessages();
			$('.sm').showLoading();
			updateStatus('<c:out value="${order.order.id}"/>');
		});
		
		$("#updateDownloadsAction").click(function() {
			resetMessages();
			$('.sm').showLoading();
			sendDownloadEmail('<c:out value="${order.order.id}"/>');
		});
		
		$("#captureAction").click(function() {
			resetMessages();
			$('#captureAction').addClass('disabled');
			$('.sm').showLoading();
			captureOrder('<c:out value="${order.order.id}"/>');
		});
		
		$(".close-modal").click(function() {
			 location.href="<c:url value="/admin/orders/editOrder.html" />?id=<c:out value="${order.order.id}"/>";
		}); 
		
		$(".billing-country-list").change(function() {
			getZones('#billingZoneList','#billingZoneText',$(this).val(),'<c:out value="${order.billing.zone.code}" />');
	    })
		
		
		<c:if test="${order.billing.state!=null && order.billing.state!=''}">
			$('#billingZoneList').hide();          
			$('#billingZoneText').show(); 
			$('#billingZoneText').val('<c:out value="${order.billing.state}"/>');
		</c:if>

		<c:if test="${order.billing.state==null || order.billing.state==''}"> 
			$('#billingZoneList').show();           
			$('#billingZoneText').hide();
			getZones('#billingZoneList','#billingZoneText','<c:out value="${order.billing.country.isoCode}" />','<c:out value="${order.billing.zone.code}" />'); 
		</c:if>
		
		<c:if test="${order.delivery.state!=null && order.delivery.state!=''}">  
			$('#shippingZoneList').hide();  
			$('#shippingZoneText').show(); 
			$('#shippingZoneText').val('<c:out value="${order.delivery.state}"/>');
		</c:if>
		<c:if test="${order.delivery.state==null || order.delivery.state==''}">
			$('#shippingZoneList').show();			
			$('#shippingZoneText').hide();
			getZones('#shippingZoneList','#shippingZoneText','<c:out value="${order.delivery.country.isoCode}" />','<c:out value="${order.delivery.zone.code}" />');
		</c:if>
		
		
		
	}); 


    $(function() {

        $("#refund").submit(function() {
        	$('#refundButton').addClass('disabled');
 			$('#refundModal').showLoading();
            var data = $(this).serializeObject();
            $.ajax({
                'type': 'POST',
                'url': "<c:url value="/admin/orders/refundOrder.html"/>",
                'contentType': 'application/json',
                'data': JSON.stringify(data),
                'dataType': 'json',
                'success': function(result) {
                   $('#refundModal').hideLoading();
                   var response = result.response;
                   var status = response.status;
                   if(status==0 || status ==9999) {
                	    //window.location='<c:url value="/admin/orders/editOrder.html" />?id=<c:out value="${order.order.id}" />';
                        $(".alert-success-modal").show();
                        //$(".close-modal").show();
                   } else { 
                        $(".alert-error-modal").html(response.statusMessage);
                        $(".alert-error-modal").show();
                   }
                   $('#refundButton').removeClass('disabled');
                }
            });
 
            return false;
        });
        /*Begin ducdv5*/
		$("#btPrepareBill").click(function() {
			 location.href="<c:url value="/admin/orders/editOrder.html" />?id=<c:out value="${order.order.id}"/>";
		}); 
		/*End ducdv5*/
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
</script>

<div class="tabbable">

		<jsp:include page="/common/adminTabs.jsp" />
					
		<div class="tab-content">

  		<div class="tab-pane active" id="order-section">

		<div class="sm-ui-component">	


		<h3>
			<div class="control-group">
                      <div class="controls">
                     		 <s:message code="label.order.preparebill" text="Prepare Bill"/> 
                     		 <c:out value="${order.order.id}" /> - <span class="lead"><s:message code="label.order.${order.order.status.value}" text="${order.order.status.value}" /></span>
                     		 <br>
                       </div>       
                  </div>
           </h3>
		<br/>
		<br/>
		    <div class="btn-group" style="z-index:400000;">
                    <button class="btn btn-info dropdown-toggle" data-toggle="dropdown"><s:message code="label.generic.moreoptions" text="More options"/> ... <span class="caret"></span></button>
                     <ul class="dropdown-menu">
				    	<li><a id="transactionsAction" href="#"><s:message code="label.order.transactions" text="Transactions list"/></a></li>
				    	<li><a id="sendInvoiceAction" href="#"><s:message code="label.order.sendinvoice" text="Send email invoice"/></a></li>
				    	<li><a id="updateStatusAction" href="#"><s:message code="label.order.updatestatus" text="Send order status email"/></a></li>
				    	<li>
				    		<c:if test="${downloads!=null}">
								<a id="updateDownloadsAction" href="#"><s:message code="label.order.downloademail" text="Send download email"/></a>
							</c:if>
				    	</li>
				    	
				    	<!--<li><a href="<c:url value="/admin/orders/printInvoice.html?id=${order.id}" />"><s:message code="label.order.printinvoice" text="Print invoice"/></a></li>-->
				    	<!-- available soon <li><a href="<c:url value="/admin/orders/printShippingLabel.html?id=${order.id}" />"><s:message code="label.order.packing" text="Print packing slip"/></a></li>-->
				    	<li>
				    		<c:if test="${customer!=null}">
								<a href="<c:url value="/admin/customers/customer.html?id=${customer.id}"/>"><s:message code="label.order.editcustomer" text="Edit customer"/></a>
							</c:if>
						</li>
						
				    	<li>
				    		<c:if test="${customer!=null}">
								<a href="<c:url value="/admin/orders/prepareBill.html?id=${order.id}"/>"><s:message code="label.order.preparebill" text="Prepare Bill"/></a>
							</c:if>
						</li>
						
												
                     </ul>
                	&nbsp;
                	<c:if test="${order.order.total>0}">
	            	<c:if test="${capturableTransaction!=null}">
	            		 <a id="captureAction" class="btn btn-primary btn-block" href="#"><s:message code="label.order.capture" text="Capture transaction"/></a>
	            	</c:if>
	            	<c:if test="${refundableTransaction!=null}">
	            		 <a id="refundAction" class="btn btn-danger btn-block" href="#"><s:message code="label.order.refund" text="Apply refund"/></a>
	            	</c:if>  
	            	</c:if>       
                     
              </div><!-- /btn-group -->
			  <br/>
 	       	 	
	     <c:url var="orderSaveBill" value="/admin/orders/saveBill.html"/>
         <form:form method="POST" enctype="multipart/form-data" modelAttribute="order" action="${orderSaveBill}">
	   
                <form:errors path="*" cssClass="alert alert-error" element="div" />
	                <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
	                <div id="store.error" class="alert alert-error" style="display:none;"><s:message code="message.error" text="An error occured"/></div>
		
		  			<form:hidden path="order.id" />
		  			<form:hidden path="order.customerId" />
 			
			 		<div class="span8"> 	
			 			<div class="span4" style="margin-left:0px;"> 
						<h6> <s:message code="label.customer.billinginformation" text="Billing information"/> </h6>
						</div>
				    </div>
	
      
      	  <div class="span8" style="margin-top:20px;">
		      <table class="table table-bordered table-striped"> 
					<thead> 
						<tr> 
							<th colspan="2" width="55%"><s:message code="label.order.item" text="Item"/></th> 
							<th colspan="1" width="15%"><s:message code="label.quantity" text="Quantity"/></th> 
							<th width="15%"><s:message code="label.order.price" text="Price"/></th>
							<th width="15%"><s:message code="label.order.total" text="Total"/></th>  
						</tr> 
					</thead> 
					
 				    <tbody> 
						<c:forEach items="${dataEx}" var="entity" varStatus="counter">	 
			            	
							<tr> 
								<td colspan="2"> 
									<c:out value="${entity.productName}" /> - <a href="<c:url value="/admin/products/viewEditProduct.html?sku=${entity.sku}"/>">
									<c:out value="${entity.sku}" /></a>
									
								</td> 
								<td ><c:out value="${entity.productQuantity}" /></td> 
			            		<td><strong><sm:monetary value="${entity.oneTimeCharge}" currency="${entity.currency}"/></strong> </td>
								<td><strong><sm:monetary value="${entity.total}" currency="${entity.currency}"/></strong></td> 
							</tr>
							<tr>
								<td colspan="6">
									<c:forEach items="${entity.relationships}" var="subEntity" varStatus="counter2">	 
										<div align="left">
										<a href="<c:url value="/admin/products/viewEditProduct.html?sku=${subEntity.sku}"/>">
												<c:out value="${subEntity.sku}" /> - <c:out value="${subEntity.productName}" />
										</a>
										</div>
									</c:forEach>
								</td>
							</tr> 
			
						</c:forEach> 
					
					 	<c:forEach items="${order.order.orderTotal}" var="orderTotal" varStatus="counter">	
							<tr class="subt"> 
								<td colspan="2">&nbsp;</td> 
								<td colspan="2" ><c:if test="${orderTotal.orderTotalCode=='refund'}"><font color="red"></c:if><s:message code="${orderTotal.orderTotalCode}" text="${orderTotal.orderTotalCode}"/><c:if test="${orderTotal.orderTotalCode=='refund'}"></font></c:if></td> 
								<td ><strong><c:if test="${orderTotal.orderTotalCode=='refund'}"><font color="red"></c:if><sm:monetary value="${orderTotal.value}" currency="${order.order.currency}"/><c:if test="${orderTotal.orderTotalCode=='refund'}"></font></c:if></strong></td> 
							</tr> 
						</c:forEach> 	 
					</tbody>    
				</table>
    	  </div>  

            <br/>   
            <div class="span8">
	              <div class="form-actions">
	              		<button  type="button" id="btBuildBill" onclick="javascript:alert('Call API Dang lam tiep.......')" class="btn btn-medium btn-primary" ><s:message code="button.label.build.bill" text="Build Bill"/></button>
	      		  </div>
      		</div> 
            <br/>   
    
    	  </div>
   
   		</form:form>       

      </div>
	 </div>
  </div>




<div id="transactionsModal"  class="modal hide" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="width:900px;z-index:500000;">
  <div class="modal-header">
          <button type="button" class="close close-modal" data-dismiss="modal" aria-hidden="true">X</button>
          <h3 id="myModalLabel"><s:message code="label.order.transactions" text="List of transactions" /></h3>
  </div>
    <div class="modal-body">
           <p>
			<table class="table table-hover" style="font-size:10px;">
			<thead>
				<tr>
				<th><s:message code="label.entity.id" text="Id" /></th>
				<th><s:message code="label.generic.date" text="Date" /></th>
				<th><s:message code="label.entity.type" text="Type" /></th>
				<th><s:message code="label.entity.amount" text="Amount" /></th>
				<th><s:message code="label.entity.details" text="Details" /></th>
				</tr>
			</thead>
			<tbody id="transactionList">
			</tbody>
			</table>
           </p>
             
    </div>  
    <div class="modal-footer">
           <button class="btn btn-primary" id="closeModal" data-dismiss="modal" aria-hidden="true"><s:message code="button.label.close" text="Close" /></button>
    </div>
</div>

<div id="refundModal"  class="modal hide" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="z-index:500000;">
  <div class="modal-header">
          <button type="button" class="close close-modal" data-dismiss="modal" aria-hidden="true">X</button>
          <h3 id="myModalLabel"><s:message code="label.order.refund" text="Apply refund"/> - 
          		<s:message code="label.order.id2" text="Order ID"/> 
                <c:out value="${order.order.id}" />
           </h3>
  </div>
    <div class="modal-body">
    
    	  <div id="store.success" class="alert alert-success alert-success-modal" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
	      <div id="store.error" class="alert alert-error alert-error-modal" style="display:none;"><s:message code="message.error" text="An error occured"/></div>

           <p>
           		<s:message code="label.order.total" text="Total" />: <strong><c:out value="${order.order.total}"/></strong>
           		<span id="refundMessage" style="display:none;"><s:message code="" text=""/><span id="refundAmount"></span></span>
           </p>
           <p>
           		<form id="refund" class="form-inline">
           		    <label><s:message code="label.generic.amount" text="Amount" /></label>&nbsp;<input type="text" id="amount" name="amount" class="input-small" placeholder="<s:message code="label.generic.amount" text="Amount" />">
           		    <input name="orderId" id="orderId" type="hidden" value="<c:out value="${order.id}"/>">
           		    <button id="refundButton" type="submit" class="btn btn-danger"><s:message code="label.order.refund" text="Apply refund"/></button>
           		 </form>
           
           </p>
             
    </div>  
    <div class="modal-footer">
           <button class="btn btn-primary close-modal" id="closeModal" data-dismiss="modal" aria-hidden="true"><s:message code="button.label.close" text="Close" /></button>
    </div>
</div>