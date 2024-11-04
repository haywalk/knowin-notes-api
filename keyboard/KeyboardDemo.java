/**
 * Demo reading from MIDI keyboard.
 * 
 * @author Hayden Walker
 * @version 2024-11-02
 */
public class KeyboardDemo implements KeyboardSubscriber {

    @Override
    public void alert(MessageStatus status, int key) {
        System.out.println("Status: " + status.name + "; key: " + key);
    }
    
    public static void main(String[] args) {
        KeyboardListener keyboard = new KeyboardListener();
        keyboard.addSubscriber(new KeyboardDemo());
    }
}
