package de.grinder.database;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ExperimentRunTest {

  @Test
  public void testGetTestCase() {
    final TestCase testCase = mock(TestCase.class);
    final Campaign camp = mock(Campaign.class);
    final ExperimentRun experimentRun = new ExperimentRun(testCase, camp);

    assertEquals(testCase, experimentRun.getTestCase());
  }
}
