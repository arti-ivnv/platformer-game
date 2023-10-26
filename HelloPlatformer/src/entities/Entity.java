package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Entity {
    
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected Rectangle2D.Float hitbox;
    // Lower the animationSpeed value -> faster animation (120 / 4 = 30)
    protected int animationTick, animationIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth; 
    protected int currentHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed = 1.0f *  Game.SCALE;

    public Entity(float x, float y, int width, int height){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    protected void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.RED);
        g.drawRect((int)attackBox.x - lvlOffsetX, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }

    protected void drawHitbox(Graphics g, int xLvlOffset){
        // For debugging
        g.setColor(Color.GREEN);
        g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
    }

    // protected void updateHitbox(){
    //     hitbox.x = (int)x;
    //     hitbox.y = (int)y;
    // }

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public int getEnemyState(){
        return state;
    }

    public int getAnimationIndex(){
        return animationIndex;
    }
}
