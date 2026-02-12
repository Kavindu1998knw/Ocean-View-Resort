<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Add Reservation | Ocean View Resort</title>
    <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
        crossorigin="anonymous"
    />
    <style>
        :root {
            --ovr-navy: #0f2a43;
            --ovr-ocean: #1a6ea0;
            --ovr-sand: #f5f2ec;
            --ovr-card: #ffffff;
        }

        body {
            background: linear-gradient(180deg, #f7fbff 0%, var(--ovr-sand) 100%);
            min-height: 100vh;
        }

        .page-header {
            background: linear-gradient(120deg, var(--ovr-navy), var(--ovr-ocean));
            color: #fff;
            border-radius: 1rem;
        }

        .form-card {
            background: var(--ovr-card);
            border: 0;
            border-radius: 1rem;
            box-shadow: 0 10px 30px rgba(15, 42, 67, 0.08);
        }

        .required::after {
            content: " *";
            color: #dc3545;
        }
    </style>
</head>
<body>
<div class="container py-4 py-md-5">
    <div class="page-header p-4 p-md-5 mb-4">
        <h1 class="h3 mb-1">Add New Reservation</h1>
        <p class="mb-0 opacity-75">Create and save guest bookings for Ocean View Resort.</p>
    </div>

    <% String errorMsg = (String) request.getAttribute("error"); %>
    <% String successMsg = (String) request.getAttribute("success"); %>
    <% String reservationNoVal = request.getAttribute("reservationNo") == null ? "" : String.valueOf(request.getAttribute("reservationNo")); %>
    <% String guestFullNameVal = request.getAttribute("guestFullName") == null ? "" : String.valueOf(request.getAttribute("guestFullName")); %>
    <% String guestEmailVal = request.getAttribute("guestEmail") == null ? "" : String.valueOf(request.getAttribute("guestEmail")); %>
    <% String contactNumberVal = request.getAttribute("contactNumber") == null ? "" : String.valueOf(request.getAttribute("contactNumber")); %>
    <% String numberOfGuestsVal = request.getAttribute("numberOfGuests") == null ? "1" : String.valueOf(request.getAttribute("numberOfGuests")); %>
    <% String checkInDateVal = request.getAttribute("checkInDate") == null ? "" : String.valueOf(request.getAttribute("checkInDate")); %>
    <% String checkOutDateVal = request.getAttribute("checkOutDate") == null ? "" : String.valueOf(request.getAttribute("checkOutDate")); %>
    <% String specialRequestsVal = request.getAttribute("specialRequests") == null ? "" : String.valueOf(request.getAttribute("specialRequests")); %>
    <% String reservationStatusVal = request.getAttribute("reservationStatus") == null ? "PENDING" : String.valueOf(request.getAttribute("reservationStatus")); %>

    <% if (errorMsg != null && !errorMsg.trim().isEmpty()) { %>
        <div class="alert alert-danger" role="alert"><%= errorMsg %></div>
    <% } %>

    <% if (successMsg != null && !successMsg.trim().isEmpty()) { %>
        <div class="alert alert-success" role="alert"><%= successMsg %></div>
    <% } %>

    <div class="card form-card">
        <div class="card-body p-4 p-md-5">
            <form action="${pageContext.request.contextPath}/admin/reservation" method="POST" class="row g-3">
                <div class="col-md-6">
                    <label for="reservationNumber" class="form-label">Reservation Number</label>
                    <input
                        type="text"
                        class="form-control"
                        id="reservationNumber"
                        name="reservationNumber"
                        value="<%= reservationNoVal %>"
                        placeholder="Auto-generated on save"
                        readonly
                    />
                </div>

                <div class="col-md-6">
                    <label for="guestFullName" class="form-label required">Guest Full Name</label>
                    <input
                        type="text"
                        class="form-control"
                        id="guestFullName"
                        name="guestFullName"
                        value="<%= guestFullNameVal %>"
                        placeholder="e.g., John Fernando"
                        required
                    />
                </div>

                <div class="col-md-6">
                    <label for="guestEmail" class="form-label required">Guest Email</label>
                    <input
                        type="email"
                        class="form-control"
                        id="guestEmail"
                        name="guestEmail"
                        value="<%= guestEmailVal %>"
                        placeholder="e.g., john@email.com"
                        required
                    />
                </div>

                <div class="col-md-6">
                    <label for="contactNumber" class="form-label required">Contact Number</label>
                    <input
                        type="text"
                        class="form-control"
                        id="contactNumber"
                        name="contactNumber"
                        value="<%= contactNumberVal %>"
                        placeholder="e.g., +94 77 123 4567"
                        required
                    />
                </div>

                <div class="col-md-6">
                    <label for="roomType" class="form-label required">Room Type</label>
                    <select id="roomType" name="roomType" class="form-select" required>
                        <option value="" ${empty roomType ? 'selected' : ''} disabled>Select room type</option>
                        <c:forEach var="type" items="${roomTypes}">
                            <option value="${type}" ${type == roomType ? 'selected' : ''}>${type}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="col-md-6">
                    <label for="numberOfGuests" class="form-label required">Number of Guests</label>
                    <input
                        type="number"
                        class="form-control"
                        id="numberOfGuests"
                        name="numberOfGuests"
                        min="1"
                        value="<%= numberOfGuestsVal %>"
                        required
                    />
                </div>

                <div class="col-md-6">
                    <label for="checkInDate" class="form-label required">Check-in Date</label>
                    <input
                        type="date"
                        class="form-control"
                        id="checkInDate"
                        name="checkInDate"
                        value="<%= checkInDateVal %>"
                        required
                    />
                </div>

                <div class="col-md-6">
                    <label for="checkOutDate" class="form-label required">Check-out Date</label>
                    <input
                        type="date"
                        class="form-control"
                        id="checkOutDate"
                        name="checkOutDate"
                        value="<%= checkOutDateVal %>"
                        required
                    />
                </div>

                <div class="col-12">
                    <label for="specialRequests" class="form-label">Special Requests</label>
                    <textarea
                        id="specialRequests"
                        name="specialRequests"
                        class="form-control"
                        rows="4"
                        placeholder="Any preferences, arrival notes, accessibility needs, etc. (optional)"
                    ><%= specialRequestsVal %></textarea>
                </div>

                <div class="col-md-6">
                    <label for="reservationStatus" class="form-label required">Reservation Status</label>
                    <select id="reservationStatus" name="reservationStatus" class="form-select" required>
                        <option value="PENDING" <%= "PENDING".equalsIgnoreCase(reservationStatusVal) ? "selected" : "" %>>PENDING</option>
                        <option value="CONFIRMED" <%= "CONFIRMED".equalsIgnoreCase(reservationStatusVal) ? "selected" : "" %>>CONFIRMED</option>
                    </select>
                </div>

                <div class="col-12 d-flex gap-2 pt-2">
                    <button type="submit" class="btn btn-primary px-4">Save Reservation</button>
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-secondary px-4">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
    integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
    crossorigin="anonymous"
></script>
</body>
</html>
