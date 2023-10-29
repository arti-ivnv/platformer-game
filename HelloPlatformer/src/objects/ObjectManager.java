package objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import utils.LoadSave;
import static utils.Constants.ObjectConstants.*;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImages, containerImages;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    
    public ObjectManager(Playing playing){
        this.playing = playing;

        loadImages();

        potions = new ArrayList<>();
        potions.add(new Potion(300,300, RED_POTION));
        potions.add(new Potion(400,300, BLUE_POTION));

        containers = new ArrayList<>();
        containers.add(new GameContainer(500, 300, BARREL));
        containers.add(new GameContainer(600, 300, BOX));
        
    }

    private void loadImages() {
        BufferedImage potionSprite = LoadSave.getLevelAtlas(LoadSave.POTION_ATLAS);
        potionImages = new BufferedImage[2][7];

        for (int j = 0; j < potionImages.length; j++){
            for (int i = 0; i < potionImages[j].length; i++){
                potionImages[j][i] = potionSprite.getSubimage(12*i, 16*j, 12, 16);
            }
        }

        BufferedImage containerSprite = LoadSave.getLevelAtlas(LoadSave.CONTAINER_ATLAS);
        containerImages = new BufferedImage[2][8];

        for (int j = 0; j < containerImages.length; j++){
            for (int i = 0; i < containerImages[j].length; i++){
                containerImages[j][i] = containerSprite.getSubimage(40*i, 30*j, 40, 30);
            }
        }
    }

    public void update(){

        for (Potion p : potions){
            if(p.isActive()){
                p.update();
            }
        }

        for (GameContainer gc : containers){
            if(gc.isActive()){
                gc.update();
            }
        }

    }

    public void draw (Graphics g, int xLvlOffset ){
        drawPorions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers){
            if(gc.isActive()){
                int type = 0;

                if(gc.getObjectType() ==  BARREL){
                    type = 1;
                } 

                g.drawImage(containerImages[type][gc.getAnimationIndex()], 
                                (int)(gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset), 
                                (int)(gc.getHitbox().y - gc.getyDrawOffset()),
                                CONTAINER_WIDTH,
                                CONTAINER_HEIGHT,
                                null);
            }
        }
    }

    private void drawPorions(Graphics g, int xLvlOffset) {

        for (Potion p : potions){
            if(p.isActive()){
                int type = 0;
                if(p.getObjectType() == RED_POTION){
                    type = 1;
                }

                g.drawImage(potionImages[type][p.getAnimationIndex()], 
                                (int)(p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), 
                                (int)(p.getHitbox().y - p.getyDrawOffset()),
                                POTION_WIDTH,
                                POTION_HEIGHT,
                                null);

            }
        }

    }
}
