package com.bramble;

import java.net.*;
import java.io.*;

public class Main {
    static Ball b = new Ball(250, 200);
    static int counter=0;
    static Thread ball;
    static Thread p1;
    static Thread p2;
        public static void main(String[] args) throws Exception {



            try{
                ServerSocket server=new ServerSocket(8888);
                System.out.println("Server Started ....");
                while(counter < 2){
                    counter++;
                    Socket serverClient=server.accept();
                    System.out.println(" >> " + "Client No:" + counter + " started!");
                    ServerClientThread sct = new ServerClientThread(serverClient,counter);
                    sct.start();
                }
                ball = new Thread(b);
                ball.start();
                p1 = new Thread(b.p1);
                p2 = new Thread(b.p2);
                p2.start();
                p1.start();
            }catch(Exception e){
                System.out.println(e);
            }
        }

        static class ServerClientThread extends Thread {
            Socket serverClient;
            int clientNo;
            int squre;
            ServerClientThread(Socket inSocket,int counter){
                serverClient = inSocket;
                clientNo=counter;
            }
            public void run(){
                DataInputStream inStream = null;
                DataOutputStream outStream = null;
                try{
                    inStream = new DataInputStream(serverClient.getInputStream());
                    outStream = new DataOutputStream(serverClient.getOutputStream());
                    String clientMessage="", serverMessage="3";
                    outStream.write(clientNo);
                    int i = 0;
                    while(true){
                        clientMessage=inStream.readUTF();
                        outStream.writeUTF(String.valueOf(b.ball.x));
                        outStream.writeUTF(String.valueOf(b.ball.y));
                        outStream.flush();
                        if (clientNo == 1) {
                        b.p1.paddle.y = Integer.parseInt((inStream.readUTF()));
                        outStream.writeUTF(String.valueOf(b.p2.paddle.y));}
                        else {
                        b.p2.paddle.y = Integer.parseInt((inStream.readUTF()));
                        outStream.writeUTF(String.valueOf(b.p1.paddle.y));}
                        outStream.writeUTF(String.valueOf(b.p1score));
                        outStream.writeUTF(String.valueOf(b.p2score));
                        if ((b.p1score == 3)|(b.p2score == 3)) {break;}
                    }
                    if (b.p1score>b.p2score) {outStream.writeUTF("Победил игрок 1");}
                    else {outStream.writeUTF("Победил игрок 2");}
                    ball.stop();
                    p1.stop();
                    p2.stop();
                }catch(Exception ex){
                    System.out.println(ex);
                }finally{
                    System.out.println("Client -" + clientNo + " exit!! ");
                    System.out.println(counter);
                    counter--;
                    clientNo--;
                }
            }
        }
    }

