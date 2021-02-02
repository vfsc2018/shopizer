<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 

<%@ page session="false" %>
<script src="<c:url value="/resources/js/barcode/JsBarcode.all.min.js" />"></script>
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
<script src="<c:url value="/resources/js/barcode/JsBarcode.all.min.js" />"></script>
<div class="tabbable">
	<jsp:include page="/common/adminTabs.jsp" />

	<img id="image" src="/resources/img/vfsc.png" alt="logo" />	
		<div align="center">
		<h3><s:message code="label.voucherCode.Print.Title" text="VOUCHER CODES"/></h3></td>
		</div>
<div id="printSection">
<style>
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

td, th {
  border: 0px solid #dddddd;
  text-align: left;
  padding-top: 8px;
}
</style>	

<table id="caculatorId"> 
	<tbody>
	<c:set var="stt" value="0" />
	<c:forEach items="${data}" var="entity" varStatus="counter">
	<c:set var="stt" value="${stt + 1}" />
		<tr>
			<td>
                    <svg id="barcode${stt}"></svg>
					<script>JsBarcode("#barcode${stt}", '<c:out value="${entity.code}" />', {height: 50});</script> 
					<h4>http://vfscfood.com</h4>
			</td>
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
