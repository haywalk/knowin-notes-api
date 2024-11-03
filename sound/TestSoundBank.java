package sound;

public class TestSoundBank extends SoundBank {

    public static final String BACKGROUND = "background";
    public static final String SFX = "pickup";

    @Override
    protected void populateSoundMap() {
        try{
            addSound(BACKGROUND, new Sound(PATH_TO_SOUNDS + BACKGROUND + WAV_EXT));
            addSound(SFX, new Sound(PATH_TO_SOUNDS + SFX + WAV_EXT));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
