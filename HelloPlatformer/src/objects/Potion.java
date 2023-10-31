package objects;

import main.Game;

public class Potion extends GameObject{

    private float howerOffset;
    private int maxHoverOffset, hoverDir = 1;

    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true;
        initHitbox(7, 14);
        xDrawOffset = (int) (3 * Game.SCALE);
        yDrawOffset = (int) (2 * Game.SCALE);

        maxHoverOffset = (int) (10 * Game.SCALE);
    }

    public void update(){
        updateAnimarionTick();
        updateHover();
    }

    private void updateHover() {
        howerOffset += (0.075f * Game.SCALE * hoverDir);

        if(howerOffset >= maxHoverOffset){
            hoverDir = -1;
        } else if(howerOffset < 0){
            hoverDir = 1;
        }

        hitbox.y = y + howerOffset;
    }
    
}
