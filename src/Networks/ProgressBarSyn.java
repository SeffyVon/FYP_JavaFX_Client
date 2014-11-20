package Networks;

import config.Config;
import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public class ProgressBarSyn {
	static double progress = 0;

	
	
	static int serverPort = 15126;
	static int clientPort = 15124;
	
	static Receiver receiver = null;
	static Sender sender = null;

	static MediaPlayer mediaPlayer = null;
	
	public ProgressBarSyn(){
		Thread thread = new Thread(new ReceiverThreaded());
		thread.start();
		if(System.getProperty("os.name").equals("Mac OS X")){
			Config.localAddrString = Config.macAddrString;
			Config.otherAddrString = Config.linuxAddrString;
			Config.localPort = Config.macPort;
			Config.otherPort = Config.linuxPort;
		}else{
			Config.localAddrString = Config.linuxAddrString;
			Config.otherAddrString = Config.macAddrString;
			Config.localPort = Config.linuxPort;
			Config.otherPort = Config.macPort;
		}
		sender = new Sender();
	}
	
	
	static double getProgress(){
		return progress;
	}
	
	public static void sendProgress(double progress0){
		System.out.println("set Progress");
		sender.send(progress0);
	}
	
	public static void receiveProgress(String senderAddr, double progress0){
		if(mediaPlayer != null & !senderAddr.equals(Config.localAddrString)){
			Platform.runLater(new Runnable(){

				@Override
				public void run() {
					
					Duration duration = mediaPlayer.getMedia().getDuration();
                	mediaPlayer.seek(duration.multiply(progress0 / 100.0));
					
				}
				
			});
		}
	}

	public void setMediaPlayer(MediaPlayer mp) {
		mediaPlayer = mp;
		
	}
	

}
