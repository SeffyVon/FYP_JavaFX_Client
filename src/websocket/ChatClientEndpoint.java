package websocket;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import tcp.ProgressBarSyn;
import model.GMessage;
import config.Profile;
 
@ClientEndpoint
public class ChatClientEndpoint {
 
    static Session session=null;

    static ObservableList<GMessage> observableList3 = FXCollections.observableArrayList();
   
    private static JsonReaderFactory factory = Json.createReaderFactory( Collections.< String, Object >emptyMap() );
    
    public static void createChatClientEndpoint(ObservableList<GMessage> observableList){
		observableList3 = observableList;
    	javax.websocket.WebSocketContainer container = javax.websocket.ContainerProvider.getWebSocketContainer();

		try {
			session = container.connectToServer(ChatClientEndpoint.class, 
					new URI("ws://localhost:8081"));
		} catch (DeploymentException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void closeChatClientEndpoint(){
    	System.out.println("Close ChatEndpoint client... " + session.getId());
        try {
			session.close( new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, Profile.currentUser.getUname()+"leave the room" ));
			session=null;
			Platform.runLater(new Runnable(){
    			@Override
    			public void run() {
    				observableList3.clear();
    			}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	@OnOpen
    public void onOpen(Session session) {
		GMessage gMessage = new GMessage("Status", "Entered", "", "", Profile.currentUser.getUname(), "", Profile.currentGroup.getGroupName());
		System.out.println("Connected ... " + session.getId());
        try {
            session.getBasicRemote().sendText(gMessage.encode());
            Platform.runLater(new Runnable(){
    			@Override
    			public void run() {
    			     observableList3.add(new GMessage(
    			    		 "Status",
    			    		 "Enter",
    			    		 "",
    			    		 "",
    			    		 Profile.currentUser.getUname(),
    			    		 "",
    			    		 Profile.currentGroup.getGroupName()
    			    		 ));
    				 
    			}
    			
    		});
        } catch (IOException | EncodeException e) {
            throw new RuntimeException(e);
        }
    }
 
    @OnMessage
    public static void onMessage(String message) {
        
        System.out.println("Received ...." + message);
        
        try( final JsonReader reader = factory.createReader( new StringReader( message ) ) ) {
            final JsonObject json = reader.readObject();
            String uname = json.getString( "uname" );
            String uname2 = json.getString( "uname2" );
            String message_type= json.getString( "message_type" );
            String groupname= json.getString( "groupname" );
            String movie_time= json.getString( "movie_type" );
            String message_time= json.getString( "message_type" );
            String message_text= json.getString( "message_text" );
            GMessage gMessage = new GMessage(message_type, message_text, movie_time, message_time, uname, uname2, groupname);
            Platform.runLater(new Runnable(){
    			@Override
    			public void run() {
    				
    				if(message_type.equals("Download")){
    					// Download....
    				}
    				
    				if(groupname.equals(Profile.currentGroup)){
    					if(message_type.equals("Sync")){
	    					ProgressBarSyn.receiveGMessage(gMessage);
	    				}
	    				observableList3.add(gMessage);
    				}
    			}
            });
            
        }catch(Exception e){
        	e.printStackTrace();
        }
        

    }
 
 
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println(String.format("Session %s close because of %s", session.getId(), closeReason));
    }
 

	public static void sendGMessage(GMessage gMessage) {
		System.out.println("sendGMessage");
		
		try{
			session.getBasicRemote().sendText(gMessage.encode());
			System.out.println("Sending ...." + gMessage.encode());
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					observableList3.add(gMessage);
				}
				
			});
		} catch (EncodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}