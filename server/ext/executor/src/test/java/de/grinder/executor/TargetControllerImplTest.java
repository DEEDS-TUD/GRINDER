package de.grinder.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.grinder.executor.servers.Listener;
import de.grinder.util.cue.CUEAbstraction;

public class TargetControllerImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testTargetControllerImpl() {
    final TargetControllerImpl targetController = new TargetControllerImpl();

    assertNull(targetController.getCueAbstraction());

    final Collection<Listener> listeners = targetController.getListeners();
    assertNotNull(listeners);
    assertTrue(listeners.isEmpty());
  }

  @Test
  public void testRegisterAndUnregisterListener() {
    final TargetControllerImpl targetController = new TargetControllerImpl();
    final Listener listener = mock(Listener.class);

    // Register listener
    targetController.registerListener(listener);

    Collection<Listener> listeners = targetController.getListeners();
    assertEquals(1, listeners.size());

    verify(listener).setMessageHandler(targetController);

    // Unregister listener
    targetController.unregisterListener(listener);
    listeners = targetController.getListeners();
    assertEquals(0, listeners.size());
  }

  @Test
  @Ignore
  public void testExperimentLivecycle() {
    final TargetControllerImpl targetController = new TargetControllerImpl();

    final CUEAbstraction cueAbstraction = mock(CUEAbstraction.class);
    targetController.setCueAbstraction(cueAbstraction);

    targetController.start();
    verify(cueAbstraction).start();

    targetController.reset();
    verify(cueAbstraction).reset();

    targetController.stop();
    verify(cueAbstraction).stop();
  }
}
