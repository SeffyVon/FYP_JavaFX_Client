package Networks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import config.Config;

public class Sender {
	
	static double progress = 0;


	static PrintWriter logWriter = null;
	
	public Sender(){
		Socket clientSocket = null;
		System.out.println(Config.localAddrString+"sender comes");
		
		
		try {
			logWriter = new PrintWriter("resources/logFileSender.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	static public void send(double progressNum){
		Socket clientSocket = null;
		System.out.println("sender comes");
		try {
			clientSocket = new Socket(Config.otherAddrString,Config.otherPort);
			logWriter.println("sender socket create");
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
			out.println(progressNum);
			out.flush();
			out.close();
			
			logWriter.println(progressNum);
			clientSocket.close();

			logWriter.println("sender closes");
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public static void main (String [] args ){
		Sender sender = new Sender();
		sender.send(12.2);
		sender.send(12.30);
		sender.send(13.30);
		//new Sender(12.02);
		System.out.println("sender closes!!!!");
		
	}
}
