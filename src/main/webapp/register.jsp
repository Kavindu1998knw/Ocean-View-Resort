<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,java.util.Map" %>
<%!
  private String esc(String value) {
    if (value == null) {
      return "";
    }
    return value
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;");
  }
%>
<%
  List<String> errors = (List<String>) request.getAttribute("errors");
  Map<String, String> fieldErrors = (Map<String, String>) request.getAttribute("fieldErrors");
  if (fieldErrors == null) {
    fieldErrors = new java.util.HashMap<>();
  }
  String fullNameVal = (String) request.getAttribute("fullName");
  String usernameVal = (String) request.getAttribute("username");
  String emailVal = (String) request.getAttribute("email");
  String contactVal = (String) request.getAttribute("contact");
%>
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
        --error: #dc2626;
        --error-bg: #fee2e2;
        --error-border: #fecaca;
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

      .alert {
        padding: 12px 14px;
        border-radius: 10px;
        font-size: 13px;
        border: 1px solid transparent;
      }

      .alert-danger {
        background: var(--error-bg);
        color: var(--error);
        border-color: var(--error-border);
      }

      #errorBox {
        display: none;
      }

      .input.is-invalid,
      select.is-invalid {
        border-color: var(--error);
        box-shadow: 0 0 0 4px rgba(220, 38, 38, 0.12);
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

      <form id="registerForm" action="register" method="post" novalidate>
        <%
          if (errors != null && !errors.isEmpty()) {
        %>
        <div class="alert alert-danger" role="alert">
          <%= esc(errors.get(0)) %>
        </div>
        <%
          }
        %>
        <div class="row">
          <div>
            <label for="fullName">Full Name</label>
            <input
              class="input <%= fieldErrors.containsKey("fullName") ? "is-invalid" : "" %>"
              id="fullName"
              name="fullName"
              type="text"
              placeholder="Jane Doe"
              value="<%= esc(fullNameVal) %>"
              required
            />
          </div>
          <div>
            <label for="username">Username</label>
            <input
              class="input <%= fieldErrors.containsKey("username") ? "is-invalid" : "" %>"
              id="username"
              name="username"
              type="text"
              placeholder="jane.doe"
              value="<%= esc(usernameVal) %>"
              required
            />
          </div>
        </div>

        <div class="row">
          <div>
            <label for="email">Email</label>
            <input
              class="input <%= fieldErrors.containsKey("email") ? "is-invalid" : "" %>"
              id="email"
              name="email"
              type="email"
              placeholder="you@oceanview.com"
              value="<%= esc(emailVal) %>"
              required
            />
          </div>
          <div>
            <label for="contact">Contact Number</label>
            <input
              class="input <%= fieldErrors.containsKey("contact") ? "is-invalid" : "" %>"
              id="contact"
              name="contact"
              type="tel"
              placeholder="0712345678"
              value="<%= esc(contactVal) %>"
              required
            />
          </div>
        </div>

        <div class="row">
          <div>
            <label for="password">Password</label>
            <div class="password-wrap">
              <input
                class="input <%= fieldErrors.containsKey("password") ? "is-invalid" : "" %>"
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
          </div>
          <div>
            <label for="confirmPassword">Confirm Password</label>
            <div class="password-wrap">
              <input
                class="input <%= fieldErrors.containsKey("confirmPassword") ? "is-invalid" : "" %>"
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
          </div>
        </div>

        <input type="hidden" name="role" value="STAFF" />
        <p class="subtitle" style="margin-top: -6px;">Accounts are created as Staff.</p>

        <div class="actions">
          <div id="errorBox" class="alert alert-danger" role="alert"></div>
          <button class="btn" type="submit">Create Account</button>
          <div class="links">
            <a href="login.jsp">Already have an account? Login</a>
          </div>
        </div>
      </form>

      <div class="footer">
        Create staff accounts for the Ocean View Resort system.
      </div>
    </main>

    <script>
      const form = document.getElementById("registerForm");
      const errorBox = document.getElementById("errorBox");

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

      form.addEventListener("submit", (e) => {
        errorBox.style.display = "none";
        errorBox.textContent = "";

        const inputs = form.querySelectorAll(".input, select");
        inputs.forEach((input) => input.classList.remove("is-invalid"));

        const fullName = document.getElementById("fullName").value.trim();
        const email = document.getElementById("email").value.trim();
        const username = document.getElementById("username").value.trim();
        const contact = document.getElementById("contact").value.trim();
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("confirmPassword").value;

        const errors = [];

        if (!fullName) {
          errors.push("Full name is required.");
          document.getElementById("fullName").classList.add("is-invalid");
        }
        if (!email) {
          errors.push("Email is required.");
          document.getElementById("email").classList.add("is-invalid");
        }
        if (!username) {
          errors.push("Username is required.");
          document.getElementById("username").classList.add("is-invalid");
        }
        if (!contact) {
          errors.push("Contact number is required.");
          document.getElementById("contact").classList.add("is-invalid");
        }
        if (!password) {
          errors.push("Password is required.");
          document.getElementById("password").classList.add("is-invalid");
        }
        if (!confirmPassword) {
          errors.push("Confirm password is required.");
          document.getElementById("confirmPassword").classList.add("is-invalid");
        }

        if (contact && !/^\d{9,12}$/.test(contact)) {
          errors.push("Contact number must be 9 to 12 digits.");
          document.getElementById("contact").classList.add("is-invalid");
        }

        if (password && password.length < 8) {
          errors.push("Password must be at least 8 characters.");
          document.getElementById("password").classList.add("is-invalid");
        }

        if (password && confirmPassword && password !== confirmPassword) {
          errors.push("Passwords do not match.");
          document
            .getElementById("confirmPassword")
            .classList.add("is-invalid");
        }

        if (errors.length > 0) {
          e.preventDefault();
          errorBox.textContent = errors[0];
          errorBox.style.display = "block";
        }
      });
    </script>
  </body>
</html>
