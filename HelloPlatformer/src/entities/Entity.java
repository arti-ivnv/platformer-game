package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected Rectangle2D.Float hitbox;

    public Entity(float x, float y, int width, int height){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    protected void drawHitbox(Graphics g){
        // For debugging
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x, (int)hitbox.y, (int)hitbox.getWidth(), (int)hitbox.getHeight());
    }

    protected void initHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    // protected void updateHitbox(){
    //     hitbox.x = (int)x;
    //     hitbox.y = (int)y;
    // }

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }
}
