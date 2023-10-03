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

public class GamePanel extends JPanel {
    
    private MouseInputs mouseInputs;
    private float xDelta = 100, yDelta = 100;
    
    // Full images of the animations
    private BufferedImage imgIdle, imgRun, imgAttack;

    // Sub images holders of the animation
    private BufferedImage imgSubIdle, imgSubRun, imgSubAttack;


    

    public GamePanel(){
        mouseInputs = new MouseInputs(this);

        importImg();
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void importImg() {
        InputStream isIdle = getClass().getResourceAsStream("/res/idle.png");
        InputStream isRun = getClass().getResourceAsStream("/res/run.png");
        InputStream isAttack = getClass().getResourceAsStream("/res/attack.png");

        try {
            imgIdle = ImageIO.read(isIdle);
            isIdle.close();

            imgRun = ImageIO.read(isRun);
            isRun.close();

            imgAttack = ImageIO.read(isAttack);
            isAttack.close();
        } catch (IOException e) {
            e.printStackTrace();
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


        // From 0 - 3
        imgSubIdle = imgIdle.getSubimage(1*32, 0 , 32, 32);
        // From 0 - 5
        imgSubRun = imgRun.getSubimage(1*32, 0 , 32, 32);
        // From 0 - 3
        imgSubAttack = imgAttack.getSubimage(1*32, 0 , 32, 32);

        g.drawImage(imgSubAttack, (int)xDelta, (int)yDelta, 128, 128, null);
    }


    public void changeXDelta(int value){

        this.xDelta += value;
    }

    public void changeYDelta(int value){

        this.yDelta += value;
    }

    public void setRectPosition(int x, int y){
        this.xDelta = x;
        this.yDelta = y;
    }

}
