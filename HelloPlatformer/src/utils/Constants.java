package utils;

import main.Game;

public class Constants {

    public static class UI{
        public static class Buttons{
            public static final int BUTTON_WIDTH_DEFAULT = 140;
            public static final int BUTTON_HEIGHT_DEFAULT = 56;
            public static final int BUTTON_WIDTH = (int)(BUTTON_WIDTH_DEFAULT * Game.SCALE);
            public static final int BUTTON_HEIGHT = (int)(BUTTON_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButtons{
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT * Game.SCALE);
            
        }
    } 

    // Fix spelling 
    public static class PlaerConstants{
        public static final int IDLE = 0;
        public static final int ATTACK = 1;
        public static final int RUNNING = 2;
        public static final int JUMP = 3;
        public static final int FALLING = 4;
        
        
        public static int getSpriteAmount(int player_action){
            switch (player_action) {
                case RUNNING:
                    return 6;
                case JUMP:
                    return 8;
                case IDLE:
                case ATTACK:
                case FALLING:
                    return 3;
            
                default:
                    return 0;
            }
        }
    }

    public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }
    
}
