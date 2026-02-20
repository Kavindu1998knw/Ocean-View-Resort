<aside class="admin-sidebar">
    <div class="sidebar-inner">
        <div class="sidebar-title">Admin Menu</div>
        <nav class="nav flex-column sidebar-nav">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">
                <span class="dot"></span>Dashboard
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/reservation">
                <span class="dot"></span>Add New Reservation
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/reservations">
                <span class="dot"></span>View Reservations
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/bill">
                <span class="dot"></span>Calculate &amp; Print Bill
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/rooms">
                <span class="dot"></span>Manage Rooms
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/staff-users">
                <span class="dot"></span>Manage Staff Users
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/logout" onclick="return confirm('Are you sure you want to logout?');">
                <span class="dot"></span>Exit / Logout
            </a>
        </nav>
    </div>
</aside>
