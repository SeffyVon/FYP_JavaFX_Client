package websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.Session;

import model.User;

import org.glassfish.tyrus.client.ClientManager;

public class WebSocketManager {
	private static WebSocketManager webSocketManager=null;
	Session groupChatSession = null;
	static User currentUser;
	
	public static WebSocketManager getInstance(User user){
		if(webSocketManager==null){
			webSocketManager = new WebSocketManager();
			currentUser = user;
		}
		return webSocketManager;
	}
	
	private WebSocketManager() {
		webSocketManager = new WebSocketManager();
	}

	void groupChat(){
		 ClientManager client = ClientManager.createClient();
	        try {
	            groupChatSession = client.connectToServer(ChatClientEndpoint.class, new URI("ws://localhost:8081"));
	            
	        } catch (DeploymentException | URISyntaxException | IOException e) {
	            throw new RuntimeException(e);
	        }
	}
	
	void closeGroupChat(){
		try {
			groupChatSession.close( new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, currentUser.getUname()+"leave the room" ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
