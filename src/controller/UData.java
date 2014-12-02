package controller;

import java.io.File;
import java.io.IOException;

import model.User;
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
	
	public void setInfo(User user){
		label1.setText(user.getUname());
		unamePicImageView.setImage(user.getMiddleImage());
	}
	
	public VBox getBox() {
	    return vBox;
	}
}
