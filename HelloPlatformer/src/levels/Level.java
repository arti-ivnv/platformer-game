package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import static utils.HelpMethods.getLevelDataHelper;
import static utils.HelpMethods.getCrabsHelper;
import static utils.HelpMethods.getPlayerSpawnHelper;

// Keeps the data about level
public class Level {

    private BufferedImage img;
    private ArrayList<Crabby> crabs;
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
        calcLevelOffsets();
        calcPlayerSpawn();
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



    
}
