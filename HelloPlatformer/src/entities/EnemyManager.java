package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import utils.LoadSave;

import static utils.Constants.EnemyConstants.*;

//  GREEN color for enemies
public class EnemyManager {
    
    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImages();
        
    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
    }

    private void loadEnemyImages() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.getLevelAtlas(LoadSave.CRABY_SPRITE);

        for(int j = 0; j < crabbyArr.length; j++){
            for(int i = 0; i < crabbyArr[j].length; i++){
                crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }

    public void update(int[][] lvlData, Player player){
        boolean isAnyActives = false;

        for(Crabby c : crabbies){
            if(c.isActive()){
                c.update(lvlData, player);
                isAnyActives = true;
            }
        }

        if(!isAnyActives){
            playing.setLevelCompleted(true);
        }

    }

    public void draw(Graphics g, int xLvlOffset){
        drawCrabs(g, xLvlOffset);
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {

        for(Crabby c : crabbies){

            if(c.isActive()){

                g.drawImage(crabbyArr[c.getEnemyState()][c.getAnimationIndex()], 
                        (int)(c.getHitbox().x - CRABBY_DRAWOFFSET_X) - xLvlOffset + c.flipX(), 
                        (int)(c.getHitbox().y - CRABBY_DRAWOFFSET_Y),
                        CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
            
                // c.drawAttackBox(g, xLvlOffset);

            }
        }
        
    }

    public void checkEnemHit(Rectangle2D.Float attackBox){

        for (Crabby c : crabbies){
            if(c.isActive()){
                if(attackBox.intersects(c.getHitbox())){
                    c.hurt(10);
                    return;
                }
            }
        }

    }

    public void resetAllEnemies() {
        for( Crabby c : crabbies){
            c.resetEnemy();
        }
    }
}
