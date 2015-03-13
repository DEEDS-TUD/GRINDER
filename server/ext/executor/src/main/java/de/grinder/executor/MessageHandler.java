package de.grinder.executor;

import de.grinder.util.message.Message;

/**
 * Handler for GRINDER messages.
 * 
 * 
 */
public interface MessageHandler {

  /**
   * Handles the given message and responds to it.
   * 
   * @param message
   *          The message that should be handled.
   * @return The response message to the given message.
   */
  public Message handle(Message message);
}
