package levels;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;

// Keeps the data about level
public class Level {

    private BufferedImage img;
    private ArrayList<Crabby> crabs;
    
    
    private int[][] lvlData;

    public Level(int[][] lvlData){
        this.lvlData = lvlData;
    }

    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }

    public int[][] getLevelData(){
        return lvlData;
    }

    
}
