package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import static utils.Constants.PlaerConstants.*;

public class Player extends Entity{

    private BufferedImage[][] animations;
    // Lower the animationSpeed value -> faster animation (120 / 4 = 30)
    private int animationTick, animationIndex, animationSpeed = 20;

    // Field for action animations (IDLE by default)
    private int playerAction = IDLE;

    private boolean up, left, down, right; 

    private float playerSpeed = 2.0f;
    // Moving flag
    private boolean moving = false, attacking = false;

    public Player(float x, float y) {
        super(x, y);
        loadAnimations();
    }

    // Player logic states
    public void update(){

        updatePosition();

        updateAnimationTick();
        setAnimation();
    }

    // Drawing
    public void render(Graphics g){
        g.drawImage(animations[playerAction][animationIndex], (int)x, (int)y, 128, 128, null);
        g.drawRect((int)x, (int)y, 128, 128);
    }

    private void updatePosition(){

        moving = false;

        if (left && !right){
            x -= playerSpeed;
            moving = true;
        } else if (right && !left){
            x += playerSpeed;
            moving = true;
        }

        if (up && !down){
            y -= playerSpeed;
            moving = true;
        } else if (down && !up){
            y += playerSpeed;
            moving = true;
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
        animations = new BufferedImage[3][6];

        InputStream isIdle = getClass().getResourceAsStream("/res/idle.png"); // From 0 - 3
        InputStream isRun = getClass().getResourceAsStream("/res/run.png"); // From 0 - 5
        InputStream isAttack = getClass().getResourceAsStream("/res/attack.png"); // From 0 - 3

        try {
            BufferedImage imgIdle = ImageIO.read(isIdle);
            BufferedImage imgRun = ImageIO.read(isRun);
            BufferedImage imgAttack = ImageIO.read(isAttack);

            // IDLE 
            for(int i = 0; i < 4; i++){
                animations[0][i] = imgIdle.getSubimage(i*32, 0, 32, 32); 
            }

            // ATTACK
            for(int i = 0; i < 4; i++){
                animations[1][i] = imgAttack.getSubimage(i*32, 0, 32, 32); 
            }

            // RUN
            for(int i = 0; i < 6; i++){
                animations[2][i] = imgRun.getSubimage(i*32, 0, 32, 32); 
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                isIdle.close(); 
                isRun.close();
                isAttack.close();
            } catch (IOException e){
                e.printStackTrace();
            }
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
    
}
