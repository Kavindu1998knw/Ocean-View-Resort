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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css" />
    <style>
        .required::after {
            content: " *";
            color: #dc3545;
        }

        .bill-item {
            display: flex;
            justify-content: space-between;
            padding: 0.35rem 0;
            border-bottom: 1px dashed #e9ecef;
        }

        .bill-item:last-child {
            border-bottom: 0;
        }
    </style>
</head>
<body class="admin-body">
<%@ include file="/WEB-INF/views/admin/partials/header.jsp" %>
<div class="admin-shell">
    <%@ include file="/WEB-INF/views/staff/partials/_sidebar.jsp" %>
    <main class="admin-content">
        <div class="content-inner">
    <div class="page-header">
        <div>
            <h1 class="h3">Add New Reservation</h1>
            <p>Create and save guest bookings for Ocean View Resort.</p>
        </div>
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/staff/dashboard">Back to Dashboard</a>
    </div>

    <% String errorMsg = (String) request.getAttribute("error"); %>
    <% String successMsg = (String) request.getAttribute("success"); %>
    <% String reservationNoVal = request.getAttribute("reservationNo") == null ? "" : String.valueOf(request.getAttribute("reservationNo")); %>
    <% String guestFullNameVal = request.getAttribute("guestFullName") == null ? "" : String.valueOf(request.getAttribute("guestFullName")); %>
    <% String guestEmailVal = request.getAttribute("guestEmail") == null ? "" : String.valueOf(request.getAttribute("guestEmail")); %>
    <% String contactNumberVal = request.getAttribute("contactNumber") == null ? "" : String.valueOf(request.getAttribute("contactNumber")); %>
    <% String roomIdVal = request.getAttribute("roomId") == null ? "" : String.valueOf(request.getAttribute("roomId")); %>
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

    <div class="card card-soft">
        <div class="card-body p-4 p-md-5">
            <form action="${pageContext.request.contextPath}/staff/reservation/add" method="POST" class="row g-3">
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
                    <label for="roomId" class="form-label required">Room (Room No)</label>
                    <select id="roomId" name="roomId" class="form-select" required>
                        <option value="" selected disabled>Select room</option>
                    </select>
                    <div id="roomIdHelp" class="form-text text-muted d-none">No active rooms available for this room type.</div>
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

                <div class="col-12">
                    <div class="card card-soft">
                        <div class="card-body">
                            <h5 class="card-title mb-3">Bill Summary (Preview)</h5>
                            <div id="billValidationMessage" class="alert alert-warning py-2 px-3 mb-3 d-none" role="alert"></div>
                            <div class="bill-item">
                                <span>Rate per Night</span>
                                <strong id="billRate">-</strong>
                            </div>
                            <div class="bill-item">
                                <span>Nights</span>
                                <strong id="billNights">-</strong>
                            </div>
                            <div class="bill-item">
                                <span>Subtotal</span>
                                <strong id="billSubtotal">-</strong>
                            </div>
                            <div class="bill-item">
                                <span>Service Charge (10%)</span>
                                <strong id="billServiceCharge">-</strong>
                            </div>
                            <div class="bill-item">
                                <span>Total</span>
                                <strong id="billTotal">-</strong>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-12 d-flex gap-2 pt-2">
                    <button type="submit" class="btn btn-primary px-4">Save Reservation</button>
                    <a href="${pageContext.request.contextPath}/staff/dashboard" class="btn btn-outline-secondary px-4">Cancel</a>
                </div>
            </form>
        </div>
    </div>
        </div>
    </main>
</div>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
    integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
    crossorigin="anonymous"
