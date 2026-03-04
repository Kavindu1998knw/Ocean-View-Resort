<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Edit Reservation | Ocean View Resort</title>
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
    <div class="page-header">
        <div>
            <h1 class="h3">Edit Reservation</h1>
            <p>Update guest booking details.</p>
        </div>
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/reservations">Back to Reservations</a>
    </div>

    <div class="card card-soft">
        <div class="card-body p-4 p-md-5">
            <form action="${pageContext.request.contextPath}/reservations/update" method="POST" class="row g-3">
                <%@ include file="/WEB-INF/views/common/form-validation.jspf" %>
                <input type="hidden" name="id" value="${oldValues['id']}" />
                <div class="col-md-6">
                    <label for="reservationNumber" class="form-label">Reservation Number</label>
                    <input
                        type="text"
                        class="form-control"
                        id="reservationNumber"
                        name="reservationNo"
                        value="${fn:escapeXml(oldValues['reservationNo'])}"
                        readonly
                    />
                </div>

                <div class="col-md-6">
                    <label for="guestFullName" class="form-label required">Guest Full Name</label>
                    <input
                        type="text"
                        class="form-control ${errors['guestFullName'] != null ? 'is-invalid' : ''}"
                        id="guestFullName"
                        name="guestFullName"
                        value="${fn:escapeXml(oldValues['guestFullName'])}"
                        required
                    />
                    <c:if test="${errors['guestFullName'] != null}">
                        <div class="invalid-feedback">${errors['guestFullName']}</div>
                    </c:if>
                </div>

                <div class="col-md-6">
                    <label for="guestEmail" class="form-label required">Guest Email</label>
                    <input
                        type="email"
                        class="form-control ${errors['guestEmail'] != null ? 'is-invalid' : ''}"
                        id="guestEmail"
                        name="guestEmail"
                        value="${fn:escapeXml(oldValues['guestEmail'])}"
                        required
                    />
                    <c:if test="${errors['guestEmail'] != null}">
                        <div class="invalid-feedback">${errors['guestEmail']}</div>
                    </c:if>
                </div>

                <div class="col-md-6">
                    <label for="contactNumber" class="form-label required">Contact Number</label>
                    <input
                        type="text"
                        class="form-control ${errors['contactNumber'] != null ? 'is-invalid' : ''}"
                        id="contactNumber"
                        name="contactNumber"
                        value="${fn:escapeXml(oldValues['contactNumber'])}"
                        required
                    />
                    <c:if test="${errors['contactNumber'] != null}">
                        <div class="invalid-feedback">${errors['contactNumber']}</div>
                    </c:if>
                </div>

                <div class="col-md-6">
                    <label for="roomType" class="form-label required">Room Type</label>
                    <select id="roomType" name="roomType" class="form-select ${errors['roomType'] != null ? 'is-invalid' : ''}" required>
                        <option value="" ${empty oldValues['roomType'] ? 'selected' : ''} disabled>Select room type</option>
                        <c:forEach var="type" items="${roomTypes}">
                            <option value="${type}" ${type == oldValues['roomType'] ? 'selected' : ''}>${type}</option>
                        </c:forEach>
                    </select>
                    <c:if test="${errors['roomType'] != null}">
                        <div class="invalid-feedback">${errors['roomType']}</div>
                    </c:if>
                </div>

                <div class="col-md-6">
                    <label for="roomId" class="form-label required">Room (Room No)</label>
                    <select id="roomId" name="roomId" class="form-select ${errors['roomId'] != null ? 'is-invalid' : ''}" required>
                        <option value="" selected disabled>Select room</option>
                    </select>
                    <div id="roomIdHelp" class="form-text text-muted d-none">No active rooms available for this room type.</div>
                    <c:if test="${errors['roomId'] != null}">
                        <div class="invalid-feedback">${errors['roomId']}</div>
                    </c:if>
                </div>

                <div class="col-md-6">
                    <label for="numberOfGuests" class="form-label required">Number of Guests</label>
                    <input
                        type="number"
                        class="form-control ${errors['numberOfGuests'] != null ? 'is-invalid' : ''}"
                        id="numberOfGuests"
                        name="numberOfGuests"
                        min="1"
                        value="${oldValues['numberOfGuests']}"
                        required
                    />
                    <c:if test="${errors['numberOfGuests'] != null}">
                        <div class="invalid-feedback">${errors['numberOfGuests']}</div>
                    </c:if>
                </div>

                <div class="col-md-6">
                    <label for="checkInDate" class="form-label required">Check-in Date</label>
                    <input
                        type="date"
                        class="form-control ${errors['checkInDate'] != null ? 'is-invalid' : ''}"
                        id="checkInDate"
                        name="checkInDate"
                        value="${oldValues['checkInDate']}"
                        required
                    />
                    <c:if test="${errors['checkInDate'] != null}">
                        <div class="invalid-feedback">${errors['checkInDate']}</div>
                    </c:if>
                </div>

                <div class="col-md-6">
                    <label for="checkOutDate" class="form-label required">Check-out Date</label>
                    <input
                        type="date"
                        class="form-control ${errors['checkOutDate'] != null ? 'is-invalid' : ''}"
                        id="checkOutDate"
                        name="checkOutDate"
                        value="${oldValues['checkOutDate']}"
                        required
                    />
                    <c:if test="${errors['checkOutDate'] != null}">
                        <div class="invalid-feedback">${errors['checkOutDate']}</div>
                    </c:if>
                </div>

                <div class="col-12">
                    <label for="specialRequests" class="form-label">Special Requests</label>
                    <textarea
                        id="specialRequests"
                        name="specialRequests"
                        class="form-control"
                        rows="4"
                    >${fn:escapeXml(oldValues['specialRequests'])}</textarea>
                </div>

                <div class="col-md-6">
                    <label for="status" class="form-label required">Reservation Status</label>
                    <select id="status" name="status" class="form-select ${errors['status'] != null ? 'is-invalid' : ''}" required>
                        <option value="PENDING" ${empty oldValues['status'] || oldValues['status'] == 'PENDING' ? 'selected' : ''}>PENDING</option>
                        <option value="CONFIRMED" ${oldValues['status'] == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                        <option value="CANCELLED" ${oldValues['status'] == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                        <option value="CHECKED_IN" ${oldValues['status'] == 'CHECKED_IN' ? 'selected' : ''}>CHECKED_IN</option>
                    </select>
                    <c:if test="${errors['status'] != null}">
                        <div class="invalid-feedback">${errors['status']}</div>
                    </c:if>
                </div>

                <div class="col-12 d-flex gap-2 pt-2">
                    <button type="submit" class="btn btn-primary px-4">Update Reservation</button>
                    <a href="${pageContext.request.contextPath}/reservations" class="btn btn-outline-secondary px-4">Cancel</a>
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

        let initialRoomId = "${oldValues['roomId']}";

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

        async function fetchRooms() {
            const roomType = roomTypeEl.value;
            resetRoomOptions();
            setRoomSelectState(false, "");

            if (!roomType) {
                return;
            }

            try {
                const url = contextPath + "/api/rooms?roomType=" + encodeURIComponent(roomType);
                const response = await fetch(url, {headers: {"Accept": "application/json"}});
                if (!response.ok) {
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
                    roomIdEl.appendChild(option);
                });

                setRoomSelectState(true, "");
                if (initialRoomId) {
                    roomIdEl.value = initialRoomId;
                    initialRoomId = "";
                }
            } catch (e) {
                setRoomSelectState(false, "No active rooms available for this room type.");
            }
        }

        roomTypeEl.addEventListener("change", fetchRooms);
        if (roomTypeEl.value) {
            fetchRooms();
        } else {
            resetRoomOptions();
            setRoomSelectState(false, "");
        }
    })();
</script>
</body>
</html>
