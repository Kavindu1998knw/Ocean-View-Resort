<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css" />
</head>
<body class="admin-body">
<%@ include file="/WEB-INF/views/admin/partials/header.jsp" %>
<div class="admin-shell">
    <%@ include file="/WEB-INF/views/admin/partials/_sidebar.jsp" %>
    <main class="admin-content">
        <div class="content-inner">
    <div class="page-header">
        <div>
            <h1 class="h3">Edit Reservation</h1>
            <p>Update reservation details and status.</p>
        </div>
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/reservations">Back to Reservations</a>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/reservations/update" class="card card-soft">
        <div class="card-body p-4">
            <%@ include file="/WEB-INF/views/common/form-validation.jspf" %>
            <input type="hidden" name="id" value="${oldValues['id']}">

            <div class="row g-3">
                <div class="col-md-4">
                    <label for="reservationNo" class="form-label">Reservation No</label>
                    <input
                        type="text"
                        class="form-control"
                        id="reservationNo"
                        name="reservationNo"
                        value="${fn:escapeXml(oldValues['reservationNo'])}"
                        readonly
                    >
                </div>
                <div class="col-md-4">
                    <label for="guestFullName" class="form-label">Guest Full Name</label>
                    <input
                        type="text"
                        class="form-control ${errors['guestFullName'] != null ? 'is-invalid' : ''}"
                        id="guestFullName"
                        name="guestFullName"
                        value="${fn:escapeXml(oldValues['guestFullName'])}"
                        required
                    >
                    <c:if test="${errors['guestFullName'] != null}">
                        <div class="invalid-feedback">${errors['guestFullName']}</div>
                    </c:if>
                </div>
                <div class="col-md-4">
                    <label for="guestEmail" class="form-label">Guest Email</label>
                    <input
                        type="email"
                        class="form-control ${errors['guestEmail'] != null ? 'is-invalid' : ''}"
                        id="guestEmail"
                        name="guestEmail"
                        value="${fn:escapeXml(oldValues['guestEmail'])}"
                        required
                    >
                    <c:if test="${errors['guestEmail'] != null}">
                        <div class="invalid-feedback">${errors['guestEmail']}</div>
                    </c:if>
                </div>
                <div class="col-md-4">
                    <label for="contactNumber" class="form-label">Contact Number</label>
                    <input
                        type="text"
                        class="form-control ${errors['contactNumber'] != null ? 'is-invalid' : ''}"
                        id="contactNumber"
                        name="contactNumber"
                        value="${fn:escapeXml(oldValues['contactNumber'])}"
                        required
                    >
                    <c:if test="${errors['contactNumber'] != null}">
                        <div class="invalid-feedback">${errors['contactNumber']}</div>
                    </c:if>
                </div>
                <div class="col-md-4">
                    <label for="roomType" class="form-label">Room Type</label>
                    <select class="form-select ${errors['roomType'] != null ? 'is-invalid' : ''}" id="roomType" name="roomType" required>
                        <option value="" ${empty oldValues['roomType'] ? 'selected' : ''} disabled>Select room type</option>
                        <c:forEach var="type" items="${roomTypes}">
                            <option value="${type}" ${type == oldValues['roomType'] ? 'selected' : ''}>${type}</option>
                        </c:forEach>
                    </select>
                    <c:if test="${errors['roomType'] != null}">
                        <div class="invalid-feedback">${errors['roomType']}</div>
                    </c:if>
                </div>
                <div class="col-md-4">
                    <label for="roomId" class="form-label">Room (Room No)</label>
                    <select class="form-select ${errors['roomId'] != null ? 'is-invalid' : ''}" id="roomId" name="roomId" required>
                        <option value="" selected disabled>Select room</option>
                    </select>
                    <div id="roomIdHelp" class="form-text text-muted d-none">No active rooms available for this room type.</div>
                    <c:if test="${errors['roomId'] != null}">
                        <div class="invalid-feedback">${errors['roomId']}</div>
                    </c:if>
                </div>
                <div class="col-md-4">
                    <label for="numberOfGuests" class="form-label">Number of Guests</label>
                    <input
                        type="number"
                        class="form-control ${errors['numberOfGuests'] != null ? 'is-invalid' : ''}"
                        id="numberOfGuests"
                        name="numberOfGuests"
                        value="${oldValues['numberOfGuests']}"
                        min="1"
                        required
                    >
                    <c:if test="${errors['numberOfGuests'] != null}">
                        <div class="invalid-feedback">${errors['numberOfGuests']}</div>
                    </c:if>
                </div>
                <div class="col-md-4">
                    <label for="checkInDate" class="form-label">Check-in Date</label>
                    <input
                        type="date"
                        class="form-control ${errors['checkInDate'] != null ? 'is-invalid' : ''}"
                        id="checkInDate"
                        name="checkInDate"
                        value="${oldValues['checkInDate']}"
                        required
                    >
                    <c:if test="${errors['checkInDate'] != null}">
                        <div class="invalid-feedback">${errors['checkInDate']}</div>
                    </c:if>
                </div>
                <div class="col-md-4">
                    <label for="checkOutDate" class="form-label">Check-out Date</label>
                    <input
                        type="date"
                        class="form-control ${errors['checkOutDate'] != null ? 'is-invalid' : ''}"
                        id="checkOutDate"
                        name="checkOutDate"
                        value="${oldValues['checkOutDate']}"
                        required
                    >
                    <c:if test="${errors['checkOutDate'] != null}">
                        <div class="invalid-feedback">${errors['checkOutDate']}</div>
                    </c:if>
                </div>
                <div class="col-md-4">
                    <label for="status" class="form-label">Status</label>
                    <select class="form-select ${errors['status'] != null ? 'is-invalid' : ''}" id="status" name="status" required>
                        <option value="PENDING" ${empty oldValues['status'] || oldValues['status'] == 'PENDING' ? 'selected' : ''}>PENDING</option>
                        <option value="CONFIRMED" ${oldValues['status'] == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                        <option value="CANCELLED" ${oldValues['status'] == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                        <option value="CHECKED_IN" ${oldValues['status'] == 'CHECKED_IN' ? 'selected' : ''}>CHECKED_IN</option>
                    </select>
                    <c:if test="${errors['status'] != null}">
                        <div class="invalid-feedback">${errors['status']}</div>
                    </c:if>
                </div>
                <div class="col-12">
                    <label for="specialRequests" class="form-label">Special Requests</label>
                    <textarea
                        class="form-control"
                        id="specialRequests"
                        name="specialRequests"
                        rows="3"
                    >${fn:escapeXml(oldValues['specialRequests'])}</textarea>
                </div>
            </div>
        </div>
        <div class="card-footer d-flex justify-content-end gap-2">
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/reservations">Cancel</a>
            <button type="submit" class="btn btn-primary">Update Reservation</button>
        </div>
    </form>
        </div>
    </main>
</div>
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
