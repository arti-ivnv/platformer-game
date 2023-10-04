package main;

import java.awt.Graphics;

import entities.Player;

public class Game implements Runnable{
    
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Player player;

    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();


        // The most last thing
        startGameLoop();
    }

    // Initializer for starter classes (Player, Enemies, etc)
    private void initClasses() {
        player = new Player (200, 200);
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    //  Update  level, player, enemy, etc
    public void update(){
        player.update();
    }

    public void render(Graphics g){
        player.render(g);

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


    public Player getPlayer(){
        return player;
    }

    public void windowFocusLost() {
        player.resetDirectionBooleans();
    }
}
