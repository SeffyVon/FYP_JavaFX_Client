

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import config.Interface;
import config.Profile;
import controller.LoginController;

//http://code.makery.ch/java/javafx-8-tutorial-part1/

public class MainApp extends Application {

	
	private AnchorPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) {
		Interface.primaryStage = primaryStage;
		Interface.primaryStage.setTitle("OnlineCinemaApp");
	    initRootLayout();
	       
	}
	private void initRootLayout() {
		try {
            // Load root layout from fxml file.
           
            URL location = MainApp.class.getResource("view/LoginScene.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            rootLayout = (AnchorPane)fxmlLoader.load();
            LoginController controller = fxmlLoader.getController();
            

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            Interface.primaryStage.setScene(scene);
            Interface.primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}



	public static void main(String[] args) {
		launch(args);
	}
}
