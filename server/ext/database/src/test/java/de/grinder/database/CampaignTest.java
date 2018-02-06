package de.grinder.database;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CampaignTest {

    @Test
    public void testGet() {
        final Campaign campaign = new Campaign();

        final TestCase t1 = createTestCase(1);
        final TestCase t2 = createTestCase(2);

        campaign.add(t1);
        campaign.add(t2);

        assertEquals(t2, campaign.get(2));
    }

    private TestCase createTestCase(final int id) {
        final TestCase tc = mock(TestCase.class);
        when(tc.getId()).thenReturn(id);
        return tc;
    }
}
