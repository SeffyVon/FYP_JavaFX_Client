package controller;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
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
import model.User;
import view.GCell;
import view.UCell;
import Networks.ProgressBarSyn;


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
	private ListView UListView;
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
	ObservableList observableList2 = FXCollections.observableArrayList();
	
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
		//System.out.println(unameLabel.getText());
		//unameLabel.setText("doge");
		setGListView();
		setUListView();
		setGroupPaneRoot(-1);
		setMoviePaneRoot(-1);//-1 default for testing
	
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
	
	void setGroupPaneRoot(int groupNum){

		if(groupNum == -1)
			noMovie();
		else{
			setMovie(groupNum);
		}
		
	}
	
	void setMovie(int groupNum){
		
	}
	
	void noMovie(){
		movieTitleLabel.setText("");
		movieBriefTextArea.setText("");
		startTLabel.setText("");
		endTLabel.setText("");
		moviePosterImageView.setVisible(false);
		movieOwnerImageView.setVisible(false);
		watchButton.setText("ADD NOW!");
	}
	
	void setMoviePaneRoot(int groupNum){

		
		File file = new File("resources/video/2011-Mobile.mp4");
		String movieURL = file.toURI().toString();
		//String movieURL =  "http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv";
		setMoviePlayer( movieURL);
	}

	void setGListView(){
		gStringSet.add("Group 1");
	    gStringSet.add("Group 2");
	    gStringSet.add("Group 3");
	    gStringSet.add("Group 4");
	    gStringSet.add("Group 5");
	    gStringSet.add("Group 6");
	    observableList.setAll(gStringSet);
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
	//        GListView.scrollTo(selectedIndex);
	        
	    });
	}
	
	void setUListView(){
		uStringSet.add("doge");
	    uStringSet.add("cate");
	    observableList2.setAll(uStringSet);
	    UListView.setItems(observableList2);
	    UListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
	        @Override
	        public ListCell<String> call(ListView<String> UListView) {
	            return new UCell();
	        }
	    });
	}
	
	void setMoviePlayer(String movieAddress){
		Media mv = new Media(movieAddress);
		MediaPlayer mp = new MediaPlayer(mv);
		mp.setAutoPlay(false);
		
        volumeSlider.setValue(80);
        mp.setVolume(0.8);
		mediaView.setMediaPlayer(mp);
		ProgressBarSyn progressBarSyn = new ProgressBarSyn();
		progressBarSyn.setMediaPlayer(mp);
		
		mp.currentTimeProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {
				
				 double currentTime = mp.getCurrentTime().toSeconds();
		          Duration duration = mp.getTotalDuration();
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
