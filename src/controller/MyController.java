package controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import model.Group;
import model.Movie;
import model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import tcp.GroupRequest;
import tcp.ProgressBarSyn;
import view.GCell;
import view.UCell;


public class MyController implements Initializable {
	
	@FXML
	private Button watchButton;
	@FXML
	private Button groupButton;
	@FXML
	private Button movieButton;
	@FXML
	private Button messageButton;
	@FXML
	private Button settingButton;
	@FXML
	private ListView GListView;
	@FXML
	private ListView<User> UListView;
	@FXML
	private Pane leftPane;
	@FXML 
	private MediaView mediaView;
	@FXML
	private Slider timeSlider;
	@FXML
	private	Slider volumeSlider;	
	@FXML
	private Button playButton;
	@FXML
	private Pane moviePane;
	@FXML
	private Label movieTitleLabel;
	@FXML
	private TextArea movieBriefTextArea;
	@FXML
	private Label startTLabel;
	@FXML
	private Label endTLabel;
	@FXML
	private ImageView moviePosterImageView;
	@FXML
	private ImageView movieOwnerImageView;
	@FXML
	private ImageView userImageView;
	@FXML
	private Label unameLabel;
	
	Stage primaryStage;
	MediaPlayer mp;
	int groupNum = 0;
	
	User user = null;
	
	private Set<String> gStringSet = new HashSet<String>();
	ObservableList observableList = FXCollections.observableArrayList();
	
	private Set<String> uStringSet = new HashSet<String>();
	ObservableList<User> observableList2 = FXCollections.observableArrayList();
	
	private HashMap<String, Group> groupMap = new HashMap<String, Group>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		groupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("You pressed group Button.");
                MovieToGroupPane();
            }
        });
		
		movieButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("You pressed message Button.");
                GroupToMoviePane();
                
            }
        });
		File file = new File("resources/pic/users/doge.jpg");
		System.out.println(file.getAbsolutePath());
		Image image = new Image(file.toURI().toString());
		    
		userImageView.setImage(image);
		System.out.println(unameLabel.getText());
		//unameLabel.setText("doge");
		//prepareGroups();
		setMoviePane(null);
		
	
	}
	
	public void setUser(User user){
		this.user = user;
		unameLabel.setText(user.getUname());
	}
	
	public void setThisStage(Stage stage){
		this.primaryStage = stage;
	}
	
	
	protected void GroupToMoviePane() {
		leftPane.setLayoutY(-600);
		
	}

	protected void MovieToGroupPane() {
		leftPane.setLayoutY(0);		
		
	}
	
	void prepareGroups(){
		
		JSONObject jsonObject= GroupRequest.getGroupMems("doge");
		ArrayList<String> groupNameArrayList = new ArrayList<String>();
		for(Iterator iterator = jsonObject.keys(); iterator.hasNext();){
			groupNameArrayList.add(iterator.next().toString());
		}

		for(String groupName:groupNameArrayList){
			JSONObject groupJsonObject = jsonObject.getJSONObject(groupName);
			JSONArray userJsonArray = groupJsonObject.getJSONArray("User");
			JSONObject movieJsonObject = groupJsonObject.getJSONObject("movie"); 
			Movie movie = null;
			if(!movieJsonObject.getString("movieName").equals("null")){
				System.out.println(movieJsonObject.getString("movieName"));
				System.out.println(movieJsonObject.getString("movieName").equals("null"));
				movie = new Movie(movieJsonObject.getString("movieName"),
						movieJsonObject.getString("owner"), 
						movieJsonObject.getString("brief"),
						movieJsonObject.getString("startTime"),
						movieJsonObject.getString("endTime"));
			}
			ArrayList<User> userArrayList = new ArrayList<User>();
			for(int i=0;i<userJsonArray.length();i++){
				JSONObject userJsonObject = userJsonArray.getJSONObject(i);
				userArrayList.add(new User(userJsonObject.getString("uname"), userJsonObject.getString("ipAddr")));

			}
			Group group = new Group(groupName, userArrayList, movie);
			groupMap.put(groupName, group);


		}

	}
	public void noMovie() {
		movieTitleLabel.setText("");
		movieBriefTextArea.setText("");
		startTLabel.setText("");
		endTLabel.setText("");
		moviePosterImageView.setVisible(false);
		movieOwnerImageView.setVisible(false);
		watchButton.setText("ADD NOW!");
	}
	
	
	void setMoviePane(String groupName){
		if(groupName == null){
			noMovie();
			return;
		}
		
		Movie movie = groupMap.get(groupName).getMovie();
		if( movie == null){
			noMovie();
			
		}else{
			movieTitleLabel.setText(movie.getMovieNameString());
			movieBriefTextArea.setText(movie.getMovieBriefString());
			startTLabel.setText(movie.getStartTimeString());
			endTLabel.setText(movie.getEndTimeString());
			
			moviePosterImageView.setVisible(true);
			movieOwnerImageView.setVisible(true);
			
			File file = new File("resources/pic/poster/"+movie.getMovieFileNameString()+".jpg");
	        Image image = new Image(file.toURI().toString());
			moviePosterImageView.setImage(image);
			
			File file2 = new File("resources/pic/users/"+movie.getOwnerNameString()+".jpg");
	        Image image2 = new Image(file2.toURI().toString());			
			movieOwnerImageView.setImage(image2);
			
			watchButton.setText("WATCH");
			
			File file3 = new File("resources/video/2011-Mobile.mp4");
			String movieURL = file3.toURI().toString();
			setMovieMediaPane( movieURL);
			
		}
	}
	


	void setGListView(){
		JSONObject jsonObject= GroupRequest.getGroupMems(user.getUname());
		ArrayList<String> groupNameArrayList = new ArrayList<String>();
		for(Iterator iterator = jsonObject.keys(); iterator.hasNext();){
			groupNameArrayList.add(iterator.next().toString());
		}
		
	    observableList.setAll(groupNameArrayList);
	    GListView.setItems(observableList);
	    GListView.setCellFactory(new Callback<ListView<String>,ListCell<String>>() {
	        @Override
	        public ListCell<String> call(ListView<String> GListView) {
	            return new GCell();
	        }
	    });
	    
	    // Handle ListView selection changes.
	    // http://code.makery.ch/blog/javafx-8-event-handling-examples/
	    GListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	        System.out.println("ListView Selection Changed (selected: " + newValue.toString() + ")");
	        int selectedIndex = GListView.getSelectionModel().getSelectedIndex();
	        System.out.println("selectedIndex:"+selectedIndex);
			setUListView(newValue.toString());
			setMoviePane(newValue.toString());
	        
	    });
	}
	
	void setUListView(String groupName){
		
		Group group = groupMap.get(groupName);
		ArrayList<User> userList = group.getUserList();
		
		//uStringSet.add("doge");
	   // uStringSet.add("cate");
	    observableList2.setAll(userList);
	    UListView.setItems(observableList2);
	    UListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
	        @Override
	        public ListCell<User> call(ListView<User> UListView) {
	            return new UCell();
	        }
	    });
	}
	
	void setMovieMediaPane(String movieAddress){
	
		Media mv = new Media(movieAddress);
		MediaPlayer mp = new MediaPlayer(mv);
		mp.setAutoPlay(false);
		
        volumeSlider.setValue(80);
        mp.setVolume(0.8);
		mediaView.setMediaPlayer(mp);
	//	mp.setCycleCount(MediaPlayer.INDEFINITE);
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
		              timeSlider.setValue(currentTime/duration.toSeconds()* 100.0);
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
	



}

//http://stackoverflow.com/questions/19588029/customize-listview-in-javafx-with-fxml
