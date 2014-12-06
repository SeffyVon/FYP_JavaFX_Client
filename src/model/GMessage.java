package model;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;


public class GMessage {
    public StringProperty message_text;
    public StringProperty movie_time;
    public StringProperty message_time;
    public StringProperty uname;
    public StringProperty groupname;
    
    public GMessage(String message_text, String movie_time, String message_time, String uname, String groupname) {
		messageTextProperty().set(message_text);
		movieTimeProperty().set(movie_time);
		messageTimeProperty().set(message_time);
		uNameProperty().set(uname);
		groupNameProperty().set(groupname);
	}
    
    public String getMessage(){
    	return uNameProperty().get()+" : "+messageTextProperty().get();
    }
    
    public String getMessageTime() {
		return messageTimeProperty().get();
	}
    
    public String getUname() {
		return uNameProperty().get();
	}
    
    public StringProperty messageTextProperty(){
    	if(message_text==null){
    		message_text = new SimpleStringProperty();
    	}
        return message_text;
    }
    
    public StringProperty messageTimeProperty(){
    	if(message_time==null){
    		message_time = new SimpleStringProperty();
    	}
        return message_time;
    }
    
    public StringProperty movieTimeProperty(){
    	if(movie_time==null){
    		movie_time =  new SimpleStringProperty();
    	}
        return movie_time;
    }
    
    public StringProperty uNameProperty(){
    	if(uname==null){
    		uname = new SimpleStringProperty();
    	}
        return uname;
    }
    
    public StringProperty groupNameProperty(){
    	if(groupname==null){
    		groupname = new SimpleStringProperty();
    	}
        return groupname;
    }
    public static Callback<GMessage, Observable[]> extractor() {
    	   return (GMessage p) -> new Observable[]{p.messageTextProperty(), p.messageTimeProperty(), p.movieTimeProperty(), p.groupNameProperty(),p.uNameProperty()};
    	}
}
