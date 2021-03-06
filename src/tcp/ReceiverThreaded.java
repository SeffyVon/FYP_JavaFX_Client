package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import config.Config;

public class ReceiverThreaded implements Runnable {
	
	static double progress = 0;


	ServerSocket serverSocket = null;

	@Override
	public void run() {
		System.out.println(Config.localAddrString+"receiver comes");
		try{
			serverSocket = new ServerSocket(Config.localPort);
			PrintWriter logWriter = new PrintWriter("resources/logFile.txt");
			
			Thread t = new Thread(new Runnable(){

				@Override
				public void run() {
					while(true){
						Socket clientSocket = null;
						try {
							clientSocket = serverSocket.accept();
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						BufferedReader bufferedReader = null;
						try {
							bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						} catch (IOException e3) {
							// TODO Auto-generated catch block
							e3.printStackTrace();
						}
						Double double1 = null;
						try {
							double1 = Double.parseDouble(bufferedReader.readLine());
						} catch (NumberFormatException | IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						ProgressBarSyn.receiveProgress(Config.otherAddrString,double1);
						System.out.println("server receive:"+double1);
						logWriter.println("server receive:"+double1);
						try {
							bufferedReader.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							clientSocket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				    }
				}
				
			});
			t.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
