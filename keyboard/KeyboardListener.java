import java.util.ArrayList;
import java.util.Collection;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

/**
 * Listen to a MIDI keyboard and alert subscribers to keys being pressed or released.
 * 
 * @author Hayden Walker
 * @version 2024-11-02
 */
public class KeyboardListener {
    /**
     * Subscribers to be alerted to keys played on this keyboard.
     */
    private ArrayList<KeyboardSubscriber> subscribers;

    /**
     * Create a new KeyboardListener object.
     */
    public KeyboardListener() {
        // initialize list of subscribers
        subscribers = new ArrayList<KeyboardSubscriber>();

        // loop over available MIDI devices
        for(MidiDevice.Info deviceInfo : MidiSystem.getMidiDeviceInfo()) {
            try {
                // attempt to connect to each device
                MidiDevice thisDevice = MidiSystem.getMidiDevice(deviceInfo);

                // subscribe to the device's transmitter
                thisDevice.getTransmitter().setReceiver(new MessageHandler(subscribers));
                thisDevice.open();
            } 

            // it seems to think every device is a MIDI device, so fail gracefully
            catch(MidiUnavailableException e) {
                continue;
            }
        }
    }

    /**
     * Add a subscriber to this KeyboardListener.
     * 
     * @param subscriber Subscriber to be alerted to messages.
     */
    public void addSubscriber(KeyboardSubscriber subscriber) {
        subscribers.add(subscriber);
    }
}

/**
 * Listen for and handle MIDI messages.
 * 
 * @author Hayden Walker
 * @version 2024-11-02
 */
class MessageHandler implements Receiver {
    /**
     * Subscribers to alert to messages.
     */
    private Collection<KeyboardSubscriber> subscribers;

    /**
     * Create a new MessageHandler object.
     * 
     * @param subscribers Subscribers to alert to messages.
     */
    public MessageHandler(Collection<KeyboardSubscriber> subscribers) {
        this.subscribers = subscribers;
    }
    
    /**
     * Handle a MIDI message.
     * 
     * @param message MIDI message.
     * @param timeStamp Timestamp.
     */
    @Override
    public void send(MidiMessage message, long timeStamp) {
        // decode message
        MessageStatus type = MessageDecoder.getMessageStatus(message);
        int key = MessageDecoder.getMessageKey(message);

        // alert subscribers
        for(KeyboardSubscriber subscriber : subscribers) {
            subscriber.alert(type, key);
        }

        //System.out.println(message.getMessage());
    }

    /**
     * Close the MessageHandler.
     */
    @Override
    public void close() {

    }
}

