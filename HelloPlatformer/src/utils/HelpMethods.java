package utils;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;

import static utils.Constants.EnemyConstants.CRABBY;
import static utils.Constants.ObjectConstants.*;

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
        if(xSpeed > 0){
            return isSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        } else {
            return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        }
    }

    public static boolean isAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData){
        
        if(IsAllTilesClear(xStart, xEnd, y, lvlData))
            for(int i = 0; i < xEnd - xStart; i++){
                    if(!isTileSolid(xStart + i, y + 1, lvlData))
                        return false;
                    
                }
        return true;

    }

    public static boolean canCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile){
        int firstXTile = (int)(firstHitbox.x / Game.TILE_SIZE);
        int secondXTile = (int)(secondHitbox.x / Game.TILE_SIZE); 

        if (firstXTile > secondXTile){
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
        } else {
            return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
        }
    }

    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData){
          for(int i = 0; i < xEnd - xStart; i++)
                if(isTileSolid(xStart + i, y, lvlData))
                    return false;
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

        // So, we keep red color for our textures.
    // We parse an outline image and store red values.
    public static int[][] getLevelDataHelper(BufferedImage img){

        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for(int j = 0; j < img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++){
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 220){
                    value = 4;
                }
                lvlData[j][i] = value;
            }
        }

        return lvlData;
    }

    public static ArrayList<Crabby> getCrabsHelper(BufferedImage img){
    
        ArrayList<Crabby> list = new ArrayList<>();

        for(int j = 0; j < img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++){
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == CRABBY){
                    list.add(new Crabby(i * Game.TILE_SIZE, j * Game.TILE_SIZE));
                }
            }
        }

        return list;

    }

    public static Point getPlayerSpawnHelper(BufferedImage img){
        for(int j = 0; j < img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++){
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == 100){
                    return new Point(i * Game.TILE_SIZE, j * Game.TILE_SIZE);
                }
            }
        }

        return new Point(1 * Game.TILE_SIZE, 1 * Game.TILE_SIZE);
    }


    public static ArrayList<Potion> getPotionsHelper(BufferedImage img){
    
        ArrayList<Potion> list = new ArrayList<>();

        for(int j = 0; j < img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++){
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                int notValue = color.getRed();
                if ((value == RED_POTION || value == BLUE_POTION) && (notValue > 250)){
                    list.add(new Potion(i * Game.TILE_SIZE, j  * Game.TILE_SIZE, value));
                }
            }
        }

        return list;

    }

    public static ArrayList<GameContainer> getContainersHelper(BufferedImage img){
    
        ArrayList<GameContainer> list = new ArrayList<>();

        for(int j = 0; j < img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++){
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == BOX || value == BARREL){
                    list.add(new GameContainer(i * Game.TILE_SIZE, j * Game.TILE_SIZE, value));
                }
            }
        }

        return list;

    }

    public static ArrayList<Spike> getSpikesHelper(BufferedImage img) {

        ArrayList<Spike> list = new ArrayList<>();

        for(int j = 0; j < img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++){
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == SPIKE){
                    list.add(new Spike(i * Game.TILE_SIZE, j * Game.TILE_SIZE, SPIKE));
                }
            }
        }


        return list;
    }

    public static ArrayList<Cannon> getCannonHelper(BufferedImage img) {

        ArrayList<Cannon> list = new ArrayList<>();

        for(int j = 0; j < img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++){
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == CANNON_LEFT || value == CANNON_RIGHT){
                    list.add(new Cannon(i * Game.TILE_SIZE, j * Game.TILE_SIZE, value));
                }
            }
        }


        return list;
    }

}
