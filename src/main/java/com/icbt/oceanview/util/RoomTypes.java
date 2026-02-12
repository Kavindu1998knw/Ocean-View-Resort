package com.icbt.oceanview.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class RoomTypes {
  public static final List<String> ROOM_TYPES =
      Collections.unmodifiableList(
          Arrays.asList(
              "Garden View",
              "Ocean Suite",
              "Deluxe King",
              "Deluxe Queen",
              "Family Villa"));

  private RoomTypes() {}

  public static List<String> getRoomTypes() {
    return ROOM_TYPES;
  }
}
