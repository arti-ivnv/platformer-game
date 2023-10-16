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
        int maxLevelWidth = lvlData[0].length * Game.TILE_SIZE;
        
        
        if (x < 0 || x >= maxLevelWidth)
            return true;

        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILE_SIZE;
        float yIndex = y / Game.TILE_SIZE;

        return isTileSolid((int)xIndex, (int)yIndex, lvlData);

    }

    public static boolean isTileSolid(int xTile, int yTile, int[][] lvlData){
        int value  = lvlData[yTile][xTile];

        if (value >= 280 || value < 0 || value != 4){
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

        if(airSpeed > 0){
            // Falling - Touching Floor
            int tileYPosition = currentTile * Game.TILE_SIZE;
            int yOffset = (int)(Game.TILE_SIZE - hitbox.height);
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

    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData){
        return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean isAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData){
        for(int i = 0; i < xEnd - xStart; i++){
                if(isTileSolid(xStart + i, y, lvlData))
                    return false;
                if(!isTileSolid(xStart + i, y + 1, lvlData))
                    return false;
                
            }
        return true;

    }

    public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile){
        int firstXTile = (int)(firstHitbox.x / Game.TILE_SIZE);
        int secondXTile = (int)(secondHitbox.x / Game.TILE_SIZE); 

        if (firstXTile > secondXTile){
            return isAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        } else {
            return isAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
        }
    }
}
