package websocket;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import model.GMessage;
import model.User;
import tcp.FileReceiver;
import tcp.FileSender;
import tcp.ProgressBarSyn;
import config.Config;
import config.Interface;
import config.Profile;
 
@ClientEndpoint
public class ChatClientEndpoint {
	static int portNum = 15132;
    static Session session=null;

    static ObservableList<GMessage> observableList3 = FXCollections.observableArrayList();
   
    private static JsonReaderFactory factory = Json.createReaderFactory( Collections.< String, Object >emptyMap() );
    
    public static void createChatClientEndpoint(ObservableList<GMessage> observableList){
		observableList3 = observableList;
    	javax.websocket.WebSocketContainer container = javax.websocket.ContainerProvider.getWebSocketContainer();

		try {
			session = container.connectToServer(ChatClientEndpoint.class, 
					new URI(Config.hostAddrString_Wx));
		} catch (DeploymentException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void closeChatClientEndpoint(){
    	System.out.println("Close ChatEndpoint client... " + session.getId());
    	sendGMessage(new GMessage("Status", "left", "", "", Profile.currentUser.getUname(), Profile.currentGroup.getGroupName()));
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
		GMessage gMessage = new GMessage("Status", "entered", "", "", Profile.currentUser.getUname(), Profile.currentGroup.getGroupName());
		System.out.println("Connected ... " + session.getId());
        try {
            session.getBasicRemote().sendText(gMessage.encode());
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
            String message_type= json.getString( "message_type" );
            String groupname= json.getString( "groupname" );
            String movie_time= json.getString( "movie_time" );
            String message_time= json.getString( "message_time" );
            String message_text= "";
            
            // ---------  message_text  -----------
            if(message_type.equals("Init Users")){
				JsonArray jsonArray = json.getJsonArray("message_text");
				ArrayList<User> onlineUsersArrayList = new ArrayList<User>();
				for (JsonValue jsonObject:jsonArray){		
					onlineUsersArrayList.add(Profile.userMap.get(jsonObject.toString()));
				}
				Interface.currentMovieController.setUListView(onlineUsersArrayList);
			}else{
				message_text= json.getString( "message_text" );
			}
           // ---------  message_text end -----------
            
            GMessage gMessage = new GMessage(message_type, message_text, movie_time, message_time, uname, groupname);
           
			if(groupname.equals(Profile.currentGroup.getGroupName())){
				
					if(message_type.equals("Init Progress")){
						ProgressBarSyn.receiveGMessage(gMessage);
					}
					else if(message_type.equals("Sync")){
						ProgressBarSyn.receiveGMessage(gMessage);
					}
					else if (message_type.equals("Status")){
						
						if(message_text.equals("entered"))
							Interface.currentMovieController.userEntered(Profile.userMap.get(uname));
						else
							Interface.currentMovieController.userLeft(Profile.userMap.get(uname));
					
					}
					else if(message_type.equals("Download_Req") && message_text.equals(Profile.currentUser.getUname())){
						// Download Prompt.... 
						// if yes
						System.out.println("Sender " + message_text);
						System.out.println("I am " + Profile.currentUser.getUname());
						System.out.println("Download from me");
						new FileSender(Profile.groupMap.get(groupname).getMovie().getMovieFileNameString(), portNum++).run();
						ChatClientEndpoint.sendGMessage(new GMessage("Download_Ack", uname, "", "", Profile.currentUser.getUname(), Profile.currentGroup.getGroupName()));
						// if no
					}
					else if(message_type.equals("Download_Ack") && message_text.equals(Profile.currentUser.getUname())){
						
						FileReceiver.receiveFromIP(
								Profile.currentGroup.getMovie().getMovieOwnerIPString(),
								Profile.currentGroup.getMovie().getMovieNameString(),
								Interface.currentMovieController.networkProgressBar,
								portNum++,
								Profile.currentGroup.getMovie().getFilesize()+1);// Start Receive ....
					}
		
				System.out.println("obs list groupname"+Profile.currentGroup.getGroupName());
	            Platform.runLater(new Runnable(){
	    			@Override
	    			public void run() {
	    				observableList3.add(gMessage);
	    			}
	    			
	            });
	    		
			}
			
    				
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
		} catch (EncodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}