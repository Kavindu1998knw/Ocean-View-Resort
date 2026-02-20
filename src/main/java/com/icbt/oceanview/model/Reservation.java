package com.icbt.oceanview.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {
  private int id;
  private String reservationNo;
  private String guestFullName;
  private String guestEmail;
  private String contactNumber;
  private String roomType;
  private Integer roomId;
  private String roomNo;
  private int numberOfGuests;
  private LocalDate checkInDate;
  private LocalDate checkOutDate;
  private String specialRequests;
  private String status;
  private LocalDateTime createdAt;

  public Reservation() {}

  public Reservation(
      String reservationNo,
      String guestFullName,
      String guestEmail,
      String contactNumber,
      String roomType,
      Integer roomId,
      int numberOfGuests,
      LocalDate checkInDate,
      LocalDate checkOutDate,
      String specialRequests,
      String status,
      LocalDateTime createdAt) {
    this.reservationNo = reservationNo;
    this.guestFullName = guestFullName;
    this.guestEmail = guestEmail;
    this.contactNumber = contactNumber;
    this.roomType = roomType;
    this.roomId = roomId;
    this.numberOfGuests = numberOfGuests;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
    this.specialRequests = specialRequests;
    this.status = status;
    this.createdAt = createdAt;
  }

  public Reservation(
      int id,
      String reservationNo,
      String guestFullName,
      String guestEmail,
      String contactNumber,
      String roomType,
      Integer roomId,
      int numberOfGuests,
      LocalDate checkInDate,
      LocalDate checkOutDate,
      String specialRequests,
      String status,
      LocalDateTime createdAt) {
    this.id = id;
    this.reservationNo = reservationNo;
    this.guestFullName = guestFullName;
    this.guestEmail = guestEmail;
    this.contactNumber = contactNumber;
    this.roomType = roomType;
    this.roomId = roomId;
    this.numberOfGuests = numberOfGuests;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
    this.specialRequests = specialRequests;
    this.status = status;
    this.createdAt = createdAt;
  }

  public static Reservation newForCreate(
      String reservationNo,
      String guestFullName,
      String guestEmail,
      String contactNumber,
      String roomType,
      Integer roomId,
      int numberOfGuests,
      LocalDate checkInDate,
      LocalDate checkOutDate,
      String specialRequests,
      String status,
      LocalDateTime createdAt) {
    String effectiveStatus = (status == null || status.trim().isEmpty()) ? "PENDING" : status;
    LocalDateTime effectiveCreatedAt = (createdAt == null) ? LocalDateTime.now() : createdAt;

    return new Reservation(
        reservationNo,
        guestFullName,
        guestEmail,
        contactNumber,
        roomType,
        roomId,
        numberOfGuests,
        checkInDate,
        checkOutDate,
        specialRequests,
        effectiveStatus,
        effectiveCreatedAt);
  }

  public static Reservation newForCreate(
      String reservationNo,
      String guestFullName,
      String guestEmail,
      String contactNumber,
      String roomType,
      int numberOfGuests,
      LocalDate checkInDate,
      LocalDate checkOutDate,
      String specialRequests,
      String status,
      LocalDateTime createdAt) {
    return newForCreate(
        reservationNo,
        guestFullName,
        guestEmail,
        contactNumber,
        roomType,
        null,
        numberOfGuests,
        checkInDate,
        checkOutDate,
        specialRequests,
        status,
        createdAt);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getReservationNo() {
    return reservationNo;
  }

  public void setReservationNo(String reservationNo) {
    this.reservationNo = reservationNo;
  }

  public String getGuestFullName() {
    return guestFullName;
  }

  public void setGuestFullName(String guestFullName) {
    this.guestFullName = guestFullName;
  }

  public String getGuestEmail() {
    return guestEmail;
  }

  public void setGuestEmail(String guestEmail) {
    this.guestEmail = guestEmail;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public String getRoomType() {
    return roomType;
  }

  public void setRoomType(String roomType) {
    this.roomType = roomType;
  }

  public Integer getRoomId() {
    return roomId;
  }

  public void setRoomId(Integer roomId) {
    this.roomId = roomId;
  }

  public String getRoomNo() {
    return roomNo;
  }

  public void setRoomNo(String roomNo) {
    this.roomNo = roomNo;
  }

  public int getNumberOfGuests() {
    return numberOfGuests;
  }

  public void setNumberOfGuests(int numberOfGuests) {
    this.numberOfGuests = numberOfGuests;
  }

  public LocalDate getCheckInDate() {
    return checkInDate;
  }

  public void setCheckInDate(LocalDate checkInDate) {
    this.checkInDate = checkInDate;
  }

  public LocalDate getCheckOutDate() {
    return checkOutDate;
  }

  public void setCheckOutDate(LocalDate checkOutDate) {
    this.checkOutDate = checkOutDate;
  }

  public String getSpecialRequests() {
    return specialRequests;
  }

  public void setSpecialRequests(String specialRequests) {
    this.specialRequests = specialRequests;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "Reservation{"
        + "id="
        + id
        + ", reservationNo='"
        + reservationNo
        + '\''
        + ", guestFullName='"
        + guestFullName
        + '\''
        + ", guestEmail='"
        + guestEmail
        + '\''
        + ", contactNumber='"
        + contactNumber
        + '\''
        + ", roomType='"
        + roomType
        + '\''
        + ", roomId="
        + roomId
        + ", roomNo='"
        + roomNo
        + '\''
        + ", numberOfGuests="
        + numberOfGuests
        + ", checkInDate="
        + checkInDate
        + ", checkOutDate="
        + checkOutDate
        + ", specialRequests='"
        + specialRequests
        + '\''
        + ", status='"
        + status
        + '\''
        + ", createdAt="
        + createdAt
        + '}';
  }
}
