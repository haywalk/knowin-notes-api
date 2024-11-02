package sound;

import java.io.File;
import java.io.IOException;

/**
 * A {@code SoundBank} containing piano sounds.
 */
public class PianoSoundBank extends SoundBank{

    public static final String C4 = "c4";
    public static final String CS4 = "c#4";
    public static final String D4 = "d4";
    public static final String DS4 = "d#4";
    public static final String E4 = "e4";
    public static final String F4 = "f4";
    public static final String FS4 = "f#4";
    public static final String G4 = "g4";
    public static final String GS4 = "g#4";
    public static final String A4 = "a4";
    public static final String AS4 = "a#4";
    public static final String B4 = "b4";
    private static final String PATH_TO_SOUNDS = ".wav" + File.pathSeparator;
    private static final String WAV_EXT = ".wav";

    @Override
    protected void populateSoundMap() {
        try{
            addSound(C4, new Sound(PATH_TO_SOUNDS + C4 + WAV_EXT));
            addSound(CS4, new Sound(PATH_TO_SOUNDS + CS4 + WAV_EXT));
            addSound(D4, new Sound(PATH_TO_SOUNDS + D4 + WAV_EXT));
            addSound(DS4, new Sound(PATH_TO_SOUNDS + DS4 + WAV_EXT));
            addSound(E4, new Sound(PATH_TO_SOUNDS + E4 + WAV_EXT));
            addSound(F4, new Sound(PATH_TO_SOUNDS + F4 + WAV_EXT));
            addSound(FS4, new Sound(PATH_TO_SOUNDS + FS4 + WAV_EXT));
            addSound(G4, new Sound(PATH_TO_SOUNDS + G4 + WAV_EXT));
            addSound(GS4, new Sound(PATH_TO_SOUNDS + GS4 + WAV_EXT));
            addSound(A4, new Sound(PATH_TO_SOUNDS + A4 + WAV_EXT));
            addSound(AS4, new Sound(PATH_TO_SOUNDS + AS4 + WAV_EXT));
            addSound(B4, new Sound(PATH_TO_SOUNDS + B4 + WAV_EXT));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (SoundFileException e){
            e.printStackTrace();
        }
    }

}