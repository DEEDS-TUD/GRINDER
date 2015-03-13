package de.grinder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.grinder.database.Campaign;
import de.grinder.database.CampaignDao;
import de.grinder.database.TargetDao;
import de.grinder.database.TargetEntity;
import de.grinder.targetrunner.TargetController;
import de.grinder.targetrunner.TargetCreationException;
import de.grinder.targetrunner.TargetFactoryImpl;

public class TargetResource extends ServerResource {

  private final static Logger LOGGER = LoggerFactory
      .getLogger(TargetResource.class);

  /**
   * Data access object for accessing the database.
   */
  private final TargetDao dao;

  private TargetEntity target;

  private static Map<Integer, TargetController> controllers = new HashMap<>();

  int id;

  /**
   * Constructs a default TargetsResource.
   */
  public TargetResource() {
    this.dao = new TargetDao();
  }

  /**
   * Constructs a TargetsResource with the given data access object.
   * 
   * @param dao
   *          The data access object
   */
  public TargetResource(TargetDao dao) {
    this.dao = dao;
  }

  @Override
  public void doInit() {
    id = Integer.parseInt((String) getRequestAttributes().get("target"));
  }

  private TargetController getController(int id) {
    if (!controllers.containsKey(id)) {
      target = dao.getById(id);
      try (Reader description = new StringReader(target.getConfiguration())) {
        controllers.put(id,
            new TargetFactoryImpl().createTargetController(description));
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (TargetCreationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return controllers.get(id);
  }

  @Get
  public Representation toXml() {
    try {
      DomRepresentation representation = new DomRepresentation(
          MediaType.TEXT_XML);
      Document d = representation.getDocument();

      Element eTarget = d.createElement("target");
      d.appendChild(eTarget);

      Element eId = d.createElement("id");
      eId.appendChild(d.createTextNode("" + target.getId()));
      eTarget.appendChild(eId);

      Element eName = d.createElement("name");
      eName.appendChild(d.createTextNode(target.getName()));
      eTarget.appendChild(eName);

      d.normalizeDocument();
      return representation;

    } catch (IOException e) {
      // TODO Auto generated catch block
      e.printStackTrace();
    }

    // TODO Return error
    return null;
  }

  @Post("xml")
  public TargetEntity post(long campaignId) {
    /*
     * TODO Quick and dirty solution.
     */
    TargetController controller = getController(id);
    Campaign campaign = new CampaignDao().getById(campaignId);
    controller.setCampaign(campaign);
    return null;
  }

  @Put
  public void perform(Representation representation) {
    String action = "";
    try {
      action = representation.getText();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    TargetController controller = getController(id);

    switch (action) {
    case "start":
      LOGGER.info(String.format("Action: %s\n", action));
      controller.start();
      break;
    case "stop":
      LOGGER.info(String.format("Action: %s\n", action));
      controller.stop();
      break;
    case "reset":
      LOGGER.info(String.format("Action: %s\n", action));
      controller.reset();
      break;
    default:
      LOGGER.error(String.format("Unknown action: %s\n", action));
      break;
    }
  }
}
