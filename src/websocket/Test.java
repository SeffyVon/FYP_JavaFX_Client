package websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.client.ClientManager;

public class Test {
	 
    public static void main(String[] args ){
    
        ClientManager client = ClientManager.createClient();
        try {
            client.connectToServer(ChatClientEndpoint.class, new URI("ws://localhost:8080/"));
       
        } catch (DeploymentException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
