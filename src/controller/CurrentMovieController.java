package controller;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;

import javax.websocket.Endpoint;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Callback;
import javafx.util.Duration;
import model.GMessage;
import model.Group;
import model.Movie;
import model.User;
import tcp.ProgressBarSyn;
import view.UCell;
import websocket.ChatClientEndpoint;
import config.Profile;

public class CurrentMovieController implements Initializable{
	
	@FXML
	Label groupName;
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
	public Button playButton;
	@FXML
	Button soundButton;
	@FXML
	Button returnButton;
	@FXML
	Button sendButton;
	@FXML
	Label movieTitleLabel;
	@FXML
	StackPane centerStackPane;
	@FXML
	TextField messageTextField;
	@FXML
	ListView<GMessage> GMessageListView;
	@FXML
	ListView<User> UListView;
	ObservableList<User> observableList2 = FXCollections.observableArrayList(); // user list
	ObservableList<GMessage> observableList3 = FXCollections.observableArrayList(GMessage.extractor()); // group message list

	public MediaPlayer mediaPlayer = null;
	
	Timer groupMessageTimer = null;
	double lastProgress = 0;
	
	Image imagePause = new Image(getClass().getResourceAsStream("../resources/pause2.png"));
	Image imagePlay = new Image(getClass().getResourceAsStream("../resources/play.png"));

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("init current movie controller");
		setGMessageListView();
		setUListView();
		groupName.setText(Profile.currentGroup.getGroupName());
		sendButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	Task<Void> t = new Task<Void>(){

					@Override
					protected Void call() throws Exception {
						ChatClientEndpoint.sendGMessage(new GMessage("Chat", messageTextField.getText(),"" ,new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()),Profile.currentUser.getUname(),"","group0"));
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								messageTextField.setText("");
							}
						});
						
						return null;
					}
					
				};
				new Thread(t).start();
	        }
		});
		

		
		
		messageTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent ke) {
				if(ke.getCode() == KeyCode.ENTER){
					Task<Void> t = new Task<Void>(){

						@Override
						protected Void call() throws Exception {
							ChatClientEndpoint.sendGMessage(new GMessage("Chat", messageTextField.getText(),"" ,new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()),Profile.currentUser.getUname(),"","group0"));
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									messageTextField.setText("");
								}
							});
							return null;
						}
						
					};
					new Thread(t).start();
				}
			}
		});
	}
	
	
	@SuppressWarnings("unchecked")
	void setMovieMediaPane(Movie currentMovie){
	
		System.out.println("filename" + currentMovie.getMovieFileNameString());
		Media mv = new Media(new File("resources/video/"+currentMovie.getMovieFileNameString()+".mp4").toURI().toString());
		mediaPlayer = new MediaPlayer(mv);
		mediaPlayer.setAutoPlay(false);
		movieTitleLabel.setText(currentMovie.getMovieNameString());
        volumeSlider.setValue(80);
        mediaPlayer.setVolume(0.8);
		mediaView.setMediaPlayer(mediaPlayer);
		ProgressBarSyn.beginProgressSyn(this);
		
		
		mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {

			@Override
			public void changed(ObservableValue<? extends Duration> observable,
					Duration oldValue, Duration newValue) {
				 double currentTime = mediaPlayer.getCurrentTime().toSeconds();
		          Duration duration = mediaPlayer.getTotalDuration();
		          if((-newValue.toSeconds()+mediaPlayer.getTotalDuration().toSeconds())<1){
		      		  mediaPlayer.pause();
		      		  mediaPlayer.seek(mediaPlayer.getStartTime());
		      		  timeSlider.setValue(0);
		      		  mediaPlayer.play();		      		
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
		
		mediaPlayer.volumeProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {
				  
		          volumeSlider.setValue(mediaPlayer.getVolume()*100.0);
			}
            });
		
		
		playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(playButton.getText().equals("Pause")){
	                System.out.println("You pressed Pause Button.");
	                setPause();
	                ProgressBarSyn.sendPlaying(false);
            	}else{
	                System.out.println("You pressed Play Button.");
	                setPlay();   
	                ProgressBarSyn.sendPlaying(true);
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
            	mediaPlayer.volumeProperty().setValue(0);
            	ProgressBarSyn.stopProgressSyn();
        		ChatClientEndpoint.closeChatClientEndpoint();
            }
        });
		timeSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
			public void changed(ObservableValue observable, Boolean wasChanging,
                    Boolean changing) {

                if (changing) {
                	lastProgress = timeSlider.getValue();
                	System.out.println("Start:"+lastProgress);
                	
                } else {
                	double currentProgress = timeSlider.getValue();
                	System.out.println("End:"+currentProgress);
                	if(currentProgress>lastProgress)
                		ProgressBarSyn.sendProgress("Forwarded", timeSlider.getValue());
                	else if(currentProgress<lastProgress)
                		ProgressBarSyn.sendProgress("Rewinded", timeSlider.getValue());
                	
                	setProgress(currentProgress);  	
                }
            }
        });
		
		volumeSlider.valueProperty().addListener(new InvalidationListener() {
		    public void invalidated(Observable ov) {
		       if(volumeSlider.isValueChanging()){
		    	   mediaPlayer.setVolume(volumeSlider.getValue()/100.0);
		       }
		    }
		});
	}
	
	public void setCenterStackPane(StackPane centerStackPane){
		this.centerStackPane = centerStackPane;
	}
	
	public void setUListView(){	
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ArrayList<User> userList = Profile.currentGroup.getUserList();

			    observableList2.setAll(userList);
			    UListView.setItems(observableList2);
			    UListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
			        @Override
			        public ListCell<User> call(ListView<User> UListView) {
			            return new UCell();
			        }
			    });
			}
		});

	}
	
	public void setGMessageListView(){	
		
		if(groupMessageTimer!=null)
			groupMessageTimer.cancel();
		
		ChatClientEndpoint.createChatClientEndpoint(observableList3);
		
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
			
				GMessageListView.setItems(observableList3);

			    System.out.println(" SET Observable List " + Profile.currentGroup.getList() + " from GMessage "  + GMessageListView.getItems() + GMessageListView.getItems().size());
			    GMessageListView.setCellFactory((ListView<GMessage> l) -> new GMCell());
			}
		});
		
	

	}
	
	public class GMCell extends ListCell<GMessage>{
		@Override
		public void updateItem(GMessage gMessage, boolean empty){
			
		    super.updateItem(gMessage,empty);
		    if(!empty && (gMessage != null)) {
		    	//System.out.println("Observable list size"+ observableList3.size()+gMessage.getInterpretText());
		        Platform.runLater(new Runnable(){
					@Override
					public void run() {
						 setText(gMessage.getInterpretText());
					}
		        });

		    }else{
		    	Platform.runLater(new Runnable(){
						@Override
						public void run() {
							setText(null);
						}
		    	});

		    }
		}

	}

	public void setPlay() {
        Platform.runLater(new Runnable() {
			@Override
			public void run() {
				 playButton.setGraphic(new ImageView(imagePause));
				 playButton.setText("Pause");
				 mediaPlayer.play();
			}
		});
		
	}


	public void setPause() {
        Platform.runLater(new Runnable() {
			@Override
			public void run() {
				 playButton.setGraphic(new ImageView(imagePlay));	 
				 playButton.setText("Play");
				 mediaPlayer.pause();
			}
		});
		
	}
	
	public void setProgress(double progress){
		Duration duration = mediaPlayer.getMedia().getDuration();
		mediaPlayer.seek(duration.multiply(progress / 100.0));
		
	}
	
   

}
