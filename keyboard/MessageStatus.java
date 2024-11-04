/**
 * Types of messages that the system can receive from the MIDI keyboard.
 * 
 * @author Hayden Walker
 * @version 2024-11-02
 */
public enum MessageStatus {
    NOTE_ON("NOTE ON", 0b1001), 
    NOTE_OFF("NOTE OFF", 0b1000),
    OTHER("OTHER", 0);

    public final String name;
    public final int code;
    private MessageStatus(String status, int code) {
        this.name = status;
        this.code = code;
    }
}
