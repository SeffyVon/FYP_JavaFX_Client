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

import org.controlsfx.dialog.Dialogs;
import org.json.JSONArray;
import org.json.JSONObject;

import tcp.FileReceiver;
import tcp.GroupRequest;
import view.GCell;
import view.UCell;
import model.Group;
import model.Movie;
import model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

public class CinemaController implements Initializable  {

	@FXML
	BorderPane moviePane;
	@FXML
	Button watchButton;
	@FXML
	Button rightButton;
	@FXML
	Button leftButton;
	@FXML
	ImageView movieOwnerImageView;
	@FXML
	ImageView moviePosterImageView;
	@FXML
	ImageView movieStarImageView;
	@FXML
	ImageView userImageView;
	@FXML
	Label unameLabel;
	@FXML
	Label endTLabel;
	@FXML
	Label startTLabel;
	@FXML
	Label movieTitleLabel;
	@FXML
	Label ratingLabel;
	@FXML
	Label shareByLabel;
	@FXML
	Label toLabel;
	@FXML
	Label watchTimeLabel;
	@FXML
	ListView<User> UListView;
	@FXML
	private ListView GListView;
	@FXML
	TextArea movieBriefTextArea;
	@FXML
	ProgressBar networkProgressBar;
 	
	Stage primaryStage;
	
	MediaPlayer mp;
	File currentVideoFile = null;	
	Movie currentMovie = null;
	
	String currentGroupName = null;	
	User user = null;
	private HashMap<String, Group> groupMap = new HashMap<String, Group>();
	private HashMap<String, User> userMap = new HashMap<String, User>();
	private Set<String> gStringSet = new HashSet<String>();
	ObservableList observableList = FXCollections.observableArrayList();
	private Set<String> uStringSet = new HashSet<String>();
	ObservableList<User> observableList2 = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		networkProgressBar.setVisible(false);
		
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
		userImageView.setImage(user.getMiddleImage());
	}
	
	public void setThisStage(Stage stage){
		this.primaryStage = stage;
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
	
	}
	
	public void noMovie() {
		setLabelVisibility(false);
		movieTitleLabel.setText("");
		movieBriefTextArea.setText("");
		startTLabel.setText("");
		endTLabel.setText("");
		watchButton.setText("ADD NOW!");
	}
	
	void addMovie(){
		System.out.println("at add movie");
		
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
	
	void selectGroup(String groupName){
		
	}

	
}
