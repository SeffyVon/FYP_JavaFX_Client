package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;
import tcp.UserRequest;
import config.Config;

public class LoginController implements Initializable{
	
	@FXML
	private TextField unameField;
	@FXML
	private PasswordField pwordField;	
	@FXML
	private Button loginButton;
	@FXML
	private Button registerButton;
	@FXML
	private Label wrongLabel;
	
	Stage primaryStage = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		System.out.println(loginButton.getText());
		try {
			System.out.println("local IP addr:"+InetAddress.getLocalHost().getHostAddress());
			Config.localAddrString = InetAddress.getLocalHost().getHostAddress();
			if(System.getProperty("os.name").equals("Mac OS X")){
				Config.macAddrString = Config.localAddrString;
			}else{
				Config.linuxAddrString = Config.localAddrString;
			}
		} catch (UnknownHostException e1) {

			e1.printStackTrace();
		}
		wrongLabel.setVisible(false);
		
		
	
		
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            primaryStage.getScene().setCursor(Cursor.WAIT);
	            System.out.println("You pressed login Button.");
	            String uname = unameField.getText();
	            String pword = pwordField.getText();
	            UserRequest userRequest = new UserRequest();
	            if( userRequest.userLogin(uname, pword, Config.localAddrString, true)){
	            	//FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MyLayout.fxml"));
	            	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/Cinema.fxml"));
	                
	            	Pane GLayout = null;
					try {
						GLayout = fxmlLoader.load();
						//MyController myController = fxmlLoader.getController();
//						myController.setUser(new User(uname,Config.localAddrString));
//						myController.prepareGroups();
//						myController.setGListView();
//						myController.setThisStage(primaryStage);
						CinemaController cinemaController = fxmlLoader.getController();
						cinemaController.setUser(new User(uname,Config.localAddrString));
						cinemaController.prepareGroups();
						cinemaController.setGListView();
						primaryStage.setResizable(false);
						primaryStage.setX(0);
						primaryStage.setY(0);
						primaryStage.setWidth(1280);
						primaryStage.setHeight(724);
						primaryStage.getScene().setCursor(Cursor.DEFAULT);
						cinemaController.setThisStage(primaryStage);
					} catch (IOException e) {
						e.printStackTrace();
					}
	            	primaryStage.setScene(new Scene(GLayout));
	            	
	            }else{
	            	wrongLabel.setVisible(true);
	            }
	        }
	    });
	}
	
	public void setThisStage(Stage stage){
		this.primaryStage = stage;
	}

}
