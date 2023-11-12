package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import utils.LoadSave;
import static utils.Constants.UI.Buttons.*;

public class MenuButton {

    private int xPosition, yPosition, rowIndex, index;
    private Gamestate state;
    private BufferedImage[] images;
    private int xOffsetCenter = BUTTON_WIDTH / 2;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;
    
    public MenuButton(int xPosition, int yPosition, int rowIndex, Gamestate state){

        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImages();
        initBounds();

    }

    private void initBounds() {
        bounds = new Rectangle(xPosition - xOffsetCenter, yPosition, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    private void loadImages() {
        images = new BufferedImage[3];
        BufferedImage temp = LoadSave.getLevelAtlas(LoadSave.MENU_BUTTONS);

        for (int i = 0; i < images.length; i++){
            images[i] = temp.getSubimage(i * BUTTON_WIDTH_DEFAULT, rowIndex * BUTTON_HEIGHT_DEFAULT, BUTTON_WIDTH_DEFAULT, BUTTON_HEIGHT_DEFAULT);
        }
    }

    public void draw(Graphics g){
        g.drawImage(images[index], xPosition - xOffsetCenter, yPosition, BUTTON_WIDTH, BUTTON_HEIGHT, null);
    }

    public void update(){
        index = 0;
        if(mouseOver){
            index = 1;
        }
        if (mousePressed){
            index = 2;
        }
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void applyGamestate(){
        Gamestate.state = state;
    }

    

    public Rectangle getBounds() {
        return bounds;
    }

    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }

    public Gamestate getState(){
        return state;
    }

    
}
