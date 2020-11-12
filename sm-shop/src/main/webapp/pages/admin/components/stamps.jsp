<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
<%@ page session="false" %>
<style>
	.styleClass{
		border:1px;
		border-collapse: collapse;
	}
	.styleClass td{
		font-size:18px;
		padding-top: 5px;
		padding-left: 5px;
		padding-bottom: 0px;
	}
</style>
<script> 
	function print() { 
		var divContents = document.getElementById('printSection').innerHTML; 
		var a = window.open('', '', 'height=500, width=500'); 
		a.document.write('<html>'); 
		a.document.write('<body >'); 
		a.document.write(divContents); 
		a.document.write('</body></html>'); 
		a.document.close(); 
		a.print(); 
	} 
</script> 
<input type="button" value="Printer" onclick="print('')">  
<div id="printSection">
<table class="styleClass">
	<tr>
		<td rowspan="5" ><H1>Vt</H1></td>
	</tr>
	<tr>
		<td><s:message code="label.stamp.productname" text="Product"/></td>
		<td><c:out value="${stamp.productName}" /></td>
	</tr>
	<tr>
		<td><s:message code="label.stamp.sku" text="Sku"/></td>
		<td><c:out value="${stamp.sku}" /></td>
	</tr>
	<tr>
		<td><s:message code="label.stamp.price" text="Price"/></td>
		<td><c:out value="${stamp.price}" /> VND</td>
	</tr>	
	<tr>
		<td><s:message code="label.stamp.netweight" text="Netweight"/></td>
		<td><c:out value="${stamp.weight}" /> kg</td>
	</tr>	
</table>
</div>