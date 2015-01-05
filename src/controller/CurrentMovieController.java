package controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

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
import tcp.GroupRequest;
import tcp.ProgressBarSyn;
import config.Config;

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
	Button sendButton;
	@FXML
	Label movieTitleLabel;
	@FXML
	StackPane centerStackPane;
	@FXML
	TextField messageTextField;
	@FXML
	ListView GMessageListView;

	ObservableList<GMessage> observableList3 = FXCollections.observableArrayList();
	private HashMap<String, User> userMap = new HashMap<String, User>();
	String currentGroupName;
	String userName;
	
	Timer groupMessageTimer = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("init current movie controller");
		sendButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	Task<Void> t = new Task<Void>(){

					@Override
					protected Void call() throws Exception {
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								messageTextField.setText("");
							}
						});
						new GroupRequest().sendGroupMessage(currentGroupName, userName, messageTextField.getText(), new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString(), "01:01:01");

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
							System.out.println("send"+currentGroupName + " " +userName);
							new GroupRequest().sendGroupMessage(currentGroupName, userName, messageTextField.getText(), new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString(), "01:01:01");
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
	
	public void setUserMap(HashMap<String, User> userMap){
		this.userMap = userMap;
	}
	
	@SuppressWarnings("unchecked")
	void setMovieMediaPane(Movie currentMovie){
	
		System.out.println("filename" + currentMovie.getMovieFileNameString());
		Media mv = new Media(new File("resources/video/"+currentMovie.getMovieFileNameString()+".mp4").toURI().toString());
		MediaPlayer mp = new MediaPlayer(mv);
		mp.setAutoPlay(false);
		movieTitleLabel.setText(currentMovie.getMovieNameString());
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
            	mp.volumeProperty().setValue(0);
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
	
	public void setGroupName(String groupName){
		this.currentGroupName = groupName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	
	public void setGMessageListView(Group currentGroup){	
		
		if(groupMessageTimer!=null)
			groupMessageTimer.cancel();
		
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				observableList3 =  currentGroup.getList();
				GMessageListView.setItems(observableList3);

			    System.out.println(" the items in [set] from map: " + currentGroup.getList() + " from GMessage "  + GMessageListView.getItems() + GMessageListView.getItems().size());
			    
			    GMessageListView.setCellFactory(new Callback<ListView<GMessage>, ListCell<GMessage>>() {
			        @Override
			        public ListCell<GMessage> call(ListView<GMessage> GMListView) {
			            return new GMCell();
			        }
			    });
			}
		});
		
		groupMessageTimer = new Timer();
		groupMessageTimer.schedule(
			    new TimerTask() {

			        @Override
			        public void run() {
			     //   	System.out.println("Current group:" + currentGroupName);
			     //   	System.out.println("Receive new group messages last message time is:" + groupMap.get(currentGroupName).getLastMessageTime());
			        	GroupRequest groupRequest = new GroupRequest();
				    	ArrayList<GMessage> newMessageList = groupRequest.getGroupMessage(currentGroupName, currentGroup.getLastMessageTime());
						if(!newMessageList.isEmpty()){	
							currentGroup.setLastMessageTime(newMessageList.get(newMessageList.size()-1).getMessageTime());
							Platform.runLater(new Runnable(){
								@Override
								public void run() {
						//			System.out.println("Receive new group messages 2");
						//			System.out.println("1 observableList in add new items " + observableList3);
									observableList3.addAll(newMessageList);
						//			System.out.println("observableList in add new items " + observableList3);
									
								}
							});

						}
			        }
			    }, 0, Config.RefreshGroupMessageRate);
		

	}
	
	public class GMCell extends ListCell<GMessage>{
		
		@Override
		public void updateItem(GMessage gMessage, boolean empty){
		    super.updateItem(gMessage,empty);
		    if(gMessage != null) {
		        GMData data = new GMData();
		        Platform.runLater(new Runnable(){
					@Override
					public void run() {
						data.setImage(userMap.get(gMessage.getUname()).getMiddleImage());
				        data.setInfo(gMessage.getMessage());
				        setGraphic(data.getBox());
					}
		        	
		        });

		    }
		}

	}

}
