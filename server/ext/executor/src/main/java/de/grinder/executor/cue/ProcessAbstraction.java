package de.grinder.executor.cue;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.grinder.util.cue.CUEAbstraction;

/**
 * This class provides a {@link CUEAbstraction} for processes.
 * 
 * It provides mechanisms to start an external process from java, extract the return
 * value, and kill that process, if necessary.
 * 
 * A possible future extension is communication with the process via its standard input,
 * output and error stream.
 * 
 * 
 */
public abstract class ProcessAbstraction implements CUEAbstraction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessAbstraction.class);

  /**
   * The executable file that is executed whenever a new process is started.
   */
  private final File executable;

  /**
   * Output stream for sending messages to the GRINDER server.
   */
  private OutputStream outputStream;

  /**
   * The child process that executes the executable.
   */
  private Process process;

  public ProcessAbstraction(final File executable) {
    super();
    this.executable = executable;
  }

  /**
   * Signal the end of the experiment to GRINDER server.
   */
  private void finishExperiment() {
    final byte[] message = new byte[6];
    message[0] = 0;
    message[1] = 2;
    message[2] = 0;
    message[3] = 0;
    message[4] = 0;
    message[5] = 0;
    try {
      outputStream.write(message);
    } catch (final IOException e) {
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Send the given text as log message to the GRINDER server.
   * 
   * @param text
   *          the content of the log message
   */
  private void log(final String text) {
    final byte[] message = new byte[text.length() + 6];
    message[0] = 0;
    message[1] = 3;
    message[2] = 0;
    message[3] = 0;
    message[4] = 0;
    message[5] = (byte) text.length();
    System.arraycopy(text.getBytes(), 0, message, 6, text.length());
    try {
      outputStream.write(message);
    } catch (final IOException e) {
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Tell the name of the current target process.
   * 
   * @return name of the target process
   */
  public String getName() {
    return executable.getName();
  }

  /**
   * Kill the process under evaluation.
   */
  @Override
  public void reset() {
    if (process != null) {
      process.destroy();
      process = null;
    }
  }

  /**
   * Run the experiment by executing the process as a child.
   */
  @Override
  public void runExperiment() {
    try {
      process = Runtime.getRuntime().exec(executable.getAbsolutePath());
    } catch (final IOException e) {
      LOGGER.error("Start failed: " + getName());
      return;
    }

    // Wait for child process to exit
    try {
      final int exitValue = process.waitFor();
      log("Exit: " + getName());
      log("Status: " + exitValue);
    } catch (final InterruptedException e) {
      LOGGER.error("Experiment interrupted");
    }

    // Delay experiment completion to wait for missing TPC messages and
    // to provide a more enjoyable user experience;)
    // This delay should be removed in the future, but evaluation of the
    // consequences is pending.
    try {
      Thread.sleep(1000);
    } catch (final InterruptedException e) {
    }
    finishExperiment();
  }

  /**
   * Open a TCP connection to GRINDER.
   */
  @Override
  public void start() {
    try {
      outputStream = new Socket("localhost", 4444).getOutputStream();
    } catch (final IOException e) {
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Close the TCP connection to GRINDER.
   */
  @Override
  public void stop() {
    try {
      outputStream.close();
    } catch (final IOException e) {
      LOGGER.error(e.getMessage());
    }
  }
}