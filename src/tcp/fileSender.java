package tcp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class fileSender {
	
	public static void main (String [] args ) throws IOException {
		
		ServerSocket serverSocket = new ServerSocket(15123);
		Socket socket = serverSocket.accept(); 
		System.out.println("Accepted connection : " + socket); 
		File transferFile = new File("video/VideoCat.mp4"); // ("video/theAmazingSpiderMan.rmvb");//
		byte [] bytearray = new byte [(int)transferFile.length()]; 
		FileInputStream fin = new FileInputStream(transferFile); 
		BufferedInputStream bin = new BufferedInputStream(fin); 
		bin.read(bytearray,0,bytearray.length); // bin -> bytearray
		OutputStream os = socket.getOutputStream(); 
		System.out.println("Sending Files..."); 
		long time1 = System.currentTimeMillis();
		os.write(bytearray,0,bytearray.length); 
		os.flush(); 
		socket.close(); 
		long time2 = System.currentTimeMillis();
		System.out.println("File transfer complete"); 
		System.out.println("speed(KB/s):"+transferFile.length()/(time2-time1));
		
	}

}

//Referenced: http://mrbool.com/file-transfer-between-2-computers-with-java/24516#ixzz3HCJuCQKd
