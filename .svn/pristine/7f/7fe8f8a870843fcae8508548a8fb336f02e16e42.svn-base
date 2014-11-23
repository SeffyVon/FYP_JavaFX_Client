package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
	
	static double progress = 0;
	static String macAddrString = "10.115.193.30";
	static String linuxAddrString = "10.115.209.74";
	static String localAddrString = "127.0.0.1";
	static int serverPort = 15126;
	static int clientPort = 15124;
	ServerSocket serverSocket = null;
	public Receiver() {
		System.out.println("receiver comes");
		try{
			serverSocket = new ServerSocket(serverPort);
			Socket clientSocket = null;
			PrintWriter logWriter = new PrintWriter("resources/logFile.txt");
			while(true){
					clientSocket  = serverSocket.accept();
					BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					Double double1 = Double.parseDouble(bufferedReader.readLine());
					ProgressBarSyn.receiveProgress(localAddrString,double1);
					System.out.println("server receive:"+double1);
					logWriter.println("server receive:"+double1);
					bufferedReader.close();
					clientSocket.close();
			}

			//serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main (String [] args ){
		new Receiver();
		
	}
}
