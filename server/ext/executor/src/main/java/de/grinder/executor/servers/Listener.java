package de.grinder.executor.servers;

import de.grinder.executor.MessageHandler;

/**
 * Listener for incoming messages to redirect them to an according handler.
 *
 *
 */
public interface Listener extends Runnable {

    public void setMessageHandler(MessageHandler handler);

    public void stop();

}
