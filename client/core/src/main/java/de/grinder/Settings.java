package de.grinder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Settings {
  private static Settings instance;

  private final static Logger LOGGER = LoggerFactory.getLogger(Settings.class);

  private final Properties properties;

  public static synchronized Settings getInstance() {
    if (null == instance) {
      instance = new Settings();
    }

    return instance;
  }

  private Settings() {
    properties = new Properties();
    try (InputStream fis = this.getClass().getClassLoader()
        .getResourceAsStream("config.properties")) {
      properties.load(fis);
    } catch (final IOException e) {
      LOGGER.error("Cannot read config.properties file.");
    }
  }

  public String getSetting(final String name) {
    return (String) properties.get(name);
  }

}
