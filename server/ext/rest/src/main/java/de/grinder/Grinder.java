package de.grinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

import de.grinder.android_fi.EmulatedAndroid;
import de.grinder.util.cue.CUEAbstractionRegistry;

/**
 * The GRINDER server application.
 * 
 * GRINDER provides an REST API at port 8182. This class implements the HTTP
 * server and the routing to subsystems.
 * 
 * 
 */
public class Grinder extends Application {

  private final static Logger LOGGER = LoggerFactory.getLogger(Grinder.class);

  /**
   * The default port of GRINDER.
   */
  private final static int PORT = 8182;

  /**
   * Starts GRINDER as server listening on port 8182.
   * 
   * @param args
   *          not used
   */
  public static void main(final String[] args) {
    /*
     * TODO Implement port setup via command line arguments.
     */
    LOGGER.info("Starting GRINDER...");
    
    /*
     * Register cue abstraction.
     */
    CUEAbstractionRegistry registry = CUEAbstractionRegistry.getInstance();
    registry.register(EmulatedAndroid.INFO);

    try {
      Component component = new Component();
      component.getServers().add(Protocol.HTTP, PORT);
      component.getDefaultHost().attach(new Grinder());
      component.start();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  @Override
  public synchronized Restlet createInboundRoot() {
    Router router = new Router(getContext());
    router.attach("/targets", TargetsResource.class);
    router.attach("/targets/{target}", TargetResource.class);
    router.attach("/campaigns", CampaignsResource.class);
    router.attach("/campaigns/{id}", CampaignResource.class);
    return router;
  }
}
