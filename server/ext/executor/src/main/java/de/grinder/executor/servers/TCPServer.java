package de.grinder.executor.servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener for TCP/IP connections.
 * 
 * 
 */
public class TCPServer extends AbstractListener {

  private final static Logger LOGGER = LoggerFactory.getLogger(TCPServer.class);

  private volatile boolean active;

  /**
   * Port, that the server listens on.
   */
  private int port;

  private final ExecutorService threadPool = Executors.newCachedThreadPool();

  public int getPort() {
    return port;
  }

  @Override
  public void run() {
    /*
     * We do not close the client socket. The MessageStream class handles closes the
     * streams. The socket is automatically closed, if the input or output stream is
     * closed.
     */
    this.active = true;

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      LOGGER.info("Accepting connections at port {}.", port);
      while (active) {
        // Accept connection for the socket
        final Socket socket = serverSocket.accept();
        LOGGER.info("TCP connection accepted from {}.", socket.getInetAddress());

        // Create a new message stream for processing the input
        final MessageStream messageStream = new MessageStream();
        messageStream.setHandler(handler);
        messageStream.setFromTarget(socket.getInputStream());
        messageStream.setToTarget(socket.getOutputStream());
        threadPool.execute(messageStream);
      }
    } catch (final IOException e) {
      LOGGER.error("Exception occured {}", e.getMessage());
    }
  }

  public void setPort(final int port) {
    this.port = port;
  }

  /**
   * Stops the listener, but keeps open connections.
   */
  @Override
  public void stop() {
    this.active = false;
  }
}
