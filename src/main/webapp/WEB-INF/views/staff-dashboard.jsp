<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Staff Dashboard | Ocean View Resort</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css" />
    <style>
        .staff-sidebar {
            background: linear-gradient(180deg, #f6f8fc 0%, #eef2f8 100%);
            border-right: 1px solid #e3e8f2;
        }

        .staff-sidebar .sidebar-title {
            font-size: 13px;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            color: #64748b;
            margin-bottom: 18px;
        }

        .staff-sidebar .sidebar-nav .nav-link {
            border-radius: 10px;
            padding: 10px 14px;
            margin-bottom: 8px;
            transition: background 0.2s ease, color 0.2s ease, transform 0.15s ease;
        }

        .staff-sidebar .sidebar-nav .nav-link:hover {
            background: #e8f0ff;
            color: #1d4ed8;
            transform: translateX(2px);
        }

        .staff-sidebar .sidebar-nav .nav-link.active {
            background: #2563eb;
            color: #ffffff;
        }

        .staff-sidebar .sidebar-nav .nav-link.active .dot {
            background: #ffffff;
        }

        .staff-hero {
            background: linear-gradient(135deg, #eaf2ff 0%, #f5f8ff 60%, #ffffff 100%);
            border-radius: 16px;
            padding: 24px 28px;
            box-shadow: 0 18px 40px rgba(15, 23, 42, 0.08);
            border: 1px solid #e3e9f5;
        }

        .staff-hero h1 {
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 6px;
        }

        .staff-hero p {
            margin: 0;
            color: #64748b;
        }

        .staff-stat-card {
            background: #ffffff;
            border-radius: 16px;
            box-shadow: 0 12px 24px rgba(15, 23, 42, 0.08);
            border: 1px solid #eef2f8;
        }

        .staff-stat-card .stat-label {
            font-size: 12px;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            color: #94a3b8;
        }

        .staff-stat-card .stat-value {
            font-size: 26px;
            font-weight: 700;
            margin-top: 6px;
            color: #0f172a;
        }

        .staff-table-card {
            border-radius: 16px;
            overflow: hidden;
            border: 1px solid #eef2f8;
            box-shadow: 0 14px 30px rgba(15, 23, 42, 0.08);
        }

        .staff-table-card .card-body {
            padding: 0;
        }

        .staff-table-header {
            background: #f8fafc;
            padding: 16px 20px;
            border-bottom: 1px solid #e2e8f0;
        }

        .staff-table-header h5 {
            margin: 0;
            font-size: 16px;
            font-weight: 600;
        }

        .staff-table-card table th,
        .staff-table-card table td {
            padding: 12px 16px;
            font-size: 14px;
        }

        .staff-table-card table tbody tr:hover {
            background: #f1f5ff;
        }

        .staff-actions .btn {
            border-radius: 10px;
            font-weight: 600;
            transition: transform 0.15s ease, box-shadow 0.2s ease;
        }

        .staff-actions .btn:hover {
            transform: translateY(-1px);
            box-shadow: 0 10px 20px rgba(37, 99, 235, 0.18);
        }
    </style>
</head>
<body class="admin-body">
<%@ include file="/WEB-INF/views/admin/partials/header.jsp" %>
<div class="admin-shell">
    <aside class="admin-sidebar staff-sidebar">
        <div class="sidebar-inner">
            <div class="sidebar-title">Staff Panel</div>
            <nav class="nav flex-column sidebar-nav">
                <a class="nav-link active" href="${pageContext.request.contextPath}/staff/dashboard">
                    <span class="dot"></span>Dashboard
                </a>
                <a class="nav-link" href="${pageContext.request.contextPath}/admin/reservations">
                    <span class="dot"></span>View Reservations
                </a>
                <a class="nav-link" href="${pageContext.request.contextPath}/admin/reservations/search">
                    <span class="dot"></span>Search Reservation
                </a>
                <a class="nav-link" href="${pageContext.request.contextPath}/logout" onclick="return confirm('Are you sure you want to logout?');">
                    <span class="dot"></span>Exit / Logout
                </a>
            </nav>
        </div>
    </aside>
    <main class="admin-content">
        <div class="content-inner">
            <div class="staff-hero mb-4">
                <h1>Welcome back, ${sessionScope.authName}.</h1>
                <p>Here is a quick overview of your reservations and tasks today.</p>
            </div>

            <div class="row g-4 mb-4">
                <div class="col-12 col-md-6 col-xl-3">
                    <div class="staff-stat-card">
                        <div class="card-body">
                            <div class="stat-label">My Reservations</div>
                            <div class="stat-value">${totalReservations}</div>
                        </div>
                    </div>
                </div>
                <div class="col-12 col-md-6 col-xl-3">
                    <div class="staff-stat-card">
                        <div class="card-body">
                            <div class="stat-label">Upcoming Check-ins</div>
                            <div class="stat-value">${upcomingCheckIns}</div>
                        </div>
                    </div>
                </div>
                <div class="col-12 col-md-6 col-xl-3">
                    <div class="staff-stat-card">
                        <div class="card-body">
                            <div class="stat-label">Upcoming Check-outs</div>
                            <div class="stat-value">${upcomingCheckOuts}</div>
                        </div>
                    </div>
                </div>
                <div class="col-12 col-md-6 col-xl-3">
                    <div class="staff-stat-card">
                        <div class="card-body">
                            <div class="stat-label">Rooms Available</div>
                            <div class="stat-value">${empty availableRooms ? 0 : availableRooms}</div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row g-4">
                <div class="col-12 col-xl-8">
                    <div class="staff-table-card">
                        <div class="staff-table-header d-flex justify-content-between align-items-center">
                            <h5>Recent Reservations</h5>
                            <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/admin/reservations">View all</a>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-clean align-middle mb-0">
                                    <thead>
                                    <tr>
                                        <th>Reservation No</th>
                                        <th>Room Type</th>
                                        <th>Room No</th>
                                        <th>Check In</th>
                                        <th>Check Out</th>
                                        <th>Status</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${empty recentReservations}">
                                            <tr>
                                                <td colspan="6" class="text-muted text-center py-4">
                                                    No recent reservations found.
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="r" items="${recentReservations}">
                                                <tr>
                                                    <td><c:out value="${r.reservationNo}"/></td>
                                                    <td><c:out value="${r.roomType}"/></td>
                                                    <td><c:out value="${empty r.roomNo ? '-' : r.roomNo}"/></td>
                                                    <td><c:out value="${r.checkInDate}"/></td>
                                                    <td><c:out value="${r.checkOutDate}"/></td>
                                                    <td><c:out value="${r.status}"/></td>
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

                <div class="col-12 col-xl-4">
                    <div class="card card-soft staff-actions">
                        <div class="card-body">
                            <h5 class="mb-3">Quick Actions</h5>
                            <div class="d-grid gap-2">
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/reservations/search">Search Reservation</a>
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/reservations">View Reservations</a>
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/bill">Print Bill</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>
</body>
</html>
