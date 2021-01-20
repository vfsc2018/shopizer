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
<script src="<c:url value="/resources/js/barcode/JsBarcode.all.min.js" />"></script>
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
		
				<h3>
						<s:message code="label.product.stamp" text="Product stamp"/>
				</h3>
			    <br/>
				<div id="printSection">
					<table class="styleClass">
						<tr>
							<td colspan="2"> 
								<table border="0px" width="100%" >
								<tr>
								<td><h1>VT</h1></td>
								<td>
									<svg id="barcode"></svg>
								</td>
								</tr>
								</table>
							</td>
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

					<div class="form-actions">
							<button class="btn btn-medium btn-primary" onclick="print()">
								<s:message code="button.label.print" text="Print" />
							</button>
					</div>
			</div>
		</div>
		
		<script>
		JsBarcode("#barcode", "<c:out value="${stamp.sku}" />", {
			  height: 50
			});
			
		</script> 
	</div>	