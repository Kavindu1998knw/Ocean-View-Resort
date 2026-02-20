<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Manage Rooms | Ocean View Resort</title>
    <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
        crossorigin="anonymous"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css" />
    <style>
        .status-badge {
            font-weight: 600;
        }
    </style>
</head>
<body class="admin-body">
<%@ include file="/WEB-INF/views/admin/partials/header.jsp" %>
<div class="admin-shell">
    <%@ include file="/WEB-INF/views/admin/partials/_sidebar.jsp" %>
    <main class="admin-content">
        <div class="content-inner">
    <div class="page-header">
        <div>
            <h1 class="h3">Manage Rooms</h1>
            <p>Add, edit, and manage room availability.</p>
        </div>
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/dashboard">Back to Dashboard</a>
    </div>

    <c:if test="${not empty param.success}">
        <div class="alert alert-success" role="alert">${param.success}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger" role="alert">${param.error}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">${error}</div>
    </c:if>

    <c:set var="formId" value="" />
    <c:set var="formRoomNo" value="${roomNo}" />
    <c:set var="formRoomType" value="${roomType}" />
    <c:set var="formActive" value="true" />
    <c:set var="formPrice" value="${pricePerNight}" />
    <c:if test="${not empty active}">
        <c:set var="formActive" value="${active}" />
    </c:if>
    <c:if test="${not empty editRoom}">
        <c:set var="formId" value="${editRoom.id}" />
        <c:set var="formRoomNo" value="${editRoom.roomNo}" />
        <c:set var="formRoomType" value="${editRoom.roomType}" />
        <c:set var="formActive" value="${editRoom.active}" />
        <c:set var="formPrice" value="${editRoom.pricePerNight}" />
    </c:if>

    <div class="row g-4">
        <div class="col-lg-4">
            <div class="card card-soft">
                <div class="card-body p-4">
                    <h5 class="card-title mb-3">
                        <c:choose>
                            <c:when test="${not empty editRoom}">Edit Room</c:when>
                            <c:otherwise>Add Room</c:otherwise>
                        </c:choose>
                    </h5>
                    <form method="post" action="${pageContext.request.contextPath}/admin/rooms">
                        <input type="hidden" name="id" value="<c:out value='${formId}'/>">
                        <div class="mb-3">
                            <label for="roomNo" class="form-label">Room No</label>
                            <input
                                type="text"
                                class="form-control"
                                id="roomNo"
                                name="roomNo"
                                value="<c:out value='${formRoomNo}'/>"
                                required
                            />
                        </div>
                        <div class="mb-3">
                            <label for="roomType" class="form-label">Room Type</label>
                            <select class="form-select" id="roomType" name="roomType" required>
                                <option value="" disabled <c:if test="${empty formRoomType}">selected</c:if>>
                                    Select room type
                                </option>
                                <c:forEach var="type" items="${roomTypes}">
                                    <option value="${type}" <c:if test="${type == formRoomType}">selected</c:if>>
                                        ${type}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="pricePerNight" class="form-label">Price Per Night (LKR)</label>
                            <input
                                type="number"
                                step="0.01"
                                min="0"
                                class="form-control"
                                id="pricePerNight"
                                name="pricePerNight"
                                value="<c:out value='${formPrice}'/>"
                                required
                            />
                        </div>
                        <div class="form-check mb-3">
                            <input
                                class="form-check-input"
                                type="checkbox"
                                id="active"
                                name="active"
                                <c:if test="${formActive}">checked</c:if>
                            />
                            <label class="form-check-label" for="active">Active</label>
                        </div>
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary flex-grow-1">
                                <c:choose>
                                    <c:when test="${not empty editRoom}">Update Room</c:when>
                                    <c:otherwise>Save Room</c:otherwise>
                                </c:choose>
                            </button>
                            <c:if test="${not empty editRoom}">
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/rooms">Cancel Edit</a>
                            </c:if>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-lg-8">
            <div class="card card-soft">
                <div class="card-body p-4">
                    <h5 class="card-title mb-3">Rooms</h5>
                    <div class="table-responsive">
                        <table class="table table-striped table-hover table-clean align-middle mb-0">
                            <thead>
                            <tr>
                                <th>Room No</th>
                                <th>Room Type</th>
                                <th>Price Per Night</th>
                                <th>Active</th>
                                <th>Created At</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty rooms}">
                                    <tr>
                                        <td colspan="6" class="text-center text-muted py-4">No rooms found.</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="r" items="${rooms}">
                                        <c:set var="badgeClass" value="bg-secondary" />
                                        <c:set var="badgeText" value="Inactive" />
                                        <c:if test="${r.active}">
                                            <c:set var="badgeClass" value="bg-success" />
                                            <c:set var="badgeText" value="Active" />
                                        </c:if>
                                        <tr>
                                            <td><c:out value="${r.roomNo}"/></td>
                                            <td><c:out value="${r.roomType}"/></td>
                                            <td><c:out value="${r.pricePerNight}"/></td>
                                            <td>
                                                <span class="badge status-badge ${badgeClass}">
                                                    <c:out value="${badgeText}"/>
                                                </span>
                                            </td>
                                            <td><c:out value="${r.createdAt}"/></td>
                                            <td class="d-flex gap-2">
                                                <a class="btn btn-sm btn-outline-primary btn-action"
                                                   href="${pageContext.request.contextPath}/admin/rooms?editId=${r.id}">
                                                    Edit
                                                </a>
                                                <a class="btn btn-sm btn-outline-danger btn-action"
                                                   href="${pageContext.request.contextPath}/admin/rooms?deleteId=${r.id}"
                                                   onclick="return confirm('Are you sure you want to delete this room?');">
                                                    Delete
                                                </a>
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
        var roomTypeSelect = document.getElementById("roomType");
        var roomNoInput = document.getElementById("roomNo");

        if (!roomTypeSelect || !roomNoInput) {
            return;
        }

        var prefixMap = {
            "Garden View": "GV",
            "Ocean Suite": "OS",
            "Deluxe King": "DK",
            "Deluxe Queen": "DQ",
            "Family Villa": "FV"
        };

        function isDigitsOnly(value) {
            return /^[0-9]+$/.test(value);
        }

        function applyPrefix(preserveCursor) {
            var roomType = roomTypeSelect.value;
            var prefix = prefixMap[roomType];
            var currentValue = roomNoInput.value.trim();

            if (!prefix || !isDigitsOnly(currentValue)) {
                return;
            }

            var newValue = prefix + "-" + currentValue;
            if (newValue === roomNoInput.value) {
                return;
            }

            var start = roomNoInput.selectionStart;
            var end = roomNoInput.selectionEnd;
            var diff = newValue.length - roomNoInput.value.length;

            roomNoInput.value = newValue;

            if (preserveCursor && start !== null && end !== null) {
                var newStart = Math.min(start + diff, newValue.length);
                var newEnd = Math.min(end + diff, newValue.length);
                roomNoInput.setSelectionRange(newStart, newEnd);
            }
        }

        roomNoInput.addEventListener("input", function () {
            applyPrefix(true);
        });

        roomTypeSelect.addEventListener("change", function () {
            applyPrefix(false);
        });
    })();
</script>
</body>
</html>
