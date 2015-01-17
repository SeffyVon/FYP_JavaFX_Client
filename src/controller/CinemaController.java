package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import model.Group;
import model.Movie;
import model.User;

import org.controlsfx.dialog.Dialogs;
import org.json.JSONArray;
import org.json.JSONObject;

import tcp.GroupRequest;
import view.GCell;
import view.UCell;
import config.Interface;
import config.Profile;

public class CinemaController implements Initializable {

	protected static final int CopyOnWriteArrayList = 0;
	@FXML
	BorderPane moviePane;
	@FXML
	Button watchButton;
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
	ListView GListView;
	@FXML
	TextArea movieBriefTextArea;

	@FXML
	StackPane centerStackPane;
	@FXML
	StackPane innerStackPane;
	@FXML
	Pane currentMoviePane;


	GroupRequest groupRequest = new GroupRequest();
 	
	Stage primaryStage;
	
	MediaPlayer mp;
	File currentVideoFile = null;	
	Movie currentMovie = null;



	ObservableList observableList = FXCollections.observableArrayList();

	ObservableList<User> observableList2 = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		Interface.cinemaController = this;

		
		watchButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	
	        	Platform.runLater(new Runnable(){

					@Override
					public void run() {
						
			        	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/CinemaRoom.fxml"));    
			    	    try {
			    	        currentMoviePane = (Pane) fxmlLoader.load();
			    	        CurrentMovieController currentMovieController = fxmlLoader.getController();
			    	        if(currentMovie == null)
			    	        	return;
<<<<<<< HEAD
			    	        currentMovieController.setMovieMediaPane();
=======
			    	        currentMovieController.setMovieMediaPane(currentMovie);
>>>>>>> FETCH_HEAD
			    	       } catch (IOException e) {
			    	        throw new RuntimeException(e);
			    	    }
			    	    //centerStackPane.getChildren().get(0);
			    	    //centerStackPane.getChildren().remove(0);
			    	    centerStackPane.getChildren().add(1, currentMoviePane);
			    	    
					}
	        		
	        	});
	        	
	
	        }
		});
		

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
	
	public void setUser(){
		unameLabel.setText(Profile.currentUser.getUname());
		userImageView.setImage(Profile.currentUser.getMiddleImage());
	}
	
	public void setThisStage(Stage stage){
		this.primaryStage = stage;
	}
	
	void prepareGroups(){
		
		boolean hasFirst = false;
		
		JSONObject jsonObject= groupRequest.getGroupMems(Profile.currentUser.getUname());
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
						movieJsonObject.getString("endTime"),
						movieJsonObject.getString("ownerIP"),
						movieJsonObject.getInt("port"),
						movieJsonObject.getInt("filesize")
						);
			}
			ArrayList<User> userArrayList = new ArrayList<User>();
			for(int i=0;i<userJsonArray.length();i++){
				JSONObject userJsonObject = userJsonArray.getJSONObject(i);
				User user = new User(userJsonObject.getString("uname"), userJsonObject.getString("ipAddr"));
				userArrayList.add(user);
				if(!Profile.userMap.containsKey(user.getUname()))
					Profile.userMap.put(user.getUname(), user);
			}
			Group group = new Group(groupName, userArrayList, movie);	
			if(hasFirst == false){
				Profile.currentGroup = group;
				hasFirst = true;
				
			}
			Profile.groupMap.put(groupName, group);
		}
		
		
	
	}
	
	public void noMovie() {
		if(moviePane.isVisible()){
			moviePane.setVisible(false);
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/AddNewMovie.fxml"));
			try {
				BorderPane moviePane2 = fxmlLoader.load();
				innerStackPane.getChildren().add(moviePane2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UploadController uploadController = fxmlLoader.getController();
			
		}
		
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

		if(Profile.currentGroup == null){
			noMovie();
			return;
		}
		
		currentMovie = Profile.currentGroup.getMovie();
		System.out.println("setcurrentMovie"+currentMovie);
		
		if( currentMovie == null){
			noMovie();
			return;
		}else{
			if(!moviePane.isVisible()){
				innerStackPane.getChildren().remove(innerStackPane.getChildren().size()-1);
				moviePane.setVisible(true);
			}
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
			
			
		}
	}
	
	@SuppressWarnings("unchecked")
	void setGListView(){
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				JSONObject jsonObject= groupRequest.getGroupMems(Profile.currentUser.getUname());
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
			    	
			    	Profile.currentGroup = Profile.groupMap.get(newValue.toString());
			     	System.out.println("ListView Selection Changed (selected: " + Profile.currentGroup.getGroupName() + ")");
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
				Group group = Profile.groupMap.get(Profile.currentGroup.getGroupName());
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
	



	
}
