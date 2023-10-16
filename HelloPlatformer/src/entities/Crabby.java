package entities;
import static utils.Constants.EnemyConstants.*;

import main.Game;
public class Crabby extends Enemy {

    public Crabby(float x, float y) {

        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int)(22*Game.SCALE), (int)(19*Game.SCALE));


    }

    public void update(int[][] lvlData, Player player){
        updateMove(lvlData, player);
        updateAnimarionTick();
    }

    private void updateMove(int[][] lvlData, Player player){
        if(firstUpdate){
            firstUpdateChheck(lvlData);

            firstUpdate = false;
        }
        if(inAir){
            updateInAir(lvlData);
        }else{
            switch(enemyState){
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if(canSeePlayer(lvlData, player)){
                        turnTowardsPlayer(player);
                    }
                    if(isPlayerCloseToAttack(player)){
                       newState(ATTACK);
                    }
                    move(lvlData);
                    break;
            }
        }
    }
    
}
