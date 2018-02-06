package de.grinder.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.junit.Ignore;
import org.junit.Test;

public class DatabaseTest {

    @Test
    public void testGetEntityManager() {
        try {
            final EntityManager em = Database.getEntityManager();
            assertNotNull("EntityManager should not be null.", em);
        } catch (final Exception e) {
            fail("Exception should not occur: " + e);
        }
    }

    @Ignore("Test cases should reduce side effects on database")
    @Test
    public void testAddTarget() {
        final Target expected = createTarget();
        Database.instance().addTarget(expected);

        final Collection<Target> targets = Database.instance().getTargets();
        assertEquals(1, targets.size());
        final Target actual = targets.iterator().next();
        assertEquals(expected.getConfiguration(), actual.getConfiguration());
    }

    private Target createTarget() {
        final Target target = new Target();
        target.setName("TestTarget");
        target.setConfiguration("TestConfiguration");
        return target;
    }
}
