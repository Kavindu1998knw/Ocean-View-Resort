<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Reservation by Number</title>
    <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
        integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
        crossorigin="anonymous"
    >
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css" />
</head>
<body class="admin-body">
<%@ include file="/WEB-INF/views/admin/partials/header.jsp" %>
<div class="admin-shell">
    <%@ include file="/WEB-INF/views/staff/partials/_sidebar.jsp" %>
    <main class="admin-content">
        <div class="content-inner">
    <div class="page-header">
        <div>
            <h1 class="h3">Search Reservation by Number</h1>
            <p>Find and review a reservation quickly.</p>
        </div>
        <div class="d-flex gap-2">
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/staff/dashboard">Back to Dashboard</a>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/staff/reservations">View All Reservations</a>
        </div>
    </div>

    <c:if test="${not empty success}">
        <div class="alert alert-success" role="alert">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/staff/search-reservation" class="card card-soft mb-4">
        <div class="card-body p-4">
            <div class="row g-3 align-items-end">
                <div class="col-md-9">
                    <label for="reservationNo" class="form-label">Reservation Number</label>
                    <input
                        type="text"
                        class="form-control"
                        id="reservationNo"
                        name="reservationNo"
                        placeholder="e.g., RES-20260212-9133"
                        value="${param.reservationNo}"
                        required
                    >
                </div>
                <div class="col-md-3 d-grid">
                    <button type="submit" class="btn btn-primary">Search</button>
                </div>
            </div>
        </div>
    </form>

    <c:if test="${not empty reservation}">
        <div class="card card-soft">
            <div class="card-header bg-transparent border-0 pb-0">
                <h2 class="h5 mb-0">Reservation Details</h2>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-clean mb-0">
                        <tbody>
                        <tr>
                            <th class="w-25">Reservation No</th>
                            <td><c:out value="${reservation.reservationNo}"/></td>
                        </tr>
                        <tr>
                            <th>Guest Name</th>
                            <td><c:out value="${reservation.guestFullName}"/></td>
                        </tr>
                        <tr>
                            <th>Email</th>
                            <td><c:out value="${reservation.guestEmail}"/></td>
                        </tr>
                        <tr>
                            <th>Contact</th>
                            <td><c:out value="${reservation.contactNumber}"/></td>
                        </tr>
                        <tr>
                            <th>Room Type</th>
                            <td><c:out value="${reservation.roomType}"/></td>
                        </tr>
                        <tr>
                            <th>Room No</th>
                            <td><c:out value="${empty reservation.roomNo ? '-' : reservation.roomNo}"/></td>
                        </tr>
                        <tr>
                            <th>Guests</th>
                            <td><c:out value="${reservation.numberOfGuests}"/></td>
                        </tr>
                        <tr>
                            <th>Check-in</th>
                            <td><c:out value="${reservation.checkInDate}"/></td>
                        </tr>
                        <tr>
                            <th>Check-out</th>
                            <td><c:out value="${reservation.checkOutDate}"/></td>
                        </tr>
                        <tr>
                            <th>Status</th>
                            <td><c:out value="${reservation.status}"/></td>
                        </tr>
                        <tr>
                            <th>Created At</th>
                            <td><c:out value="${reservation.createdAt}"/></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="card-footer d-flex gap-2 justify-content-end">
                <a
                    href="${pageContext.request.contextPath}/staff/reservation/edit?id=${reservation.id}"
                    class="btn btn-outline-primary btn-action"
                >
                    Edit
                </a>
                <form
                    method="post"
                    action="${pageContext.request.contextPath}/staff/reservation/delete"
                    onsubmit="return confirm('Are you sure you want to delete this reservation?');"
                >
                    <input type="hidden" name="id" value="${reservation.id}" />
                    <button type="submit" class="btn btn-outline-danger btn-action">Delete</button>
                </form>
            </div>
        </div>
    </c:if>

    <c:if test="${not empty notFound}">
        <div class="alert alert-warning mt-3" role="alert">
            No reservation found for the given number.
        </div>
    </c:if>
        </div>
    </main>
</div>
</body>
</html>
