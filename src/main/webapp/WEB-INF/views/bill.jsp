<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calculate &amp; Print Bill</title>
    <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
        integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
        crossorigin="anonymous"
    >
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css" />
    <style>
        @media print {
            @page {
                margin: 12mm;
            }

            body {
                background: #ffffff !important;
            }

            .no-print,
            .ov-navbar,
            .admin-sidebar,
            .admin-shell,
            .admin-content,
            .content-inner,
            .page-header,
            #billForm,
            .card,
            .table-responsive,
            .alert,
            .actions,
            .action-buttons {
                display: none !important;
            }

            #print-area {
                display: block !important;
                width: 100% !important;
                max-width: 100% !important;
                padding: 0 !important;
                margin: 0 !important;
                box-shadow: none !important;
                border: none !important;
                background: transparent !important;
            }

            #print-area .print-card {
                box-shadow: none !important;
                border: 1px solid #e5e7eb !important;
            }

            #print-area h1,
            #print-area h2 {
                color: #000 !important;
            }

            #print-area table {
                page-break-inside: avoid;
            }

            #print-area .print-section {
                page-break-inside: avoid;
            }

            .print-only {
                display: block !important;
            }
        }

        .print-only {
            display: none;
        }

        #print-area .print-card {
            border-radius: 12px;
            border: 1px solid #e5e7eb;
            background: #ffffff;
            padding: 24px;
        }

        #print-area .print-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 16px;
        }

        #print-area .print-title {
            font-size: 22px;
            font-weight: 700;
            margin: 0;
        }

        #print-area .print-meta {
            font-size: 13px;
            color: #4b5563;
        }

        #print-area .print-section-title {
            font-size: 16px;
            font-weight: 600;
            margin: 16px 0 8px;
        }

        #print-area .print-table {
            width: 100%;
            border-collapse: collapse;
            font-size: 14px;
        }

        #print-area .print-table th,
        #print-area .print-table td {
            padding: 6px 0;
            vertical-align: top;
        }

        #print-area .print-table th {
            width: 45%;
            color: #374151;
            font-weight: 600;
        }

        #print-area .print-footer {
            margin-top: 16px;
            font-size: 13px;
            color: #4b5563;
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
            <h1 class="h3">Calculate &amp; Print Bill</h1>
            <p>Generate a bill summary for a reservation.</p>
        </div>
        <a class="btn btn-outline-secondary no-print" href="${pageContext.request.contextPath}/admin/dashboard">Back to Dashboard</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">${error}</div>
    </c:if>
    <c:if test="${not empty notFound}">
        <div class="alert alert-warning" role="alert">No reservation found.</div>
    </c:if>

    <form method="get" action="${pageContext.request.contextPath}/admin/bill" class="card card-soft mb-4 no-print" id="billForm">
        <div class="card-body p-4">
            <div class="row g-3 align-items-end">
                <div class="col-md-9">
                    <label for="reservationNo" class="form-label">Reservation Number</label>
                    <input
                        type="text"
                        class="form-control"
                        id="reservationNo"
                        name="reservationNo"
                        value="${param.reservationNo}"
                        placeholder="e.g., RES-20260212-9133"
                        required
                    >
                </div>
                <div class="col-md-3 d-grid">
                    <button type="submit" class="btn btn-primary">Calculate</button>
                </div>
            </div>
        </div>
    </form>

    <div class="card card-soft mb-4 no-print">
        <div class="card-header bg-transparent border-0 pb-0">
            <h2 class="h5 mb-0">Reservations</h2>
        </div>
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${empty recentReservations}">
                    <div class="p-4 text-muted">No recent reservations found.</div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-striped table-hover table-clean w-100 align-middle mb-0">
                            <thead>
                            <tr>
                                <th>Reservation No</th>
                                <th>Guest Name</th>
                                <th>Room Type</th>
                                <th>Room No</th>
                                <th>Check-in</th>
                                <th>Check-out</th>
                                <th>Status</th>
                                <th class="actions-col">Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="r" items="${recentReservations}">
                                <tr>
                                    <td class="cell-wrap"><c:out value="${r.reservationNo}"/></td>
                                    <td><c:out value="${r.guestFullName}"/></td>
                                    <td><c:out value="${r.roomType}"/></td>
                                    <td><c:out value="${empty r.roomNo ? '-' : r.roomNo}"/></td>
                                    <td><c:out value="${r.checkInDate}"/></td>
                                    <td><c:out value="${r.checkOutDate}"/></td>
                                    <td><c:out value="${r.status}"/></td>
                                    <td class="actions text-center align-middle">
                                        <div class="action-buttons">
                                            <button
                                                type="button"
                                                class="btn btn-sm btn-outline-primary select-res"
                                                data-resno="${r.reservationNo}">
                                                Select
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <c:if test="${not empty reservation}">
        <div id="print-area" class="print-section">
            <div class="print-card">
                <div class="print-header">
                    <div>
                        <h1 class="print-title">Ocean View Resort – Bill</h1>
                        <div class="print-meta">Printed on <span class="print-date" data-print-date></span></div>
                    </div>
                    <div class="print-meta text-end">
                        Reservation No: <strong><c:out value="${reservation.reservationNo}"/></strong>
                    </div>
                </div>

                <div class="print-section">
                    <div class="print-section-title">Reservation Details</div>
                    <table class="print-table">
                        <tbody>
                        <tr>
                            <th>Guest Name</th>
                            <td><c:out value="${reservation.guestFullName}"/></td>
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
                            <th>Check-in</th>
                            <td><c:out value="${reservation.checkInDate}"/></td>
                        </tr>
                        <tr>
                            <th>Check-out</th>
                            <td><c:out value="${reservation.checkOutDate}"/></td>
                        </tr>
                        <tr>
                            <th>Nights</th>
                            <td><c:out value="${nights}"/></td>
                        </tr>
                        <tr>
                            <th>Guests</th>
                            <td><c:out value="${reservation.numberOfGuests}"/></td>
                        </tr>
                        <tr>
                            <th>Status</th>
                            <td><c:out value="${reservation.status}"/></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="print-section">
                    <div class="print-section-title">Bill Summary</div>
                    <table class="print-table">
                        <tbody>
                        <tr>
                            <th>Rate per Night</th>
                            <td class="text-end">LKR <c:out value="${rate}"/></td>
                        </tr>
                        <tr>
                            <th>Subtotal</th>
                            <td class="text-end">LKR <c:out value="${subtotal}"/></td>
                        </tr>
                        <tr>
                            <th>Service Charge (10%)</th>
                            <td class="text-end">LKR <c:out value="${serviceCharge}"/></td>
                        </tr>
                        <tr class="border-top">
                            <th>Total</th>
                            <td class="text-end fw-bold">LKR <c:out value="${total}"/></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="print-footer">Thank you for staying with Ocean View Resort.</div>
            </div>
        </div>

        <div class="d-flex justify-content-end mt-3 no-print">
            <button type="button" class="btn btn-success" onclick="window.print()">Print Bill</button>
        </div>
    </c:if>
        </div>
    </main>
</div>
<script>
    (function () {
        var form = document.getElementById("billForm");
        var input = document.getElementById("reservationNo");
        if (!form || !input) {
            return;
        }

        var buttons = document.querySelectorAll(".select-res");
        buttons.forEach(function (btn) {
            btn.addEventListener("click", function () {
                var resNo = btn.getAttribute("data-resno");
                if (!resNo) {
                    return;
                }
                input.value = resNo;
                form.submit();
            });
        });

        var printDate = document.querySelector("[data-print-date]");
        if (printDate) {
            var now = new Date();
            printDate.textContent = now.toLocaleString();
        }
    })();
</script>
</body>
</html>
