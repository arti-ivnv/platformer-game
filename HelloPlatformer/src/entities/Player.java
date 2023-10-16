package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.Game;
import utils.LoadSave;

import static utils.Constants.PlaerConstants.*;
import static utils.HelpMethods.*;

public class Player extends Entity{

    private BufferedImage[][] animations;
    // Lower the animationSpeed value -> faster animation (120 / 4 = 30)
    private int animationTick, animationIndex, animationSpeed = 20;

    // Field for action animations (IDLE by default)
    private int playerAction = IDLE;

    private boolean up, left, down, right, jump; 

    private float playerSpeed = 1.0f *  Game.SCALE;
    // Moving flag
    private boolean moving = false, attacking = false;

    private int[][] lvlData;

    private float xDrawOffset = 10 * Game.SCALE;
    private float yDrawOffset = 7 * Game.SCALE;

    // Jumping and Gravity
    private float airSpeed = 0f;
    private float gravity = 0.08f * Game.SCALE;
    // Negative because we are going y axis direction up;
    private float jumpSpeed = -3.45f * Game.SCALE;
    private float fallSpeedAfterColision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    // Status Bar UI

    private BufferedImage statusBarImage;

    // Status Bar position
    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    // Sub bar (health  indicator)
    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarX = (int) (34 * Game.SCALE);
    private int healthBarY = (int) (14 * Game.SCALE);

    private int maxHealth = 100; 
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    // Attack hitbox
    private Rectangle2D.Float attackBox;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, (int) (11 * Game.SCALE), (int) (24 * Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x , y, (int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    // Player logic states
    public void update(){

        updateHealthBar();
        updateAttackBox();

        updatePosition();
        updateAnimationTick();
        setAnimation();
    }

    private void updateAttackBox() {
        if(right){
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10) - 30;
        }else if(left){
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10) - 30;
        }
        attackBox.y = hitbox.y + (int)(Game.SCALE * 10) - 10;
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    // Drawing
    public void render(Graphics g, int lvlOffset){
        g.drawImage(animations[playerAction][animationIndex], (int)(hitbox.x - xDrawOffset) - lvlOffset, (int)(hitbox.y - yDrawOffset), width, height, null);
        // drawHitbox(g, lvlOffset);
        drawAttackBox(g, lvlOffset);
        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.RED);
        g.drawRect((int)attackBox.x - lvlOffsetX, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImage, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarX + statusBarX, healthBarY + statusBarY, healthWidth, healthBarHeight);

    }

    private void updatePosition(){

        moving = false;
        
        if (jump)
            jump();

        // if(!left && !right && !inAir){
        //     return;
        // }
        
        if(!inAir){
            if((!left && !right) || (right && left)){
                return;
            }
        }

        float xSpeed = 0;

        if (left){
            xSpeed -= playerSpeed;
        } 
        if (right){
            xSpeed += playerSpeed;
        }

        if(!inAir){
            if(!isEntityOnFloor(hitbox, lvlData)){
                inAir = true;
            }
        }

        if(inAir){
            if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                undateXPos(xSpeed);
            } else {
                hitbox.y = getEntityYPositionUnderRoofOrAboveFloor(hitbox, airSpeed);
                if(airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterColision;
                undateXPos(xSpeed);
            }
        } else {
            undateXPos(xSpeed);
        }
        moving = true;
        
    }


    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void undateXPos(float xSpeed) {

        if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            hitbox.x += xSpeed;
        } else {
            hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
        }

    }

    public void changeHealth(int value){
        currentHealth += value;

        if (currentHealth <= 0){
            currentHealth = 0;
            // gameOver();
        } else if(currentHealth >= maxHealth){
            currentHealth = maxHealth; 
        }
    }

    private void updateAnimationTick() {
       
        animationTick++;

        if (animationTick >= animationSpeed){

            animationTick = 0;
            animationIndex++;

            if (animationIndex >= getSpriteAmount(playerAction)){
                animationIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {

        int startAnimation = playerAction;

        if(moving){
           playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if(inAir){
            playerAction = JUMP;
            
            // NOT WORKING
            // if (airSpeed < 0){
            //     playerAction = JUMP;
            // } else {
            //     playerAction = FALLING;
            // }
        }

        if (attacking){
            playerAction = ATTACK;
        }

        if(startAnimation != playerAction){
            resetAnimationTick();
        }
    }

    private void resetAnimationTick() {
        animationIndex = 0;
        animationTick = 0;
    }

    // Parse BufferImage and devide it into subimages.
    // Store the subimages in BufferedImage 2-D array.
    private void loadAnimations() {
        animations = new BufferedImage[5][8];

        // Helper class to upload animation images
        BufferedImage [] loader = LoadSave.getPlayerAtlas();


        // IDLE 
        for(int i = 0; i < 4; i++){
            animations[0][i] = loader[0].getSubimage(i*32, 0, 32, 32); 
        }

        // ATTACK
        for(int i = 0; i < 4; i++){
            animations[1][i] = loader[1].getSubimage(i*32, 0, 32, 32); 
        }

        // RUN
        for(int i = 0; i < 6; i++){
            animations[2][i] = loader[2].getSubimage(i*32, 0, 32, 32); 
        }

        // JUMP
        for(int i = 0; i < 8; i++){
            animations[3][i] = loader[3].getSubimage(i*32, 0, 32, 32);
        }

        // FALLING
        // for(int i = 4; i < 7; i++){
        //     animations[4][i] = loader[4].getSubimage(i*32, 0, 32, 32);
        // }

        statusBarImage = LoadSave.getLevelAtlas(LoadSave.STATUS_BAR);

        



    }

    public void loadLevelData(int[][] lvlData){
        this.lvlData = lvlData;
        if(!isEntityOnFloor(hitbox, lvlData)){
            inAir = true;
        }
    }

    public void resetDirectionBooleans() {
            up = false;
            left = false;
            right = false;
            down = false;
    }

    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump){
        this.jump = jump;
    }
    
}
