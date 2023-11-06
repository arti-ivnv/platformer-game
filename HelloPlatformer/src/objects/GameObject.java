package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.ObjectConstants.*;




public class GameObject {
    
    protected int x,y, objectType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int animationTick, animationIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objectType)
    {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
    }

    public void drawHitbox(Graphics g, int xLvlOffset){
        // For debugging
        g.setColor(Color.GREEN);
        g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    protected void updateAnimarionTick(){
        animationTick++;

        if(animationTick  >= ANI_SPEED){
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= getSpriteAmount(objectType)){
                animationIndex = 0;
                if(objectType == BARREL || objectType == BOX){
                    doAnimation = false;
                    active = false;
                } else if (objectType ==  CANNON_LEFT || objectType  == CANNON_RIGHT){
                    doAnimation = false;
                }
            }
        }
    }

    public void reset(){
        animationIndex = 0;
        animationTick = 0;
        active = true;

        if(objectType == BARREL || objectType == BOX || objectType == CANNON_LEFT || objectType == CANNON_RIGHT){
            doAnimation = false;

        } else {
            doAnimation = true;
        }
    }

    public int getObjectType() {
        return objectType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAnimationIndex(){
        return animationIndex;
    }

    public void setAnimation(boolean doAnimation){
        this.doAnimation = doAnimation;
    }


    public int getAnimationTick(){
        return animationTick;
    }




}
