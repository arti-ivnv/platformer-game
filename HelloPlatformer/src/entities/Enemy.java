package entities;

import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.*;
import static utils.Constants.Directions.*;

import main.Game;

public abstract class Enemy extends Entity {

    private int animationIndex, enemyState, enemyType;
    private int animationTick, animationSpeed = 25; 
    private boolean firstUpdate = true;
    private boolean inAir = false;
    private float fallSpeed;
    private float gravity = 0.04f * Game.SCALE;
    private float walkSpeed = 0.35f * Game.SCALE;
    private int walkDirection = LEFT;


    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    private void updateAnimarionTick(){
        animationTick++;

        if(animationTick  >= animationSpeed){
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= getSpriteAmount(enemyType, enemyState)){
                animationIndex = 0;
            }
        }
    }

    public void update(int[][] lvlData){
        updateMove(lvlData);
        updateAnimarionTick();
    }

    private void updateMove(int[][] lvlData){
        if(firstUpdate){
            if(!isEntityOnFloor(hitbox, lvlData)){
                inAir = true;
            }

            firstUpdate = false;
        }
        if(inAir){
            if(canMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            }else{
                inAir = false;
                hitbox.y = getEntityYPositionUnderRoofOrAboveFloor(hitbox, fallSpeed);
            }
        }else{
            switch(enemyState){
                case IDLE:
                    enemyState = RUNNING;
                    break;
                case RUNNING:
                    float xSpeed = 0;
                    if(walkDirection == LEFT){
                        xSpeed = -walkSpeed;
                    } else {
                        xSpeed = walkSpeed;
                    }
                    
                    if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
                        if(isFloor(hitbox, xSpeed,lvlData)){
                            hitbox.x += xSpeed;
                            return;
                        }
                    }
                    changeWalkDirection();
                    break;
            }
        }
    }

    private void changeWalkDirection() {
        if(walkDirection == LEFT){
            walkDirection = RIGHT;
        } else {
            walkDirection = LEFT;
        }
    }

    public int getAnimationIndex(){
        return animationIndex;
    }

    public int getEnemyState(){
        return enemyState;
    }
    
}
