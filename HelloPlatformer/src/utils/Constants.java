package utils;

public class Constants {

    public static class PlaerConstants{
        public static final int IDLE = 0;
        public static final int ATTACK = 1;
        public static final int RUNNING = 2;
        
        public static int getSpriteAmount(int player_action){
            switch (player_action) {
                case RUNNING:
                    return 6;
                case IDLE:
                case ATTACK:
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
