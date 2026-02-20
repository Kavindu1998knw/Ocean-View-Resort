package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.model.Reservation;
import com.icbt.oceanview.util.RoomTypes;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/staff/reservation/edit")
public class StaffEditReservationServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    if (!isStaff(request)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String idParam = request.getParameter("id");
    if (idParam == null || idParam.trim().isEmpty()) {
      response.sendRedirect(
          request.getContextPath() + "/staff/reservations?error=Invalid+reservation+ID.");
      return;
    }

    int id;
    try {
      id = Integer.parseInt(idParam);
    } catch (NumberFormatException e) {
      response.sendRedirect(
          request.getContextPath() + "/staff/reservations?error=Invalid+reservation+ID.");
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    Reservation reservation = reservationDAO.findById(id);
    if (reservation == null) {
      response.sendRedirect(
          request.getContextPath() + "/staff/reservations?error=Reservation+not+found.");
      return;
    }

    request.setAttribute("id", reservation.getId());
    request.setAttribute("reservationNo", reservation.getReservationNo());
    request.setAttribute("guestFullName", reservation.getGuestFullName());
    request.setAttribute("guestEmail", reservation.getGuestEmail());
    request.setAttribute("contactNumber", reservation.getContactNumber());
    request.setAttribute("roomType", reservation.getRoomType());
    request.setAttribute("roomId", reservation.getRoomId());
    request.setAttribute("numberOfGuests", reservation.getNumberOfGuests());
    request.setAttribute("checkInDate", reservation.getCheckInDate());
    request.setAttribute("checkOutDate", reservation.getCheckOutDate());
    request.setAttribute("specialRequests", reservation.getSpecialRequests());
    request.setAttribute("status", reservation.getStatus());
    request.setAttribute("roomTypes", RoomTypes.ROOM_TYPES);
    request.getRequestDispatcher("/WEB-INF/views/staff-edit-reservation.jsp").forward(request, response);
  }

  private boolean isStaff(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return false;
    }
    Object roleObj = session.getAttribute("authRole");
    if (roleObj == null) {
      return false;
    }
    String role = String.valueOf(roleObj);
    return "STAFF".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
  }
}
