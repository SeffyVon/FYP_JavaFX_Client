package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class GMData {
	@FXML
	private HBox hBox;
	@FXML
	private TextArea gMessageArea;
	@FXML
	private ImageView gMessageImageView;
	public GMData() {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/GMCellItem.fxml"));
	    fxmlLoader.setController(this);
	    try {
	        fxmlLoader.load();
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
	public void setInfo(String string){		
		gMessageArea.setText(string);
	}
	
	public void setImage(Image image){		
		gMessageImageView.setImage(image);
	}
		
	public HBox getBox() {
		 return hBox;
	}
		
}
