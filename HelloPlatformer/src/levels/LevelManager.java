package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utils.LoadSave;

public class LevelManager {
    
    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.getLevelData());
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.getLevelAtlas(LoadSave.LEVEL_TEXTURES);
        levelSprite = new BufferedImage[280];

        for (int j = 0; j < 14; j++){
            for (int i = 0; i < 20; i++){
                int index = j * 20 + i;
                levelSprite[index] = img.getSubimage(i * 24, j * 24, 24, 24);

            }

        }

        

    }

    public void draw(Graphics g){

        for(int j = 0; j < Game.TILES_IN_HEIGHT; j++){
            for (int i = 0; i < Game.TILES_IN_WIDTH; i++){
                int  index = levelOne.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i* Game.TILE_SIZE, j * Game.TILE_SIZE , Game.TILE_SIZE, Game.TILE_SIZE, null);
            }
        }

        // g.drawImage(levelSprite[4], 0, 0, null);
        // g.drawRect(0, 0, 32, 32);
    }

    public void update(){
    }

    public Level getCurrentLevel(){
        return levelOne;
    }
}
