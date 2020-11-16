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
<img id="image" src="/resources/img/vfsc.png" alt="logo" />		
<table style="width:300px">
	<tr>
		<td><Strong><s:message code="label.customer.firstname" text="First Name"/>:</Strong></td>
		<td><c:out value="${order.billing.firstName}" /> <c:out value="${order.billing.lastName}" /> </td>
	</tr>
	<tr>
		<td><Strong><s:message code="label.generic.address" text="Address"/>:</Strong></td>
		<td><c:out value="${order.billing.address}" />/ <c:out value="${order.billing.city}" /> </td>
	</tr>	
	<tr>
		<td><Strong><s:message code="label.generic.phone" text="Phone"/>:</Strong></td> 
		<td><c:out value="${order.billing.telephone}" /> </td>
	</tr>
</table>
					
<h6><s:message code="label.customer.billinginformation" text="Billing information"/></h6>
						
					      <table style="width:300px" class="table table-bordered table-striped" id="caculatorId"> 
			 				    
									<thead> 
										<tr> 
											<th width="55%"><s:message code="label.order.item" text="Product"/></th> 
											<th width="15%"><s:message code="label.quantity" text="Quantity"/></th> 
											<th width="15%"><s:message code="label.generic.price" text="Price"/></th>
											<th width="15%"><s:message code="label.order.total" text="Total"/></th>  
										</tr> 
									</thead>
									<tbody> 
									<c:forEach items="${dataEx}" var="entity" varStatus="counter">	 
						            	
										<tr> 
											<td> 
												<c:out value="${entity.productName}" />
											</td> 
											<td> 
												sl
											</td> 
											<td> 
												gia
											</td> 
											<td> 
												tien
											</td> 
										</tr>
										
										<tr>
											<td colspan="4">
											    <table style="border:0px"> 
														
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
								</tbody>
					      </table>
								 	
					<div class="subt"> 
							<Strong><s:message code="label.order.totals" text="Total"/>:</Strong>
							<span id="totalMoney">
							<strong><sm:monetary value="${totalMoney}" currency="${order.order.currency}"/></strong>
							</span>
										
			    	</div>  
</div>
					<div class="form-actions">
							<button class="btn btn-medium btn-primary" onclick="print()">
								<s:message code="button.label.print" text="Print" />
							</button>
					</div>
</div>
</div>
</div>
