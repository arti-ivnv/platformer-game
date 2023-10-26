package entities;
import static utils.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import static utils.Constants.Directions.*;

import main.Game;
public class Crabby extends Enemy {

    // Attack hitbox
    private int attackBoxOffsetX;

    public Crabby(float x, float y) {

        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(22, 19);
        initAttackBox();


    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int)(19 * Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE * 30);
    }

    public void update(int[][] lvlData, Player player){
        updateBehavior(lvlData, player);
        updateAnimarionTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x =  hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvlData, Player player){
        if(firstUpdate){
            firstUpdateChheck(lvlData);

            firstUpdate = false;
        }
        if(inAir){
            updateInAir(lvlData);
        }else{
            switch(state){
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if(canSeePlayer(lvlData, player)){
                        turnTowardsPlayer(player);
                    
                        if(isPlayerCloseToAttack(player)){
                        newState(ATTACK);
                        }
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if(animationIndex == 0){
                        attackChecked = false;
                    }
                    if(animationIndex == 3 && !attackChecked){
                        checkEnemyHit(attackBox,player);
                    }
                    break;
                case HIT:
                    break;

            }
        }
    }

    public int flipX(){
        if(walkDirection == RIGHT){
            return width;
        } else {
            return 0;
        }
    }

    public int flipW(){
        if(walkDirection == RIGHT){
            return -1;
        } else {
            return 1;
        }
    }

    
}
