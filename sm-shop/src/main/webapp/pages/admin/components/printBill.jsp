<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
		<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
			<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
				<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>

					<%@ page session="false" %>

						<script>
							function print() {
								var divContents = document.getElementById('printSection').innerHTML;
								var a = window.open('', '', 'height=500, width=500');
								a.document.write('<html>');
								a.document.write('<body onload="window.print();window.close()">');
								a.document.write(divContents);
								a.document.write('</body></html>');
								a.document.close();
								setTimeout(function () {
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
										<c:set value="${product.id}" var="productId" scope="request" />
										<jsp:include page="/pages/admin/products/product-menu.jsp" />
									</c:if>

									<div id="printSection">
										<div style="width:300px">
											<style>
												table {
													font-family: arial, sans-serif;
													border-collapse: collapse;
													width: 100%;
												}

												td,
												th {
													border: 1px solid #dddddd;
													text-align: left;
													padding: 8px;
												}
											</style>
											<img id="image" src="/resources/img/vfsc.png" alt="logo" />
											<script src="<c:url value=" /resources/js/barcode/JsBarcode.all.min.js" />">
											</script>

											<div align="center">
												<h3>
													<s:message code="label.bill.billPrintTitle" text="Title bill" />
												</h3>
												</td>
											</div>

											<div>
												<Strong>
													<s:message code="label.bill.store" text="Store" />:
												</Strong>
												<span></span>
											</div>

											<div>
												<Strong>
													<s:message code="label.bill.phone" text="Phone" />:
												</Strong>
												<span></span>
											</div>

											<div>
												<Strong>
													<s:message code="label.bill.cashier" text="Cashier" />:
												</Strong>
												<span></span>
											</div>

											<div>
												<Strong>
													<s:message code="label.bill.createAt" text="Date Time" />:
												</Strong>
												<span>
													<c:out value="${dataEx.createAt}" />
												</span>
											</div>

											<div>
												<Strong>
													<s:message code="label.bill.orderCode" text="Order Code" />:
												</Strong>
												<span>
													<c:out value="${dataEx.order.id}" />
												</span>
											</div>

											<div>
												<Strong>
													<s:message code="label.bill.billCode" text="Bill Code" />:
												</Strong>
												<span>
													<c:out value="${dataEx.id}" />
												</span>
											</div>
											<div>
												<Strong>
													<s:message code="label.bill.billNote" text="Note" />:
												</Strong>
												<span>
													<c:out value="${dataEx.description}" />
												</span>
											</div>

											</br>

											<table style="width:300px" id="caculatorId">
												<thead>
													<tr>
														<th width="150px">
															<s:message code="label.order.item" text="Product" />
														</th>
														<th width="30px">
															<s:message code="label.quantity" text="Quantity" />
														</th>
														<th width="50px">
															<s:message code="label.generic.price" text="Price" />
														</th>
														<th width="70px">
															<s:message code="label.order.total" text="Total" />
														</th>
													</tr>
												</thead>
												<tbody>

													<c:set var="totalMoney" value="0" />
													<c:forEach items="${dataEx.items}" var="entity" varStatus="counter">
														<c:if test="${entity.parentId==0}">
															<c:set var="totalParent"
																value="${entity.price * entity.quantity }" />
															<tr>
																<td>
																	<Strong>
																		<c:out value="${entity.name}" />
																	</Strong>
																</td>
																<td>
																	<Strong>
																		<c:out value="${entity.quantity}" />
																	</Strong>
																</td>
																<td>
																	<Strong>
																		<sm:monetary value="${entity.price}"
																			currency="${order.order.currency}" />
																	</Strong>
																</td>
																<td>
																	<Strong>
																		<sm:monetary value="${totalParent}"
																			currency="${order.order.currency}" />
																	</Strong>
																</td>


															</tr>
															<c:set var="totalMoney"
																value="${totalMoney + totalParent }" />
														</c:if>


														<c:if test="${entity.parentId>0}">
															<c:if test="${entity.quantity > 0}">
																<c:set var="totalSub"
																	value="${entity.price * entity.quantity }" />
																<tr>
																	<td>
																		<c:out value="${entity.name}" />
																	</td>
																	<td>
																		<c:out value="${entity.quantity}" />
																		<c:out value="${entity.unit}" />
																	</td>
																	<td>

																	</td>
																	<td>

																	</td>
																</tr>
															</c:if>
														</c:if>


													</c:forEach>
												</tbody>
											</table>
											</br>
											<div class="subt">
												<Strong>
													<s:message code="label.order.totals" text="Total" />:
												</Strong>
												<span id="totalMoney">
													<strong>
														<sm:monetary value="${totalMoney}"
															currency="${order.order.currency}" />
													</strong>
												</span>
											</div>
											<div class="subt">
												<svg id="barcode"></svg>
												<script>
													JsBarcode("#barcode", "<c:out value="${ dataEx.order.id }" />-<c:out value="${ dataEx.id }" />");
												</script>
											</div>

											</br>
											<div><Strong>
													<s:message code="label.customer.name" text="Customer" />:
												</Strong>
												<span>
													<c:out value="${order.billing.firstName}" />
												</span>
											</div>

											<div><Strong>
													<s:message code="label.generic.address" text="Address" />:
												</Strong>
												<span>
													<c:out value="${dataEx.address}" />
												</span>
											</div>

											<div><Strong>
													<s:message code="label.generic.phone" text="Phone" />:
												</Strong>
												<span>
													<c:out value="${dataEx.phone}" />
												</span>
											</div>

										</div>
									</div>
									<div class="form-actions">
										<button class="btn btn-medium btn-primary" onclick="print()">
											<s:message code="button.label.print" text="Print" />
										</button>
									</div>
								</div>
							</div>
						</div>