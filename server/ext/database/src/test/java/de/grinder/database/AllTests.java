package de.grinder.database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DatabaseTest.class, CampaignTest.class, TestCaseTest.class,
    ExperimentRunTest.class })
public class AllTests {

}
