import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class window extends JFrame {

    public byte[] bytes;
    public Board board;
    public  window() {
        board = new Board();
       add(board);

        setResizable(true);
        pack();
        setTitle("Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    public static void main(String[] args) {


        EventQueue.invokeLater(() -> {
            JFrame ex = new window();
            ex.setVisible(true);
            /*
            String Name = (String)JOptionPane.showInputDialog(
                    ex,
                    "Enter Your Name",
                    "Game",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "ham");
            ((window) ex).board.Name=Name;*/
            ex.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                    DataOutputStream dataOut = new DataOutputStream(byteOut);
                    try {
                        dataOut.writeInt(6);

                        dataOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ((window) ex).bytes = byteOut.toByteArray();
                    ((window) ex).board.udpClient.sendData(((window) ex).bytes);

                    System.out.println(((window) ex).board.NR);
                    System.exit(0);
                }
            });
        });
    }
}