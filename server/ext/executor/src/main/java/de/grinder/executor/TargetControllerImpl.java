package de.grinder.executor;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.grinder.database.Campaign;
import de.grinder.database.ExperimentRun;
import de.grinder.database.TestCase;
import de.grinder.database.TestCase.KserviceNameToLongException;
import de.grinder.executor.servers.Listener;
import de.grinder.util.cue.CUEAbstraction;
import de.grinder.util.message.Message;
import de.grinder.util.message.MessageType;

/**
 * Controls the execution of experiments for one target system.
 * 
 */
public class TargetControllerImpl implements TargetController, MessageHandler {

  private final static Logger LOGGER = LoggerFactory
      .getLogger(TargetControllerImpl.class);

  /**
   * The {@link CUEAbstraction} to control the target system.
   */
  private CUEAbstraction cueAbstraction;

  private final ExecutorService executor = Executors.newCachedThreadPool();

  /**
   * The current experiment.
   */
  private volatile ExperimentRun experiment;

  /**
   * {@link Listener}s that provide messages for this controller.
   */
  private final Set<Listener> listeners;

  private final Callable<Void> reset = new Callable<Void>() {
    @Override
    public Void call() {
      LOGGER.info("Resetting {}...", cueAbstraction);
      cueAbstraction.reset();
      return null;
    }
  };

  private final Callable<Void> run = new Callable<Void>() {
    @Override
    public Void call() {
      LOGGER.info("Starting {}...", experiment);
      cueAbstraction.runExperiment();
      return null;
    }
  };

  private final Runner runner;

  private final Callable<Void> start = new Callable<Void>() {
    @Override
    public Void call() {
      LOGGER.info("Starting {}...", cueAbstraction);
      cueAbstraction.start();
      return null;
    }
  };

  private final Callable<Void> stop = new Callable<Void>() {
    @Override
    public Void call() {
      LOGGER.info("Stopping {}...", cueAbstraction);
      cueAbstraction.stop();
      return null;
    }
  };

  /**
   * The current campaign.
   */
  Campaign campaign;

  public TargetControllerImpl() {
    super();
    this.listeners = new HashSet<>();
    this.runner = new Runner();
  }

  @Override
  public CUEAbstraction getCueAbstraction() {
    return cueAbstraction;
  }

  @Override
  public Collection<Listener> getListeners() {
    return new HashSet<Listener>(listeners);
  }

  @Override
  public Message handle(final Message message) {
    Message response;
    switch (message.getType()) {
    case FINISH_EXPERIMENT:
      response = null;
      // care that a result isn't overwritten by another FINISH_EXPERIMENT
      // message (e.g. extern detector signals end and afterwards intern
      // detector signals experiment end before experiment was safely
      // stopped)
      if (experiment.getResult() == 0) {
        // log result
        final byte[] body = message.getBody();
        final short result = (short) (body[0] << 8 | body[1]);
        experiment.setResult(result);

        if (body.length > 2) {
          experiment.setActivated(new String(Arrays.copyOfRange(body, 2, body.length)));
        }

        // end experiment
        synchronized (runner) {
          runner.notifyAll();
        }
      }
      break;
    case LOG:
      experiment.appendLog(new String(message.getBody()));
      response = null;
      break;
    case GET_CONFIGURATION:
      response = new Message(MessageType.SEND_CONFIGURATION);
      final String configuration = experiment.getConfiguration();
      LOGGER.debug("Sending configuration: {}", configuration);
      response.setBody(configuration.getBytes());
      break;
    case GET_INJECTIONPARAMETERS:
      response = new Message(MessageType.SEND_INJECTIONPARAMETERS);
      byte[] params;
      try {
        params = experiment.getInjectionParameters();
      } catch (final KserviceNameToLongException e) {
        e.printStackTrace();
        return null;
      }
      LOGGER.debug("Sending injection parameters: service: {}, param: {}, bit: {}",
          new Object[] {
              new String(params, 0, 10),
              params[TestCase.MAX_KSERVICE_LENGTH] << 8
                  | params[TestCase.MAX_KSERVICE_LENGTH + 1],
              params[TestCase.MAX_KSERVICE_LENGTH + 2] << 8
                  | params[TestCase.MAX_KSERVICE_LENGTH + 3] });
      response.setBody(params);
      break;
    default:
      LOGGER.debug("Unrecognized message: {}", message);
      response = null;
      break;
    }
    return response;
  }

  @Override
  public void registerListener(final Listener listener) {
    LOGGER.info("Register {}", listener);
    listener.setMessageHandler(this);
    listeners.add(listener);
    executor.submit(listener);
  }

  @Override
  public void reset() {
    // TODO Break execution of runner
    executor.submit(reset);
  }

  @Override
  public void setCampaign(final Campaign campaign) {
    this.campaign = campaign;
  }

  public void setCueAbstraction(final CUEAbstraction cueAbstraction) {
    this.cueAbstraction = cueAbstraction;
  }

  /**
   * Start the campaign for this Target.
   * 
   * Starts a new Thread that sequentially runs each experiment of the campaign.
   */
  @Override
  public void start() {
    if (campaign == null) {
      LOGGER.warn("No Campaign loaded.");
      return;
    }

    if (runner.stopped) {
      runner.stopped = false;
      executor.submit(runner);
    }
  }

  @Override
  public void stop() {
    runner.stopped = true;
  }

  @Override
  public void unregisterListener(final Listener listener) {
    LOGGER.info("Unregister {}", listener);
    listener.setMessageHandler(null);
    listeners.remove(listener);
    listener.stop();
  }

  /**
   * Run the campaign that is loaded for this target.
   * 
   * This method loads the next experiment, if it exists, resets the target, and waits for
   * the end of the experiment run.
   */
  private class Runner implements Runnable {
    private volatile boolean stopped = true;

    @Override
    public synchronized void run() {
      // ----------------------------------------------
      // hack for continuing experiments at a given test case
      final Iterator<TestCase> iterator = campaign.iterator();
//    int till = 379; // set to next case minus 2
//
//    for(int i = 0; i < till; i++)
//    iterator.next();
      // ----------------------------------------------

      new FutureTask<Void>(start).run();
      while (!stopped && iterator.hasNext()) {

        final TestCase testCase = iterator.next();
        LOGGER.debug("Test case id: {}", testCase.getId());

        LOGGER.info("Starting experiment...");
        experiment = new ExperimentRun(testCase, campaign);
        LOGGER.debug("Experiment id: {}", experiment.getId());

        executor.submit(run);
        experiment.setStartExperiment();
        try {
          LOGGER.info("Waiting for end of experiment...");
          wait();
          LOGGER.info("Continue experiment.");
        } catch (final InterruptedException e) {
          // TODO Handle interrupt
        }

        final Future<Void> submit = executor.submit(reset);
        while (!submit.isDone()) {
          try {
            Thread.sleep(100);
          } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }

        experiment.setEndExperiment();
        experiment.save();
      }
      this.stopped = true;
      executor.submit(stop);
    }
  }
}
