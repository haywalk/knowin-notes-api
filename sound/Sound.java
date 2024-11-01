package sound;

import java.io.File;
import java.io.IOException;

public class Sound{

    private File soundFile;

    public Sound(String path) throws SoundFileException, IOException {
        soundFile = new File(path);
        // ensure file exists
        if(!soundFile.exists()){
            throw new IOException("The given file does not exist: " + path);
        }
        // ensure file is not a directory
        if(soundFile.isDirectory()){
            throw new IOException("The given path must lead to a sound file, not a directory: " + path);
        }
        // ensure file is a supported audio file


    }

    public File getSoundFile(){
        return soundFile;
    }

    public String toString(){
        return soundFile.getAbsolutePath();
    }
}