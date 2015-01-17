package tcp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

import model.GMessage;
import javafx.collections.ObservableList;

public class FileSender extends Thread {
	String filenameString;
	boolean isOcuppied = false;
	int portNum;

	
	public FileSender(String filenameString, int portNum){
		this.filenameString = filenameString;
		isOcuppied = true;
		this.portNum = portNum;
	}
	
	 @Override
	 public void run() {
		 ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(portNum);
			
			
				Socket socket = serverSocket.accept(); 
				System.out.println("Accepted connection : " + socket); 
				File transferFile = new File("resources/video/tosend/"+filenameString+".mp4"); // ("video/theAmazingSpiderMan.rmvb");//
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
			
				
				// prompt...
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	 }
	
	 public static void main (String [] args ) throws IOException {
		
		// new FileSender("harrypotter").run();
		 //new FileSender("thehobbits").run();
		
	}

}

//Referenced: http://mrbool.com/file-transfer-between-2-computers-with-java/24516#ixzz3HCJuCQKd
