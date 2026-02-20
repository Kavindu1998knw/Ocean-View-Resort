<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<nav class="ov-navbar">
    <div class="ov-brand">
        <img class="ov-logo" src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Ocean View Resort"/>
        <div class="ov-brand-text">
            <div class="ov-title">Ocean View Resort</div>
            <div class="ov-subtitle">Admin Panel</div>
        </div>
    </div>

    <div class="user-info">
        <span>
            Logged in as 
            <strong>${not empty loggedUserName ? loggedUserName : sessionScope.authName}</strong>
        </span>

        <a href="${pageContext.request.contextPath}/logout"
           class="logout-btn"
           onclick="return confirm('Are you sure you want to logout?');">
            Logout
        </a>
    </div>
</nav>
