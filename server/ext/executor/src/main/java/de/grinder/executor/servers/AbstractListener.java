package de.grinder.executor.servers;

import de.grinder.executor.MessageHandler;

public abstract class AbstractListener implements Listener {

  protected MessageHandler handler;

  @Override
  public void setMessageHandler(final MessageHandler handler) {
    this.handler = handler;
  }
}