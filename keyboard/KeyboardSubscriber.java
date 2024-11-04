/**
 * Subscriber that is alerted to MIDI messages from a keyboard.
 * 
 * @author Hayden Walker
 * @version 2024-11-02
 */
public interface KeyboardSubscriber {
    /**
     * Alert the subscriber to a MIDI message.
     * 
     * @param status Message status.
     * @param key Key affected by message.
     */
    public void alert(MessageStatus status, int key);
}
