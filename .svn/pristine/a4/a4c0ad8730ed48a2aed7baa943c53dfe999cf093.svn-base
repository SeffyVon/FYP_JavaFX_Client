package view;

import controller.UData;
import javafx.scene.control.ListCell;

public class UCell extends ListCell<String>{

	@Override
	public void updateItem(String string, boolean empty){
	    super.updateItem(string,empty);
	    if(string != null) {
	        UData data = new UData();
	        data.setInfo(string);
	        setGraphic(data.getBox());
	    }
	}
}
