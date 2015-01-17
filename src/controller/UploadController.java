package controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import config.Interface;
import config.Profile;

public class UploadController implements Initializable{
	
	@FXML
	BorderPane moviePane;
	@FXML
	Button movieCoverButton;
	@FXML
	Button movieFileButton;
	@FXML
	Button watchButton;
	@FXML
	DatePicker fromDatePicker;
	@FXML
	DatePicker toDatePicker;
	@FXML
	ImageView movieOwnerImageView;
	@FXML
	ImageView moviePosterImageView;
	@FXML
	Label watchTimeLabel;
	@FXML
	ComboBox<String> ratingComboBox;

	@FXML
	TextArea movieBriefTextArea;
	@FXML
	TextField movieNameTextField;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		movieOwnerImageView.setImage(Profile.currentUser.getSmallImage());
		
		movieFileButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	 FileChooser fileChooser = new FileChooser();
	        	 fileChooser.setTitle("Choose Movie File");
	        	 fileChooser.getExtensionFilters().add(
	        	         new ExtensionFilter("MP4 Files(.mp4)", "*.mp4"));
	        	 Platform.runLater(new Runnable() {
						
						@Override
						public void run() {
							File selectedFile = fileChooser.showOpenDialog(Interface.primaryStage);
						
							// encrypt it here .....
							
							// move it to the place ....
						
						}
					});
	        	 
	        }
		});
		
		movieCoverButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	 FileChooser fileChooser = new FileChooser();
	        	 fileChooser.setTitle("Choose Movie Cover File");
	        	 fileChooser.getExtensionFilters().add(
	        	         new ExtensionFilter("JPG Files(.jpg)", "*.jpg"));
	        	 Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						File selectedFile = fileChooser.showOpenDialog(Interface.primaryStage);
					}
				});
	        	
	        }
		});
		
		fromDatePicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		     public void handle(ActionEvent event) {
		         LocalDate date = fromDatePicker.getValue();
		         System.err.println("Selected date: " + date);
		     }
		 });
		
		toDatePicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		     public void handle(ActionEvent event) {
		         LocalDate date = toDatePicker.getValue();
		         System.err.println("Selected date: " + date);
		     }
		 });
		ObservableList<String> options = 
			    FXCollections.observableArrayList(
			        "5",
			        "4",
			        "3",
			        "2",
			        "1"
			    );
		ratingComboBox.getItems().addAll(options);
		
		
	
	
	}
	
	
	
	
}
