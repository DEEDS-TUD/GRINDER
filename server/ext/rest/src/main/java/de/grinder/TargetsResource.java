package de.grinder;

import java.util.Collection;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import de.grinder.database.TargetDao;
import de.grinder.database.TargetEntity;

/**
 * Resource for listing all available targets of a GRINDER server.
 * 
 * 
 */
public class TargetsResource extends ServerResource {

  /**
   * Data access object for accessing the database.
   */
  private final TargetDao dao;

  /**
   * Constructs a default TargetsResource.
   */
  public TargetsResource() {
    this.dao = new TargetDao();
  }

  /**
   * Constructs a TargetsResource with the given data access object.
   * 
   * @param dao
   *          The data access object
   */
  public TargetsResource(TargetDao dao) {
    this.dao = dao;
  }

  /**
   * Creates a new target based on the given representation.
   * 
   * @param entity
   *          The representation of the target that should be created
   * @return The completed representation of the created target
   */
  @Post
  public Representation createTarget(Representation entity) {
    Form form = new Form(entity);
    String targetName = form.getFirstValue("name");

    TargetEntity targetEntity = new TargetEntity();
    targetEntity.setName(targetName);
    dao.save(targetEntity);

    // TODO Return entity with completed fields
    return null;
  }

  @Get("xml")
  public Collection<TargetEntity> getAll() {
    return dao.getAll();
  }
}
