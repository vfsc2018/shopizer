<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>			
<div class="tabbable">

 					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">

    					<div class="tab-pane active" id="transactions-section">
								<div class="sm-ui-component">
								
								

			<div>
			<table width="100%" border="0px">
				<tr>
					<td align="left" width="50%">
						<h3><s:message code="label.transactions.title" text="List transactions" /></h3>	
						 <c:set value="/admin/transactions/paging.html" var="pagingUrl" scope="request"/>
						 <c:set value="/admin/transactions/remove.html" var="removeUrl" scope="request"/>
						 <c:set value="/admin/transactions/viewBill.html" var="editUrl" scope="request"/>
						 <c:set value="/admin/transactions/list.html" var="afterRemoveUrl" scope="request"/>
						 <c:set var="entityId" value="id" scope="request"/>
						 <c:set var="componentTitleKey" value="label.transactions.title" scope="request"/>
					</td>
				<td align="right">
		              		              		
		      	</td>
		      	</tr>
	      	</table>	  
      		</div> 
      		<br/>
      		
								 <c:set var="gridHeader" value="/pages/admin/transactions/transactions-gridHeader.jsp" scope="request"/>
								 <c:set var="canRemoveEntry" value="false" scope="request"/>
				
				            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
								 <!-- End listing grid include -->
			      			     
			      			     
      					</div>
   					</div>
  					</div>

				</div>	

	