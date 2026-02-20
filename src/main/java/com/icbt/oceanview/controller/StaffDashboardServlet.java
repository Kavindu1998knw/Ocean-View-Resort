package com.icbt.oceanview.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.dao.RoomManagementDAO;
import com.icbt.oceanview.model.Reservation;

@WebServlet("/staff/dashboard")
public class StaffDashboardServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    Object roleObj = session.getAttribute("authRole");
    if (roleObj == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String role = String.valueOf(roleObj);
    if ("ADMIN".equalsIgnoreCase(role)) {
      response.sendRedirect(request.getContextPath() + "/admin/dashboard");
      return;
    }
    if (!"STAFF".equalsIgnoreCase(role)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String authName = String.valueOf(session.getAttribute("authName"));
    request.setAttribute("loggedUserName", authName);

    ReservationDAO reservationDAO = new ReservationDAO();
    RoomManagementDAO roomDAO = new RoomManagementDAO();
    LocalDate today = LocalDate.now();
    int totalReservations = reservationDAO.countAllReservations();
    int upcomingCheckIns = reservationDAO.countUpcomingCheckIns(today);
    int upcomingCheckOuts = reservationDAO.countUpcomingCheckOuts(today);
    int availableRooms = roomDAO.countAvailableRooms(today);
    List<Reservation> recentReservations = reservationDAO.findRecentReservationsWithRoomNo(5);

    request.setAttribute("totalReservations", totalReservations);
    request.setAttribute("upcomingCheckIns", upcomingCheckIns);
    request.setAttribute("upcomingCheckOuts", upcomingCheckOuts);
    request.setAttribute("availableRooms", availableRooms);
    request.setAttribute("recentReservations", recentReservations);

    request.getRequestDispatcher("/WEB-INF/views/staff-dashboard.jsp").forward(request, response);
  }
}
