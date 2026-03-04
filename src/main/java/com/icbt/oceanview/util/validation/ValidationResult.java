package com.icbt.oceanview.util.validation;

import java.util.LinkedHashMap;
import java.util.Map;

public class ValidationResult {
  private final Map<String, String> errors = new LinkedHashMap<>();
  private final Map<String, String> oldValues = new LinkedHashMap<>();

  public void addError(String field, String msg) {
    if (field == null || field.trim().isEmpty() || msg == null) {
      return;
    }
    errors.put(field, msg);
  }

  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  public String error(String field) {
    return errors.get(field);
  }

  public Map<String, String> getErrors() {
    return errors;
  }

  public Map<String, String> getOldValues() {
    return oldValues;
  }
}
