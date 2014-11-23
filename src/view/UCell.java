package view;

import javafx.scene.control.ListCell;
import model.User;
import controller.UData;

public class UCell extends ListCell<User>{

	@Override
	public void updateItem(User user, boolean empty){
	    super.updateItem(user,empty);
	    if(user != null) {
	        UData data = new UData();
	        data.setInfo(user);
	        setGraphic(data.getBox());
	    }
	}
}
