package udp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import tcp.ProgressBarSyn;
import config.Config;

public class UDPReceiverThreaded extends Thread{
	

    protected MulticastSocket socket = null;
    protected BufferedReader in = null;
    protected int port = 4447;
    protected String host = "230.0.0.1";
    
    public UDPReceiverThreaded() {
    	System.out.println("Receier Comes");
        try {

            socket = new MulticastSocket(port);
            InetAddress address = InetAddress.getByName(host);
			socket.joinGroup(address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

      

	}
    
	public void run() {
		DatagramPacket packet = null;
		while(true){
			// receive request
			byte[] buf = new byte[256];

            packet = new DatagramPacket(buf, buf.length);

            	
            //System.out.println("Receive Progress Num: " + received + packet.getData().toString() + packet.getLength());
            try {
				socket.receive(packet);
	            String received = new String(packet.getData(), 0, packet.getLength());
	            System.out.println("receive");
	            
	            System.out.println(received);
	            double progressDouble = Double.parseDouble(received);
	            ProgressBarSyn.receiveProgress(Config.otherAddrString,progressDouble);
	            System.out.println("server receive and set progress:"+progressDouble);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
