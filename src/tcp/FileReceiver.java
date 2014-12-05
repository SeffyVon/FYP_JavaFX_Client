package tcp;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class FileReceiver {
	
	public boolean receiveFromIP(String ipAddr, String movieFileName, ProgressBar networkProgressBar){

		Task<Void> task = new Task<Void>() {	
			@Override
			protected Void call() throws Exception {
				
				System.out.println("Task2 ");
				updateProgress(2,10);
				System.out.println("Task3");
				int filesize=1922805507;
        	    int bytesRead;
        	    int currentTot = 0;
        	    System.out.println("Task4");
    		    byte [] bytearray  = new byte [filesize];
    		    InputStream is = null;
    		    Socket socket = null;
    		    System.out.println("New socket 0");
        		try {
        			
        			socket = new Socket("127.0.0.1",15123);
        			System.out.println("New socket 1");
        			//PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        		
        		    is = socket.getInputStream();
        		}
        		catch(Exception e){
        			networkProgressBar.setVisible(false);
        			return null;
        		}
        		try{
        		    FileOutputStream fos;
        		    
        		    updateProgress(4,10);
    				
        			fos = new FileOutputStream("resources/video/"+movieFileName+".mp4");
        		    BufferedOutputStream bos = new BufferedOutputStream(fos);
        		   
        		    bytesRead = is.read(bytearray,0,bytearray.length);
        		    currentTot = bytesRead;
        		
        		    do {
        		       bytesRead =
        		          is.read(bytearray, currentTot, (bytearray.length-currentTot));
        		       if(bytesRead >= 0) {
        		    	   currentTot += bytesRead;
        		    	   final double currentTot2= currentTot;
        		    	 
        		    		updateProgress(currentTot2,bytearray.length);
        					
                           System.out.println(new Double(currentTot)/bytearray.length+" currentTot: " + currentTot + "byteRead" + bytearray.length);
        		       }
        		    } while(bytesRead > -1);
        		    
        		    updateProgress(10,10);
        		    
        		    bos.write(bytearray, 0 , currentTot);
        		    bos.flush();
        		    bos.close();
        		    socket.close();
        		    Platform.runLater(new Runnable(){

    					@Override
    					public void run() {
    	    				networkProgressBar.setVisible(false);
    					}
    					
    				});
    			
    				
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
				return null;
				
				
			}
		};
		networkProgressBar.progressProperty().bind(task.progressProperty());
		new Thread(task).start();
		
	            	
//				}
//			}
//				);

		return true;
	}
	
	public static void main (String [] args ) throws IOException {
	   FileReceiver fileReceiver = new FileReceiver();
	
  }
}


//Read more: http://mrbool.com/file-transfer-between-2-computers-with-java/24516#ixzz3HCLCdZ4t

