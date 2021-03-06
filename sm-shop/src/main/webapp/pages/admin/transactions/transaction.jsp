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
$(document).ready(function(){
		autoCaculator();
		$("#caculatorId input[name='code']").change(function(){
			findCode($(this).val(),this,'<c:out value="${order.order.id}"/>');
		});
		
		  $("#caculatorId input[name='oneTimeCharge']").change(function(){
			    var quantity = $(this).parent().parent().find("#quantity").val();
			    caculatorPrice($(this).val(),quantity,this);
		  });
		  
		  $("#caculatorId input[name='quantity']").change(function(){
		    var price = $(this).parent().parent().find("#oneTimeCharge").val();
		    caculatorPrice($(this).val(),price,this);
		  });

			  
});


function autoCaculator(){
	var totalTien=0;
	$("#caculatorId").find("#resultId").each(function() {
		$(this).text($(this).text().replaceAll('.00','')); 
		totalTien += parseInt($(this).text().replaceAll(',',''));
	});

}
function caculatorPrice(p1,p2,obj){
	$(obj).parent().parent().find("#resultId").html(p1*p2);
	autoCaculator();
}

function findCode(code,obj,orderId){
	var quantity = $(obj).parent().parent().find("#quantity").val();
	
	$.ajax({
	  type: 'POST',
	  url: '<c:url value="/admin/orders/validationCode.html"/>',
	  data: 'code=' + code+'&quantity='+quantity+'&orderId='+orderId,
	  dataType: 'json',
	  success: function(data){
		if(data!=null){
			$(obj).parent().parent().find("#quantity").val(data.productQuantity);
			$(obj).parent().parent().find("#oneTimeCharge").val(data.oneTimeCharge);
			$(obj).parent().parent().find("#resultId").html(data.total);
			autoCaculator();
		}else{
			alert(code+ "not found in store");
		}

	  },
	    error: function(xhr, textStatus, errorThrown) {
	  	alert('error ' + errorThrown);
	  	$('.sm').hideLoading();
	  }

	});
	
}





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
        
		
		$("#btSaveBill").click(function() {
			$( "#typeSave" ).val(1);
			$( "#FormBuildBill" ).submit();
		});
		$("#btBuildBill").click(function() {
			
			$( "#typeSave" ).val(0);
			$( "#FormBuildBill" ).submit();
		});		
		
		
		$("#btPrintBill").click(function() {
			 location.href="<c:url value="/admin/bills/printBill.html" />?id=<c:out value="${dataEx.id}"/>";
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
    
    
    
    
    
    $(document).ready(function() {

        // process the form
        	$("#FormBuildBill").submit(function(event){
        	    event.preventDefault(); 
        	    var post_url = $(this).attr("action"); 
        	    var request_method = $(this).attr("method");
        		var form_data = new FormData(this); 
        	    $.ajax({
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
                     		 <s:message code="label.order.preparebill" text="Draft Bill"/> 
                     		 <c:out value="${dataEx.id}" /> - <span class="lead"><s:message code="label.order.${dataEx.status}" text="${dataEx.status}" /></span>
                     		 <br>
                       </div>       
                  </div>
           </h3>
		   <br/>
 	       	 	
	     <c:url var="buildBill" value="/admin/bills/buildBill.html"/>
	     
         <form:form method="POST" id="FormBuildBill" modelAttribute="dataEx" action="${buildBill}">
	   			<input type="hidden" name="typeSave" id="typeSave" value="0" />
                <form:errors path="*" cssClass="alert alert-error" element="div" />
	                <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
	                <div id="store.error" class="alert alert-error" style="display:none;"><s:message code="message.error" text="An error occured"/></div>
					
					<input name="id" id="id" type="hidden" value="<c:out value="${dataEx.id}"/>">
					<input name="orderId" id="orderId" type="hidden" value="<c:out value="${order.id}"/>">

 			
			 		<div class="span8"> 	
			 			<div class="span4" style="margin-left:0px;"> 
						<h6> <s:message code="label.customer.billinginformation" text="Billing information"/> </h6>
						</div>
				    </div>
					
					
					<div class="span8">
										<table class="table table-bordered"> 
														<thead> 
															<tr> 
																<th colspan="2" style="width: 250px"><s:message code="label.order.item" text="Item"/></th> 
																<th colspan="1" style="width: 50px"><s:message code="label.quantity" text="Quantity"/></th> 
																<th style="width: 120px"><s:message code="label.order.price" text="Price"/></th>
																<th ><s:message code="label.order.total" text="Total"/></th>  
															</tr> 
														</thead>
														
														<c:set var="totalParent" value="0" />
														<tbody> 
															<c:forEach items="${dataEx.relationships}" var="subEntity" varStatus="counter2">	
															
																<c:if test="${subEntity.parentId==0}">
																<tr>
																		<td style="width: 250px" colspan="2"> 
																			<Strong><c:out value="${subEntity.productName}" />(<c:out value="${subEntity.sku}" />)</Strong>
																		</td>															
																		<td colspan="1">
																			<Strong><c:out value="${subEntity.productQuantity}" /></Strong>
																		</td>
																		<td>
																			<Strong><c:out value="${subEntity.oneTimeCharge}" /></Strong>
																		</td>
																		<td id="resultId" style="text-align:right">	
																			
																			<Strong><sm:monetary value="${subEntity.total}" currency="${subEntity.currency}"/></Strong>
																										
																		</td>	
																</tr>
																
																<c:set var="totalParent" value="${totalParent + subEntity.total }" />															
																</c:if>
																
																<c:if test="${subEntity.parentId>0}">
																<input type="hidden" id="itemId" name="itemId" value="${subEntity.id}" /> 
																	<tr>
																	
																		<td style="width: 100px"> 
																			<input type="text" name="code" id="code" style="width: 90px" value="<c:out value="${subEntity.sku}" />" />
																		</td>
																		<td style="">
																			<c:out value="${subEntity.productName}" />
																		</td>																	
																		<td colspan="1">
																			<input type="text" name="quantity" style="width: 50px" id="quantity" value="<c:out value="${subEntity.productQuantity}" />" />
																		</td>
																		<td>
																			<input type="text" name="oneTimeCharge" style="width: 120px" id="oneTimeCharge" value="<c:out value="${subEntity.oneTimeCharge}" />" />
																		</td>
																		<td id="resultId">	
																			
																			<sm:monetary value="${subEntity.total}" currency="${subEntity.currency}"/>
																										
																		</td>
																	</tr>
																</c:if>
															</c:forEach>
														</tbody>
															<tr> 
																<th colspan="2" style="width: 250px"></th> 
																<th colspan="1" style="width: 50px"></th> 
																<th style="width: 120px">
																			<Strong><s:message code="label.order.totals" text="Totals"/>:</Strong>
																</th>
																<th style="text-align:right">
																		<strong><sm:monetary value="${totalParent}" currency="${order.order.currency}"/></strong>
																</th>  
															</tr> 
													</table>					
					
					
					
					
					</div>
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.deliveryDate" text="Delivery date"/></label>	 
					                  <div class="controls"> 
					                        <form:input id="dateExported" cssClass="small" path="dateExported" data-date-format="<%=com.salesmanager.core.business.constants.Constants.DEFAULT_DATE_FORMAT%>"/>      
											<script type="text/javascript">
												$('#dateExported').datepicker();
											</script>    
					                   </div>
					           </div>       				
      				</div>
      				
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.generic.phone" text="Phone"/></label>	 
					                  <div class="controls"> 
					                        <form:input  cssClass="small" path="phone"/>      														
					                   </div>
					           </div>       				
      				</div>
      				<div class="span8">
							<div class="control-group">
				                  <label><s:message code="label.generic.address" text="Address"/></label>	 
				                  <div class="controls"> 
				                        <form:input  cssClass="input-large highlight" path="address"/>      														
				                   </div>
				           </div>       				
      				</div>
      				<div class="span8">
								<div class="control-group">
					                  <label><s:message code="label.entity.status" text="Status"/></label>	 
					                  <div class="controls">         
											<form:select path="status">
							  						<form:options items="${orderStatusList}" />
						       				</form:select>      		                   			
					                   </div>
					           </div> 
				     		   <div class="control-group">  
				                    <label></label>
				                     <div class="controls">
				                     	 <form:textarea  cssClass="input-large" cols="10" rows="3" path="description"/>
				                     	    
				                    </div> 
				               </div>      				
      				
      				</div>

            <br/>   
            <div class="span8">
	              <div class="form-actions">
	              		<button  type="button" id ="btSaveBill" class="btn btn-medium btn-primary" ><s:message code="button.label.submit" text="Save"/></button>
	              		
	              		<button  type="button" id="btPrintBill" class="btn btn-medium btn-primary" ><s:message code="label.generic.print" text="Print"/></button>	              		
	      		  </div>
      		</div> 
            <br/>   
    
    	  
   
   		</form:form>       

      </div>
	 </div>
  </div>
</div>

