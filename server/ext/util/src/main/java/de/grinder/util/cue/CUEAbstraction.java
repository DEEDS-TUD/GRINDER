package de.grinder.util.cue;

/**
 * This interface abstracts differences of target systems.
 * 
 * Each target system requires a class that implements this interface. GRINDER uses this
 * interface to control the experiment process.
 * 
 * 
 */
public interface CUEAbstraction {

  /**
   * Starts the target system and sets up the test environment.
   */
  public void start();

  /**
   * Stops the target system and tears down the test environment.
   */
  public void stop();

  /**
   * Runs one experiment run on the target system.
   */
  public void runExperiment();

  /**
   * Resets the target system and test environment for the next experiment run.
   */
  public void reset();

}