package view;

import javafx.application.Platform;
import javafx.scene.control.ListCell;
import model.User;
import controller.UData;

public class UCell extends ListCell<User>{

	@Override
	public void updateItem(User user, boolean empty){
	    super.updateItem(user,empty);
	    UData data = new UData();
	    Platform.runLater(new Runnable(){
	    	@Override
			public void run() {
				if(user != null) {
			        data.setInfo(user);
			        setGraphic(data.getBox());
			    }else{
			    	setGraphic(null);
			    }
			}	    		
    	});
	    
	}
}
