<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Reservation</title>
    <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
        integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
        crossorigin="anonymous"
    >
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1 class="h3 mb-0">Edit Reservation</h1>
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/reservations">Back to Reservations</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/admin/reservation/update" class="card shadow-sm">
        <div class="card-body">
            <input type="hidden" name="id" value="${reservation.id}">

            <div class="row g-3">
                <div class="col-md-4">
                    <label for="reservationNo" class="form-label">Reservation No</label>
                    <input
                        type="text"
                        class="form-control"
                        id="reservationNo"
                        name="reservationNo"
                        value="${reservation.reservationNo}"
                        readonly
                    >
                </div>
                <div class="col-md-4">
                    <label for="guestFullName" class="form-label">Guest Full Name</label>
                    <input
                        type="text"
                        class="form-control"
                        id="guestFullName"
                        name="guestFullName"
                        value="${reservation.guestFullName}"
                        required
                    >
                </div>
                <div class="col-md-4">
                    <label for="guestEmail" class="form-label">Guest Email</label>
                    <input
                        type="email"
                        class="form-control"
                        id="guestEmail"
                        name="guestEmail"
                        value="${reservation.guestEmail}"
                        required
                    >
                </div>
                <div class="col-md-4">
                    <label for="contactNumber" class="form-label">Contact Number</label>
                    <input
                        type="text"
                        class="form-control"
                        id="contactNumber"
                        name="contactNumber"
                        value="${reservation.contactNumber}"
                        required
                    >
                </div>
                <div class="col-md-4">
                    <label for="roomType" class="form-label">Room Type</label>
                    <select class="form-select" id="roomType" name="roomType" required>
                        <c:forEach var="type" items="${roomTypes}">
                            <option value="${type}" ${type == reservation.roomType ? 'selected' : ''}>${type}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="numberOfGuests" class="form-label">Number of Guests</label>
                    <input
                        type="number"
                        class="form-control"
                        id="numberOfGuests"
                        name="numberOfGuests"
                        value="${reservation.numberOfGuests}"
                        min="1"
                        required
                    >
                </div>
                <div class="col-md-4">
                    <label for="checkInDate" class="form-label">Check-in Date</label>
                    <input
                        type="date"
                        class="form-control"
                        id="checkInDate"
                        name="checkInDate"
                        value="${reservation.checkInDate}"
                        required
                    >
                </div>
                <div class="col-md-4">
                    <label for="checkOutDate" class="form-label">Check-out Date</label>
                    <input
                        type="date"
                        class="form-control"
                        id="checkOutDate"
                        name="checkOutDate"
                        value="${reservation.checkOutDate}"
                        required
                    >
                </div>
                <div class="col-md-4">
                    <label for="status" class="form-label">Status</label>
                    <select class="form-select" id="status" name="status" required>
                        <option value="PENDING" <c:if test="${reservation.status == 'PENDING'}">selected</c:if>>PENDING</option>
                        <option value="CONFIRMED" <c:if test="${reservation.status == 'CONFIRMED'}">selected</c:if>>CONFIRMED</option>
                        <option value="CANCELLED" <c:if test="${reservation.status == 'CANCELLED'}">selected</c:if>>CANCELLED</option>
                    </select>
                </div>
                <div class="col-12">
                    <label for="specialRequests" class="form-label">Special Requests</label>
                    <textarea
                        class="form-control"
                        id="specialRequests"
                        name="specialRequests"
                        rows="3"
                    >${reservation.specialRequests}</textarea>
                </div>
            </div>
        </div>
        <div class="card-footer d-flex justify-content-end gap-2">
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/reservations">Cancel</a>
            <button type="submit" class="btn btn-primary">Update Reservation</button>
        </div>
    </form>
</div>
</body>
</html>
