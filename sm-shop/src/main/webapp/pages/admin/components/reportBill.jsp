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
			<th>Id</th> 
			<th>OrderId</th> 
			<th>Customer</th>
			<th>Phone</th>
			<th>Addres</th>
			<th>Date exported</th>  
			<th>Status</th>  
		</tr> 
	</thead>
	<tbody>
	<c:forEach items="${data}" var="entity" varStatus="counter">
		<tr>
				<td><c:out value="${entity.id}" /></td>
				<td><c:out value="${entity.order.id}" /></td>
				<td><c:out value="${entity.order.billing.firstName}" /></td>
				<td><c:out value="${entity.phone}" /></td>
				<td><c:out value="${entity.address}" /></td>
				<td><c:out value="${entity.dateExported}" /></td>
				<td><c:out value="${entity.status}" /></td>
	
		</tr>
	</c:forEach>
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
