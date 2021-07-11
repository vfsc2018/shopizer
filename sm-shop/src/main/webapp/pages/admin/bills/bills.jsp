<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>	
<link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>

		
<div class="tabbable">

 					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">

    					<div class="tab-pane active" id="catalogue-section">
								<div class="sm-ui-component">
								
								

			<div>
			<table width="100%" border="0px">
				<tr>
					<td align="left">
						<h3><s:message code="label.listBills.title" text="Bills" /><span id="totalRows"></span></h3>	
						 <c:set value="/admin/bills/paging.html" var="pagingUrl" scope="request"/>
						 <c:set value="/admin/bills/remove.html" var="removeUrl" scope="request"/>
						 <c:set value="/admin/bills/viewBill.html" var="editUrl" scope="request"/>
						 <c:set value="/admin/bills/list.html" var="afterRemoveUrl" scope="request"/>
						 <c:set var="entityId" value="id" scope="request"/>
						 <c:set var="componentTitleKey" value="label.listBills.title" scope="request"/>
								 
					</td>
				</tr>
				<tr>
				<td align="right" style="vertical-align: middle;" nowrap="nowrap">
				
				<c:url var="buildBill" value="/admin/bills/reportBill.html"/>
				
				<form:form method="POST" id="FormBuildBill" target="_blank" modelAttribute="dataEx" action="${buildBill}">
				<input type="hidden" name="type" id="type" value="1" />
				
				
				        <input id="fromDate" type="text" style="width:100px; margin-top:10px" class="small" name="fromDate" />      
						<script type="text/javascript">
							$('#fromDate').datepicker();
						</script>   
						
						<input id="toDate" type="text" style="width:100px; margin-top:10px" css="small" name="toDate" />      
						<script type="text/javascript">
							$('#toDate').datepicker();
						</script>   
		              	<button  type="button" id ="btReportBill" class="btn btn-medium btn-primary" ><s:message code="button.label.report" text="Report"/></button>
		              	<button  type="button" id="btCollectBill" class="btn btn-medium btn-primary" ><s:message code="button.label.summary" text="Summary"/></button>
		        </form:form>	              		
		      	</td>
		      	</tr>
	      	</table>	  
      		</div> 
      		<br/>
      		
								 <c:set var="gridHeader" value="/pages/admin/bills/bills-gridHeader.jsp" scope="request"/>
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
			$( "#type").val(1);
			$( "#FormBuildBill").submit();
			
			// location.href="<c:url value="/admin/bills/reportBill.html" />?id=0";
		}); 
		
		$("#btCollectBill").click(function() {
			$( "#type").val(2);
			$( "#FormBuildBill").submit();
			
			// location.href="<c:url value="/admin/bills/collectBill.html" />?id=0";
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