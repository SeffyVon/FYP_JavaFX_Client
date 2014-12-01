package view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class TestDialog {
	
    private final ComboBox<StageStyle> styleCombobox = new ComboBox<>();
    private final static ComboBox<Modality> modalityCombobox = new ComboBox<>();
    private final static CheckBox cbUseBlocking = new CheckBox();
    private final static CheckBox cbCloseDialogAutomatically = new CheckBox();
    private final CheckBox cbShowMasthead = new CheckBox();
    private final static CheckBox cbSetOwner = new CheckBox();
    private final CheckBox cbCustomGraphic = new CheckBox();
	
	public static void main (String [] args ){
        final CheckBox cbShowCancel = new CheckBox("Show Cancel Button");
        cbShowCancel.setSelected(true);
        
		Alert dlg = createAlert(AlertType.CONFIRMATION);
        dlg.setTitle("You do want dialogs right?");
        String optionalMasthead = "Just Checkin'";
        dlg.getDialogPane().setContentText("I was a bit worried that you might not want them, so I wanted to double check.");

        if (!cbShowCancel.isSelected()) {
            dlg.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
        }

        
        showDialog(dlg);
		
		
	}
	
    static Alert createAlert(AlertType type) {
        Window owner =  null;
        Alert dlg = new Alert(type, "");
        dlg.initModality(modalityCombobox.getValue());
        dlg.initOwner(owner);
        return dlg;
    }
    
    private static void showDialog(Dialog<?> dlg) {
        if (cbCloseDialogAutomatically.isSelected()) {
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Attempting to close dialog now...");
                Platform.runLater(() -> dlg.close());
            }).start();
        }
 
        if (cbUseBlocking.isSelected()) {
            dlg.showAndWait().ifPresent(result -> System.out.println("Result is " + result));
        } else {
            dlg.show();
            dlg.resultProperty().addListener(o -> System.out.println("Result is: " + dlg.getResult()));
            System.out.println("This println is _after_ the show method - we're non-blocking!");
        }
    }
	
}
