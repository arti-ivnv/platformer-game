package audio;

import java.util.Random;

import javax.sound.sampled.Clip;

public class AudioPlayer {
    
    // Songs
    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;

    // Effects
    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;
    public static int ATTACK_ONE = 4;
    public static int ATTACK_TWO = 5;
    public static int ATTACK_THREE = 6;

    // In personal projects it's better to use external libs instead of Clip
    // Cons:
    //      1. Possible delay
    //      2. Only works with wav
    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 0.5f;
    private boolean songMute, effectMute;
    // Attack sounds
    private Random rand = new Random();


    public AudioPlayer(){

    }


}
