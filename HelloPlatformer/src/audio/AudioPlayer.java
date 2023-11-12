package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
    private float volume = 1f;
    private boolean songMute, effectMute;
    // Attack sounds
    private Random rand = new Random();


    public AudioPlayer(){
        loadSongs();
        loadEffects();
        playSong(MENU_1);
    }

    public void loadSongs(){

        String[] names = {"menu", "level1", "level2"};
        songs = new Clip[names.length];

        for(int i = 0; i < songs.length; i++){
            songs[i] = getClip(names[i]);
        }
    }
    
    public void loadEffects(){
        String[] effectNames = {"die", "jump", "gameover","lvlcompleted", "attack1", "attack2","attack3"};
        effects = new Clip[effectNames.length];

        for(int i = 0; i < effects.length; i++){
            effects[i] = getClip(effectNames[i]);
            // System.out.println("["+ i +"] effect: " + effects[i]);
        }


        updateEffectVolume();
    }

    private Clip getClip(String name){
        URL url = getClass().getResource("/res/audio/" + name +".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);


            audio.close();
        return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setVolume(float volume){
        this.volume = volume;
        updateSongVolume();
        updateEffectVolume();
    }

    public void stopSong(){
        if(songs[currentSongId].isActive()){
            songs[currentSongId].stop();
        }
    }

    public void setLevelSong(int lvlIndex){
        if(lvlIndex % 2 == 0){
            playSong(LEVEL_1);
        } else {
            playSong(LEVEL_2);
        }
    }

    public void lvlCompleted(){
        stopSong();
        playSong(LVL_COMPLETED);
    }

    public void toggleSongMute(){
        this.songMute = !songMute;
        for(Clip c : songs){
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void playAttackSound(){
        int start = 4;
        start += rand.nextInt(3);
        playEffect(start);
    }

    public void playEffect(int effect){
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }
    public void playSong(int song){
        stopSong();

        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void toggleEffectMute(){
        this.effectMute = !effectMute;
        for(Clip c : effects){
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
        if(!effectMute){
            System.out.println("yoonk");
            playEffect(JUMP);
        }
    }

    private void updateSongVolume(){
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    private void updateEffectVolume(){
        // We need to update all the effects
        for (Clip c : effects){
            FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }




}
