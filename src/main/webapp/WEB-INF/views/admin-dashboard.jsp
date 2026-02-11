<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" isELIgnored="false" %>
<%
    javax.servlet.http.HttpSession currentSession = request.getSession(false);
    Object loggedUserAttr = (currentSession != null) ? currentSession.getAttribute("loggedUser") : null;

    String loggedUser = "Admin";
    if (loggedUserAttr instanceof com.icbt.oceanview.model.User) {
        com.icbt.oceanview.model.User sessionUser = (com.icbt.oceanview.model.User) loggedUserAttr;
        if (sessionUser.getName() != null && !sessionUser.getName().trim().isEmpty()) {
            loggedUser = sessionUser.getName();
        }
    } else if (loggedUserAttr != null) {
        loggedUser = String.valueOf(loggedUserAttr);
    }
    String contextPath = request.getContextPath();

    String successMsg = (request.getAttribute("success") != null)
        ? String.valueOf(request.getAttribute("success"))
        : null;

    String errorMsg = (request.getAttribute("error") != null)
        ? String.valueOf(request.getAttribute("error"))
        : null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Admin Dashboard | Ocean View Resort</title>
    <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
        crossorigin="anonymous"
    />
    <style>
        :root {
            --ovr-navy: #0b1b2b;
            --ovr-ocean: #0e6ba8;
            --ovr-sea: #1ba5b8;
            --ovr-sand: #f5f0e6;
            --ovr-foam: #f8fbff;
            --ovr-muted: #6b7a89;
        }

        body {
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(180deg, #eef7fb 0%, #f8fbff 45%, #ffffff 100%);
            color: #1c2733;
            min-height: 100vh;
        }

        .navbar {
            background: var(--ovr-navy);
        }

        .navbar-brand {
            font-weight: 700;
            letter-spacing: 0.5px;
        }

        .navbar .nav-link,
        .navbar .navbar-text {
            color: #f0f6ff;
        }

        .navbar .nav-link:hover {
            color: #bcdcff;
        }

        .layout {
            display: flex;
            min-height: calc(100vh - 56px);
        }

        .sidebar {
            width: 260px;
            background: #ffffff;
            border-right: 1px solid #e2e8f0;
            box-shadow: 0 12px 30px rgba(11, 27, 43, 0.06);
            padding: 24px 16px;
        }

        .sidebar-title {
            font-size: 12px;
            letter-spacing: 1px;
            text-transform: uppercase;
            color: var(--ovr-muted);
            margin-bottom: 12px;
        }

        .sidebar .nav-link {
            color: #1d2a39;
            border-radius: 10px;
            padding: 10px 12px;
            margin-bottom: 4px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .sidebar .nav-link.active,
        .sidebar .nav-link:hover {
            background: rgba(14, 107, 168, 0.1);
            color: var(--ovr-ocean);
        }

        .sidebar .nav-link .dot {
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background: currentColor;
            opacity: 0.6;
        }

        .content {
            flex: 1;
            padding: 24px 28px 40px;
        }

        .hero {
            background: linear-gradient(135deg, rgba(14, 107, 168, 0.1), rgba(27, 165, 184, 0.12));
            border-radius: 18px;
            padding: 24px;
            display: flex;
            flex-direction: column;
            gap: 6px;
            border: 1px solid rgba(14, 107, 168, 0.12);
        }

        .hero h1 {
            font-size: 24px;
            margin: 0;
        }

        .hero p {
            margin: 0;
            color: var(--ovr-muted);
        }

        .stat-card {
            border: none;
            border-radius: 16px;
            background: #ffffff;
            box-shadow: 0 10px 24px rgba(11, 27, 43, 0.08);
            height: 100%;
        }

        .stat-card .card-body {
            display: flex;
            flex-direction: column;
            gap: 6px;
        }

        .stat-label {
            font-size: 13px;
            color: var(--ovr-muted);
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .stat-value {
            font-size: 28px;
            font-weight: 700;
            color: var(--ovr-navy);
        }

        .stat-foot {
            color: var(--ovr-ocean);
            font-size: 13px;
            font-weight: 600;
        }

        .table-card {
            border-radius: 16px;
            border: none;
            box-shadow: 0 12px 28px rgba(11, 27, 43, 0.08);
        }

        .table thead th {
            font-size: 12px;
            text-transform: uppercase;
            letter-spacing: 1px;
            color: var(--ovr-muted);
        }

        .status-pill {
            padding: 4px 10px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: 600;
            display: inline-block;
        }

        .status-confirmed {
            background: rgba(27, 165, 184, 0.15);
            color: #0d6c7b;
        }

        .status-pending {
            background: rgba(246, 193, 79, 0.2);
            color: #9b6b00;
        }

        .status-checkedin {
            background: rgba(64, 201, 125, 0.18);
            color: #1c7a4b;
        }

        .alert {
            border-radius: 12px;
        }

        .search-card {
            border-radius: 16px;
            border: 1px solid #e3eaf2;
            background: #ffffff;
            padding: 16px;
        }

        @media (max-width: 991px) {
            .layout {
                flex-direction: column;
            }

            .sidebar {
                width: 100%;
                display: flex;
                flex-wrap: wrap;
                gap: 8px;
            }

            .sidebar .nav-link {
                flex: 1 1 calc(50% - 8px);
            }
        }

        @media (max-width: 575px) {
            .sidebar .nav-link {
                flex: 1 1 100%;
            }

            .content {
                padding: 18px 16px 28px;
            }
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
        <a class="navbar-brand text-white" href="<%= contextPath %>/admin/dashboard">Ocean View Resort</a>
        <button
            class="navbar-toggler text-white"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent"
            aria-expanded="false"
            aria-label="Toggle navigation"
        >
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
            <span class="navbar-text me-3">Logged in as <strong><%= loggedUser %></strong></span>
            <a class="nav-link" href="<%= contextPath %>/logout">Logout</a>
        </div>
    </div>
</nav>

<div class="layout">
    <aside class="sidebar">
        <div class="w-100">
            <div class="sidebar-title">Admin Menu</div>
            <nav class="nav flex-column">
                <a class="nav-link active" href="<%= contextPath %>/admin/dashboard"><span class="dot"></span>Dashboard</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/admin/reservation"><span class="dot"></span>Add New Reservation</a>
                <a class="nav-link" href="<%= contextPath %>/reservations"><span class="dot"></span>View Reservations</a>
                <a class="nav-link" href="#searchReservation"><span class="dot"></span>Search Reservation by Number</a>
                <a class="nav-link" href="<%= contextPath %>/bill"><span class="dot"></span>Calculate &amp; Print Bill</a>
                <a class="nav-link" href="<%= contextPath %>/users"><span class="dot"></span>Manage Staff Users</a>
                <a class="nav-link" href="<%= contextPath %>/logout"><span class="dot"></span>Exit / Logout</a>
            </nav>
        </div>
    </aside>

    <main class="content">
        <div class="hero mb-4">
            <h1>Welcome back, <%= loggedUser %>.</h1>
            <p>Here is a quick overview of Ocean View Resort operations today.</p>
        </div>

        <% if (successMsg != null) { %>
            <div class="alert alert-success" role="alert"><%= successMsg %></div>
        <% } %>
        <% if (errorMsg != null) { %>
            <div class="alert alert-danger" role="alert"><%= errorMsg %></div>
        <% } %>

        <div class="row g-4 mb-4">
            <div class="col-12 col-md-6 col-xl-3">
                <div class="card stat-card">
                    <div class="card-body">
                        <div class="stat-label">Total Reservations</div>
                        <div class="stat-value">248</div>
                        <div class="stat-foot">+12 this week</div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-6 col-xl-3">
                <div class="card stat-card">
                    <div class="card-body">
                        <div class="stat-label">Total Guests</div>
                        <div class="stat-value">612</div>
                        <div class="stat-foot">Occupancy 78%</div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-6 col-xl-3">
                <div class="card stat-card">
                    <div class="card-body">
                        <div class="stat-label">Available Rooms</div>
                        <div class="stat-value">19</div>
                        <div class="stat-foot">Suite + Deluxe</div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-6 col-xl-3">
                <div class="card stat-card">
                    <div class="card-body">
                        <div class="stat-label">Todays Check-ins</div>
                        <div class="stat-value">14</div>
                        <div class="stat-foot">Next arrival 2:30 PM</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row g-4">
            <div class="col-12 col-xl-8">
                <div class="card table-card">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5 class="mb-0">Recent Reservations</h5>
                            <a class="btn btn-sm btn-outline-primary" href="<%= contextPath %>/reservations">View all</a>
                        </div>
                        <div class="table-responsive">
                            <table class="table align-middle mb-0">
                                <thead>
                                <tr>
                                    <th>Reservation No</th>
                                    <th>Guest Name</th>
                                    <th>Room Type</th>
                                    <th>Check In</th>
                                    <th>Check Out</th>
                                    <th>Status</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>RSV-10234</td>
                                    <td>Lila Patterson</td>
                                    <td>Ocean Suite</td>
                                    <td>Feb 11, 2026</td>
                                    <td>Feb 14, 2026</td>
                                    <td><span class="status-pill status-checkedin">Checked In</span></td>
                                </tr>
                                <tr>
                                    <td>RSV-10221</td>
                                    <td>Marcela Ruiz</td>
                                    <td>Deluxe King</td>
                                    <td>Feb 12, 2026</td>
                                    <td>Feb 16, 2026</td>
                                    <td><span class="status-pill status-confirmed">Confirmed</span></td>
                                </tr>
                                <tr>
                                    <td>RSV-10198</td>
                                    <td>Oliver Chen</td>
                                    <td>Family Villa</td>
                                    <td>Feb 10, 2026</td>
                                    <td>Feb 13, 2026</td>
                                    <td><span class="status-pill status-checkedin">Checked In</span></td>
                                </tr>
                                <tr>
                                    <td>RSV-10177</td>
                                    <td>Priya Nair</td>
                                    <td>Garden View</td>
                                    <td>Feb 13, 2026</td>
                                    <td>Feb 15, 2026</td>
                                    <td><span class="status-pill status-pending">Pending</span></td>
                                </tr>
                                <tr>
                                    <td>RSV-10162</td>
                                    <td>Samuel Diaz</td>
                                    <td>Deluxe Queen</td>
                                    <td>Feb 11, 2026</td>
                                    <td>Feb 12, 2026</td>
                                    <td><span class="status-pill status-confirmed">Confirmed</span></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-12 col-xl-4">
                <div id="searchReservation" class="search-card mb-4">
                    <h5 class="mb-3">Search Reservation</h5>
                    <form class="d-flex flex-column gap-2" action="<%= contextPath %>/reservations" method="get">
                        <label for="reservationNo" class="form-label">Reservation Number</label>
                        <input
                            type="text"
                            class="form-control"
                            id="reservationNo"
                            name="reservationNo"
                            placeholder="e.g., RSV-10234"
                        />
                        <button class="btn btn-primary mt-2" type="submit">Search</button>
                    </form>
                </div>

                <div class="card table-card">
                    <div class="card-body">
                        <h5 class="mb-3">Quick Actions</h5>
                        <div class="d-grid gap-2">
                            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/reservation">Add New Reservation</a>
                            <a class="btn btn-outline-primary" href="<%= contextPath %>/bill">Calculate &amp; Print Bill</a>
                            <a class="btn btn-outline-primary" href="<%= contextPath %>/users">Manage Staff Users</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>

