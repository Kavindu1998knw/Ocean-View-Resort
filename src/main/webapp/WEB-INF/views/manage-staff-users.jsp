<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Staff Users</title>
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
            <h1 class="h3">Manage Staff Users</h1>
            <p>Create and manage staff access accounts.</p>
        </div>
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/dashboard">Back to Dashboard</a>
    </div>

    <c:if test="${not empty success}">
        <div class="alert alert-success" role="alert">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">${error}</div>
    </c:if>

    <div class="card card-soft mb-4">
        <div class="card-header bg-transparent border-0 pb-0">
            <h2 class="h5 mb-0">Create Staff User</h2>
        </div>
        <div class="card-body p-4">
            <form method="post" action="${pageContext.request.contextPath}/admin/staff-users">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label for="name" class="form-label">Full Name</label>
                        <input type="text" class="form-control" id="name" name="name" value="${formName}" required>
                    </div>
                    <div class="col-md-6">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" value="${formEmail}" required>
                    </div>
                    <div class="col-md-6">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" minlength="8" required>
                    </div>
                    <div class="col-md-3">
                        <label for="role" class="form-label">Role</label>
                        <select id="role" name="role" class="form-select" required>
                            <option value="ADMIN" ${formRole == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                            <option value="STAFF" ${formRole == 'STAFF' ? 'selected' : ''}>STAFF</option>
                        </select>
                    </div>
                    <div class="col-md-3 d-flex align-items-end">
                        <div class="form-check mb-2">
                            <input class="form-check-input" type="checkbox" id="active" name="active" ${formActive == null || formActive ? 'checked' : ''}>
                            <label class="form-check-label" for="active">Active</label>
                        </div>
                    </div>
                </div>
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary">Create User</button>
                </div>
            </form>
        </div>
    </div>

    <div class="card card-soft">
        <div class="card-header bg-transparent border-0 pb-0">
            <h2 class="h5 mb-0">Staff Users</h2>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-striped table-hover table-clean mb-0 align-middle">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Active</th>
                        <th>Created At</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty users}">
                            <tr>
                                <td colspan="7" class="text-center">No users found.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td>${u.id}</td>
                                    <td>${u.name}</td>
                                    <td>${u.email}</td>
                                    <td>${u.role}</td>
                                    <td>
                                        <span class="badge ${u.active ? 'bg-success' : 'bg-secondary'}">
                                            ${u.active ? 'Yes' : 'No'}
                                        </span>
                                    </td>
                                    <td>${u.createdAt}</td>
                                    <td class="d-flex gap-2">
                                        <a
                                            class="btn btn-sm btn-outline-primary btn-action"
                                            href="${pageContext.request.contextPath}/admin/staff-users?toggleId=${u.id}"
                                        >
                                            Toggle Active
                                        </a>
                                        <a
                                            class="btn btn-sm btn-outline-danger btn-action"
                                            href="${pageContext.request.contextPath}/admin/staff-users?deleteId=${u.id}"
                                            onclick="return confirm('Are you sure you want to delete this user?');"
                                        >
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
    </main>
</div>
</body>
</html>
