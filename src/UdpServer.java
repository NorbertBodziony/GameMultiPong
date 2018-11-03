import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class UdpServer extends Thread {

    private DatagramSocket socket;
    private List<Board> board=new ArrayList<>();
    private int por;
    public Player P1=null;
    public Player P2=null;
    public  UdpServer(Board board)

    {

        try {
            this.socket=new DatagramSocket(1331);

        } catch (SocketException e) {
            e.printStackTrace();
        }

    }
    public  void run()
    {
        while(true)
        {
            byte[] data=new  byte[1024];
            DatagramPacket packet = new DatagramPacket(data,data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message = new String((packet.getData()));
            por=packet.getPort();
            System.out.println("Client --"+packet.getAddress().getHostAddress()+" port "+packet.getPort());
            sendDatatoAllClients(data);

        }
    }
    public void sendData(byte[] data,InetAddress ipAddress,int port)
    {
        DatagramPacket packet= new DatagramPacket(data,data.length,ipAddress,port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendDatatoAllClients(byte[] data)
    {
        for(Board b : board)
        {
            sendData(data,b.udpClient.ipAddress,por);
        }
    }

}
