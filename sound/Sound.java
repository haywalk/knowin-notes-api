package sound;

import java.io.IOException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * A wrapper class for {@code javax.sound.sampled.Clip}, 
 * exposing only relevant methods like {@code play()} and {@code stop()}.
 */
public class Sound {

    private String path;
    private Clip clip;

    /**
     * Creates a Sound from the sound file at the given path.
     * @param path The path to the sound file.
     * @throws IOException If the file does not exist.
     * @throws SoundFileException If the file is in an unsupported format or 
     * if the line is unavailable (something else is likely already using the file).
     */
    public Sound(String path) {
        this.path = path;
    }

    /**
     * Plays the sound from the beginning.
     */
    public void play() throws IOException, SoundFileException {
        
        // Play the clip
        try {
            // Get a clip that audio can be played on.
            clip = AudioSystem.getClip();
            // Close the line the clip in on when the clip finishes playing to free resources
            clip.addLineListener(event -> {
                if(LineEvent.Type.STOP.equals(event.getType())){
                    event.getLine().close();
                }
            });
            // Convert the audio file to an input stream
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
            // Have the clip read from the audio file/input stream
            clip.open(audioInputStream);
            // Start playing the clip
            clip.start();
        } catch (UnsupportedAudioFileException e){
            throw new SoundFileException("Unsupported audio file: " + path);
        } 
        catch (LineUnavailableException e) {
            throw new SoundFileException("Line unavailable: " + path);
        }
    }

    /**
     * Makes the sound stop playing.
     */
    public void stop(){
        if(clip != null){
            clip.stop();
        }
    }
}