package de.grinder.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestCaseTest {

    @Test
    public void testTestCase() {
        final TestCase testCase = new TestCase();
        assertNotNull("Created testcase should not be null.", testCase);
    }

    @Test
    public void testToString() {
        final TestCase testCase = new TestCase();
        assertEquals("Correct string should be created.", "TestCase [id=0]",
                     testCase.toString());
    }
}
