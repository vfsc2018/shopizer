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
								<h3><s:message code="label.listBills.title" text="Orders" /></h3>	
								<br/>
				 <!-- Listing grid include -->
				 <c:set value="/admin/bills/paging.html" var="pagingUrl" scope="request"/>
				 <c:set value="/admin/bills/remove.html" var="removeUrl" scope="request"/>
				 <c:set value="/admin/bills/editOrder.html" var="editUrl" scope="request"/>
				 <c:set value="/admin/bills/list.html" var="afterRemoveUrl" scope="request"/>
				 <c:set var="entityId" value="orderId" scope="request"/>
				 <c:set var="componentTitleKey" value="label.listBills.title" scope="request"/>
				 <c:set var="gridHeader" value="/pages/admin/bills/bills-gridHeader.jsp" scope="request"/>
				 <c:set var="canRemoveEntry" value="false" scope="request"/>

            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
				 <!-- End listing grid include -->
			      			     
			      			     
      					</div>
   					</div>
  					</div>

				</div>		      			     