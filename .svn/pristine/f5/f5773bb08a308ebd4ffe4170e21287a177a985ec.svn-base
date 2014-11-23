package udp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPSender {
	
	protected DatagramSocket socket = null;
    int sender_port = 4448;
    int receiver_port = 4447;
    String host = "230.0.0.1";
	public UDPSender() {
		try {
			socket = new DatagramSocket(sender_port);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

	}
	
	public void send(double progressNum) throws UnknownHostException{
		byte[] buf = new byte[256];

	    // send it
        InetAddress group;
		try {
			group = InetAddress.getByName(host);
			buf = String.valueOf(progressNum).getBytes();
			
			DatagramPacket packet = new DatagramPacket(buf, buf.length, group, receiver_port);
			socket.send(packet);
			System.out.println("send:"+progressNum);
		} catch (IOException e) {
			e.printStackTrace();
		} 
      
	}
	public static void main (String [] args ){
		UDPSender sender = new UDPSender();
		try {
			
			while(true){
				
				sender.send(12.2);
				new Thread().sleep(1000);
				sender.send(12.30);
				new Thread().sleep(1000);
				sender.send(13.30);
			}
			
		} catch ( Exception e) {
			
			e.printStackTrace();
		}
		//new Sender(12.02);
		System.out.println("sender closes!!!!");
		
	}
	
}