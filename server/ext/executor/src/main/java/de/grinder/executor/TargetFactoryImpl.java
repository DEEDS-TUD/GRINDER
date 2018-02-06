package de.grinder.executor;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.grinder.executor.servers.AbstractListener;
import de.grinder.executor.servers.TCPServer;
import de.grinder.util.cue.CUEAbstraction;
import de.grinder.util.cue.CUEAbstractionRegistry;

public class TargetFactoryImpl implements TargetFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(TargetFactoryImpl.class);

    /**
     * Return a {@link CUEAbstraction} for the given description.
     *
     * @param description
     *          Description of a CUE abstraction.
     * @return {@link CUEAbstraction} specified by the description.
     * @throws TargetCreationException
     *           If the given description could not be instantiated as CUE abstraction.
     */
    public CUEAbstraction createCueAbstraction(final Element description)
    throws TargetCreationException {
        final Collection<Class<? extends CUEAbstraction>> cueAbstractions = CUEAbstractionRegistry
                .getInstance().getCUEAbstractions();
        final String name = description.getChildText("name");
        LOGGER.info("Creating CUE abstraction: " + name);

        // Look up given name
        for (final Class<? extends CUEAbstraction> cueAbstraction : cueAbstractions) {
            if (name.equals(cueAbstraction.getName())) {
                LOGGER.debug("Found CUE abstraction: " + name);
                try {
                    return cueAbstraction.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    final String msg = String.format("Failed to instantiate CUE abstraction [%s].",
                                                     cueAbstraction.getName());
                    LOGGER.error(msg, e);
                    throw new TargetCreationException(msg, e);
                }
            }
        }

        LOGGER.warn(String.format(
                        "Could not find CUE abstraction [%s] in registry. Trying dynamically.", name));
        try {
            final Class<?> clazz = Class.forName(name);
            if (CUEAbstraction.class.isAssignableFrom(clazz)) {
                return (CUEAbstraction) clazz.newInstance();
            } else {
                final String msg = String.format("Invalid CUE abstraction class [%s]. "
                                                 + "Valid CUE abstractions must implement [%s].", name,
                                                 CUEAbstraction.class.getName());
                LOGGER.error(msg);
                throw new TargetCreationException(msg);
            }
        } catch (final ClassNotFoundException e) {
            final String msg = String.format(
                                   "Cannot create CUE Abstraction [%s]. Unknown class.", name);
            LOGGER.error(msg, e);
            throw new TargetCreationException(msg, e);
        } catch (InstantiationException | IllegalAccessException e) {
            final String msg = String.format("Failed to instantiate CUE abstraction [%s].",
                                             name);
            LOGGER.error(msg, e);
            throw new TargetCreationException(msg, e);
        }
    }

    /**
     * Create the {@link AbstractListener}s that are specified in the XML configuration.
     *
     * @return The instantiated server.
     * @throws TargetCreationException
     *           If the requested server could not be instantiated.
     */
    public AbstractListener createServer(final Element description)
    throws TargetCreationException {
        try {
            final String clazz = description.getChildText("class");
            final int port = Integer.parseInt(description.getChildText("port"));
            final TCPServer server = (TCPServer) Class.forName(clazz).newInstance();
            server.setPort(port);
            return server;
        } catch (final Exception e) {
            final String msg = String.format("Failed to instantiate server [%s].",
                                             description.getChildText("class"));
            LOGGER.error(msg, e);
            throw new TargetCreationException(msg, e);
        }
    }

    @Override
    public TargetController createTargetController(final Reader description)
    throws TargetCreationException {
        final TargetControllerImpl targetController = new TargetControllerImpl();

        LOGGER.info("Parsing XML target configuration");
        final Document doc = parseXML(description);

        // Create cueAbstraction
        final Element cueDescription = doc.getRootElement().getChild("cue_abstraction");
        final CUEAbstraction cueAbstraction = createCueAbstraction(cueDescription);
        targetController.setCueAbstraction(cueAbstraction);

        // Create listeners
        @SuppressWarnings("unchecked")
        final List<Element> serverDescriptions = doc.getRootElement().getChild("servers")
                .getChildren();
        for (final Element serverDescription : serverDescriptions) {
            final AbstractListener abstractServer = createServer(serverDescription);
            targetController.registerListener(abstractServer);
        }

        return targetController;
    }

    /**
     * Parse the XML file and return the XML Structure as {@link Document}.
     *
     * @param file
     *          The XML file.
     * @return The XML Structure as {@link Document}.
     * @throws TargetCreationException
     *           If the description could not be parsed.
     */
    public Document parseXML(final Reader description) throws TargetCreationException {
        // TODO Add a validity check of the given XML
        Document document;

        try {
            final SAXBuilder builder = new SAXBuilder();
            document = builder.build(description);
        } catch (IOException | JDOMException e) {
            final String msg = "Failed to parse XML file";
            LOGGER.error(msg, e);
            throw new TargetCreationException(msg, e);
        }

        return document;
    }
}
