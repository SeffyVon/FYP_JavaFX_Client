package controller;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GData {

	@FXML
	private VBox vBox;
	@FXML
	private Label label1;
	
	public GData() {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/GCellItem.fxml"));
	    fxmlLoader.setController(this);
	    try {
	        fxmlLoader.load();
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public void setInfo(String string){
	   label1.setText(string);
	}
	
	public VBox getBox() {
	    return vBox;
	}
}