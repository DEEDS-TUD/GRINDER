package de.grinder.executor;

import java.util.Collection;

import de.grinder.database.Campaign;
import de.grinder.executor.servers.Listener;
import de.grinder.util.cue.CUEAbstraction;
import de.grinder.util.message.Message;

/**
 * TargetController controls the experiments for a single target device.
 *
 *
 */
public interface TargetController {

    public Message handle(Message message);

    /**
     * Registers the given listener.
     *
     * @param listener
     */
    public void registerListener(Listener listener);

    /**
     * Unregisters the given message listener.
     *
     * @param listener
     *          The listener that should be unregistered.
     */
    public void unregisterListener(Listener listener);

    /**
     * Returns all listeners.
     */
    public Collection<Listener> getListeners();

    /**
     * Initializes the target system and start experiment execution.
     */
    public void start();

    /**
     * Finishes the current experiment and stops the target system.
     */
    public void stop();

    /**
     * Cancels the current experiment and sets target system into initial state.
     */
    public void reset();

    public CUEAbstraction getCueAbstraction();

    public void setCampaign(Campaign campaign);
}
