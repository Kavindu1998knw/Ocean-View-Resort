package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.model.Reservation;
import com.icbt.oceanview.util.RoomTypes;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reservations/edit")
public class EditReservationServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String role = AuthHelper.requireRole(request, response, true, true);
    if (role == null) {
      return;
    }

    String idParam = request.getParameter("id");
    if (idParam == null || idParam.trim().isEmpty()) {
      response.sendRedirect(request.getContextPath() + "/reservations?error=Invalid+reservation+ID.");
      return;
    }

    int id;
    try {
      id = Integer.parseInt(idParam);
    } catch (NumberFormatException e) {
      response.sendRedirect(request.getContextPath() + "/reservations?error=Invalid+reservation+ID.");
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    Reservation reservation = reservationDAO.findById(id);
    if (reservation == null) {
      response.sendRedirect(request.getContextPath() + "/reservations?error=Reservation+not+found.");
      return;
    }

    Map<String, String> oldValues = new LinkedHashMap<>();
    oldValues.put("id", String.valueOf(reservation.getId()));
    oldValues.put("reservationNo", reservation.getReservationNo());
    oldValues.put("reservationNumber", reservation.getReservationNo());
    oldValues.put("guestFullName", reservation.getGuestFullName());
    oldValues.put("guestEmail", reservation.getGuestEmail());
    oldValues.put("contactNumber", reservation.getContactNumber());
    oldValues.put("roomType", reservation.getRoomType());
    oldValues.put("roomId", reservation.getRoomId() == null ? "" : String.valueOf(reservation.getRoomId()));
    oldValues.put("numberOfGuests", String.valueOf(reservation.getNumberOfGuests()));
    oldValues.put("checkInDate", reservation.getCheckInDate() == null ? "" : reservation.getCheckInDate().toString());
    oldValues.put("checkOutDate", reservation.getCheckOutDate() == null ? "" : reservation.getCheckOutDate().toString());
    oldValues.put("specialRequests", reservation.getSpecialRequests() == null ? "" : reservation.getSpecialRequests());
    oldValues.put("status", reservation.getStatus());

    request.setAttribute("oldValues", oldValues);
    request.setAttribute("roomTypes", RoomTypes.getRoomTypes());
    if (AuthHelper.ROLE_ADMIN.equals(role)) {
      request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
      return;
    }

    request.getRequestDispatcher("/WEB-INF/views/staff-edit-reservation.jsp").forward(request, response);
  }
}
