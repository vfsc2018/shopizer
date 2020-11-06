<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
<%@ page session="false" %>
<style>
	.styleClass{
		background-color:#f1f1f1;border:1px dotted #CCC;
		border-collapse: collapse;
	}
	.styleClass td{
		background-color:#f1f1f1;font:12px; border:1px dotted #CCC;
		padding: 5px;
	}
</style>
<table class="styleClass"  width="350px">
	<tr>
		<td  rowspan="4" width="100px" align="center" >[QR CODE]</td>
	</tr>
	<tr>
		<td><c:out value="${stamp.sku}" /></td>
	</tr>
	<tr>
		<td><c:out value="${stamp.productName}" /></td>
	</tr>
	<tr>
		<td><sm:monetary value="${stamp.price}" currency="${stamp.currency}"/></td>
	</tr>	
</table>