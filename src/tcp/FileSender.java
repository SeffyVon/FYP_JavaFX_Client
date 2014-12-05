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

public class FileSender extends Thread {
	String filenameString;
	
	FileSender(String filenameString){
		this.filenameString = filenameString;
	}
	
	
	 @Override
	 public void run() {
		 ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(15123);
			
			while(true){
				Socket socket = serverSocket.accept(); 
				System.out.println("Accepted connection : " + socket); 
//				BufferedReader in = new BufferedReader( 
//						new InputStreamReader( socket.getInputStream())); 
//				String movieFileName = in.readLine();
//				System.out.println("get movie File Name"+movieFileName);
				String movieFileName = "harrypotter";
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
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	 }
	
	 public static void main (String [] args ) throws IOException {
		
		 new FileSender("harrypotter").run();
		
	}

}

//Referenced: http://mrbool.com/file-transfer-between-2-computers-with-java/24516#ixzz3HCJuCQKd
