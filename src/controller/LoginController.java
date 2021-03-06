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
import model.User;
import tcp.UserRequest;
import config.Config;
import config.Profile;
import config.Interface;

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
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		System.out.println(loginButton.getText());
		try {
			System.out.println("local IP addr:"+InetAddress.getLocalHost().getHostAddress());
//			Config.localAddrString = InetAddress.getLocalHost().getHostAddress();
//			if(System.getProperty("os.name").equals("Mac OS X")){
//				Config.macAddrString = Config.localAddrString;
//			}else{
//				Config.linuxAddrString = Config.localAddrString;
//			}
		} catch (UnknownHostException e1) {

			e1.printStackTrace();
		}
		wrongLabel.setVisible(false);
		
		
	
		
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            Interface.primaryStage.getScene().setCursor(Cursor.WAIT);
	            System.out.println("You pressed login Button.");
	            String uname = unameField.getText();
	            String pword = pwordField.getText();
	            UserRequest userRequest = new UserRequest();
	            if( userRequest.userLogin(uname, pword, Config.localAddrString, true)){
	            	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/Cinema.fxml"));
	                
	            	Pane GLayout = null;
					try {
						Profile.currentUser = new User(uname,Config.localAddrString);
						GLayout = fxmlLoader.load();
						CinemaController cinemaController = fxmlLoader.getController();
						cinemaController.prepareGroups();
						cinemaController.setUser();
						cinemaController.setGListView();
						Interface.primaryStage.setResizable(false);
						Interface.primaryStage.setX(0);
						Interface.primaryStage.setY(0);
						Interface.primaryStage.setWidth(1280);
						Interface.primaryStage.setHeight(724);
						Interface.primaryStage.getScene().setCursor(Cursor.DEFAULT);
						cinemaController.setThisStage(Interface.primaryStage);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Interface.primaryStage.setScene(new Scene(GLayout));
	            	
	            }else{
	            	wrongLabel.setVisible(true);
	            }
	        }
	    });
	}
	

}
