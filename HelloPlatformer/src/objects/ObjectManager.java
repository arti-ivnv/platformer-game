package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utils.LoadSave;
import static utils.Constants.ObjectConstants.*;
import static utils.Constants.Projectiles.*;
import static utils.HelpMethods.canCannonSeePlayer;
import static utils.HelpMethods.isProjectileHittingLevel;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImages, containerImages;
    private BufferedImage[] cannonImage;
    private BufferedImage spikeImage, cannonBallImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    
    public ObjectManager(Playing playing){
        this.playing = playing;

        loadImages();

        
    }

    public void checkSpikesTouched(Player player){
        for(Spike s : spikes){
            if(s.getHitbox().intersects(player.getHitbox())){
                player.kill();
            }
        }
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox){
        for(Potion p : potions){
            if(p.isActive()){
                if(hitbox.intersects(p.getHitbox())){
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
        }
    }

    public void applyEffectToPlayer(Potion p){
        if(p.getObjectType() == RED_POTION){
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        } else {
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
        }
    }

    public void checkObjectHit(Rectangle2D.Float attackbox){

        for(GameContainer gc : containers){
            if(gc.isActive() && !gc.doAnimation){
                if(gc.getHitbox().intersects(attackbox)){
                    gc.setAnimation(true);

                    int type = 0;
                    if (gc.getObjectType() == BARREL){
                        type = 1;
                    }
                    potions.add(new Potion((int)(gc.getHitbox().x + gc.getHitbox().width / 2),
                                            (int)(gc.getHitbox().y - gc.getHitbox().height / 2), 
                                            type));
                    return;
                }
            }
        }
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

        spikeImage = LoadSave.getLevelAtlas(LoadSave.TRAP_ATLAS);

        cannonImage = new BufferedImage[7];
        BufferedImage temp = LoadSave.getLevelAtlas(LoadSave.CANNON_ATLAS);

        for (int  i = 0; i < cannonImage.length; i++){
            cannonImage[i] = temp.getSubimage(i * 40, 0, 40, 26);

        }

        cannonBallImg = LoadSave.getLevelAtlas(LoadSave.CANNON_BALL);
    }

    public void update(int[][] lvlData, Player player){

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

        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);

    }

    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p : projectiles){
            if(p.isActive()){
                p.updatePos();
                if(p.getHitbox().intersects(player.getHitbox())){
                    player.changeHealth(-25);
                    p.setActive(false);
                } else if(isProjectileHittingLevel(p, lvlData)){
                    p.setActive(false);
                }
            }
        }
    }

    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c : cannons){

            if(!c.doAnimation){
                if(c.getTileY() == player.getTileY()){
                    if(isPlayerInRange(c,player)){
                        if(isPlayerInfrontOfCannon(c, player)){
                            if(canCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY())){
                                c.setAnimation(true);
                            }
                        }
                    }
                }
            }

            c.update();
            if(c.getAnimationIndex() == 4 && c.getAnimationTick() == 0){
                shootCannon(c);
            }
        }




    }

    private void shootCannon(Cannon c) {

        int dir = 1; 
        if(c.getObjectType() == CANNON_LEFT){
            dir = -1;
        }

        projectiles.add(new Projectile((int)c.getHitbox().x, (int)c.getHitbox().y, dir));
    }

    private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
        if(c.getObjectType() == CANNON_LEFT){
            if(c.getHitbox().x > player.getHitbox().x){
                return true;
            }
        } else if(c.getHitbox().x < player.getHitbox().x){
                return true;
            }

        return false;
    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int)Math.abs(player.getHitbox().x - c.getHitbox().x);
        return absValue <= Game.TILE_SIZE * 5;
    }

    public void draw (Graphics g, int xLvlOffset ){
        drawPorions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for(Projectile p : projectiles){
            if(p.isActive()){
                g.drawImage(cannonBallImg, (int)(p.getHitbox().x - xLvlOffset) ,(int)(p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
            }
        }
    }

    private void drawCannons(Graphics g, int xLvlOffset) {

        for(Cannon c : cannons){

            int x = (int)(c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;

            if (c.getObjectType() == CANNON_RIGHT){
                x += width;
                width *= -1;
            }

            g.drawImage(cannonImage[c.animationIndex], 
                                    x, 
                                    (int)(c.getHitbox().y), 
                                    width, 
                                    CANNON_HEIGHT, 
                                    null);
        }
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s : spikes){
            g.drawImage(spikeImage, 
                        (int)(s.getHitbox().x - xLvlOffset),
                        (int)(s.getHitbox().y - s.getyDrawOffset()), 
                        SPIKE_WIDTH, 
                        SPIKE_HEIGHT, 
                        null);
        }
    }

    public void loadObjects(Level newLevel){
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
        cannons = newLevel.getCannons();
        projectiles.clear();
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


    public void resetAllObjects() {

        loadObjects(playing.getLevelManager().getCurrentLevel());
        
        for (Potion p : potions){
            p.reset();
        }

        for (GameContainer gc : containers){
            gc.reset();
        }

        for (Cannon c : cannons){
            c.reset();
        }
    }
}
