package utils;

import java.awt.geom.Rectangle2D;

import main.Game;

public class HelpMethods {
    
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] lvlData ){
        if (!isSolid(x, y, lvlData))
        {
            if(!isSolid(x+width, y+height, lvlData))
            {
                if(!isSolid(x+width, y, lvlData))
                {
                    if(!isSolid(x, y+height, lvlData)){
                        return true;
                    }
                }
            }
        }
        
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

            System.out.println("isSolid value: " + value);

            return true;

        }

        return false;


    }

    public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){

        int currentTile = (int)(hitbox.x / Game.TILE_SIZE);

        if(xSpeed > 0){
            // right
            int tileXPosition = currentTile * Game.TILE_SIZE;
            int xOffset = (int)(Game.TILE_SIZE - hitbox.width);
            return tileXPosition + xOffset - 1;
        } else {
            // left
            return currentTile * Game.TILE_SIZE;
        }

    }

    public static float getEntityYPositionUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed){
        int currentTile = (int)(hitbox.y / Game.TILE_SIZE);

        System.out.println("hitbox.y: " + hitbox.y);

        if(airSpeed > 0){
            // Falling - Touching Floor
            int tileYPosition = currentTile * Game.TILE_SIZE;
            int yOffset = (int)(Game.TILE_SIZE - hitbox.height);
            System.out.println("hitbox.height: " + hitbox.height);
            System.out.println("currentTile: " + currentTile);
            System.out.println("tileYPosition: " + tileYPosition);
            System.out.println("yOffset: " + yOffset);
            System.out.println("position y: " + (tileYPosition + yOffset - 1));
            return tileYPosition + yOffset - 1;
        } else {
            // Jumping
            return currentTile * Game.TILE_SIZE;
        }
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        // Check the pixels bellow bottom_left and bottom_rigt
        if(!isSolid(hitbox.x, hitbox.y+hitbox.height + 1, lvlData))
            if (!isSolid(hitbox.x + hitbox.width, hitbox.y+hitbox.height + 1, lvlData))
                return false;
        
        return true;
    }
}
