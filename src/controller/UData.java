package controller;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class UData {
	@FXML
	private VBox vBox;
	@FXML
	private Label label1;
	@FXML
	private ImageView unamePicImageView;
	
	public UData() {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/UCellItem.fxml"));
	    fxmlLoader.setController(this);
	    try {
	        fxmlLoader.load();
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public void setInfo(String string){
	   label1.setText(string);
	   File file = new File("resources/pic/users/"+string+".jpg");
		//System.out.println(file.getAbsolutePath());
		Image image = new Image(file.toURI().toString());
		    
		unamePicImageView.setImage(image);
	}
	
	public VBox getBox() {
	    return vBox;
	}
}