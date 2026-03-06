package com.icbt.oceanview.util.validation;

import junit.framework.TestCase;

public class ValidationUtilTest extends TestCase {

  public void testTrimToNull() {
    assertNull(ValidationUtil.trimToNull("   "));
    assertEquals("ocean", ValidationUtil.trimToNull("  ocean  "));
  }

  public void testIsValidSriLankaMobile() {
    assertTrue(ValidationUtil.isValidSriLankaMobile("0771234567"));
    assertTrue(ValidationUtil.isValidSriLankaMobile("+94 77-123-4567"));
    assertFalse(ValidationUtil.isValidSriLankaMobile("071234567"));
  }
}
