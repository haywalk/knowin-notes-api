package sound;

import java.util.HashMap;

/**
 * 
 */
public class SoundPlayer{

    private HashMap<Sound, Thread> soundThreads;
    private SoundBank soundBank;

    public SoundPlayer(SoundBank soundBank){
        setSoundBank(soundBank);
        soundThreads = new HashMap<Sound, Thread>();
    }

    public void playSound(String id){
        Sound sound = soundBank.getSound(id);

        Thread thread = new Thread(() -> {
            sound.play();
        });
        thread.run();
        soundThreads.put(sound, thread);
    }

    public void stopSound(String id){
        Sound sound = soundBank.getSound(id);
        if(soundThreads.containsKey(sound)){
            sound.stop();
            soundThreads.get(sound).interrupt();
        }
    }

    public void setSoundBank(SoundBank soundBank){        
        if (soundBank == null) {
            throw new IllegalArgumentException("SoundBank cannot be null");
        }
        this.soundBank = soundBank;
    }

    public void closeSounds(){
        this.soundBank.closeSounds();
    }

}