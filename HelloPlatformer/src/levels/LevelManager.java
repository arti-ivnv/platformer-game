package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Gamestate;
import main.Game;
import utils.LoadSave;

public class LevelManager {
    
    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.getAllLevels();

        for(BufferedImage img : allLevels){
            levels.add(new Level(img));
        }
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

    public void draw(Graphics g, int lvlOffset){

        for(int j = 0; j < Game.TILES_IN_HEIGHT; j++){
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++){
                int  index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i* Game.TILE_SIZE - lvlOffset, j * Game.TILE_SIZE , Game.TILE_SIZE, Game.TILE_SIZE, null);
            }
        }

    }

    public void update(){
    }

    public Level getCurrentLevel(){
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels(){
        return levels.size();
    }

    public void loadNextLevel() {
        lvlIndex++;
        
        if(lvlIndex >= levels.size()){
            lvlIndex = 0;
            System.out.println("No more levels!!");
            Gamestate.state = Gamestate.MENU;
        }

        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLevelData(newLevel.getLevelData());
        game.getPlaying().setLvlOfmaxLevelOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
        
    }

    public int getLvlIngex(){
        return lvlIndex;
    }
}
