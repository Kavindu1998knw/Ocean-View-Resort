package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.RoomManagementDAO;
import com.icbt.oceanview.model.RoomInfo;
import com.icbt.oceanview.util.RoomTypes;
import com.icbt.oceanview.util.validation.RequestUtil;
import com.icbt.oceanview.util.validation.ValidationResult;
import com.icbt.oceanview.util.validation.ValidationUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/admin/rooms", "/api/room-price", "/api/rooms"})
public class RoomManagementServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String servletPath = request.getServletPath();
    if ("/api/room-price".equals(servletPath)) {
      if (AuthHelper.requireRole(request, response, true, true) == null) {
        return;
      }
      handlePriceApi(request, response);
      return;
    }
    if ("/api/rooms".equals(servletPath)) {
      if (AuthHelper.requireRole(request, response, true, true) == null) {
        return;
      }
      handleRoomsApi(request, response);
      return;
    }
    if (AuthHelper.requireRole(request, response, true, false) == null) {
      return;
    }

    RoomManagementDAO dao = new RoomManagementDAO();

    String toggleIdParam = ValidationUtil.trimToNull(request.getParameter("toggleId"));
    if (toggleIdParam != null) {
      Integer toggleId = ValidationUtil.parseIntSafe(toggleIdParam);
      if (!ValidationUtil.isPositiveInt(toggleId)) {
        response.sendRedirect(request.getContextPath() + "/admin/rooms?error=" + encode("Invalid room ID."));
        return;
      }
      boolean toggled = dao.toggleActive(toggleId);
      if (toggled) {
        response.sendRedirect(request.getContextPath() + "/admin/rooms?success=" + encode("Room status updated."));
      } else {
        response.sendRedirect(request.getContextPath() + "/admin/rooms?error=" + encode("Failed to update room status."));
      }
      return;
    }

    String deleteIdParam = ValidationUtil.trimToNull(request.getParameter("deleteId"));
    if (deleteIdParam != null) {
      Integer deleteId = ValidationUtil.parseIntSafe(deleteIdParam);
      if (!ValidationUtil.isPositiveInt(deleteId)) {
        response.sendRedirect(request.getContextPath() + "/admin/rooms?error=" + encode("Invalid room ID."));
        return;
      }
      boolean deleted = dao.deleteById(deleteId);
      if (deleted) {
        response.sendRedirect(request.getContextPath() + "/admin/rooms?success=" + encode("Room deleted successfully."));
      } else {
        response.sendRedirect(request.getContextPath() + "/admin/rooms?error=" + encode("Failed to delete room."));
      }
      return;
    }

    String editIdParam = ValidationUtil.trimToNull(request.getParameter("editId"));
    if (editIdParam != null) {
      Integer editId = ValidationUtil.parseIntSafe(editIdParam);
      if (ValidationUtil.isPositiveInt(editId)) {
        RoomInfo editRoom = dao.findById(editId);
        if (editRoom != null) {
          request.setAttribute("oldValues", toOldValues(editRoom));
        } else {
          Map<String, String> errors = new LinkedHashMap<>();
          errors.put("global", "Room not found.");
          request.setAttribute("errors", errors);
        }
      } else {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("global", "Invalid room ID.");
        request.setAttribute("errors", errors);
      }
    }

    request.setAttribute("rooms", dao.findAllRoomsWithPrices());
    request.setAttribute("roomTypes", RoomTypes.getRoomTypes());
    request.getRequestDispatcher("/WEB-INF/views/manage-rooms.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    String servletPath = request.getServletPath();
    if (!"/admin/rooms".equals(servletPath)) {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      return;
    }
    if (AuthHelper.requireRole(request, response, true, false) == null) {
      return;
    }

    ValidationResult vr = new ValidationResult();
    RequestUtil.collectOldValues(request, vr, "id", "roomNo", "roomType", "pricePerNight");
    boolean active = request.getParameter("active") != null;
    vr.getOldValues().put("active", String.valueOf(active));

    String idParam = ValidationUtil.trimToNull(request.getParameter("id"));
    String roomNo = ValidationUtil.trimToNull(request.getParameter("roomNo"));
    String roomType = ValidationUtil.trimToNull(request.getParameter("roomType"));
    BigDecimal price = ValidationUtil.parseBigDecimalSafe(request.getParameter("pricePerNight"));

    if (ValidationUtil.isBlank(roomNo)) {
      vr.addError("roomNo", "Room number is required.");
    }

    if (ValidationUtil.isBlank(roomType)) {
      vr.addError("roomType", "Room type is required.");
    } else if (!RoomTypes.getRoomTypes().contains(roomType)) {
      vr.addError("roomType", "Invalid room type selected.");
    }

    if (price == null) {
      vr.addError("pricePerNight", "Price must be a valid numeric value.");
    } else if (!ValidationUtil.isPositiveBigDecimal(price)) {
      vr.addError("pricePerNight", "Price must be greater than 0.");
    }

    RoomManagementDAO dao = new RoomManagementDAO();
    Integer id = null;
    boolean createMode = idParam == null;
    if (!createMode) {
      id = ValidationUtil.parseIntSafe(idParam);
      if (!ValidationUtil.isPositiveInt(id)) {
        vr.addError("id", "Invalid room ID.");
      }
    }

    if (vr.error("roomNo") == null) {
      if (createMode) {
        if (dao.roomNoExists(roomNo)) {
          vr.addError("roomNo", "Room number already exists.");
        }
      } else if (vr.error("id") == null && dao.roomNoExistsExcludingId(id, roomNo)) {
        vr.addError("roomNo", "Room number already exists.");
      }
    }

    if (vr.hasErrors()) {
      vr.addError("global", "Please fix the highlighted fields.");
      request.setAttribute("rooms", dao.findAllRoomsWithPrices());
      request.setAttribute("roomTypes", RoomTypes.getRoomTypes());
      RequestUtil.forwardWithErrors(request, response, "/WEB-INF/views/manage-rooms.jsp", vr);
      return;
    }

    boolean saved;
    if (createMode) {
      RoomInfo info = new RoomInfo(roomNo, roomType, active, price);
      saved = dao.createRoomWithPrice(info);
    } else {
      RoomInfo info = new RoomInfo(id, roomNo, roomType, active, price, null, null);
      saved = dao.updateRoomWithPrice(info);
    }

    if (saved) {
      response.sendRedirect(request.getContextPath() + "/admin/rooms?success=" + encode("Room saved successfully."));
      return;
    }

    vr.addError("global", "Failed to save room. Please try again.");
    request.setAttribute("rooms", dao.findAllRoomsWithPrices());
    request.setAttribute("roomTypes", RoomTypes.getRoomTypes());
    RequestUtil.forwardWithErrors(request, response, "/WEB-INF/views/manage-rooms.jsp", vr);
  }

  private void handlePriceApi(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");

    String roomType = ValidationUtil.trimToNull(request.getParameter("roomType"));
    if (roomType == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("{\"error\":\"ROOM_TYPE_REQUIRED\"}");
      return;
    }

    RoomManagementDAO dao = new RoomManagementDAO();
    BigDecimal price = dao.findPriceByRoomType(roomType);
    if (price == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("{\"error\":\"NOT_FOUND\"}");
      return;
    }

    String normalizedPrice = price.setScale(2, RoundingMode.HALF_UP).toPlainString();
    response
        .getWriter()
        .write(
            "{\"roomType\":\""
                + escapeJson(roomType)
                + "\",\"pricePerNight\":"
                + normalizedPrice
                + "}");
  }

  private void handleRoomsApi(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");

    String roomType = ValidationUtil.trimToNull(request.getParameter("roomType"));
    if (roomType == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("{\"error\":\"ROOM_TYPE_REQUIRED\"}");
      return;
    }

    RoomManagementDAO dao = new RoomManagementDAO();
    List<RoomInfo> rooms = dao.findActiveRoomsByType(roomType);
    if (rooms.isEmpty()) {
      response.getWriter().write("[]");
      return;
    }

    StringBuilder json = new StringBuilder();
    json.append('[');
    for (int i = 0; i < rooms.size(); i++) {
      RoomInfo room = rooms.get(i);
      if (i > 0) {
        json.append(',');
      }
      String price =
          room.getPricePerNight() == null
              ? "0.00"
              : room.getPricePerNight().setScale(2, RoundingMode.HALF_UP).toPlainString();
      json.append("{\"id\":")
          .append(room.getId())
          .append(",\"roomNo\":\"")
          .append(escapeJson(room.getRoomNo()))
          .append("\",\"pricePerNight\":")
          .append(price)
          .append('}');
    }
    json.append(']');
    response.getWriter().write(json.toString());
  }

  private Map<String, String> toOldValues(RoomInfo room) {
    Map<String, String> oldValues = new LinkedHashMap<>();
    oldValues.put("id", String.valueOf(room.getId()));
    oldValues.put("roomNo", room.getRoomNo());
    oldValues.put("roomType", room.getRoomType());
    oldValues.put(
        "pricePerNight", room.getPricePerNight() == null ? "" : room.getPricePerNight().toPlainString());
    oldValues.put("active", String.valueOf(room.isActive()));
    return oldValues;
  }

  private String encode(String value) {
    if (value == null) {
      return "";
    }
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
    } catch (java.io.UnsupportedEncodingException e) {
      return "";
    }
  }

  private String escapeJson(String value) {
    return value.replace("\\", "\\\\").replace("\"", "\\\"");
  }
}
