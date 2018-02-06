package de.grinder.controller;

import java.util.HashMap;

import javax.swing.Action;

public class ActionHandler {

    private static final HashMap<Class<? extends Action>, Action> ACTIONS = new HashMap<>();

    static {
        ACTIONS.put(NewTargetAction.class, new NewTargetAction());
        ACTIONS.put(ExitAction.class, new ExitAction());
        ACTIONS.put(AboutAction.class, new AboutAction());
    }

    public static Action getActionFor(final Class<? extends Action> actionClass) {
        return ACTIONS.get(actionClass);
    }
}
