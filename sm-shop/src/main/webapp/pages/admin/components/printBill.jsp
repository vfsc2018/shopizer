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
											<th width="150px"><s:message code="label.order.item" text="Product"/></th> 
											<th width="30px"><s:message code="label.quantity" text="Quantity"/></th> 
											<th width="50px"><s:message code="label.generic.price" text="Price"/></th>
											<th width="70px"><s:message code="label.order.total" text="Total"/></th>  
										</tr> 
									</thead>
									<tbody> 
									<c:forEach items="${dataEx}" var="entity" varStatus="counter">	 
						            	<c:set var="total111" value="${entity.oneTimeCharge * entity.productQuantity }" />
										<tr> 
											<td> 
												<c:out value="${entity.productName}" />
											</td> 
											<td> 
												<c:out value="${entity.productQuantity}" />
											</td> 
											<td> 
												<c:out value="${entity.oneTimeCharge}" />
											</td> 
											<td> 
												<sm:monetary value="${total111}" currency="${entity.currency}"/>
											</td> 
										</tr>
										
										<tr>
											<td colspan="4">
											    <table style="border:0px"> 
														
														<tbody> 
															<c:forEach items="${entity.relationships}" var="subEntity" varStatus="counter2">	 
																<c:set var="total" value="${subEntity.oneTimeCharge * subEntity.productQuantity }" />
																
																<tr>
																	<td width="150px" >
																		<c:out value="${subEntity.productName}" />
																	</td>
																	<td width="30px" >
																		<c:out value="${subEntity.productQuantity}" />
																	</td>
																	<td width="50px">
																		<c:out value="${subEntity.oneTimeCharge}" />
																	</td>
																	<td width="70px" id="resultId" align="right">	
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
