package controller;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import model.Group;
import model.Movie;
import model.User;

import org.controlsfx.dialog.Dialogs;
import org.json.JSONArray;
import org.json.JSONObject;

import tcp.FileReceiver;
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
	@FXML
	private ProgressBar networkProgressBar;
	@FXML
	private Label downloadingLabel;
	@FXML
	private Button addGroupButton;
	@FXML
	private Button addUserButton;
	@FXML
	private ImageView movieStarImageView;
	@FXML
	private Label ratingLabel;
	@FXML
	private Label watchTimeLabel;
	@FXML
	private Label toLabel;	
	@FXML
	private Label shareByLabel;
	@FXML
	private TreeView<String> groupTreeView;
	@FXML
	private ListView<String> chatListView;
	@FXML
	private TitledPane chatTitledPane;
	@FXML
	private TextField chatInputTextField;

 	
	Stage primaryStage;
	
	MediaPlayer mp;
	File currentVideoFile = null;
	
	String currentGroupName = null;
	
	User user = null;
	Movie currentMovie = null;
	private HashMap<String, Group> groupMap = new HashMap<String, Group>();
	private HashMap<String, User> userMap = new HashMap<String, User>();
	
	
	private Set<String> gStringSet = new HashSet<String>();
	ObservableList observableList = FXCollections.observableArrayList();
	
	private Set<String> uStringSet = new HashSet<String>();
	ObservableList<User> observableList2 = FXCollections.observableArrayList();
	
	ObservableList<String> observableList3 = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		groupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("You pressed group Button.");
                toGroupPane();
            }
        });
		
		movieButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(watchButton.getText()!="WATCH"){
            		System.out.println("This group doesn't have a movie yet!");
            	}else{
	                System.out.println("You pressed movie Button.");
	                toMoviePane();
            	}
            }
        });
		
		watchButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	System.out.println(watchButton.getText());
	        	if(watchButton.getText()=="ADD NOW!"){
	        		addMovie();
	        	}else if(watchButton.getText()=="DOWNLOAD"){//DOWNLOAD
					receiveFromUser();
	        	}else{//WATCH
	        		setMovieMediaPane();
	        		toMoviePane();
	        	}
	        }
		});
		
		messageButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	toMessagePane();
	        }

		});
		
		settingButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	toSettingPane();
	        }

		});
		
		
		addGroupButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	System.out.println("add group");
	    		Optional<String> response = Dialogs.create()
	    		        .owner(primaryStage)
	    		        .title("Create a group")
	    		        .masthead("Create a group")
	    		        .message("group name:")
	    		        .showTextInput("");
	        }
		});
		
		addUserButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	System.out.println("add user");
	    		Optional<String> response = Dialogs.create()
	    		        .owner(primaryStage)
	    		        .title("Invite a user")
	    		        .masthead("Who do you want to invite to this group?")
	    		        .message("user name:")
	    		        .showTextInput("");
	        }
		});
		
		File file = new File("resources/pic/users/doge.jpg");
		System.out.println(file.getAbsolutePath());
		Image image = new Image(file.toURI().toString());
		    
		userImageView.setImage(image);
		downloadingLabel.setVisible(false);
		networkProgressBar.setVisible(false);
		setLabelVisibility(false);
		System.out.println(unameLabel.getText());
		setMoviePane();
		toGroupPane();
		chatInputTextField.setEditable(true);
	}
	
	public void setTreeView(){
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				TreeItem<String> treeItemRoot = new TreeItem<> ("Root");
				
				Set<String> groupNames = groupMap.keySet();
				Iterator<String> groupNameIterator = groupNames.iterator();
				for(;groupNameIterator.hasNext();){
					String groupName = groupNameIterator.next();
					TreeItem<String> groupNodeItem = new TreeItem<>(groupName);
					Group group = groupMap.get(groupName);
					ArrayList<User> userList = group.getUserList();
					for(User user:userList){
						TreeItem<String> userTreeItem = new TreeItem<>(user.getUname(),user.getSmallImageView());
						groupNodeItem.getChildren().add(userTreeItem);
					}
					treeItemRoot.getChildren().add(groupNodeItem);
				}
				
				groupTreeView.setRoot(treeItemRoot);
				groupTreeView.setShowRoot(false);

				
			}
		});
		
		groupTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem>() {
            @Override
            public void changed(ObservableValue observable, TreeItem oldValue, TreeItem newValue) {
                if (newValue != null) {
                   System.out.println("select: " + newValue.getValue());
                   setChatPanel(newValue.getValue().toString());
                }
            }
        });
	}
	
	void setChatPanel(String uname){
		ArrayList<String> messageList = new ArrayList<String>();
		messageList.add("1");
		messageList.add("2");
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				chatTitledPane.setText("Message with " + uname);
			    observableList3.setAll(messageList);
			    chatListView.setItems(observableList3);
			}
		});

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

			    while(true){
			    	Platform.runLater(new Runnable(){
						@Override
						public void run() {
							observableList3.add("new message");
						}
			    	
			    	});
			    
			    	Thread.sleep(1000);
			    }
			}

		
		};
		new Thread(task).start();

	}
	
	public void setLabelVisibility(boolean visible){
		if(visible){
			moviePosterImageView.setVisible(true);
			movieOwnerImageView.setVisible(true);
			ratingLabel.setVisible(true);
			watchTimeLabel.setVisible(true);
			toLabel.setVisible(true);
			movieStarImageView.setVisible(true);
			startTLabel.setVisible(true);
			endTLabel.setVisible(true);
			shareByLabel.setVisible(true);
		}else{
			moviePosterImageView.setVisible(false);
			movieOwnerImageView.setVisible(false);
			ratingLabel.setVisible(false);
			watchTimeLabel.setVisible(false);
			toLabel.setVisible(false);
			movieStarImageView.setVisible(false);
			startTLabel.setVisible(false);
			endTLabel.setVisible(false);
			shareByLabel.setVisible(false);
			
		}
	}
	
	public void setUser(User user){
		this.user = user;
		unameLabel.setText(user.getUname());
	}
	
	public void setThisStage(Stage stage){
		this.primaryStage = stage;
	}
	
	
	protected void toMoviePane() {
		leftPane.setLayoutY(-600);
		
	}

	protected void toGroupPane() {
		leftPane.setLayoutY(0);		
		
	}
	
	void toMessagePane(){
		leftPane.setLayoutY(-1200);
	}
	
	void toSettingPane(){
		leftPane.setLayoutY(-1800);
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
				User user = new User(userJsonObject.getString("uname"), userJsonObject.getString("ipAddr"));
				userArrayList.add(user);
				if(!userMap.containsKey(user.getUname()))
					userMap.put(user.getUname(), user);
			}
			Group group = new Group(groupName, userArrayList, movie);	
			groupMap.put(groupName, group);
		}
	
		setTreeView();

	}
	public void noMovie() {
		setLabelVisibility(false);
		movieTitleLabel.setText("");
		movieBriefTextArea.setText("");
		startTLabel.setText("");
		endTLabel.setText("");
		watchButton.setText("ADD NOW!");
	}
	//http://code.makery.ch/blog/javafx-8-dialogs/
	void addMovie(){
		System.out.println("at add movie");

		//TextInputDialog movieInputDialog= new TextInputDialog();
//		Dialogs.create()
//        .owner(primaryStage)
//        .title("Information Dialog")
//        .masthead("Look, an Information Dialog")
//        .message("I have a great message for you!")
//        .showInformation();
		
		Optional<String> response = Dialogs.create()
		        .owner(primaryStage)
		        .title("Add a movie")
		        .masthead("Add a movie to your group")
		        .message("movie name")
		        .showTextInput("walter");
		
	}
	
	
	void downloadRequest(){
		System.out.println("at download Request");

		Alert dlg = createAlert(AlertType.CONFIRMATION);
		dlg.setTitle("Someone asks you to send them a video file!");
        dlg.show();
  
	}
	
	private Alert createAlert(AlertType type) {
        Window owner = primaryStage;
        Alert dlg = new Alert(type, "");
        dlg.initOwner(owner);
        return dlg;
    }
	
	void setMoviePane(){

		if(currentGroupName == null){
			noMovie();
			return;
		}
		
		currentMovie = groupMap.get(currentGroupName).getMovie();
		
		if( currentMovie == null){
			noMovie();
			
		}else{

			movieTitleLabel.setText(currentMovie.getMovieNameString());
			movieBriefTextArea.setText(currentMovie.getMovieBriefString());
			startTLabel.setText(currentMovie.getStartTimeString());
			endTLabel.setText(currentMovie.getEndTimeString());
			
			File file = new File("resources/pic/poster/"+currentMovie.getMovieFileNameString()+".jpg");
	        Image image = new Image(file.toURI().toString());
			moviePosterImageView.setImage(image);
			
			File file2 = new File("resources/pic/users/"+currentMovie.getOwnerNameString()+".jpg");
	        Image image2 = new Image(file2.toURI().toString());			
			movieOwnerImageView.setImage(image2);
			
			setLabelVisibility(true);
			
			Path path = Paths.get("resources/video/"+currentMovie.getMovieFileNameString()+".mp4");
			if (!Files.exists(path)) {
				watchButton.setText("DOWNLOAD");
			}else{
				watchButton.setText("WATCH");
			}
		}
	}
	
	void receiveFromUser(){
		String ipAddrString = "127.0.0.1";        
		FileReceiver fileReceiver = new FileReceiver();
		System.out.println("receive from ip");
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				downloadingLabel.setVisible(true);
				networkProgressBar.setVisible(true);
			}
			
		});
		fileReceiver.receiveFromIP(ipAddrString, currentMovie.getMovieFileNameString(), networkProgressBar);
		
	}

	@SuppressWarnings("unchecked")
	void setGListView(){
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
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
			        
			    	currentGroupName = newValue.toString();
			     	System.out.println("ListView Selection Changed (selected: " + currentGroupName + ")");
			        int groupNum = GListView.getSelectionModel().getSelectedIndex();
			        System.out.println("selected Group:"+groupNum);
			        
					setUListView();
					setMoviePane();
			        
			    });
				
			}
		});
		
	
	}
	
	void setUListView(){	
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Group group = groupMap.get(currentGroupName);
				ArrayList<User> userList = group.getUserList();

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
	
	@SuppressWarnings("unchecked")
	void setMovieMediaPane(){
	
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
