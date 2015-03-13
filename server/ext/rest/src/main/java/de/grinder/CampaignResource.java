package de.grinder;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.grinder.database.Campaign;
import de.grinder.database.CampaignDao;

public class CampaignResource extends ServerResource {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(CampaignResource.class);

  private final CampaignDao dao;

  public CampaignResource() {
    this.dao = new CampaignDao();
  }

  public CampaignResource(CampaignDao dao) {
    this.dao = dao;
  }
  
  @Get("xml")
  public Campaign getCampaign() {
    Long id = Long.parseLong((String) getRequestAttributes().get("id"));
    Campaign campaign = dao.getById(id);
    LOGGER.debug("Request campaign: id={} campaign={}", id, campaign);
    
    if(campaign == null) {
      setStatus(Status.CLIENT_ERROR_NOT_FOUND);
      return null;
    }
    
    return campaign;
  }
}
