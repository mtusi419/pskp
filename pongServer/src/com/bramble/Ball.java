package com.bramble;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.net.Socket;
import java.util.Random;


public class Ball implements Runnable {


    static Socket socket;


    int x, y;
    double xDirection, yDirection;


    int p1score, p2score;

    Paddle p1 = new Paddle(10, 25, 1);
    Paddle p2 = new Paddle(485, 25, 2);

    Rectangle ball;


    public Ball(int x, int y){
        p1score = p2score = 0;
        this.x = x;
        this.y = y;


        Random r = new Random();
        int rXDir = r.nextInt(1)+9;
        if (rXDir == 9)
            rXDir=-10;
        setXDirection(rXDir);

        int rYDir = r.nextInt(1) +9;
        if (rYDir == 9)
            rYDir=-10;
        setYDirection(rYDir);


        ball = new Rectangle(this.x, this.y, 15, 15);
    }

    public void setXDirection(double xDir){
        xDirection = xDir;
    }
    public void setYDirection(double yDir){
        yDirection = yDir;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(ball.x, ball.y, ball.width, ball.height);

    }

    public void collision(){
        if(ball.intersects(p1.paddle))
            setXDirection(+10);
        if(ball.intersects(p2.paddle))
            setXDirection(-10);
    }
    public void move() {
        collision();
        ball.x += xDirection;
        ball.y += yDirection;

        if (ball.x <= 0) {
            setXDirection(+10);
            p2score++;
        }
        if (ball.x >= 485) {
            setXDirection(-10);
            p1score++;
        }

        if (ball.y <= 10) {
            setYDirection(+10);
        }

        if (ball.y >= 385) {
            setYDirection(-10);
        }
    }

    @Override
    public void run() {
        try{
            while (true) {
                move();
                Thread.sleep(100);

            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}



