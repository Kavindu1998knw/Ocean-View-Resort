<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Admin Dashboard | Ocean View Resort</title>
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
    <%@ include file="/WEB-INF/views/admin/partials/_sidebar.jsp" %>
    <main class="admin-content">
        <div class="content-inner">
        <div class="hero mb-4">
            <h1>Welcome back, ${loggedUserName}.</h1>
            <p>Here is a quick overview of Ocean View Resort operations today.</p>
        </div>

        <c:if test="${not empty success}">
            <div class="alert alert-success" role="alert">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">${error}</div>
        </c:if>

        <div class="row g-4 mb-4">
            <div class="col-12 col-md-6 col-xl-3">
                <div class="card card-soft stat-card">
                    <div class="card-body">
                        <div class="stat-label">Total Reservations</div>
                        <div class="stat-value">${totalReservations}</div>
                        <div class="stat-foot">+12 this week</div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-6 col-xl-3">
                <div class="card card-soft stat-card">
                    <div class="card-body">
                        <div class="stat-label">Total Guests</div>
                        <div class="stat-value">${totalGuests}</div>
                        <div class="stat-foot">Occupancy 78%</div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-6 col-xl-3">
                <div class="card card-soft stat-card">
                    <div class="card-body">
                        <div class="stat-label">Available Rooms</div>
                        <div class="stat-value">${empty availableRooms ? 0 : availableRooms}</div>
                        <div class="stat-foot">Suite + Deluxe</div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-6 col-xl-3">
                <div class="card card-soft stat-card">
                    <div class="card-body">
                        <div class="stat-label">Today's Check-ins</div>
                        <div class="stat-value">${todayCheckIns}</div>
                        <div class="stat-foot">Next arrival 2:30 PM</div>
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
                            <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/admin/reservations">View all</a>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover table-clean align-middle mb-0">
                                <thead>
                                <tr>
                                    <th>Reservation No</th>
                                    <th>Guest Name</th>
                                    <th>Room Type</th>
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
                                            <fmt:parseDate value="${r.checkInDate}" pattern="yyyy-MM-dd" var="checkInDateParsed"/>
                                            <fmt:parseDate value="${r.checkOutDate}" pattern="yyyy-MM-dd" var="checkOutDateParsed"/>
                                            <tr>
                                                <td><c:out value="${r.reservationNo}"/></td>
                                                <td><c:out value="${r.guestFullName}"/></td>
                                                <td><c:out value="${r.roomType}"/></td>
                                                <td><fmt:formatDate value="${checkInDateParsed}" pattern="MMM d, yyyy"/></td>
                                                <td><fmt:formatDate value="${checkOutDateParsed}" pattern="MMM d, yyyy"/></td>
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
                <div id="searchReservation" class="search-card mb-4">
                    <h5 class="mb-3">Search Reservation</h5>
                    <form class="d-flex flex-column gap-2" action="${pageContext.request.contextPath}/admin/reservations/search" method="get">
                        <label for="reservationNo" class="form-label">Reservation Number</label>
                        <input
                            type="text"
                            class="form-control"
                            id="reservationNo"
                            name="reservationNo"
                            placeholder="e.g., RSV-10234"
                        />
                        <button class="btn btn-primary mt-2" type="submit">Search</button>
                    </form>
                </div>

                <div class="card card-soft">
                    <div class="card-body">
                        <h5 class="mb-3">Quick Actions</h5>
                        <div class="d-grid gap-2">
                            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/reservation">Add New Reservation</a>
                            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/bill">Calculate &amp; Print Bill</a>
                            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/rooms">Manage Rooms</a>
                            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/staff-users">Manage Staff Users</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </div>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>

