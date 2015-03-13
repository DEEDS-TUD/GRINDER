package de.grinder;

import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestClient {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(RestClient.class);

  private static final String URL = "http://localhost:8182";

  private static RestClient instance = null;

  private ClientResource resource = new ClientResource(URL);

  private RestClient() {
    super();
    resource = new ClientResource(URL);
    LOGGER.debug("Reference: {}", resource.getRequest().getResourceRef()
        .getBaseRef());
  }

  public static RestClient instance() {
    if (instance == null) {
      instance = new RestClient();
    }
    return instance;
  }

  public ClientResource getResource() {
    return new ClientResource(resource);
  }
}
