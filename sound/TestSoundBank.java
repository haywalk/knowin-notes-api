package sound;

/**
 * An example {@code SoundBank} with which the {@code SoundPlayer} can be tested.
 */
public class TestSoundBank extends SoundBank {

    public static final String BACKGROUND = "background";
    public static final String SFX = "pickup";

    @Override
    protected void populateSoundMap() {
        addSound(BACKGROUND, new Sound(PATH_TO_SOUNDS + BACKGROUND + WAV_EXT));
        addSound(SFX, new Sound(PATH_TO_SOUNDS + SFX + WAV_EXT));
    }
}
