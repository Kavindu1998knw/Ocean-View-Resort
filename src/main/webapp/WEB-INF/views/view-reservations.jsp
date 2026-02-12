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
    <style>
        .actions-col {
            width: 200px;
            min-width: 180px;
            white-space: nowrap;
            text-align: center;
        }

        .action-btn {
            cursor: pointer !important;
        }

        .action-btn:hover {
            transform: translateY(-1px);
            transition: 0.2s ease-in-out;
        }
    </style>
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1 class="h3 mb-0">View Reservations</h1>
        <div class="d-flex gap-2">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/reservation">Add New Reservation</a>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/dashboard">Back to Dashboard</a>
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
            <div class="table-responsive">
                <table class="table table-striped table-bordered align-middle">
                    <thead class="table-dark">
                    <tr>
                        <th>Reservation No</th>
                        <th>Guest Name</th>
                        <th>Email</th>
                        <th>Contact</th>
                        <th>Room Type</th>
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
                            <td><c:out value="${r.reservationNo}"/></td>
                            <td><c:out value="${r.guestFullName}"/></td>
                            <td><c:out value="${r.guestEmail}"/></td>
                            <td><c:out value="${r.contactNumber}"/></td>
                            <td><c:out value="${r.roomType}"/></td>
                            <td><c:out value="${r.numberOfGuests}"/></td>
                            <td><c:out value="${r.checkInDate}"/></td>
                            <td><c:out value="${r.checkOutDate}"/></td>
                            <td><c:out value="${r.status}"/></td>
                            <td class="actions-col d-flex justify-content-center gap-2">
                                <a
                                    href="${pageContext.request.contextPath}/admin/reservation/edit?id=${r.id}"
                                    class="btn btn-sm btn-outline-primary action-btn"
                                >
                                    Edit
                                </a>
                                <a
                                    href="${pageContext.request.contextPath}/admin/reservation/delete?id=${r.id}"
                                    class="btn btn-sm btn-outline-danger action-btn"
                                    onclick="return confirm('Are you sure you want to delete this reservation?');"
                                >
                                    Delete
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
