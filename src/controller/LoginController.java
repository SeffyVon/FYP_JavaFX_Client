package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import tcp.UserRequest;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;
import config.Config;

public class LoginController implements Initializable{
	
	@FXML
	private TextField unameField;
	@FXML
	private TextField pwordField;	
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
	            System.out.println("You pressed login Button.");
	            String uname = unameField.getText();
	            String pword = pwordField.getText();
	            UserRequest userRequest = new UserRequest();
	            if( userRequest.userLogin(uname, pword, Config.localAddrString, true)){
	            	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MyLayout.fxml"));
	                AnchorPane GLayout = null;
					try {
						GLayout = (AnchorPane)fxmlLoader.load();
						MyController myController = fxmlLoader.getController();
					
						myController.setUser(new User(uname,Config.localAddrString));
						myController.prepareGroups();
						myController.setGListView();
						myController.setThisStage(primaryStage);
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
