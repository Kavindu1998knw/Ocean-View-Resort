<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Staff Dashboard | Ocean View Resort</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css" />
</head>
<body class="admin-body">
<nav class="ov-navbar">
    <div class="ov-brand">
        <img class="ov-logo" src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Ocean View Resort"/>
        <div class="ov-brand-text">
            <div class="ov-title">Ocean View Resort</div>
            <div class="ov-subtitle">Staff Dashboard</div>
        </div>
    </div>

    <div class="user-info">
        <span>
            Logged in as
            <strong>${loggedUserName}</strong>
        </span>
        <a href="${pageContext.request.contextPath}/logout"
           class="logout-btn"
           onclick="return confirm('Are you sure you want to logout?');">
            Logout
        </a>
    </div>
</nav>

<div class="admin-shell">
    <main class="admin-content">
        <div class="content-inner">
            <div class="hero mb-4">
                <h1>Staff Dashboard</h1>
                <p>Welcome to your workspace.</p>
            </div>
        </div>
    </main>
</div>
</body>
</html>
