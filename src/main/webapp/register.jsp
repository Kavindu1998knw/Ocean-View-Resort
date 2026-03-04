<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Register - Ocean View Resort</title>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"
      crossorigin="anonymous"
      referrerpolicy="no-referrer"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/validation.css" />
    <style>
      :root {
        color-scheme: light;
        --bg: #f5f7fb;
        --card: #ffffff;
        --text: #1f2937;
        --muted: #6b7280;
        --primary: #2563eb;
        --primary-dark: #1d4ed8;
        --border: #e5e7eb;
        --shadow: 0 20px 40px rgba(31, 41, 55, 0.12);
        --radius: 14px;
      }

      * {
        box-sizing: border-box;
      }

      body {
        margin: 0;
        font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
        background: var(--bg);
        color: var(--text);
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 24px;
      }

      .register-card {
        width: 100%;
        max-width: 520px;
        background: var(--card);
        border-radius: var(--radius);
        box-shadow: var(--shadow);
        padding: 32px 28px;
      }

      .header {
        text-align: center;
        margin-bottom: 24px;
      }

      .brand {
        font-size: 22px;
        font-weight: 800;
        margin: 0 0 6px 0;
        letter-spacing: 0.3px;
      }

      .subtitle {
        margin: 0;
        color: var(--muted);
        font-size: 14px;
      }

      form {
        display: grid;
        gap: 16px;
      }

      .row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 16px;
      }

      @media (max-width: 640px) {
        .row {
          grid-template-columns: 1fr;
        }
      }

      label {
        display: block;
        font-size: 14px;
        margin-bottom: 6px;
        color: var(--text);
        font-weight: 600;
      }

      .input,
      select {
        width: 100%;
        padding: 12px 14px;
        border: 1px solid var(--border);
        border-radius: 10px;
        font-size: 14px;
        transition: border-color 0.2s, box-shadow 0.2s;
        background: #fff;
      }

      .input:focus,
      select:focus {
        outline: none;
        border-color: var(--primary);
        box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.12);
      }

      .password-wrap {
        position: relative;
      }

      .password-wrap .input {
        padding-right: 46px;
      }

      .toggle-visibility {
        position: absolute;
        right: 12px;
        top: 50%;
        transform: translateY(-50%);
        background: transparent;
        border: none;
        cursor: pointer;
        font-size: 16px;
        padding: 6px;
        color: var(--muted);
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .toggle-visibility:focus {
        outline: none;
        color: var(--primary);
      }

      .actions {
        display: grid;
        gap: 10px;
      }

      .btn {
        width: 100%;
        border: none;
        border-radius: 10px;
        padding: 12px 16px;
        background: var(--primary);
        color: #ffffff;
        font-weight: 600;
        font-size: 15px;
        cursor: pointer;
        transition: background 0.2s, transform 0.05s;
      }

      .btn:hover {
        background: var(--primary-dark);
      }

      .btn:active {
        transform: translateY(1px);
      }

      .links {
        display: flex;
        justify-content: space-between;
        font-size: 13px;
      }

      .links a {
        color: var(--primary);
        text-decoration: none;
      }

      .links a:hover {
        text-decoration: underline;
      }

      .footer {
        text-align: center;
        font-size: 13px;
        color: var(--muted);
        margin-top: 6px;
      }

      .footer a {
        color: var(--primary);
        text-decoration: none;
      }

      .footer a:hover {
        text-decoration: underline;
      }
    </style>
  </head>
  <body>
    <main class="register-card">
      <div class="header">
        <h1 class="brand">Ocean View Resort</h1>
        <p class="subtitle">Staff Registration</p>
      </div>

      <form id="registerForm" action="${pageContext.request.contextPath}/register" method="post" novalidate>
        <%@ include file="/WEB-INF/views/common/form-validation.jspf" %>

        <div class="row">
          <div>
            <label for="fullName">Full Name</label>
            <input
              class="input ${errors['fullName'] != null ? 'is-invalid' : ''}"
              id="fullName"
              name="fullName"
              type="text"
              placeholder="Jane Doe"
              value="${fn:escapeXml(oldValues['fullName'])}"
              required
            />
            <c:if test="${errors['fullName'] != null}">
              <div class="invalid-feedback">${errors['fullName']}</div>
            </c:if>
          </div>
          <div>
            <label for="username">Username</label>
            <input
              class="input ${errors['username'] != null ? 'is-invalid' : ''}"
              id="username"
              name="username"
              type="text"
              placeholder="jane_doe"
              value="${fn:escapeXml(oldValues['username'])}"
              required
            />
            <c:if test="${errors['username'] != null}">
              <div class="invalid-feedback">${errors['username']}</div>
            </c:if>
          </div>
        </div>

        <div class="row">
          <div>
            <label for="email">Email</label>
            <input
              class="input ${errors['email'] != null ? 'is-invalid' : ''}"
              id="email"
              name="email"
              type="email"
              placeholder="you@oceanview.com"
              value="${fn:escapeXml(oldValues['email'])}"
              required
            />
            <c:if test="${errors['email'] != null}">
              <div class="invalid-feedback">${errors['email']}</div>
            </c:if>
          </div>
          <div>
            <label for="contact">Contact Number</label>
            <input
              class="input ${errors['contact'] != null ? 'is-invalid' : ''}"
              id="contact"
              name="contact"
              type="tel"
              placeholder="0712345678"
              value="${fn:escapeXml(oldValues['contact'])}"
              required
            />
            <c:if test="${errors['contact'] != null}">
              <div class="invalid-feedback">${errors['contact']}</div>
            </c:if>
          </div>
        </div>

        <div class="row">
          <div>
            <label for="password">Password</label>
            <div class="password-wrap">
              <input
                class="input ${errors['password'] != null ? 'is-invalid' : ''}"
                id="password"
                name="password"
                type="password"
                placeholder="At least 8 characters"
                required
                autocomplete="new-password"
              />
              <button
                type="button"
                class="toggle-visibility"
                aria-label="Show password"
                aria-pressed="false"
                title="Show password"
                data-target="password"
              >
                <i class="fa-solid fa-eye" aria-hidden="true"></i>
              </button>
            </div>
            <c:if test="${errors['password'] != null}">
              <div class="invalid-feedback">${errors['password']}</div>
            </c:if>
          </div>
          <div>
            <label for="confirmPassword">Confirm Password</label>
            <div class="password-wrap">
              <input
                class="input ${errors['confirmPassword'] != null ? 'is-invalid' : ''}"
                id="confirmPassword"
                name="confirmPassword"
                type="password"
                placeholder="Re-enter password"
                required
                autocomplete="new-password"
              />
              <button
                type="button"
                class="toggle-visibility"
                aria-label="Show password"
                aria-pressed="false"
                title="Show password"
                data-target="confirmPassword"
              >
                <i class="fa-solid fa-eye" aria-hidden="true"></i>
              </button>
            </div>
            <c:if test="${errors['confirmPassword'] != null}">
              <div class="invalid-feedback">${errors['confirmPassword']}</div>
            </c:if>
          </div>
        </div>

        <input type="hidden" name="role" value="STAFF" />
        <p class="subtitle" style="margin-top: -6px;">Accounts are created as Staff.</p>

        <div class="actions">
          <button class="btn" type="submit">Create Account</button>
          <div class="links">
            <a href="${pageContext.request.contextPath}/login">Already have an account? Login</a>
          </div>
        </div>
      </form>

      <div class="footer">
        Create staff accounts for the Ocean View Resort system.
      </div>
    </main>

    <script>
      const passwordToggles = document.querySelectorAll(".toggle-visibility");
      passwordToggles.forEach((btn) => {
        btn.addEventListener("click", () => {
          const targetId = btn.getAttribute("data-target");
          const input = document.getElementById(targetId);
          const icon = btn.querySelector("i");
          const isHidden = input.type === "password";

          input.type = isHidden ? "text" : "password";
          btn.setAttribute("aria-pressed", String(isHidden));
          btn.setAttribute(
            "aria-label",
            isHidden ? "Hide password" : "Show password"
          );
          btn.setAttribute(
            "title",
            isHidden ? "Hide password" : "Show password"
          );

          icon.classList.toggle("fa-eye", !isHidden);
          icon.classList.toggle("fa-eye-slash", isHidden);
        });
      });
    </script>
  </body>
</html>
