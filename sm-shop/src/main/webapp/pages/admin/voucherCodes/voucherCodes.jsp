<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>			
<div class="tabbable">

 					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">

    					<div class="tab-pane active" id="vouchers-section">
								<div class="sm-ui-component">
								
								

			<div>
			<table width="100%" border="0px">
				<tr>
					<td align="left" width="50%">
						<h3><s:message code="label.voucherCodes.title" text="List of vouchers" /></h3>	
						 <c:set value="/admin/vouchercodes/paging.html" var="pagingUrl" scope="request"/>
						 <c:set value="/admin/vouchercodes/remove.html" var="removeUrl" scope="request"/>
						 <c:set value="/admin/vouchercodes/view.html" var="editUrl" scope="request"/>
						 <c:set value="/admin/vouchercodes/list.html" var="afterRemoveUrl" scope="request"/>
						 <c:set var="entityId" value="id" scope="request"/>
						 <c:set var="componentTitleKey" value="label.voucherCodes.title" scope="request"/>
					</td>
				<td align="right">
		              	<button  type="button" id ="btReportBill" class="btn btn-medium btn-primary" ><s:message code="button.label.report" text="Report"/></button>	              		
		      	</td>
		      	</tr>
		      	<tr>
		      		<td colspan="2">
							<div class="span8">
								<Strong><s:message code="label.entity.id" text="Voucher Id"/>:</Strong> <c:out value="${voucher.id}"/>
					        </div>			      		
							<div class="span8">
								<Strong><s:message code="label.entity.code" text="Code"/>:</Strong> <c:out value="${voucher.code}"/>
					        </div>		      			
							<div class="span8">
								<Strong><s:message code="label.entity.description" text="Description"/>:</Strong> <c:out value="${voucher.description}"/>
					        </div>	
							<div class="span8">
								<Strong><s:message code="label.entity.total" text="Total code"/>:</Strong> <c:out value="${intAmount}"/>
					        </div>	
		      		</td>
		      	</tr>
	      	</table>	  
      		</div> 
      		<br/>
      		
								 <c:set var="gridHeader" value="/pages/admin/vouchercodes/voucherCodes-gridHeader.jsp" scope="request"/>
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
			 location.href="<c:url value="/admin/vouchercodes/reportCode.html" />?id=0";
		}); 
});
</script>		