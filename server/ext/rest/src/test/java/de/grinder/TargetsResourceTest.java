package de.grinder;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import de.grinder.database.TargetDao;
import de.grinder.database.TargetEntity;

public class TargetsResourceTest {

  @Test
  public void testCreateTarget() {
    TargetDao dao = mock(TargetDao.class);
    TargetsResource targetsResource = new TargetsResource(dao);
    Representation representation = createRepresentation();

    targetsResource.createTarget(representation);

    verify(dao).save(any(TargetEntity.class));
  }

  private Representation createRepresentation() {
    String configuration = "<target><name>android_fi</name></target>";
    return new StringRepresentation(configuration);
  }
}
