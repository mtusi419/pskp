
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Ball implements Runnable {



    int x, y;


    int p1score, p2score;

    Paddle p1 = new Paddle(10, 25, 1);
    Paddle p2 = new Paddle(485, 25, 2);

    Rectangle ball;


    public Ball(int x, int y){
        p1score = p2score = 0;
        this.x = x;
        this.y = y;
        ball = new Rectangle(this.x, this.y, 15, 15);
    }

    public void setXPos(int xPos){
        ball.x = xPos;
    }
    public void setYPos(int yPos){
        ball.y = yPos;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(ball.x, ball.y, ball.width, ball.height);

    }



    @Override
    public void run() {

            }
        }



