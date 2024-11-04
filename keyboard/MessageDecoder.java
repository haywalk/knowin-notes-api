import javax.sound.midi.MidiMessage;

/**
 * Methods for extracting data from MidiMessage objects.
 * 
 * @author Hayden Walker
 * @version 2024-11-02
 */
public class MessageDecoder {

    /**
     * Private constructor.
     */
    private MessageDecoder() {
        
    }

    /**
     * Given a MIDI message, return its status.
     * 
     * @param message MIDI message.
     * @return Type of message.
     */
    public static MessageStatus getMessageStatus(MidiMessage message) {

        //System.out.println(Byte.toUnsignedInt(message.getMessage()[0]) >> 4);


        // first byte is status byte per MIDI spec
        int status = message.getStatus();

        System.out.println(Integer.toBinaryString(status));

        // first four bits are the status code (unsigned)
        int code = status >> 4;

        System.out.println(Integer.toBinaryString(code));

        // check for note off event
        if(code == MessageStatus.NOTE_OFF.code || 
            (code == MessageStatus.NOTE_ON.code && message.getMessage()[2] == 0)) { // workaround
            return MessageStatus.NOTE_OFF;
        }

        // check for note on event
        if(code == MessageStatus.NOTE_ON.code) {
            return MessageStatus.NOTE_ON;
        } 


        // otherwise some other kind of code
        return MessageStatus.OTHER;
    }
    
    /**
     * Given a MIDI message, return the key it pertains to. Assumes there is key data.
     * 
     * @param message MIDI message.
     * @return Key message affects.
     */
    public static int getMessageKey(MidiMessage message) {
        // second byte is key per MIDI spec
        return (int) message.getMessage()[1];
    }
}
