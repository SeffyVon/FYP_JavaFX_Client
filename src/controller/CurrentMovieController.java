package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import model.Movie;
import tcp.ProgressBarSyn;

public class CurrentMovieController implements Initializable{
	@FXML
	MediaView mediaView;
	@FXML
	Group controlGroup;
	@FXML
	Pane mediaControlPane;
	@FXML
	Slider timeSlider;
	@FXML
	Slider volumeSlider;	
	@FXML
	Button playButton;
	@FXML
	Button soundButton;
	@FXML
	Button returnButton;
	@FXML
	StackPane centerStackPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("init");
//		mediaView.setOnMouseEntered(new EventHandler<Event>() {
//			@Override
//			public void handle(Event event) {
//				controlGroup.setVisible(true);
//			}
//		});
//		mediaView.setOnMouseExited(new EventHandler<Event>() {
//			
//			@Override
//			public void handle(Event event) {
//				controlGroup.setVisible(false);
//			}
//		});
	}
	
	@SuppressWarnings("unchecked")
	void setMovieMediaPane(Movie currentMovie){
		System.out.println("filename" + currentMovie.getMovieFileNameString());
		Media mv = new Media(new File("resources/video/"+currentMovie.getMovieFileNameString()+".mp4").toURI().toString());
		MediaPlayer mp = new MediaPlayer(mv);
		mp.setAutoPlay(false);
		
        volumeSlider.setValue(80);
        mp.setVolume(0.8);
		mediaView.setMediaPlayer(mp);

		ProgressBarSyn progressBarSyn = new ProgressBarSyn();
		progressBarSyn.setMediaPlayer(mp);
		
		mp.currentTimeProperty().addListener(new ChangeListener<Duration>() {

			@Override
			public void changed(ObservableValue<? extends Duration> observable,
					Duration oldValue, Duration newValue) {
				 double currentTime = mp.getCurrentTime().toSeconds();
		          Duration duration = mp.getTotalDuration();
		          if((-newValue.toSeconds()+mp.getTotalDuration().toSeconds())<1){
		      		  mp.pause();
		      		  mp.seek(mp.getStartTime());
		      		  timeSlider.setValue(0);
		      		  mp.play();		      		
		      	  }
		      		
		          if(duration == Duration.UNKNOWN)
		        	  return;
		          timeSlider.setDisable(duration.isUnknown());
		          if (!timeSlider.isDisabled() 
		            && duration.greaterThan(Duration.ZERO) 
		            && !timeSlider.isValueChanging()) {
		        	  Platform.runLater(new Runnable(){

						@Override
						public void run() {
							 timeSlider.setValue(currentTime/duration.toSeconds()* 100.0);
						}
		        		  
		        	  });
		             
		          }
				
			}
            });
		
		mp.volumeProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {
				  
		          volumeSlider.setValue(mp.getVolume()*100.0);
			}
            });
		
		Image imagePause = new Image(getClass().getResourceAsStream("../resources/pause2.png"));
		Image imagePlay = new Image(getClass().getResourceAsStream("../resources/play.png"));
		
		
		playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	System.out.println("You pressed Pause Button.");
            	if(playButton.getText().equals("Pause")){
	                System.out.println("You pressed Pause Button.");
	                playButton.setGraphic(new ImageView(imagePlay));	  
	                playButton.setText("Play");
	                mp.pause();
            	}else{
            		
	                System.out.println("You pressed Play Button.");
	                playButton.setText("Pause");
	                playButton.setGraphic(new ImageView(imagePause));
	                mp.play();           		
            	}
            }
        });
		
		
		soundButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(volumeSlider.isVisible())
            		volumeSlider.setVisible(false);
            	else
            		volumeSlider.setVisible(true);
            }
        });
		
		returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	centerStackPane.getChildren().remove(centerStackPane.getChildren().size()-1);
            }
        });
		timeSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
			public void changed(ObservableValue observable, Boolean wasChanging,
                    Boolean changing) {

                if (changing) {
                	System.out.println("Start:"+timeSlider.getValue());
                } else {
                	System.out.println("Finish:"+timeSlider.getValue());
                	Duration duration = mp.getMedia().getDuration();
            
                	mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
                	
                	progressBarSyn.sendProgress(timeSlider.getValue());
                }
            }
        });
		
		volumeSlider.valueProperty().addListener(new InvalidationListener() {
		    public void invalidated(Observable ov) {
		       if(volumeSlider.isValueChanging()){
		    	   mp.setVolume(volumeSlider.getValue()/100.0);
		       }
		    }
		});
	}
	
	public void setCenterStackPane(StackPane centerStackPane){
		this.centerStackPane = centerStackPane;
	}
	
	

}
