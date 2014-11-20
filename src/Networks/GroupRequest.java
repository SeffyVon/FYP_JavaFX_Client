package Networks;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class GroupRequest {
	static public JSONObject getGroupsAndMovies(String uname){
		try {
			HttpResponse<JsonNode> responseString = Unirest.post("http://127.0.0.1:63342/OnlineCinema_Server/src/groups/getGroups.php")
			  .field("uname",uname)
			  .asJson();
			if(!responseString.getBody().toString().equals("{}"))
				return responseString.getBody().getObject();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	static public JSONObject getGroupMems(String uname){
		try {
			HttpResponse<JsonNode> responseString = Unirest.post("http://127.0.0.1:63342/OnlineCinema_Server/src/groups/getGroupMems.php")
			  .field("uname",uname)
			  .asJson();
			//System.out.println(responseString.getBody());
			if(!responseString.getBody().toString().equals("{}"))
				return responseString.getBody().getObject();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static void main(String[] args) {
		System.out.println(getGroupsAndMovies("doge"));
		System.out.println(getGroupMems("doge"));
		System.out.println("ohh");
	}
}
