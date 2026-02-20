<aside class="admin-sidebar">
    <div class="sidebar-inner">
        <div class="sidebar-title">Staff Panel</div>
        <nav class="nav flex-column sidebar-nav">
            <a class="nav-link active" href="${pageContext.request.contextPath}/staff/dashboard">
                <span class="dot"></span>Dashboard
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/staff/reservation/add">
                <span class="dot"></span>Add New Reservation
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/staff/reservations">
                <span class="dot"></span>View Reservations
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/staff/bill">
                <span class="dot"></span>Calculate &amp; Print Bill
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/logout" onclick="return confirm('Are you sure you want to logout?');">
                <span class="dot"></span>Exit / Logout
            </a>
        </nav>
    </div>
</aside>
