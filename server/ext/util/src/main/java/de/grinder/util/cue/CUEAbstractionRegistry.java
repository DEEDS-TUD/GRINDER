package de.grinder.util.cue;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Singleton registry for all known cueAbstractions
 *
 *
 */
public class CUEAbstractionRegistry {

    private static CUEAbstractionRegistry instance;

    private final Collection<Class<? extends CUEAbstraction>> cueAbstractions;

    public static CUEAbstractionRegistry getInstance() {
        // TODO Implement thread safety
        if (null == instance) {
            instance = new CUEAbstractionRegistry();
        }

        return instance;
    }

    private CUEAbstractionRegistry() {
        super();
        cueAbstractions = new LinkedList<>();
    }

    public Collection<Class<? extends CUEAbstraction>> getCUEAbstractions() {
        return Collections.unmodifiableCollection(cueAbstractions);
    }

    public void register(final Class<? extends CUEAbstraction> clazz) {
        cueAbstractions.add(clazz);
    }
}
