public class Fairy extends Enemy {

    public Fairy(String name, int hp) {
        super(150, 380, name, hp, 500L);
    }

    public Fairy(float x, float y, String name, int hp) {
        super(x, y, name, hp, 500L);
    }

}