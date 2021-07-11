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
					<td>
						<h3><s:message code="label.listNotifications.title" text="List of Notification" /><span id="totalRows"></span></h3>	
						 <c:set value="/admin/notifications/paging.html" var="pagingUrl" scope="request"/>
						 <c:set value="/admin/notifications/remove.html" var="removeUrl" scope="request"/>
						 <c:set value="/admin/notifications/viewNotifications.html" var="editUrl" scope="request"/>
						 <c:set value="/admin/notifications/list.html" var="afterRemoveUrl" scope="request"/>
						 <c:set var="entityId" value="id" scope="request"/>
						 <c:set var="componentTitleKey" value="label.listNotifications.title" scope="request"/>
								 
					</td>
		      	</tr>
	      	</table>	  
      		</div> 
      		<br/>
      		
								 <c:set var="gridHeader" value="/pages/admin/notifications/notifications-gridHeader.jsp" scope="request"/>
								 <c:set var="canRemoveEntry" value="false" scope="request"/>
				
				            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
								 <!-- End listing grid include -->
			      			     
			      			     
      					</div>
   					</div>
  					</div>

				</div>	

<script>				
$(document).ready(function(){ 				
		
		
});
</script>		