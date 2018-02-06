package de.grinder.executor;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import de.grinder.database.Target;

public class Executor {

    private final Collection<TargetController> controllers = new ArrayList<>();

    private static Executor instance = new Executor();

    public TargetController addController(final Target target) {
        final TargetFactory factory = new TargetFactoryImpl();

        try (StringReader reader = new StringReader(target.getConfiguration())) {
            final TargetController controller = factory.createTargetController(reader);
            controllers.add(controller);
            return controller;
        } catch (final TargetCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static Executor instance() {
        return instance;
    }

}
