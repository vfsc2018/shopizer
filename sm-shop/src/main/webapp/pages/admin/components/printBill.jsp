<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 

<%@ page session="false" %>

<script> 
    function print() { 
        var divContents = document.getElementById('printSection').innerHTML; 
        var a = window.open('', '', 'height=500, width=500'); 
        a.document.write('<html>'); 
        a.document.write('<body onload="window.print();window.close()">'); 
        a.document.write(divContents); 
        a.document.write('</body></html>'); 
        a.document.close(); 
        setTimeout(function(){
            a.print();
        }, 3000)
        return false; 
    } 
</script> 
<div class="tabbable">
	<jsp:include page="/common/adminTabs.jsp" />

	<div class="tab-content">

	<div class="tab-pane active" id="catalogue-section">
	<c:if test="${product.id!=null && product.id>0}">
			<c:set value="${product.id}" var="productId" scope="request"/>
			<jsp:include page="/pages/admin/products/product-menu.jsp" />
	</c:if>	
<div id="printSection">		
<table style="width:300px">
	<tr>
		<td colspan="2">
		<img id="image" src="/resources/img/vfsc.png" alt="logo" />		
		</td>
	</tr>
	<tr>
		<td colspan="2">
					<table style="width:300px">
						<tr>
							<th align="left"><Strong><s:message code="label.customer.firstname" text="First Name"/>:</Strong></th>
							<td align="left"><c:out value="${order.billing.firstName}" /> <c:out value="${order.billing.lastName}" /> </td>
						</tr>
						<tr>
							<th align="left"><Strong><s:message code="label.customer.billingaddress" text="Billing address"/>:</Strong></th>
							<td align="left"><c:out value="${order.billing.address}" />/ <c:out value="${order.billing.city}" /> </td>
						</tr>	
						<tr>
							<th align="left"><Strong><s:message code="label.generic.phone" text="Phone"/>:</Strong></th> 
							<td align="left"><c:out value="${order.billing.telephone}" /> </td>
						</tr>
					</table>
					
			 		<div class="span8"> 	
			 			<div class="span4" style="margin-left:0px;"> 
						<h6><s:message code="label.customer.billinginformation" text="Billing information"/></h6>
						</div>
				    </div>
	
      
			      	  <div class="span8" style="margin-top:20px;">
					      <table style="width:300px" class="table table-bordered table-striped" id="caculatorId"> 
			 				    <tbody> 
			 				    	
									<c:forEach items="${dataEx}" var="entity" varStatus="counter">	 
						            	
										<tr> 
											<td colspan="2"> 
												<c:out value="${entity.productName}" />
											</td> 
										</tr>
										
										<tr>
											<td colspan="2">
											      <table class="table table-bordered table-striped"> 
														<thead> 
															<tr> 
																<th colspan="2" width="55%">Ten SP</th> 
																<th colspan="1" width="15%">SL</th> 
																<th width="15%">Gia</th>
																<th width="15%">Tong</th>  
															</tr> 
														</thead>
														
														<tbody> 
															<c:forEach items="${entity.relationships}" var="subEntity" varStatus="counter2">	 
																<c:set var="total" value="${subEntity.oneTimeCharge * subEntity.productQuantity }" />
																
																<tr>
																	<td colspan="2">
																		<c:out value="${subEntity.productName}" />
																	</td>
																	<td colspan="1">
																		<c:out value="${subEntity.productQuantity}" />
																	</td>
																	<td>
																		<c:out value="${subEntity.oneTimeCharge}" />
																	</td>
																	<td id="resultId" align="right">	
																		<strong><sm:monetary value="${total}" currency="${subEntity.currency}"/></strong>
																		
																	</td>
																</tr>
															</c:forEach>
														</tbody>
														
													</table>	
																		    

												
											</td>
										</tr> 
										
						
									</c:forEach> 
										
								 	
									<tr class="subt"> 
										<td  width="50%">&nbsp;</td> 
										<td width="50%" align="right">
										<Strong><s:message code="label.order.total" text="Total"/>:</Strong>
										<span id="totalMoney">
										<strong><sm:monetary value="${totalMoney}" currency="${order.order.currency}"/></strong>
										</span>
										</td> 
									</tr> 
									
								</tbody>    
							</table>
			    	  </div>  

	
		</td>
	</tr>
</table>
</div>
					<div class="form-actions">
							<button class="btn btn-medium btn-primary" onclick="print()">
								<s:message code="button.label.print" text="Print" />
							</button>
					</div>
</div>
</div>
</div>
