package sound;

/**
 * A custom exception for indicating there is an issue with a sound file.
 */
public class SoundFileException extends Exception {

    /**
     * Creates a SoundFileException.
     * 
     * @param message The reason the exception is being thrown.
     */
    public SoundFileException(String message){
        super(message);
    }
    
}
