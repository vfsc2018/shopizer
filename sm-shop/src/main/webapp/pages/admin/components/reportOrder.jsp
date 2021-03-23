<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 

<%@ page session="false" %>

<script> 
    function print() { 
        var divContents = document.getElementById('printSection').innerHTML; 
        var a = window.open('', '', 'height=900, width=900'); 
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
	
<div id="printSection" >
<style>
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

td, th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
}
</style>	
<img id="image" src="/resources/img/vfsc.png" alt="logo" />	
		<div align="center">
		<h3><s:message code="label.bill.billPrintTitle" text="Title bill"/></h3></td>
		</div>
<table id="caculatorId" width="100%"> 
	<thead> 
		<tr>
			<th><s:message code="label.order.reportBill.stt" text="STT"/></th> 
			<th><s:message code="label.order.reportBill.orderId" text="OrderId"/></th> 
			<th><s:message code="label.order.reportBill.customer" text="Customer"/></th>
			<th><s:message code="label.order.reportBill.phone" text="Phone"/></th>
			<th><s:message code="label.order.reportBill.address" text="Address"/></th>
			<th><s:message code="label.order.reportBill.dateExported" text="Date exported"/></th>  
			<th><s:message code="label.order.reportBill.totalMoney" text="Total"/></th>  
		</tr> 
	</thead>
	<tbody>
	<c:set var="stt" value="0" />
	<c:set var="total" value="0" />
	<c:forEach items="${data}" var="entity" varStatus="counter">
	<c:set var="stt" value="${stt + 1 }" />
	<c:set var="total" value="${total + entity.total}" />
		<tr>
				<td><c:out value="${stt}" /></td>
				<td><c:out value="${entity.id}" /></td>
				<td><c:out value="${entity.billing.firstName}" /></td>
				<td><c:out value="${entity.billing.telephone}" /></td>
				<td><c:out value="${entity.billing.address}" /></td>
				<td><c:out value="${entity.datePurchased}" /></td>
				<td style="text-align: right;"><sm:monetary value="${entity.total}" currency="${currency}"/></td>
	
		</tr>
	</c:forEach>
	<tr>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td style="text-align: right;"><h3><sm:monetary value="${total}" currency="${currency}"/></h3></td>

	</tr>
	</tbody>
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
