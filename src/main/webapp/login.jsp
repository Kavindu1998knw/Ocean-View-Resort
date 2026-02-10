<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String registered = request.getParameter("registered");
  boolean showRegistered = "1".equals(registered);
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login</title>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"
      crossorigin="anonymous"
      referrerpolicy="no-referrer"
    />
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
        --success: #15803d;
        --success-bg: #dcfce7;
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

      .login-card {
        width: 100%;
        max-width: 420px;
        background: var(--card);
        border-radius: var(--radius);
        box-shadow: var(--shadow);
        padding: 32px 28px;
      }

      .header {
        text-align: center;
        margin-bottom: 24px;
      }

      .header h1 {
        margin: 0 0 8px 0;
        font-size: 24px;
        font-weight: 700;
      }

      .header p {
        margin: 0;
        color: var(--muted);
        font-size: 14px;
      }

      form {
        display: grid;
        gap: 16px;
      }

      label {
        display: block;
        font-size: 14px;
        margin-bottom: 6px;
        color: var(--text);
        font-weight: 600;
      }

      .input {
        width: 100%;
        padding: 12px 14px;
        border: 1px solid var(--border);
        border-radius: 10px;
        font-size: 14px;
        transition: border-color 0.2s, box-shadow 0.2s;
      }

      .input:focus {
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
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 13px;
      }

      .actions a {
        color: var(--primary);
        text-decoration: none;
      }

      .actions a:hover {
        text-decoration: underline;
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

      .footer {
        text-align: center;
        font-size: 13px;
        color: var(--muted);
        margin-top: 12px;
      }

      .footer a {
        color: var(--primary);
        text-decoration: none;
      }

      .footer a:hover {
        text-decoration: underline;
      }

      .notice {
        background: var(--success-bg);
        color: var(--success);
        border: 1px solid #bbf7d0;
        padding: 10px 12px;
        border-radius: 10px;
        font-size: 13px;
      }

      @media (max-width: 420px) {
        .login-card {
          padding: 26px 20px;
        }
      }
    </style>
  </head>
  <body>
    <main class="login-card">
      <div class="header">
        <h1>Welcome Back</h1>
        <p>Sign in to continue</p>
      </div>

      <form action="#" method="post">
        <%
          if (showRegistered) {
        %>
        <div class="notice">Account created successfully. Please sign in.</div>
        <%
          }
        %>
        <div>
          <label for="email">Email</label>
          <input
            class="input"
            id="email"
            name="email"
            type="email"
            placeholder="you@example.com"
            required
          />
        </div>

        <div>
          <label for="password">Password</label>
          <div class="password-wrap">
            <input
              class="input"
              id="password"
              name="password"
              type="password"
              placeholder="Enter your password"
              required
              autocomplete="current-password"
            />
            <button
              type="button"
              class="toggle-visibility"
              aria-label="Show password"
              aria-pressed="false"
              title="Show password"
              id="togglePassword"
            >
              <i class="fa-solid fa-eye" aria-hidden="true"></i>
            </button>
          </div>
        </div>

        <div class="actions">
          <a href="#">Forgot password?</a>
        </div>

        <button class="btn" type="submit">Login</button>
      </form>

      <div class="footer">
        New here?
        <a href="<%= request.getContextPath() %>/register.jsp">Create an account</a>
      </div>
    </main>

    <script>
      const toggleButton = document.getElementById("togglePassword");
      const passwordInput = document.getElementById("password");
      const icon = toggleButton.querySelector("i");

      toggleButton.addEventListener("click", () => {
        const isHidden = passwordInput.type === "password";
        passwordInput.type = isHidden ? "text" : "password";
        toggleButton.setAttribute("aria-pressed", String(isHidden));
        toggleButton.setAttribute(
          "aria-label",
          isHidden ? "Hide password" : "Show password"
        );
        toggleButton.setAttribute(
          "title",
          isHidden ? "Hide password" : "Show password"
        );
        icon.classList.toggle("fa-eye", !isHidden);
        icon.classList.toggle("fa-eye-slash", isHidden);
      });
    </script>
  </body>
</html>
