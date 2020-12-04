<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				



<div class="tabbable">

 					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">

    					<div class="tab-pane active" id="catalogue-section">



								<div class="sm-ui-component">
			<div>
			<table width="100%" border="0px">
				<tr>
					<td align="left" width="50%">
						<h3><s:message code="label.order.title" text="Orders" /></h3>	
						 <!-- Listing grid include -->
						 <c:set value="/admin/orders/paging.html" var="pagingUrl" scope="request"/>
						 <c:set value="/admin/orders/remove.html" var="removeUrl" scope="request"/>
						 <c:set value="/admin/orders/editOrder.html" var="editUrl" scope="request"/>
						 <c:set value="/admin/orders/list.html" var="afterRemoveUrl" scope="request"/>
						 <c:set var="entityId" value="orderId" scope="request"/>
						 <c:set var="componentTitleKey" value="label.order.title" scope="request"/>
								 
					</td>
				<td align="right">
		              	<button  type="button" id ="btReportBill" class="btn btn-medium btn-primary" ><s:message code="button.label.report" text="Report"/></button>
		              	<button  type="button" id="btCollectBill" class="btn btn-medium btn-primary" ><s:message code="button.label.summary" text="Summary"/></button>	              		
		      	</td>
		      	</tr>
	      	</table>	  
      		</div> 
      		<br/>
      		
								
								

				 <c:set var="gridHeader" value="/pages/admin/orders/orders-gridHeader.jsp" scope="request"/>
				 <c:set var="canRemoveEntry" value="false" scope="request"/>

            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
				 <!-- End listing grid include -->
			      			     
			      			     
      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>		

<script>				
$(document).ready(function(){ 				
		$("#btReportBill").click(function() {
			 location.href="<c:url value="/admin/orders/reportOrder.html" />?id=0";
		}); 
		
		$("#btCollectBill").click(function() {
			 location.href="<c:url value="/admin/orders/collectOrder.html" />?id=0";
		});
		
});
</script>		