package model;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

import javax.json.Json;
import javax.websocket.EncodeException;


public class GMessage {
	private StringProperty message_type; // Download, Sync, Chat, Status
	private StringProperty message_text;
	private StringProperty movie_time;
	private StringProperty message_time;
	private StringProperty uname;
	private StringProperty groupname;
	
	public GMessage(String message_type, String message_text, String movie_time, String message_time, String uname, String groupname) {
		uNameProperty().set(uname);

		groupNameProperty().set(groupname);
		movieTimeProperty().set(movie_time);
		messageTimeProperty().set(message_time);
		messageTypeProperty().set(message_type);
		messageTextProperty().set(message_text);
    }
    
    
    public String getMessageTime() {
		return messageTimeProperty().getValue();
	}
    
    public String getUname() {
		return uNameProperty().getValue();
	}
    
    public String getMessageText() {
		return messageTextProperty().getValue();
	}
    
    public String getMessageType() {
		return messageTypeProperty().getValue();
	}
    
    public String getMovieTime() {
		return movieTimeProperty().getValue();
	}
    

    public String getGroupName(){
    	return groupNameProperty().getValue();
    }
    
    public StringProperty messageTypeProperty(){
    	if(message_type==null){
    		message_type = new SimpleStringProperty();
    	}
        return message_type;
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
    	   return (GMessage p) -> new Observable[]{p.messageTypeProperty(), p.messageTextProperty(), p.messageTimeProperty(), p.movieTimeProperty(), p.groupNameProperty(),p.uNameProperty()};
    	   // http://www.javacodegeeks.com/2014/11/properties-extractor-best-way-to-get-the-listview-instantly-updating-its-elements.html	
    }
 
    public String getInterpretText(){
    	if(messageTypeProperty().getValue()=="Sync"){
    		return getUname() + " " + getMessageText() + ".";
    	}else if(messageTypeProperty().getValue()=="Chat"){
    		return getUname() + ":" + getMessageText();
    	}else if(messageTypeProperty().getValue()=="Status"){
    		return getUname() + " " + getMessageText() + ".";
    	}else if(messageTypeProperty().getValue()=="Download_Req"){
    		return getUname()  + "asked to download from you.";
    	}else if(messageTypeProperty().getValue()=="Download_Ack"){
    		return getUname() + "agreed to let you download. Start downloading...";
    	}else{
			return "";
		}
    }
    
    
    public String encode() throws EncodeException {
    	
  
        String jsonString = Json.createObjectBuilder()
            .add( "uname", getUname() )
            .add( "message_text", getMessageText() )
            .add( "message_time", getMessageTime())
            .add( "message_type", getMessageType())
            .add( "groupname", getGroupName())
            .add( "movie_time",getMovieTime())
            .build()
            .toString();
        	return jsonString;
    
    }
    
}
