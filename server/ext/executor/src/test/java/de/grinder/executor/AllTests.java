package de.grinder.executor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.grinder.executor.servers.MessageStreamTest;
import de.grinder.executor.servers.TCPServerTest;

@RunWith(Suite.class)
@SuiteClasses({ TargetControllerImplTest.class, TargetFactoryImplTest.class,
                TCPServerTest.class, MessageStreamTest.class
              })
public class AllTests {

}
