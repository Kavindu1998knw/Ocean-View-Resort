package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.dao.RoomManagementDAO;
import com.icbt.oceanview.model.Reservation;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String role = AuthHelper.requireRole(request, response, true, true);
    if (role == null) {
      return;
    }

    HttpSession session = request.getSession(false);
    request.setAttribute("loggedUserName", AuthHelper.resolveDisplayName(session));

    ReservationDAO reservationDAO = new ReservationDAO();
    RoomManagementDAO roomDAO = new RoomManagementDAO();
    LocalDate today = LocalDate.now();

    if (AuthHelper.ROLE_ADMIN.equals(role)) {
      int totalReservations = reservationDAO.countAllReservations();
      int todayCheckIns = reservationDAO.countTodayCheckIns();
      int totalGuests = reservationDAO.countTotalGuests();
      int availableRooms = roomDAO.countAvailableRooms(today);
      List<Reservation> recentReservations = reservationDAO.findRecentReservations(5);

      String success = request.getParameter("success");
      String error = request.getParameter("error");

      request.setAttribute("success", success);
      request.setAttribute("error", error);
      request.setAttribute("totalReservations", totalReservations);
      request.setAttribute("todayCheckIns", todayCheckIns);
      request.setAttribute("totalGuests", totalGuests);
      request.setAttribute("availableRooms", availableRooms);
      request.setAttribute("recentReservations", recentReservations);

      request.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp").forward(request, response);
      return;
    }

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
