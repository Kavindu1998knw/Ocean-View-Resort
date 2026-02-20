<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Staff Dashboard | Ocean View Resort</title>
    <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
        crossorigin="anonymous"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css" />
</head>
<body class="admin-body">
<%@ include file="/WEB-INF/views/admin/partials/header.jsp" %>
<div class="admin-shell">
    <%@ include file="/WEB-INF/views/staff/partials/_sidebar.jsp" %>
    <main class="admin-content">
        <div class="content-inner">
            <div class="hero mb-4">
                <h1>Welcome back, ${not empty loggedUserName ? loggedUserName : sessionScope.authName}.</h1>
                <p>Here is a quick overview of your reservations and tasks today.</p>
            </div>

            <div class="row g-4 mb-4">
                <div class="col-12 col-md-6 col-xl-3">
                    <div class="card card-soft stat-card">
                        <div class="card-body">
                            <div class="stat-label">My Reservations</div>
                            <div class="stat-value">${totalReservations}</div>
                        </div>
                    </div>
                </div>
                <div class="col-12 col-md-6 col-xl-3">
                    <div class="card card-soft stat-card">
                        <div class="card-body">
                            <div class="stat-label">Upcoming Check-ins</div>
                            <div class="stat-value">${upcomingCheckIns}</div>
                        </div>
                    </div>
                </div>
                <div class="col-12 col-md-6 col-xl-3">
                    <div class="card card-soft stat-card">
                        <div class="card-body">
                            <div class="stat-label">Upcoming Check-outs</div>
                            <div class="stat-value">${upcomingCheckOuts}</div>
                        </div>
                    </div>
                </div>
                <div class="col-12 col-md-6 col-xl-3">
                    <div class="card card-soft stat-card">
                        <div class="card-body">
                            <div class="stat-label">Rooms Available</div>
                            <div class="stat-value">${empty availableRooms ? 0 : availableRooms}</div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row g-4">
                <div class="col-12 col-xl-8">
                    <div class="card card-soft">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h5 class="mb-0">Recent Reservations</h5>
                                <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/staff/reservations">View all</a>
                            </div>
                            <div class="table-responsive">
                                <table class="table table-striped table-hover table-clean align-middle mb-0">
                                    <thead>
                                    <tr>
                                        <th>Reservation No</th>
                                        <th>Room Type</th>
                                        <th>Room No</th>
                                        <th>Check In</th>
                                        <th>Check Out</th>
                                        <th>Status</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${empty recentReservations}">
                                            <tr>
                                                <td colspan="6" class="text-muted text-center py-4">
                                                    No recent reservations found.
                                                </td>
                                            </tr>
                                        </c:when>
                                    <c:otherwise>
                                        <c:forEach var="r" items="${recentReservations}">
                                            <c:set var="statusClass" value="status-pending"/>
                                            <c:if test="${r.status == 'CONFIRMED'}">
                                                <c:set var="statusClass" value="status-confirmed"/>
                                            </c:if>
                                            <c:if test="${r.status == 'CHECKED_IN'}">
                                                <c:set var="statusClass" value="status-checkedin"/>
                                            </c:if>
                                            <tr>
                                                <td><c:out value="${r.reservationNo}"/></td>
                                                <td><c:out value="${r.roomType}"/></td>
                                                <td><c:out value="${empty r.roomNo ? '-' : r.roomNo}"/></td>
                                                <td><c:out value="${r.checkInDate}"/></td>
                                                <td><c:out value="${r.checkOutDate}"/></td>
                                                <td>
                                                    <span class="status-pill ${statusClass}">
                                                        <c:out value="${r.status}"/>
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-xl-4">
                    <div class="card card-soft">
                        <div class="card-body">
                            <h5 class="mb-3">Search Reservation</h5>
                            <form method="post" action="${pageContext.request.contextPath}/staff/search-reservation">
                                <div class="mb-3">
                                    <label for="reservationNo" class="form-label">Reservation Number</label>
                                    <input
                                        type="text"
                                        class="form-control"
                                        id="reservationNo"
                                        name="reservationNo"
                                        placeholder="e.g., RSV-10234"
                                        required
                                    />
                                </div>
                                <button type="submit" class="btn btn-primary w-100">Search</button>
                            </form>
                        </div>
                    </div>

                    <div class="card card-soft mt-4">
                        <div class="card-body">
                            <h5 class="mb-3">Quick Actions</h5>
                            <div class="d-grid gap-2">
                                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/staff/search-reservation">Search Reservation</a>
                                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/staff/reservations">View Reservations</a>
                                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/staff/reservation/add">Add New Reservation</a>
                                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/staff/bill">Calculate &amp; Print Bill</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>
</body>
</html>
