package sound;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * A wrapper class for {@code javax.sound.sampled.Clip}, 
 * exposing only relevant methods like {@code play()} and {@code stop()}.
 */
public class Sound {

    private Clip clip;

    /**
     * Creates a Sound from the sound file at the given path.
     * @param path The path to the sound file.
     * @throws IOException If the file does not exist.
     * @throws SoundFileException If the file is in an unsupported format or 
     * if the line is unavailable (something else is likely already using the file).
     */
    public Sound(String path) throws IOException, SoundFileException {
        File soundFile = new File(path);
        // ensure file exists
        if(!soundFile.exists()){
            throw new IOException("The given file does not exist: " + path);
        }

        // Prepare the clip.
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e){
            throw new SoundFileException("Unsupported audio file: " + path);
        } 
        catch (LineUnavailableException e) {
            throw new SoundFileException("Line unavailable: " + path);
        }
    }

    /**
     * Plays the sound from the beginning.
     */
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // rewind to the beginning
            clip.start();
        }
    }

    public void stop(){
        if(clip != null){
            clip.stop();
        }
    }

    /**
     * Closes this sound's clip. 
     * Must be called to free resources when Sound goes out of scope
     */
    public void close(){
        if(clip != null){
            clip.close();
        }
    }

    /**
     * @return The Clip from which this Sound is based. 
     */
    public Clip getClip(){
        return clip;
    }
}