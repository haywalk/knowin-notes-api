package sound;

import java.util.HashMap;

/**
 * A utility class for playing sounds based on a provided {@code SoundBank}.
 */
public class SoundPlayer{

    private HashMap<Sound, Thread> soundThreads;
    private SoundBank soundBank;

    /**
     * Creates a {@code SoundPlayer} that will use the given {@code SoundBank}
     * to play {@code Sound}s.
     * 
     * @param soundBank Contains all of the {@code Sound}s that can be played.
     */
    public SoundPlayer(SoundBank soundBank){
        setSoundBank(soundBank);
        soundThreads = new HashMap<Sound, Thread>();
    }

    /**
     * Plays the {@code Sound} with the given id.
     * 
     * @param id An identifying {@code String} for the {@code Sound} to be played.
     */
    public void playSound(String id){
        Sound sound = soundBank.getSound(id);

        Thread thread = new Thread(() -> {
            try{
                sound.play();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
        thread.run();
        soundThreads.put(sound, thread);
    }

    /**
     * Stops the {@code Sound} with the given id.
     * 
     * @param id An identifying {@code String} for the {@code Sound} to be stopped.
     */
    public void stopSound(String id){
        Sound sound = soundBank.getSound(id);
        if(soundThreads.containsKey(sound)){
            sound.stop();
            soundThreads.get(sound).interrupt();
        }
    }

    /**
     * Sets the {@code SoundBank} that the {@code SoundPlayer} should use.
     * 
     * @param soundBank The {@code SoundBank} to use.
     */
    public void setSoundBank(SoundBank soundBank){        
        if (soundBank == null) {
            throw new IllegalArgumentException("SoundBank cannot be null");
        }
        this.soundBank = soundBank;
    }

}