

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import controller.LoginController;

//http://code.makery.ch/java/javafx-8-tutorial-part1/

public class MainApp extends Application {

	public Stage primaryStage;
	private AnchorPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) {
		  	this.primaryStage = primaryStage;
	        this.primaryStage.setTitle("OnlineCinemaApp");
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
            controller.setThisStage(primaryStage);

	            controller.setThisStage(primaryStage);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	   /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }


	public static void main(String[] args) {
		launch(args);
	}
}
