package entities;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.Directions.*;
import static utils.HelpMethods.*;
import static utils.Constants.GRAVITY;
import static utils.Constants.ANI_SPEED;

import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Enemy extends Entity {

    protected int enemyType;
    protected boolean firstUpdate = true;
    protected float walkSpeed = 0.35f * Game.SCALE;
    protected int walkDirection = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILE_SIZE;

    protected boolean active = true;
    protected boolean attackChecked;




    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = getMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }

    protected void firstUpdateChheck(int[][] lvlData){
        if(!isEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    protected void updateInAir(int[][] lvlData){
        if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        }else{
            inAir = false;
            hitbox.y = getEntityYPositionUnderRoofOrAboveFloor(hitbox, airSpeed);
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
        this.state = enemyState;
        animationIndex = 0;
        animationTick = 0;
    }

    public void hurt(int damage){
        currentHealth -= damage;

        if(currentHealth <= 0){
            newState(DEAD);
        } else {
            newState(HIT);
        }
    }

    protected void checkEnemyHit(Rectangle2D.Float attackBox,Player player) {
        if (attackBox.intersects(player.hitbox)){
            player.changeHealth(-getEnemyDamage(enemyType));
        }
        attackChecked = true;
    }


    protected void updateAnimarionTick(){
        animationTick++;

        if(animationTick  >= ANI_SPEED){
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= getSpriteAmount(enemyType, state)){
                animationIndex = 0;

                switch(state){
                    case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
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


    public boolean isActive(){
        return active;
    }

    public void resetEnemy(){
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
    }
    
}
