package udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.atomic.AtomicBoolean;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;

import tcp.ProgressBarSyn;
import config.Config;

public class UDPReceiverThreaded extends Thread{
	

    protected MulticastSocket socket = null;
    protected BufferedReader in = null;
    protected int port = 4447;
    protected String host = "230.0.0.1";
    
    AtomicBoolean isRunning = new AtomicBoolean(true);
    
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
    
    public void stopThread(){
    	isRunning.set(false);
    	System.out.println("stop receiver thread");
    }
    @Override
	public void run() {
		DatagramPacket packet = null;
		while(isRunning.get()){
			// receive request
			byte[] buf = new byte[256];

            packet = new DatagramPacket(buf, buf.length);

            	
            //System.out.println("Receive Progress Num: " + received + packet.getData().toString() + packet.getLength());
            try {
				socket.receive(packet);
	            String received = new String(packet.getData(), 0, packet.getLength());
	            System.out.println("receive");
	            
	            System.out.println(received);
	            
	            if(received.startsWith("DOWNLOAD")){

	            }else{
	            	double progressDouble = Double.parseDouble(received);
	            	ProgressBarSyn.receiveProgress(Config.otherAddrString,progressDouble);
	            }
	            
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}

	}
}
