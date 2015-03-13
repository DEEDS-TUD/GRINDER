package de.grinder;

import de.grinder.android_fi.EmulatedAndroid;
import de.grinder.database.Database;
import de.grinder.executor.Executor;
import de.grinder.util.cue.CUEAbstractionRegistry;

/**
 * 
 * 
 */
public class Grinder {

  public Grinder() {
    registerCUEAbstractions();
  }

  private void registerCUEAbstractions() {
    /*
     * TODO Find a better way to register CUEAbstractions at GRINDER.
     * 
     * A possible solution would be reflection to find all classes that implement the
     * CUEAbstraction interface.
     */
    final CUEAbstractionRegistry registry = CUEAbstractionRegistry.getInstance();
    registry.register(EmulatedAndroid.class);
  }

  /**
   * Get access to the database of GRINDER
   * 
   * @return The database instance
   */
  public Database getDatabase() {
    return Database.instance();
  }

  /**
   * Get access to the executor of GRINDER
   * 
   * @return The executor instance
   */
  public Executor getExecutor() {
    return Executor.instance();
  }
}
