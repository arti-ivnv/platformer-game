package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Crabby;
import main.Game;

import static utils.Constants.EnemyConstants.CRABBY;

public class LoadSave {

    public static final String LEVEL_TEXTURES = "res/lvl_sprites/oak_woods_tileset.png";
    // public static final String LEVEL_1_OUTLINE = "res/lvl_sprites/pixil-frame-0.png";
    public static final String LEVEL_1_OUTLINE = "res/lvl_sprites/pixil-frame-2.png";
    public static final String MENU_BUTTONS = "res/ui_sprites/button_atlas.png";
    public static final String MENU_BACKGROUND = "res/ui_sprites/menu_background.png";
    public static final String PAUSE_BACKGROUND = "res/ui_sprites/pause_menu.png";
    public static final String SOUND_BUTTONS = "res/ui_sprites/sound_button.png";
    public static final String URM_BUTTONS = "res/ui_sprites/urm_buttons.png";
    public static final String VOLUME_BUTTONS = "res/ui_sprites/volume_buttons.png";
    public static final String MENU_BACKGROUND_IMG = "res/lvl_sprites/background_menu.png";
    public static final String PLAYING_BG_IMG = "res/lvl_sprites/playing_bg_img.png";
    public static final String BIG_CLOUDS = "res/lvl_sprites/big_clouds.png";
    public static final String SMALL_CLOUDS = "res/lvl_sprites/small_clouds.png";
    public static final String CRABY_SPRITE = "res/crabby_sprite.png";

    public static BufferedImage[] getPlayerAtlas(){

        BufferedImage[] playerAnimation = new BufferedImage[5];

        InputStream isIdle = LoadSave.class.getResourceAsStream("/res/player_sprites/idle.png"); // From 0 - 3
        InputStream isRun = LoadSave.class.getResourceAsStream("/res/player_sprites/run.png"); // From 0 - 5
        InputStream isAttack = LoadSave.class.getResourceAsStream("/res/player_sprites/attack.png"); // From 0 - 3
        InputStream isJumpingOrFalling = LoadSave.class.getResourceAsStream("/res/player_sprites/jump.png"); // From 0 - 7
        InputStream isJumpingOrFalling2 = LoadSave.class.getResourceAsStream("/res/player_sprites/jump.png"); // From 0 - 3
        try {

            playerAnimation[0] = ImageIO.read(isIdle);
            playerAnimation[1] = ImageIO.read(isAttack);
            playerAnimation[2] = ImageIO.read(isRun);
            playerAnimation[3] = ImageIO.read(isJumpingOrFalling);
            playerAnimation[4] = ImageIO.read(isJumpingOrFalling2);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                isIdle.close(); 
                isRun.close();
                isAttack.close();
                isJumpingOrFalling.close();
                isJumpingOrFalling2.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        return playerAnimation;
    }

    public static BufferedImage getLevelAtlas(String filenme){
        BufferedImage lvl = null;

        InputStream isLvl = LoadSave.class.getResourceAsStream("/" + filenme);

        try{
            lvl = ImageIO.read(isLvl);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                isLvl.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        return lvl;
    }

    public static ArrayList<Crabby> getCrabs(){
        BufferedImage img = getLevelAtlas(LEVEL_1_OUTLINE);
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

    // So, we keep red color for our textures.
    // We parse an outline image and store red values.
    public static int[][] getLevelData(){

        BufferedImage img = getLevelAtlas(LEVEL_1_OUTLINE);
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for(int j = 0; j < img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++){
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 280){
                    value = 0;
                }
                lvlData[j][i] = value;
            }
        }

        return lvlData;
    }


}
