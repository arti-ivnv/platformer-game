package main;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.PlaerConstants.*;
import static utils.Constants.Directions.*;

public class GamePanel extends JPanel {
    
    private MouseInputs mouseInputs;
    private float xDelta = 100, yDelta = 100;
    
    // Full images of the animations
    private BufferedImage imgIdle, imgRun, imgAttack;
    private BufferedImage[][] animations;

    // Lower the animationSpeed value -> faster animation (120 / 4 = 30)
    private int animationTick, animationIndex, animationSpeed = 20;

    private int playerAction = IDLE;
    private int playerDirection = -1;
    private boolean moving = false;


    

    public GamePanel(){
        mouseInputs = new MouseInputs(this);

        importImg();
        loadAnimations();

        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    // Parse BufferImage and devide it into subimages.
    // Store the subimages in BufferedImage 2-D array.
    private void loadAnimations() {
        animations = new BufferedImage[3][6];

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
    }

    private void importImg() {
        InputStream isIdle = getClass().getResourceAsStream("/res/idle.png"); // From 0 - 3
        InputStream isRun = getClass().getResourceAsStream("/res/run.png"); // From 0 - 5
        InputStream isAttack = getClass().getResourceAsStream("/res/attack.png"); // From 0 - 3

        try {
            imgIdle = ImageIO.read(isIdle);
            imgRun = ImageIO.read(isRun);
            imgAttack = ImageIO.read(isAttack);

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


    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        updateAnimationTick();

        setAnimation();
        updatePosition();

        g.drawImage(animations[playerAction][animationIndex], (int)xDelta, (int)yDelta, 128, 128, null);
        g.drawRect((int)xDelta, (int)yDelta, 128, 128);
    }


    private void updatePosition() {
        if(moving){
            switch (playerDirection){
                case LEFT:
                    xDelta -= 3;
                    break;
                case UP:
                    yDelta -= 3;
                    break;
                case RIGHT:
                    xDelta += 3;
                    break;
                case DOWN:
                    yDelta += 3;
                    break;
            }
        }
    }

    private void setAnimation() {
        if(moving){
           playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
    }

    private void updateAnimationTick() {
        animationTick++;

        if (animationTick >= animationSpeed){

            animationTick = 0;
            animationIndex++;

            if (animationIndex >= getSpriteAmount(playerAction)){
                animationIndex = 0;
            }
        }
    }

    public void setDirection(int direction){
        this.playerDirection = direction;
        moving = true;
    }

    public void setMoving(boolean moving){
        this.moving = moving;
    }
}
