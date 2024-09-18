import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Pipe {

    Image topPipeImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("toppipe.png"));
    Image bottomPipeImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("bottompipe.png"));
    int x = 360;

    int y = 0;

    int width = 64;

    int height = 512;

    int velocity = -4;

    Image image;

    boolean top;

    boolean passed = false;

    public Pipe(int height, int y, boolean top) throws IOException {
        this.height = height;
        this.y = y;
        this.image = top ? topPipeImg : bottomPipeImg;
    }

    public void move() {
        this.x += this.velocity;
    }
}
