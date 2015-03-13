package de.grinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SettingsTest {

  @Test
  public void testGetInstance() {
    final Settings settings = Settings.getInstance();
    final Settings settings1 = Settings.getInstance();

    assertNotNull("Settings is not null", settings);
    assertTrue("Only one instance of Settings", settings == settings1);
  }

  @Test
  public void testGetSetting() {
    final Settings settings = Settings.getInstance();
    final String defaultConfigPath = settings.getSetting("defaultConfigPath");

    assertEquals("Correct defaultConfigPath loaded", ".", defaultConfigPath);
  }

  @Test
  public void testGetSetting_missingSetting() {
    final Settings settings = Settings.getInstance();
    final String missingSetting = settings.getSetting("missingSetting");

    assertNull("Missing setting is null", missingSetting);
  }

}
