package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.model.Reservation;
import com.icbt.oceanview.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/reservations/search")
public class SearchReservationServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    Object userObj = session == null ? null : session.getAttribute("user");
    if (!(userObj instanceof User)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String reservationNo = request.getParameter("reservationNo");
    if (reservationNo == null || reservationNo.trim().isEmpty()) {
      request.getRequestDispatcher("/WEB-INF/views/search-reservation.jsp").forward(request, response);
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    Reservation reservation = reservationDAO.findByReservationNo(reservationNo.trim());
    if (reservation != null) {
      request.setAttribute("reservation", reservation);
    } else {
      request.setAttribute("notFound", true);
    }

    request.getRequestDispatcher("/WEB-INF/views/search-reservation.jsp").forward(request, response);
  }
}
