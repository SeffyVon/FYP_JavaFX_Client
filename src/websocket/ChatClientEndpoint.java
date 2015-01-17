package websocket;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Collections;
=======
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
>>>>>>> FETCH_HEAD

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
<<<<<<< HEAD

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;
=======
import javafx.scene.control.ProgressBar;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
>>>>>>> FETCH_HEAD
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

<<<<<<< HEAD
import model.GMessage;
import model.User;
import tcp.FileReceiver;
import tcp.FileSender;
import tcp.ProgressBarSyn;
import config.Config;
=======
import tcp.FileReceiver;
import tcp.FileSender;
import tcp.ProgressBarSyn;
import model.GMessage;
>>>>>>> FETCH_HEAD
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
<<<<<<< HEAD
					new URI(Config.hostAddrString_Wx));
=======
					new URI("ws://localhost:8081"));
>>>>>>> FETCH_HEAD
		} catch (DeploymentException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void closeChatClientEndpoint(){
    	System.out.println("Close ChatEndpoint client... " + session.getId());
<<<<<<< HEAD
    	sendGMessage(new GMessage("Status", "left", "", "", Profile.currentUser.getUname(), Profile.currentGroup.getGroupName()));
=======
>>>>>>> FETCH_HEAD
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
<<<<<<< HEAD
		GMessage gMessage = new GMessage("Status", "entered", "", "", Profile.currentUser.getUname(), Profile.currentGroup.getGroupName());
		System.out.println("Connected ... " + session.getId());
        try {
            session.getBasicRemote().sendText(gMessage.encode());
=======
		GMessage gMessage = new GMessage("Status", "Entered", "", "", Profile.currentUser.getUname(), Profile.currentGroup.getGroupName());
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
    			    		 Profile.currentGroup.getGroupName()
    			    		 ));
    				 
    			}
    			
    		});
>>>>>>> FETCH_HEAD
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
<<<<<<< HEAD
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
			
    				
=======
            String message_text= json.getString( "message_text" );
            GMessage gMessage = new GMessage(message_type, message_text, movie_time, message_time, uname, groupname);
            Platform.runLater(new Runnable(){
    			@Override
    			public void run() {
    				if(groupname.equals(Profile.currentGroup)){
    					if(message_type.equals("Sync")){
	    					ProgressBarSyn.receiveGMessage(gMessage);
	    				}
	    				observableList3.add(gMessage);
    				}
    				
    				else if(message_type.equals("Download_Req") && message_text.equals(Profile.currentUser.getUname())){
    					// Download Prompt.... 
    					// if yes
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
    				
    			}
            });
            
>>>>>>> FETCH_HEAD
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
<<<<<<< HEAD
=======
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					observableList3.add(gMessage);
				}
				
			});
>>>>>>> FETCH_HEAD
		} catch (EncodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}