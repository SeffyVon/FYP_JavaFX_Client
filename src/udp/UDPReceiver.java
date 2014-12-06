package udp;

import java.io.IOException;

public class UDPReceiver {
	
	UDPReceiverThreaded udpReceiverThreaded = null;
	
	public UDPReceiver() {
		udpReceiverThreaded = new UDPReceiverThreaded();
		udpReceiverThreaded.start();
	}
	 public static void main(String[] args) throws IOException {
		new UDPReceiver();
	}
	 
}
