package com.icbt.oceanview.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RoomInfo {
  private int id;
  private String roomNo;
  private String roomType;
  private boolean active;
  private BigDecimal pricePerNight;
  private LocalDateTime createdAt;
  private LocalDateTime priceUpdatedAt;

  public RoomInfo() {}

  public RoomInfo(
      int id,
      String roomNo,
      String roomType,
      boolean active,
      BigDecimal pricePerNight,
      LocalDateTime createdAt,
      LocalDateTime priceUpdatedAt) {
    this.id = id;
    this.roomNo = roomNo;
    this.roomType = roomType;
    this.active = active;
    this.pricePerNight = pricePerNight;
    this.createdAt = createdAt;
    this.priceUpdatedAt = priceUpdatedAt;
  }

  public RoomInfo(String roomNo, String roomType, boolean active, BigDecimal pricePerNight) {
    this.roomNo = roomNo;
    this.roomType = roomType;
    this.active = active;
    this.pricePerNight = pricePerNight;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getRoomNo() {
    return roomNo;
  }

  public void setRoomNo(String roomNo) {
    this.roomNo = roomNo;
  }

  public String getRoomType() {
    return roomType;
  }

  public void setRoomType(String roomType) {
    this.roomType = roomType;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public BigDecimal getPricePerNight() {
    return pricePerNight;
  }

  public void setPricePerNight(BigDecimal pricePerNight) {
    this.pricePerNight = pricePerNight;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getPriceUpdatedAt() {
    return priceUpdatedAt;
  }

  public void setPriceUpdatedAt(LocalDateTime priceUpdatedAt) {
    this.priceUpdatedAt = priceUpdatedAt;
  }
}
