package de.grinder.util.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Types of messages for communication with the target system.
 * 
 * 
 */
public enum MessageType {

  //@formatter:off
  NONE((short) 0), 
  GET_CONFIGURATION((short) 1), 
  FINISH_EXPERIMENT((short) 2), 
  LOG((short) 3), 
  SEND_CONFIGURATION((short) 4),
  GET_INJECTIONPARAMETERS((short)5),
  SEND_INJECTIONPARAMETERS((short)6);
  //@formatter:on

  private final short id;
  private static final Map<Short, MessageType> lookup = new HashMap<Short, MessageType>();

  private MessageType(final short id) {
    this.id = id;
  }

  static {
    for (final MessageType messageType : MessageType.values()) {
      lookup.put(messageType.id, messageType);
    }
  }

  public static MessageType getMessageType(final short id) {
    final MessageType type = lookup.get(id);
    if (type == null) {
      throw new RuntimeException(String.format("Illegal message type: %d", id));
    }
    return type;
  }

  public short getId() {
    return id;
  }
}
