package de.grinder;

import java.util.Collection;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import de.grinder.database.Campaign;
import de.grinder.database.CampaignDao;

/**
 * 
 * 
 */
public class CampaignsResource extends ServerResource {
  
  private final CampaignDao dao; 
  
  public CampaignsResource() {
    this(new CampaignDao());
  }
  
  public CampaignsResource(CampaignDao dao) {
    super();
    this.dao = dao;
  }

  @Post
  public Representation postCampaign(Representation entity) {
    // TODO Implement
    return null;
  }

  @Get("xml")
  public Collection<Campaign> getAll() {
    return dao.getAll();
  }
}
