package sound;

public class Test {
    public static void main(String[] args){
        // Create a sound player
        SoundPlayer sp = new SoundPlayer(new TestSoundBank());

        // Start playing background music
        sp.playSound(TestSoundBank.BACKGROUND);

        // Play SFX every 5 seconds 5 times
        for(int i = 0; i < 5; i++){
            sleep(5000);
            sp.playSound(TestSoundBank.SFX);
        }
        // Small delay before shutting down
        sleep(2000);
        // Cease playing the background music
        sp.stopSound(TestSoundBank.BACKGROUND);
        // Clean up the sound player (may not be necessary as the program closes)
        sp.closeSounds();
    }

    /**
     * Helper function for making a thread sleep.
     * @param milli The time to sleep in milli seconds (2000 milliseconds -> 2 seconds)
     */
    public static void sleep(long milli){
        try{
            Thread.sleep(milli);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
