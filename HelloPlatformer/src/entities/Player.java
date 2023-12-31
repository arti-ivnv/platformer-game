package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;
import static utils.Constants.GRAVITY;
import static utils.Constants.ANI_SPEED;

public class Player extends Entity{

    private BufferedImage[][] animations;


    private boolean left, right, jump; 

    // Moving flag
    private boolean moving = false, attacking = false;

    private int[][] lvlData;

    private float xDrawOffset = 10 * Game.SCALE;
    private float yDrawOffset = 7 * Game.SCALE;

    // Jumping and Gravity

    // Negative because we are going y axis direction up;
    private float jumpSpeed = -2.40f * Game.SCALE;
    private float fallSpeedAfterColision = 0.5f * Game.SCALE;

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
    private int healthWidth = healthBarWidth;

    // Sub bar (power/stamina  indicator)
    private int powerBarWidth = (int) (104 * Game.SCALE);
    private int powerBarHeight = (int) (2 * Game.SCALE);
    private int powerBarX = (int) (44 * Game.SCALE);
    private int powerBarY = (int) (34 * Game.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 200;
    private int powerValue = powerMaxValue;


    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;

    private Playing playing;

    private int tileY = 0;

    private boolean powerAttackActive;
    private int powerAttackTick;
    // Stamina
    private int powerGrowSpeed = 15;
    private int powerGrowTick;


    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.walkSpeed = Game.SCALE * 1.0f;
        loadAnimations();
        initHitbox(11, 24);
        initAttackBox();
    }

    public void setSpawn(Point spawn){
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x , y, (int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    // Player logic states
    public void update(){

        updateHealthBar();
        updatePowerBar();

        if(currentHealth <= 0){
            
            if(state != DEAD){
                state = DEAD;
                animationTick = 0;
                animationIndex = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            } else if (animationIndex == getSpriteAmount(DEAD) - 1 && animationTick >= ANI_SPEED - 1){
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            } else {
                updateAnimationTick();
            }
            return;
        }

        updateAttackBox();

        updatePosition();
        if(moving){
            checkPotionTouched();
            checkSpikesTouched();
            tileY = (int)(hitbox.y / Game.TILE_SIZE);
            if (powerAttackActive){
                powerAttackTick++;
                if(powerAttackTick >= 35){
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }
        }
        if(attacking || powerAttackActive){
            checkAttack();
        }
        updateAnimationTick();
        setAnimation();

    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if(attackChecked || animationIndex != 1){
            return;
        }
        attackChecked = true;

        if(powerAttackActive){
            attackChecked = false;
        }
        
        playing.checkEnemyHit(attackBox);
        playing.checkObejectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void updateAttackBox() {
        if (right && left){
            if(flipW == 1){
                attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10) - 30;
            } else {
                attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10) + 15;
            }
        }else if(right || (powerAttackActive && flipW == 1)){
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10) - 30;
        }else if(left || (powerAttackActive && flipW == -1)){
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10) + 15;
        }
        attackBox.y = hitbox.y + (int)(Game.SCALE * 10) - 10;
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    private void updatePowerBar(){
        powerWidth = (int)((powerValue / (float) powerMaxValue) * powerBarWidth);

        powerGrowTick++;
        if(powerGrowTick >= powerGrowSpeed){
            powerGrowTick = 0;
            changePower(1);
        }
    }

    // Drawing
    public void render(Graphics g, int lvlOffset){
        g.drawImage(animations[state][animationIndex], 
                        (int)(hitbox.x - xDrawOffset) - lvlOffset + flipX, 
                        (int)(hitbox.y - yDrawOffset), 
                        width * flipW, height, null);
        // drawHitbox(g, lvlOffset);
        // drawAttackBox(g, lvlOffset);
        drawUI(g);
    }


    private void drawUI(Graphics g) {
        // Background UI
        g.drawImage(statusBarImage, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        // Health UI
        g.setColor(Color.RED);
        g.fillRect(healthBarX + statusBarX, healthBarY + statusBarY, healthWidth, healthBarHeight);

        // Power UI
        g.setColor(Color.yellow);
        g.fillRect(powerBarX + statusBarX, powerBarY + statusBarY, powerWidth, powerBarHeight);

    }

    private void updatePosition(){

        moving = false;
        
        if (jump)
            jump();

        // if(!left && !right && !inAir){
        //     return;
        // }
        
        if(!inAir){
            if (!powerAttackActive){
                if ((!left && !right) || (right && left)){
                    return;
                }
            }

        }

        float xSpeed = 0;

        if (left && !right){
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        } 
        if (right && !left){
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if(powerAttackActive){
            if(!left && !right || (left && right)){
                if(flipW == -1){
                    xSpeed = -walkSpeed;
                } else {
                    xSpeed = walkSpeed;
                }
            }

            xSpeed *= 3;
        }

        if(!inAir){
            if(!isEntityOnFloor(hitbox, lvlData)){
                inAir = true;
            }
        }

        if(inAir && !powerAttackActive){
            if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
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
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
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
            if(powerAttackActive){
                powerAttackActive = false;
                powerAttackTick = 0;
            }
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

        if (animationTick >= ANI_SPEED){

            animationTick = 0;
            animationIndex++;

            if (animationIndex >= getSpriteAmount(state)){
                animationIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void setAnimation() {

        int startAnimation = state;

        if(moving){
           state = RUNNING;
        } else {
            state = IDLE;
        }

        if(inAir){
            state = JUMP;
            
            // NOT WORKING
            // if (airSpeed < 0){
            //     playerAction = JUMP;
            // } else {
            //     playerAction = FALLING;
            // }
        }

        if(powerAttackActive){
            state = ATTACK;
            animationIndex = 1;
            animationTick = 0;
            return;
        }

        if (attacking){
            state = ATTACK;
            if(startAnimation != ATTACK){
                animationIndex = 1;
                animationTick = 0;
                return;
            }
        }

        if(startAnimation != state){
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
        animations = new BufferedImage[8][8];

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

        // HURT
        for(int i = 0; i < 4; i++){
            animations[5][i] = loader[5].getSubimage(i * 32, 0, 32, 32);
        }

        // HURT
        for(int i = 0; i < 8; i++){
            animations[6][i] = loader[6].getSubimage(i * 32, 0, 32, 32);
        }

        // Power attack
        for (int i = 0; i < 6; i++){
            animations[7][i] = loader[7].getSubimage(i * 32, 0, 32, 32);
        }

        statusBarImage = LoadSave.getLevelAtlas(LoadSave.STATUS_BAR);

        



    }

    public void loadLevelData(int[][] lvlData){
        this.lvlData = lvlData;
        if(!isEntityOnFloor(hitbox, lvlData)){
            inAir = true;
        }
    }

    public void resetDirectionBooleans() {
            left = false;
            right = false;
    }

    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
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

    public void resetAll() {
        resetDirectionBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        airSpeed = 0f;
        state = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if(flipW == 1){
                attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10) - 30;
            } else {
                attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10) + 15;
            }

        if(!isEntityOnFloor(hitbox, lvlData)){
            inAir = true;
        }

    }

    private void resetAttackBox(){
        
    }

    public void changePower(int value) {
        powerValue += value;
        if (powerValue >= powerMaxValue){
            powerValue = powerMaxValue;
        } else if (powerValue <= 0){
            powerValue = 0;
        }
    }

    public void kill() {
        currentHealth = 0;
    }

    public int  getTileY(){
        return tileY;
    }

    public void powerAttack() {
        if (powerAttackActive){
            return;
        }
        if (powerValue >= 60){
            powerAttackActive = true;
            changePower(-60);
        }
    }
    
}
