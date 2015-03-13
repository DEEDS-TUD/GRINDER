package de.grinder.executor;

import java.io.Reader;

public interface TargetFactory {

  /**
   * Create a {@link TargetController} according the given description
   * 
   * @param description
   * @return
   * @throws TargetCreationException
   */
  public TargetController createTargetController(final Reader description)
      throws TargetCreationException;

}