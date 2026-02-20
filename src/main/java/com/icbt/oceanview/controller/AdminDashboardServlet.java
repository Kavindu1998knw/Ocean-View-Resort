package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.dao.RoomManagementDAO;
import com.icbt.oceanview.model.Reservation;
import com.icbt.oceanview.model.User;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    Object userObj = session == null ? null : session.getAttribute("user");
    if (!(userObj instanceof User)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    RoomManagementDAO roomDAO = new RoomManagementDAO();
    LocalDate today = LocalDate.now();
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
  }
}
