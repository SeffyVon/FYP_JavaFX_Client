package websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import model.GMessage;
import model.User;

import org.glassfish.tyrus.client.ClientManager;

import config.Profile;
 
@ClientEndpoint
public class ChatClientEndpoint {
 
    static Session session=null;

    static ObservableList<GMessage> observableList3 = FXCollections.observableArrayList();
   
    
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
		System.out.println("Connected ... " + session.getId());
        try {
            session.getBasicRemote().sendText("start");
            Platform.runLater(new Runnable(){
    			@Override
    			public void run() {
    				 
    			     observableList3.add(new GMessage("Connected","00:00:00" ,"2015-1-3",Profile.currentUser.getUname(),Profile.currentGroup.getGroupName()));
    				 
    			}
    			
    		});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
 
    @OnMessage
    public static void onMessage(String message) {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Received ...." + message);
        Platform.runLater(new Runnable(){
			@Override
			public void run() {
				
				observableList3.add(new GMessage("Received ...." + message,"00:00:01" ,"2015-1-3","cate" ,"group0"));
			}
        });
    }
 
 
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println(String.format("Session %s close because of %s", session.getId(), closeReason));
        Platform.runLater(new Runnable(){
			@Override
			public void run() {
				observableList3.add(new GMessage("Session" + session.getId() + "close because of" + closeReason,"00:00:01" ,"2015-1-3","cate","group0"));
			}
        });
    }
 
    public static void main(String[] args) {
    	ObservableList<GMessage> observableList = FXCollections.observableArrayList();
    	ChatClientEndpoint.createChatClientEndpoint(observableList);
    	System.out.println(observableList.size());
    	ChatClientEndpoint.sendGMessage("helo");
    	System.out.println(observableList.size());
    	ChatClientEndpoint.sendGMessage("heloo2");
    	System.out.println(observableList.size());
    }

	public static void sendGMessage(String message) {
		System.out.println("Sending ...." + message);
		try {
			session.getBasicRemote().sendText(message);
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					observableList3.add(new GMessage("Sending ...." + message,"00:00:01" ,"2015-1-3",Profile.currentUser.getUname(),"group0"));
				}
				
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}