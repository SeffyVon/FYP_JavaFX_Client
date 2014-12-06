package model;


public class GMessage {
    public String message_text;
    public String movie_time;
    public String message_time;
    public String uname;
    public String groupname;
    
    public GMessage(String message_text, String movie_time, String message_time, String uname, String groupname) {
		this.message_text = message_text;
		this.movie_time = movie_time;
		this.message_time = message_time;
		this.uname = uname;
		this.groupname = groupname;
	}
    
    public String getMessage(){
    	return uname+" : "+message_text;
    }
    
    public String getMessageTime() {
		return message_time;
	}
    
    public String getUname() {
		return uname;
	}
    
}
