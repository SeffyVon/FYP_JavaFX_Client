package tcp;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import model.GMessage;
import websocket.ChatClientEndpoint;
import config.Profile;
import controller.CurrentMovieController;


public class ProgressBarSyn {


	private static CurrentMovieController currentMovieController = null;

	
	public static void stopProgressSyn(){
		currentMovieController = null;
	}

	public static void sendPlaying(boolean isPlaying){
		System.out.println("send Playing");
		 if(currentMovieController!=null){
			if(isPlaying)
<<<<<<< HEAD
				ChatClientEndpoint.sendGMessage(new GMessage("Sync", "played", "", "", Profile.currentUser.getUname(), Profile.currentGroup.getGroupName()));
			else {
				ChatClientEndpoint.sendGMessage(new GMessage("Sync", "paused", "", "", Profile.currentUser.getUname(), Profile.currentGroup.getGroupName()));
=======
				ChatClientEndpoint.sendGMessage(new GMessage("Sync", "Played", "", "", Profile.currentUser.getUname(), Profile.currentGroup.getGroupName()));
			else {
				ChatClientEndpoint.sendGMessage(new GMessage("Sync", "Paused", "", "", Profile.currentUser.getUname(), Profile.currentGroup.getGroupName()));
>>>>>>> FETCH_HEAD
			}
		 }
	}
	
	public static void receiveGMessage(GMessage gMessage){
		if(currentMovieController!=null){
<<<<<<< HEAD
			if(gMessage.getMessageText().equals("played") ){
				receivePlaying(gMessage.getUname(), true);
			}else if(gMessage.getMessageText().equals("paused")){
				receivePlaying(gMessage.getUname(), false);
			}else if(gMessage.getMessageText().equals("forwarded") || gMessage.getMessageText().equals("rewinded")){
				receiveProgress(gMessage.getUname(),Double.parseDouble(gMessage.getMovieTime())); // Correspondent to String.valueOf(progress0)
=======
			if(gMessage.getMessageText().equals("Played") ){
				receivePlaying(gMessage.getUname(), true);
			}else if(gMessage.getMessageText().equals("Paused")){
				receivePlaying(gMessage.getUname(), false);
			}else if(gMessage.getMessageText().equals("Forwarded") || gMessage.getMessageText().equals("Rewinded")){
				receiveProgress(gMessage.getUname(),Double.parseDouble(gMessage.getMovieTime()));
>>>>>>> FETCH_HEAD
			}
		}
	}
	
	public static void sendProgress(String playBackMessage, double progress0){
		
		System.out.println("send Progress");
		if(currentMovieController!=null){
<<<<<<< HEAD
			ChatClientEndpoint.sendGMessage(new GMessage("Sync", playBackMessage, String.valueOf(progress0), "",Profile.currentUser.getUname(), Profile.currentGroup.getGroupName()));
=======
			ChatClientEndpoint.sendGMessage(new GMessage("Sync", playBackMessage, String.valueOf(progress0), Profile.currentUser.getUname(), "", Profile.currentGroup.getGroupName()));
>>>>>>> FETCH_HEAD
			}
	}
	
	public static void receiveProgress(String senderName, double progress0){
		
		if(currentMovieController != null & !senderName.equals(Profile.currentUser.getUname())){
			Platform.runLater(new Runnable(){

				@Override
				public void run() {
					System.out.println("server receive and set progress:"+progress0);
					currentMovieController.setProgress(progress0);
				}
				
			});
		}
	}
	
	public static void receivePlaying(String senderName, boolean isPlaying){
		if(currentMovieController != null & !senderName.equals(Profile.currentUser.getUname())){
			Platform.runLater(new Runnable(){

				@Override
				public void run() {
					System.out.println("server receive and set playing:"+ isPlaying);
					if(isPlaying){
		                currentMovieController.setPlay();

					}else{
		                currentMovieController.setPause();
					}
				}
				
			});
		}
	}

	public static void beginProgressSyn(CurrentMovieController currentMovieController) {
		
		// receive from the system...
		
		ProgressBarSyn.currentMovieController = currentMovieController;
		
	}

	

}
