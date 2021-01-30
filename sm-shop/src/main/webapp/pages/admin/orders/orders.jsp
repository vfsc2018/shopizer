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
					<td align="left" >
						<h3><s:message code="label.order.title" text="Orders" /></h3>	
						 <!-- Listing grid include -->
						 <c:set value="/admin/orders/paging.html" var="pagingUrl" scope="request"/>
						 <c:set value="/admin/orders/remove.html" var="removeUrl" scope="request"/>
						 <c:set value="/admin/orders/editOrder.html" var="editUrl" scope="request"/>
						 <c:set value="/admin/orders/list.html" var="afterRemoveUrl" scope="request"/>
						 <c:set var="entityId" value="orderId" scope="request"/>
						 <c:set var="componentTitleKey" value="label.order.title" scope="request"/>
								 
					</td>
				<td align="right" nowrap="nowrap">
													        <input id="fromDate" style="width:100px" class="small" name="fromDate" />      
											<script type="text/javascript">
												$('#fromDate').datepicker();
											</script>   
											
											<input id="toDate" style="width:100px" css="small" name="toDate" />      
											<script type="text/javascript">
												$('#toDate').datepicker();
											</script> 
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
<style>
.datepicker {
	position: absolute;
    z-index: 999999;
    margin-left: 0;
    margin-right: 0;
    margin-bottom: 18px;
    padding-bottom: 4px;
    width: 218px;
}
</style>		