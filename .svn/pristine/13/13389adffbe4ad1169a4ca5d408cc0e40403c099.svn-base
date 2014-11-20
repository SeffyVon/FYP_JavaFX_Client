package view;

import controller.GData;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;

public class GCell extends ListCell<String> {

	@Override
	public void updateItem(String string, boolean empty){
	    super.updateItem(string,empty);
	    if(string != null) {
	        GData data = new GData();
	        data.setInfo(string);
	        setGraphic(data.getBox());
	    }
	}
}
