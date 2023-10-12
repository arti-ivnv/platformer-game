package utils;

import main.Game;

public class Constants {

    public static class Environment{
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HIGHT_DEFAULT = 101;
        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HIGHT_DEFAULT = 24;


        public static final int BIG_CLOUD_WIDTH = (int)(BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int BIG_CLOUD_HIGHT = (int)(BIG_CLOUD_HIGHT_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUD_WIDTH = (int)(SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUD_HIGHT = (int)(SMALL_CLOUD_HIGHT_DEFAULT * Game.SCALE);
    }

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

        public static class URMButtons{
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
        }

        public static class VolumeButton{
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Game.SCALE);

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
