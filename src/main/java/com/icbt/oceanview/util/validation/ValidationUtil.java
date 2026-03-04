package com.icbt.oceanview.util.validation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.regex.Pattern;

public final class ValidationUtil {
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
  private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,20}$");

  private ValidationUtil() {}

  public static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }

  public static String trimToNull(String s) {
    if (s == null) {
      return null;
    }
    String trimmed = s.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  public static boolean isValidEmail(String email) {
    String value = trimToNull(email);
    return value != null && EMAIL_PATTERN.matcher(value).matches();
  }

  public static boolean isValidPhone10(String phone) {
    String value = trimToNull(phone);
    return value != null && value.matches("^\\d{10}$");
  }

  public static boolean isValidSriLankaMobile(String phone) {
    String value = trimToNull(phone);
    if (value == null) {
      return false;
    }

    String normalized = value.replaceAll("[\\s-]", "");
    if (normalized.startsWith("+94")) {
      normalized = "0" + normalized.substring(3);
    } else if (normalized.startsWith("94")) {
      normalized = "0" + normalized.substring(2);
    }

    return normalized.matches("^07\\d{8}$");
  }

  public static boolean isValidUsername(String u) {
    String value = trimToNull(u);
    return value != null && USERNAME_PATTERN.matcher(value).matches();
  }

  public static boolean isStrongPassword(String p) {
    return p != null && p.length() >= 8;
  }

  public static Integer parseIntSafe(String s) {
    String value = trimToNull(s);
    if (value == null) {
      return null;
    }
    try {
      return Integer.valueOf(value);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public static BigDecimal parseBigDecimalSafe(String s) {
    String value = trimToNull(s);
    if (value == null) {
      return null;
    }
    try {
      return new BigDecimal(value);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public static boolean isPositiveInt(Integer n) {
    return n != null && n > 0;
  }

  public static boolean isPositiveBigDecimal(BigDecimal x) {
    return x != null && x.compareTo(BigDecimal.ZERO) > 0;
  }

  public static LocalDate parseDateSafe(String s) {
    String value = trimToNull(s);
    if (value == null) {
      return null;
    }
    try {
      return LocalDate.parse(value);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  public static boolean isDateOrderValid(LocalDate in, LocalDate out) {
    return in != null && out != null && in.isBefore(out);
  }

  public static boolean isInAllowedSet(String value, Set<String> allowed) {
    String normalized = trimToNull(value);
    return normalized != null && allowed != null && allowed.contains(normalized);
  }
}
