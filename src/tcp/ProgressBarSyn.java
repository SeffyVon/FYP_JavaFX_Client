package tcp;

import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import udp.UDPReceiver;
import udp.UDPSender;
import config.Config;


public class ProgressBarSyn {
	static double progress = 0;
	
	static UDPReceiver receiver = null;
	static UDPSender sender = null;

	static MediaPlayer mediaPlayer = null;
	static boolean isSyn;
	
	public ProgressBarSyn(){
		receiver = new UDPReceiver();
		sender = new UDPSender();
	}
	
	public void stopProgressSyn(){
		receiver.stopReceiver();
		sender = null;
	}
	
	public void resumeProgressSyn(){
		receiver.resumeReceiver();
		sender = new UDPSender();
	}
	
	static double getProgress(){
		return progress;
	}
	
	public static void sendProgress(double progress0){
		if(isSyn==false){
			return;
		}
		System.out.println("set Progress");
		try {
			sender.send(progress0);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static void receiveProgress(String senderAddr, double progress0){
		
		if(mediaPlayer != null & !senderAddr.equals(Config.localAddrString)){
			Platform.runLater(new Runnable(){

				@Override
				public void run() {
					System.out.println("server receive and set progress:"+progress0);
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
