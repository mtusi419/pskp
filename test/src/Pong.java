

import java.awt.*;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.util.Random;
import javax.swing.*;

public class Pong extends JFrame {

    static int clientNo;
    int gWidth = 500;
    int gHeight = 450;
    static String count = "";
    Dimension screenSize = new Dimension(gWidth, gHeight);
    Image dbImage;
    Graphics dbGraphics;


    static Ball b = new Ball(250, 200); //


    static Socket socket;

    public Pong() {            // конструктор окна
        this.setTitle("Pong!");
        this.setSize(screenSize);
        this.setResizable(false);
        this.setVisible(true);
        this.setBackground(Color.DARK_GRAY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(new AL());
    }

    public static void main(String[] args) throws AWTException, InterruptedException {
        Pong pg = new Pong();
        final Random random = new Random();
        Thread ball = new Thread(b);
        ball.start();
        Thread p1 = new Thread(b.p1);
        Thread p2 = new Thread(b.p2);
        p1.start();
        p2.start();



        try {

            //socket = new Socket("178.173.21.54", 8008);
            socket = new Socket("26.27.212.187", 8888);
            DataInputStream inStream=new DataInputStream(socket.getInputStream());
            DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
            clientNo = inStream.read();
            String serverMessage;

            while(true){
                System.out.println(clientNo);    //
                outStream.writeUTF("sss");
                serverMessage=inStream.readUTF();
                System.out.println(serverMessage);
                if (serverMessage.length()> 5 ) {
                    break;
                }
                b.setXPos(Integer.parseInt(serverMessage));
                serverMessage=inStream.readUTF();
                //System.out.println(serverMessage);
                b.setYPos(Integer.parseInt(serverMessage));
                //System.out.println(b.p1.paddle.x);
                if (clientNo == 1) {
                    outStream.writeUTF(String.valueOf(b.p1.paddle.y));
                    b.p2.paddle.y = Integer.parseInt((inStream.readUTF()));
                }
                else {
                outStream.writeUTF(String.valueOf(b.p2.paddle.y));
                b.p1.paddle.y = Integer.parseInt((inStream.readUTF()));}
                b.p1score = Integer.parseInt(inStream.readUTF());
                b.p2score = Integer.parseInt(inStream.readUTF());


                Robot r = new Robot();
                if (clientNo == 1) {
                    int randomInteger = random.nextInt(2);
                    if (randomInteger == 0) {
                        r.keyPress(KeyEvent.VK_W);
                        Thread.sleep(100);
                    }
                    else if (randomInteger == 1){
                        r.keyPress(KeyEvent.VK_S);
                        Thread.sleep(100);
                    }
                    r.keyRelease(ALLBITS);
                }
                else if (clientNo == 2) {
                    int randomInteger = random.nextInt(2);
                    if (randomInteger == 0) {
                        r.keyPress(KeyEvent.VK_UP);
                        Thread.sleep(100);
                    }
                    else if(randomInteger == 1){
                        r.keyPress(KeyEvent.VK_DOWN);
                        Thread.sleep(100);
                        r.keyRelease(ALLBITS);
                    }

                }






            }
            count = serverMessage;
        } catch (IOException e) {
            e.printStackTrace();
        }

        {


        }

    }


    @Override
    public void paint(Graphics g) {
        dbImage = createImage(getWidth(), getHeight());
        dbGraphics = dbImage.getGraphics();
        draw(dbGraphics);
        g.drawImage(dbImage, 0, 25, this);
    }

    public void draw(Graphics g) {
        b.draw(g);
        b.p1.draw(g);
        b.p2.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        g.drawString("P1: " +b.p1score, 25, 20);
        g.drawString("P2: " +b.p2score, 400, 20);
        g.drawString(count, 125, 200);
        if (clientNo == 1) {
            g.drawString("Ваша платформа слева", 100, 20);
        }
        else if (clientNo == 2){
            g.drawString("Ваша платформа справа", 100, 20);
        }
        else {g.drawString("НЕТ ПОДКЛЮЧЕНИЯ", 100, 250);}
        repaint();
    }

    public class AL extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e)  {
                b.p1.keyPressed(e);
            b.p2.keyPressed(e);
        }
        @Override
        public void keyReleased(KeyEvent e) {

            b.p1.keyReleased(e);

            b.p2.keyReleased(e);}


    }
}