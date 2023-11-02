package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utils.HelpMethods;

import static utils.HelpMethods.getLevelDataHelper;
import static utils.HelpMethods.getCrabsHelper;
import static utils.HelpMethods.getPlayerSpawnHelper;

// Keeps the data about level
public class Level {

    private BufferedImage img;
    private ArrayList<Crabby> crabs;
    private ArrayList<Potion> potions;
    private ArrayList<Spike> spikes;
    private ArrayList<GameContainer> containers;
    private int[][] lvlData;

    private int lvlTilesWide;
    // amounts of tiles we have - amount we can see 
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createContainers();
        createSpikes();
        calcLevelOffsets();
        calcPlayerSpawn();
    }

    private void createSpikes() {
        spikes = HelpMethods.getSpikesHelper(img);
    }

    private void createContainers() {
        containers = HelpMethods.getContainersHelper(img);
    }

    private void createPotions() {
        potions = HelpMethods.getPotionsHelper(img);
    }

    private void calcPlayerSpawn() {
        playerSpawn = getPlayerSpawnHelper(img);
    }

    private void calcLevelOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLevelOffsetX = Game.TILE_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = getCrabsHelper(img);
    }

    private void createLevelData() {
        lvlData = getLevelDataHelper(img);

    }

    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }

    public int[][] getLevelData(){
        return lvlData;
    }

    public int getLvlOffset(){
        return maxLevelOffsetX;
    }

    public ArrayList<Crabby> getCrabs(){
        return crabs;
    }

    public Point getPlayerSpawn(){
        return playerSpawn;
    }

    public ArrayList<Potion> getPotions(){
        return potions;
    }

    public ArrayList<GameContainer> getContainers(){
        return containers;
    }

    public ArrayList<Spike> getSpikes(){
        return spikes;
    }



    
}
