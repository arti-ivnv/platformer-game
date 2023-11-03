package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Crabby;
import main.Game;

import static utils.Constants.EnemyConstants.CRABBY;

public class LoadSave {

    public static final String LEVEL_TEXTURES = "res/lvl_sprites/oak_woods_tileset.png";
    // public static final String LEVEL_1_OUTLINE = "res/lvl_sprites/pixil-frame-0.png";
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
    public static final String STATUS_BAR = "res/player_sprites/health_power_bar.png";
    public static final String COMPLETED_IMG = "res/ui_sprites/completed_sprite.png";
    public static final String TRAP_ATLAS = "res/lvl_sprites/trap_atlas.png";
    public static final String CANNON_ATLAS = "res/cannon_atlas.png";

    public static final String POTION_ATLAS = "res/potions_sprites.png";
    public static final String CONTAINER_ATLAS = "res/objects_sprites.png";


    public static BufferedImage[] getPlayerAtlas(){

        BufferedImage[] playerAnimation = new BufferedImage[7];

        InputStream isIdle = LoadSave.class.getResourceAsStream("/res/player_sprites/idle.png"); // From 0 - 3
        InputStream isRun = LoadSave.class.getResourceAsStream("/res/player_sprites/run.png"); // From 0 - 5
        InputStream isAttack = LoadSave.class.getResourceAsStream("/res/player_sprites/attack.png"); // From 0 - 3
        InputStream isJumpingOrFalling = LoadSave.class.getResourceAsStream("/res/player_sprites/jump.png"); // From 0 - 7
        InputStream isJumpingOrFalling2 = LoadSave.class.getResourceAsStream("/res/player_sprites/jump.png"); // From 0 - 3
        InputStream isHurt = LoadSave.class.getResourceAsStream("/res/player_sprites/hurt.png"); // From 0 - 3
        InputStream isDeath = LoadSave.class.getResourceAsStream("/res/player_sprites/death.png"); // From 0 - 7

        try {

            playerAnimation[0] = ImageIO.read(isIdle);
            playerAnimation[1] = ImageIO.read(isAttack);
            playerAnimation[2] = ImageIO.read(isRun);
            playerAnimation[3] = ImageIO.read(isJumpingOrFalling);
            playerAnimation[4] = ImageIO.read(isJumpingOrFalling2);
            playerAnimation[5] = ImageIO.read(isHurt);
            playerAnimation[6] = ImageIO.read(isDeath);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                isIdle.close(); 
                isRun.close();
                isAttack.close();
                isJumpingOrFalling.close();
                isJumpingOrFalling2.close();
                isHurt.close();
                isDeath.close();
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


    // URL - location to the resource
    // URI - 
    public static BufferedImage[] getAllLevels(){
        URL url = LoadSave.class.getResource("/res/levels");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];


        for (int i = 0; i < filesSorted.length; i++){
            for (int j = 0; j < files.length; j++){
                if(files[j].getName().equals((i + 1) + ".png")){
                    filesSorted[i] = files[j];
                }
            }
        }


        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for(int i = 0; i < imgs.length; i++){
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return imgs;
    }



}
