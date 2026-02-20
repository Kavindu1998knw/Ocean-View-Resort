package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.dao.RoomManagementDAO;
import com.icbt.oceanview.model.Reservation;
import com.icbt.oceanview.model.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/bill")
public class BillServlet extends HttpServlet {
  private static final BigDecimal SERVICE_CHARGE_RATE = new BigDecimal("0.10");

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
    request.setAttribute("recentReservations", reservationDAO.findRecentReservationsWithRoomNo(20));

    String reservationNo = request.getParameter("reservationNo");
    if (reservationNo == null || reservationNo.trim().isEmpty()) {
      request.getRequestDispatcher("/WEB-INF/views/bill.jsp").forward(request, response);
      return;
    }

    Reservation reservation = reservationDAO.findByReservationNo(reservationNo.trim());
    if (reservation == null) {
      request.setAttribute("notFound", true);
      request.getRequestDispatcher("/WEB-INF/views/bill.jsp").forward(request, response);
      return;
    }

    LocalDate checkInDate = reservation.getCheckInDate();
    LocalDate checkOutDate = reservation.getCheckOutDate();
    if (checkInDate == null || checkOutDate == null) {
      request.setAttribute("error", "Reservation dates are invalid for billing.");
      request.getRequestDispatcher("/WEB-INF/views/bill.jsp").forward(request, response);
      return;
    }

    long dayDiff = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    int nights = (int) Math.max(1L, dayDiff);

    RoomManagementDAO roomManagementDAO = new RoomManagementDAO();
    BigDecimal rate = roomManagementDAO.findPriceByRoomType(reservation.getRoomType());
    if (rate == null) {
      request.setAttribute("error", "Room price not configured for this room type.");
      request.getRequestDispatcher("/WEB-INF/views/bill.jsp").forward(request, response);
      return;
    }

    BigDecimal subtotal = rate.multiply(BigDecimal.valueOf(nights)).setScale(2, RoundingMode.HALF_UP);
    BigDecimal serviceCharge =
        subtotal.multiply(SERVICE_CHARGE_RATE).setScale(2, RoundingMode.HALF_UP);
    BigDecimal total = subtotal.add(serviceCharge).setScale(2, RoundingMode.HALF_UP);

    request.setAttribute("reservation", reservation);
    request.setAttribute("nights", nights);
    request.setAttribute("rate", rate.setScale(2, RoundingMode.HALF_UP));
    request.setAttribute("subtotal", subtotal);
    request.setAttribute("serviceCharge", serviceCharge);
    request.setAttribute("total", total);
    request.getRequestDispatcher("/WEB-INF/views/bill.jsp").forward(request, response);
  }
}
