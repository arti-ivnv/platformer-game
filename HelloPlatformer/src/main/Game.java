package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;
import utils.LoadSave;

public class Game implements Runnable{
    
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;


    // one tile is 32x32
    public final static int TILES_DEFAULT_SIZE = 32;
    // how much should we scale everything (player, enemies, level, etc)
    // always try to adjast a round number for tiles size
    public final static float SCALE = 2.0f;
    // how many tiles in width do we want
    public final static int TILES_IN_WIDTH = 26; // visible
    // how many tiles in height do we want
    public final static int TILES_IN_HEIGHT = 14; // visible
    public final static int TILE_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILE_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILE_SIZE * TILES_IN_HEIGHT;


    public Game() {

        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();


        // The most last thing
        startGameLoop();
    }

    // Initializer for starter classes (Player, Enemies, etc)
    private void initClasses() {
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        gameOptions = new GameOptions(this);
        
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    //  Update  level, player, enemy, etc
    public void update(){

        switch (Gamestate.state){
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
                gameOptions.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g){

        switch (Gamestate.state){
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            case OPTIONS:
                gameOptions.draw(g);
                break;
            default:
                break;
        }

    }

    // Game Loop Executable code
    @Override
    public void run() {
        
        // We are using nanoseconds since it's more accurate
        double timePerFrame = 1_000_000_000.0 / FPS_SET; 
        double timePerUpdate = 1_000_000_000.0 / UPS_SET;
        long previousTime = System.nanoTime();

        // Variables for FPS check
        int frames = 0;
        long lastCheck = System.currentTimeMillis();

        // Variable for UPS check
        int updates = 0;

        double deltaU = 0;
        double deltaF = 0;


        while(true){
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            //  This choice is better because when we are deltaU - 1 we still keep some extra time 0.1...
            // Updates take care about canging the states (positionining, etc)
            if(deltaU >= 1){
                update();
                updates++;
                deltaU--;
            }

            // Frames take care about repainting
            if (deltaF >= 1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            // Taking current time subtract last time and if it's >= than timePerFrame, it means we should draw again
            // However, if it is > than it means we are losing some frames beyond > 
            // if (now - lastFrame >= timePerFrame){
            //     gamePanel.repaint();
            //     lastFrame = now;
            //     frames++;
            // }

            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " UPS: " + updates);
                frames = 0;
                updates = 0;

            }
        }
    }


    public void windowFocusLost() {
        if(Gamestate.state == Gamestate.PLAYING){
            playing.getPlayer().resetDirectionBooleans();
        }
    }


    public Menu getMenu(){
        return menu;
    }
    

    public Playing getPlaying(){
        return playing;
    }

    public GameOptions getGameOptions(){
        return gameOptions;
    }

    public AudioOptions getAudioOptions(){
        return audioOptions;
    }

    public AudioPlayer getAudioPlayer(){
        return audioPlayer;
    }
}