></script>
<script>
    (function () {
        const contextPath = "${pageContext.request.contextPath}";
        const roomTypeEl = document.getElementById("roomType");
        const roomIdEl = document.getElementById("roomId");
        const roomIdHelpEl = document.getElementById("roomIdHelp");
        const checkInEl = document.getElementById("checkInDate");
        const checkOutEl = document.getElementById("checkOutDate");

        const rateEl = document.getElementById("billRate");
        const nightsEl = document.getElementById("billNights");
        const subtotalEl = document.getElementById("billSubtotal");
        const serviceEl = document.getElementById("billServiceCharge");
        const totalEl = document.getElementById("billTotal");
        const validationEl = document.getElementById("billValidationMessage");

        let initialRoomId = "<%= roomIdVal %>";
        let currentRate = null;

        function formatLkr(value) {
            return "LKR " + Number(value).toLocaleString("en-LK", {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            });
        }

        function resetBill() {
            rateEl.textContent = "-";
            nightsEl.textContent = "-";
            subtotalEl.textContent = "-";
            serviceEl.textContent = "-";
            totalEl.textContent = "-";
        }

        function setRoomSelectState(enabled, message) {
            roomIdEl.disabled = !enabled;
            if (message) {
                roomIdHelpEl.textContent = message;
                roomIdHelpEl.classList.remove("d-none");
            } else {
                roomIdHelpEl.textContent = "";
                roomIdHelpEl.classList.add("d-none");
            }
        }

        function resetRoomOptions() {
            roomIdEl.innerHTML = "";
            const option = document.createElement("option");
            option.value = "";
            option.textContent = "Select room";
            option.disabled = true;
            option.selected = true;
            roomIdEl.appendChild(option);
        }

        function showValidation(message) {
            if (!message) {
                validationEl.classList.add("d-none");
                validationEl.textContent = "";
                return;
            }
            validationEl.textContent = message;
            validationEl.classList.remove("d-none");
        }

        function parseIsoDate(value) {
            if (!value) return null;
            const parts = value.split("-");
            if (parts.length !== 3) return null;
            const year = Number(parts[0]);
            const month = Number(parts[1]) - 1;
            const day = Number(parts[2]);
            const date = new Date(year, month, day);
            return Number.isNaN(date.getTime()) ? null : date;
        }

        function calculatePreview() {
            showValidation("");

            if (currentRate === null) {
                resetBill();
                return;
            }

            rateEl.textContent = formatLkr(currentRate);

            const checkIn = parseIsoDate(checkInEl.value);
            const checkOut = parseIsoDate(checkOutEl.value);

            if (!checkIn || !checkOut) {
                nightsEl.textContent = "-";
                subtotalEl.textContent = "-";
                serviceEl.textContent = "-";
                totalEl.textContent = "-";
                return;
            }

            const msPerDay = 24 * 60 * 60 * 1000;
            const rawNights = Math.floor((checkOut.getTime() - checkIn.getTime()) / msPerDay);

            if (rawNights <= 0) {
                showValidation("Check-out date must be later than check-in date.");
                nightsEl.textContent = "-";
                subtotalEl.textContent = "-";
                serviceEl.textContent = "-";
                totalEl.textContent = "-";
                return;
            }

            const nights = Math.max(1, rawNights);
            const subtotal = nights * currentRate;
            const serviceCharge = subtotal * 0.10;
            const total = subtotal + serviceCharge;

            nightsEl.textContent = nights;
            subtotalEl.textContent = formatLkr(subtotal);
            serviceEl.textContent = formatLkr(serviceCharge);
            totalEl.textContent = formatLkr(total);
        }

        function setRateFromSelection() {
            const selected = roomIdEl.options[roomIdEl.selectedIndex];
            if (!selected || !selected.dataset.price) {
                currentRate = null;
                resetBill();
                return;
            }
            currentRate = Number(selected.dataset.price);
            calculatePreview();
        }

        async function fetchRoomsAndRecalculate() {
            const roomType = roomTypeEl.value;
            currentRate = null;
            resetBill();
            showValidation("");
            resetRoomOptions();
            setRoomSelectState(false, "");

            if (!roomType) {
                return;
            }

            try {
                const url = contextPath + "/staff/api/rooms?roomType=" + encodeURIComponent(roomType);
                const response = await fetch(url, {headers: {"Accept": "application/json"}});
                if (!response.ok) {
                    showValidation("Unable to load rooms. Please try again.");
                    setRoomSelectState(false, "No active rooms available for this room type.");
                    return;
                }

                const data = await response.json();
                if (!Array.isArray(data) || data.length === 0) {
                    setRoomSelectState(false, "No active rooms available for this room type.");
                    return;
                }

                data.forEach((room) => {
                    if (!room || !room.id || !room.roomNo) {
                        return;
                    }
                    const option = document.createElement("option");
                    option.value = room.id;
                    option.textContent = room.roomNo;
                    if (typeof room.pricePerNight === "number") {
                        option.dataset.price = String(room.pricePerNight);
                    }
                    roomIdEl.appendChild(option);
                });

                setRoomSelectState(true, "");
                if (initialRoomId) {
                    roomIdEl.value = initialRoomId;
                    initialRoomId = "";
                }
                setRateFromSelection();
            } catch (e) {
                showValidation("Unable to load rooms. Please try again.");
                setRoomSelectState(false, "No active rooms available for this room type.");
            }
        }

        roomTypeEl.addEventListener("change", fetchRoomsAndRecalculate);
        roomIdEl.addEventListener("change", setRateFromSelection);
        checkInEl.addEventListener("change", calculatePreview);
        checkOutEl.addEventListener("change", calculatePreview);

        if (roomTypeEl.value) {
            fetchRoomsAndRecalculate();
        } else {
            resetRoomOptions();
            setRoomSelectState(false, "");
        }
    })();
</script>
</body>
</html>
