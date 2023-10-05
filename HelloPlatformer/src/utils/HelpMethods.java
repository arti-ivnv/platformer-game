package utils;

import main.Game;

public class HelpMethods {
    
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] lvlData ){
        if (!isSolid(x, y, lvlData))
            if(!isSolid(x+width, y+height, lvlData))
                if(!isSolid(x+width, y, lvlData))
                    if(!isSolid(x, y+height, lvlData))
                        return true;
        
        return false;
    }

    private static boolean isSolid(float x, float y, int[][] lvlData){
        if (x < 0 || x >= Game.GAME_WIDTH)
            return true;

        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILE_SIZE;
        float yIndex = y / Game.TILE_SIZE;

        int value  = lvlData[(int)yIndex][(int)xIndex];

        if (value >= 280 || value < 0 || value != 4){

            return true;

        }

        return false;


    }
}
