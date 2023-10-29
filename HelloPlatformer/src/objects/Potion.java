package objects;

import main.Game;

public class Potion extends GameObject{

    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true;
        initHitbox(7, 14);
        xDrawOffset = (int) (3 * Game.SCALE);
        yDrawOffset = (int) (2 * Game.SCALE);

    }

    public void update(){
        updateAnimarionTick();
    }
    
}
