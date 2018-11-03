

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.io.*;
import java.net.DatagramPacket;
import javax.swing.Timer;
import javax.xml.crypto.Data;

public class Board extends JPanel implements ActionListener {
    public boolean inGame = false;
    private final int SIZE = 10;
    private  int M1 = 0;
    private  int M2 = 0;
     public double width,height;
    public int k[]=new int [4];
    public int x[];
    public int y[];
    public int player;
    byte[] bytes;
    public boolean ready=false;
    public int NR;
    public int P1;
    public int P2;
    public   long nextSecond = System.currentTimeMillis() + 1000;
    public  int frameInLastSecond = 0;
    public  int framesInCurrentSecond = 0;
    public boolean init;


    public ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    public DataOutputStream dataOut = new DataOutputStream(byteOut);


    public   UdpClient udpClient;
    Ball ball1;
    public  Board() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         width = screenSize.getWidth()/2;
         height = screenSize.getHeight()/2-50;
        System.out.println(width +" "+height);
        x=new int[(int)width];
        y=new int[(int)height];
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setDoubleBuffered(true);
        setPreferredSize(new Dimension((int)width,(int) height));
         ball1=new Ball((int)width/2,(int)height/2,(int)width,(int)height);

        initGame();
    }
    private void initGame()  {

        Timer timer = new Timer(10, this);
        timer.start();

        udpClient = new UdpClient(this, "localhost");
        udpClient.start();
        ready=true;
        try {
            dataOut.writeInt(1);// Inicjalizacja gry
            dataOut.writeInt((int)width);
            dataOut.writeInt((int)height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
         bytes = byteOut.toByteArray();
        udpClient.sendData(bytes);


    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    byte[] toBytes(int i)
    {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);

        return result;
    }
    private void doDrawing(Graphics g) {
    if(ready==true) {
        if (inGame) {       //g.setColor(new Color((int)(Math.random() * 0x1000000)));
            g.setColor(new Color(200, 200, 200));
            g.drawLine(x.length - 3 * SIZE, P1, x.length - 3 * SIZE, 200 + P1);
            g.drawLine(x.length - 4 * SIZE, P1, x.length - 4 * SIZE, 200 + P1);
            g.drawLine(3 * SIZE, P2, 3 * SIZE, 200 + P2);
            g.drawLine(4 * SIZE, P2, 4 * SIZE, 200 + P2);
            if (NR == 1) {
                if (k[0] == 1 && P1 >= 0) {
                    P1 -= 10;
                }
                if (k[1] == 1 && P1 < (y.length - 200)) {
                    P1 += 10;
                }
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                DataOutputStream dataOut = new DataOutputStream(byteOut);
                try {
                    dataOut.writeInt(3);// Pozycja gracza
                    dataOut.writeInt(P1);
                    dataOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bytes = byteOut.toByteArray();
                udpClient.sendData(bytes);

            } else {
                if (k[0] == 1 && P2 >= 0) {
                    P2 -= 10;
                }
                if (k[1] == 1 && P2 < (y.length - 200)) {
                    P2 += 10;
                }
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                DataOutputStream dataOut = new DataOutputStream(byteOut);
                try {
                    dataOut.writeInt(3);// pozycja Gracza
                    dataOut.writeInt(P2);
                    dataOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bytes = byteOut.toByteArray();
                udpClient.sendData(bytes);
            }

            ball1.draw(g);

            long currentTime = System.currentTimeMillis();
            if (currentTime > nextSecond) {
                nextSecond += 1000;
                frameInLastSecond = framesInCurrentSecond;
                framesInCurrentSecond = 0;
            }
            framesInCurrentSecond++;

            g.drawString(Integer.toString(frameInLastSecond) + " fps", 60, 20);

        } else {

            g.drawString("W8ing for 2nd player",x.length/2-50,y.length/2);

        }
    }else
    {
        gameOver(g);
    }
    }

    private void gameOver(Graphics g)
    {
        g.setColor(Color.CYAN);
        g.drawString("Game Over",x.length/2-50,y.length/2);
        g.drawString("Press Enter To restart",x.length/2-70,y.length/2+50);
        init=true;

    }

    private class TAdapter extends KeyAdapter {


        public synchronized void keyPressed(KeyEvent e) {

            if ((e.getKeyCode() == (KeyEvent.VK_W))) {
                k[0]=1;
            }

            if (((e.getKeyCode() == (KeyEvent.VK_S)))) {
                k[1]=1;
            }

            if (((e.getKeyCode() == (KeyEvent.VK_ENTER)))) {

                if(ready==false){
                initGame();
                //System.out.println("w8ing for 2nd player");
                }
            }
        }


        public synchronized void keyReleased(KeyEvent e) {
            if ((e.getKeyCode() == (KeyEvent.VK_W))) {
                k[0]=0; M1=0;
            }

            if (((e.getKeyCode() == (KeyEvent.VK_S)))) {
                k[1]=0; M1=0;
            }



        }

        @Override
        public void keyTyped(KeyEvent e) {/* Not used */ }
    }
    public void actionPerformed(ActionEvent e) {



        repaint();
    }
}
