<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Reservations</title>
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
            <h1 class="h3">View Reservations</h1>
            <p>Review current reservations and manage updates.</p>
        </div>
        <div class="d-flex gap-2">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/staff/reservation/add">Add New Reservation</a>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/staff/dashboard">Back to Dashboard</a>
        </div>
    </div>

    <c:if test="${not empty success}">
        <div class="alert alert-success" role="alert">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">${error}</div>
    </c:if>

    <c:choose>
        <c:when test="${empty reservations}">
            <div class="alert alert-info" role="alert">No reservations found.</div>
        </c:when>
        <c:otherwise>
            <div class="card card-soft">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-clean w-100 align-middle mb-0">
                        <thead>
                    <tr>
                        <th>Reservation No</th>
                        <th>Guest Name</th>
                        <th>Email</th>
                        <th>Contact</th>
                        <th>Room Type</th>
                        <th>Room No</th>
                        <th>Guests</th>
                        <th>Check-in</th>
                        <th>Check-out</th>
                        <th>Status</th>
                        <th class="actions-col">Actions</th>
                    </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="r" items="${reservations}">
                            <tr>
                                <td class="cell-wrap"><c:out value="${r.reservationNo}"/></td>
                                <td><c:out value="${r.guestFullName}"/></td>
                                <td class="cell-wrap"><c:out value="${r.guestEmail}"/></td>
                                <td><c:out value="${r.contactNumber}"/></td>
                                <td><c:out value="${r.roomType}"/></td>
                                <td><c:out value="${empty r.roomNo ? '-' : r.roomNo}"/></td>
                                <td><c:out value="${r.numberOfGuests}"/></td>
                                <td><c:out value="${r.checkInDate}"/></td>
                                <td><c:out value="${r.checkOutDate}"/></td>
                                <td><c:out value="${r.status}"/></td>
                                <td class="actions text-center align-middle">
                                    <div class="action-buttons">
                                        <a
                                            href="${pageContext.request.contextPath}/staff/reservation/edit?id=${r.id}"
                                            class="btn btn-sm btn-outline-primary btn-action"
                                        >
                                            Edit
                                        </a>
                                        <form action="${pageContext.request.contextPath}/staff/reservation/delete" method="post">
                                            <input type="hidden" name="id" value="${r.id}" />
                                            <button
                                                type="submit"
                                                class="btn btn-sm btn-outline-danger btn-action"
                                                onclick="return confirm('Are you sure you want to delete this reservation?');"
                                            >
                                                Delete
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
        </div>
    </main>
</div>
</body>
</html>
