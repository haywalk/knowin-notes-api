package sound;

import java.io.File;
import java.util.HashMap;

/**
 * An abstract class for mapping sound identifiers to {@code Sound} objects.
 */
public abstract class SoundBank{

    protected static final String PATH_TO_SOUNDS = ".wav" + File.separator;
    protected static final String WAV_EXT = ".wav";

    private HashMap<String, Sound> soundMap;

    /**
     * Creates a SoundBank. The {@code populateSoundMap()} function is invoked during
     * initializaion.
     */
    protected SoundBank(){
        soundMap = new HashMap<String, Sound>();
        populateSoundMap();
    }

    /**
     * Subclasses should add entries to the soundMap when overriding this method.
     * Called during the initialization of the SoundBank.
     */
    protected abstract void populateSoundMap();
    
    /**
     * Adds the given {@code Sound} to the {@code SoundBank}, 
     * accessible through the given id.
     * 
     * @param id The id with which to access the given {@code Sound}.
     * @param sound The {@code Sound} to add to the {@code SoundBank}.
     */
    protected void addSound(String id, Sound sound){
        soundMap.put(id, sound);
    }

    /**
     * Gets the {@code Sound} at the given id.
     * 
     * @param id The id of the {@code Sound} requested.
     * @return The {@code Sound} at the given id.
     * @throws IllegalArgumentException If there is no {@code Sound} corresponding to the 
     * given id.
     */
    public Sound getSound(String id) throws IllegalArgumentException {

        if(!soundMap.containsKey(id)){
            throw new IllegalArgumentException("The given sound, " + id + ", could not be found.");
        }

        return soundMap.get(id);
    } 

    /**
     * Closes all of the {@code Sound}s in the {@code SoundBank}. 
     * Must be called before the {@code SoundBank} goes out of scope.
     */
    public void closeSounds(){
        for(Sound sound : soundMap.values()){
            sound.close();
        }
    }

}