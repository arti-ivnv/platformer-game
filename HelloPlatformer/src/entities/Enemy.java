package entities;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.Directions.*;
import static utils.HelpMethods.*;

import java.awt.geom.Rectangle2D.Float;

import main.Game;

public abstract class Enemy extends Entity {

    protected int animationIndex, enemyState, enemyType;
    private int animationTick, animationSpeed = 25; 
    protected boolean firstUpdate = true;
    protected boolean inAir = false;
    protected float fallSpeed;
    protected float gravity = 0.04f * Game.SCALE;
    protected float walkSpeed = 0.35f * Game.SCALE;
    protected int walkDirection = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILE_SIZE;



    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    protected void firstUpdateChheck(int[][] lvlData){
        if(!isEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    protected void updateInAir(int[][] lvlData){
        if(canMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        }else{
            inAir = false;
            hitbox.y = getEntityYPositionUnderRoofOrAboveFloor(hitbox, fallSpeed);
            tileY = (int)(hitbox.y / Game.TILE_SIZE);
        }
    }

    protected void move(int[][] lvlData){
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
    }

    protected boolean canSeePlayer(int[][]lvlData, Player player){
        int playerTileY = (int) (player.hitbox.y / Game.TILE_SIZE);
        
        if (playerTileY == tileY){
            if (isPlayerInRange(player)){
                if(isSightClear(lvlData, hitbox, player.hitbox,tileY)){
                    return true;
                }
            }
        }

        return false;
        
    }

    protected void turnTowardsPlayer(Player player){
        if(player.hitbox.x > hitbox.x){
            walkDirection = RIGHT;
        } else {
            walkDirection = LEFT;
        }
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseToAttack(Player player){
        int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance;
    }

    // When changing a state also reset animation
    protected void newState(int enemyState){
        this.enemyState = enemyState;
        animationIndex = 0;
        animationTick = 0;
    }

    protected void updateAnimarionTick(){
        animationTick++;

        if(animationTick  >= animationSpeed){
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= getSpriteAmount(enemyType, enemyState)){
                animationIndex = 0;
                if(enemyState == ATTACK){
                    enemyState = IDLE;
                }
            }
        }
    }


    protected void changeWalkDirection() {
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
