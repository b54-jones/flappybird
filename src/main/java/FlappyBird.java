import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    int scoreX = 10;

    int scoreY = 35;

    //Images
    Image backgroundImage;
    Image birdImg;

    Bird bird;

    Timer gameLoop;

    Timer placePipesTimer;

    List<Pipe> pipes;

    double score;

    boolean gameOver = false;


    FlappyBird() throws IOException {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        //Load images
        backgroundImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("flappybirdbg.png"));
        birdImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("flappybird.png"));

        bird = new Bird();

        pipes = new ArrayList<>();

        placePipesTimer = new Timer(2000, e -> {
            try {
                placePipes();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        placePipesTimer.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void placePipes() throws IOException {
        Random random = new Random();
        int topPipeHeight = 75 + random.nextInt(325);
        int distanceBetweenPipes = 150;
        Pipe topPipe = new Pipe(topPipeHeight, 0, true);
        int bottomPipeHeight = boardHeight - (topPipeHeight + distanceBetweenPipes);
        Pipe bottomPipe = new Pipe(bottomPipeHeight, boardHeight-bottomPipeHeight, false);
        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }

    public void draw(Graphics graphics) {
        //background
        graphics.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);

        //bird
        graphics.drawImage(bird.image, bird.x, bird.y, bird.width, bird.height, null);

        //pipes
        for (int i = 0; i<pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            graphics.drawImage(pipe.image, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 32));

        if (gameOver) {
            graphics.drawString("Game Over: " + (int) score, scoreX, scoreY);
        } else {
            graphics.drawString("Score: " + (int) score, scoreX, scoreY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        bird.move();
        pipes.forEach(Pipe::move);
        gameOver = checkForCollisions();

        updateScore();
        repaint();

        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    private void updateScore() {
        for (int i = 0; i<pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }
        }
    }

    private boolean checkForCollisions() {
        boolean collision = false;
        if (bird.y > boardHeight) {
            collision = true;
        }

        for(int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            if (collision(bird, pipe)) {
                collision = true;
                break;
            }
        }
        return collision;
    }

    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            bird.jump();
            if (gameOver) {
                bird.y = 320;
                bird.x = 45;
                pipes.clear();
                bird.velocity = 0;
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
