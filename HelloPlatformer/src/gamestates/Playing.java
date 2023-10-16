package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.PauseOverlay;
import utils.LoadSave;
import static utils.Constants.Environment.*;

public class Playing extends State implements Statemethods{

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;

    // Decides either the game is paused or not.
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int lvlTilesWide = LoadSave.getLevelData()[0].length;
    // amounts of tiles we have - amount we can see 
    private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
    private int maxLevelOffsetX = maxTilesOffset * Game.TILE_SIZE;

    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudsPos;
    private Random rnd = new Random();


    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg = LoadSave.getLevelAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.getLevelAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.getLevelAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for(int i = 0; i < smallCloudsPos.length; i++){
            // 90 is the smallest pos we can get
            // rand between 0 - 100
            smallCloudsPos[i] = (int)(90 * Game.SCALE) + rnd.nextInt((int)(100 * Game.SCALE));
        }
    }
    

    // Initializer for starter classes (Player, Enemies, etc)
    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player (200, 200, (int)(32 * Game.SCALE), (int)(32 * Game.SCALE));
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);
    }


    @Override
    public void update() {
        if(!paused){
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        } else {
            pauseOverlay.update();
        }
    }


    private void checkCloseToBorder() {

        int playerX = (int)(player.getHitbox().x);

        // if this difference is > than right border
        // then we know the player is beyond the rigt border and we need to move the level right
        // same for the left case
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder){
            xLvlOffset += diff - rightBorder;
        } else if (diff < leftBorder){
            xLvlOffset += diff - leftBorder;
        }

        if(xLvlOffset > maxLevelOffsetX){
            xLvlOffset = maxLevelOffsetX;
        } else if (xLvlOffset < 0){
            xLvlOffset = 0;
        }
    }


    @Override
    public void draw(Graphics g) {
        
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        drawClouds(g);

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);

        if(paused){
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }

    }

    private void drawClouds(Graphics g) {
        for(int i = 0; i < 3; i++){
             g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int)(xLvlOffset * 0.3), (int)(204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HIGHT, null);
        }

        for(int i = 0; i < smallCloudsPos.length; i++){
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int)(xLvlOffset * 0.7), smallCloudsPos[i] , SMALL_CLOUD_WIDTH, SMALL_CLOUD_HIGHT,null);
        }
    }


    public void mouseDragged(MouseEvent e) {
        if(paused){
            pauseOverlay.mouseDragged(e);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            player.setAttacking(true);
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {
        if(paused){
            pauseOverlay.mousePressed(e);
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        if(paused){
            pauseOverlay.mouseReleased(e);
        }
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        if(paused){
            pauseOverlay.mouseMoved(e);
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
               player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                break;
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }

    public void unpauseGame(){
        paused = false;
    }


    public Player getPlayer(){
        return player;
    }

    public void windowFocusLost() {
        player.resetDirectionBooleans();
    }

}
