import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Bird {

    public Bird() throws IOException {
    }

    int x = 45;
    int y = 320;

    int velocity = 0;

    int gravity = 1;

    int height = 24;

    int width = 34;

    Image image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("flappybird.png"));;

    public void move() {
        this.velocity += gravity;
        this.y += velocity;
        this.y = Math.max(this.y, 0);
    }

    public void jump() {
        this.velocity = -9;
    }
}
