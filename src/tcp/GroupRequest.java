package tcp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import model.GMessage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.omg.CORBA.RepositoryIdHelper;

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
	
	static public ArrayList<GMessage> getGroupMessage(String groupname, String lasttime){
		try {
			HttpResponse<JsonNode> responseJson = Unirest.post("http://127.0.0.1:63342/OnlineCinema_Server/src/groups/groupMessage.php")
			  .field("groupname", groupname)
			  .field("lasttime", lasttime)
			  .asJson();
			
	//		System.out.println(responseJson.getBody());
			JSONArray jsonArray = responseJson.getBody().getArray();

			ArrayList<GMessage> messageList = new ArrayList<GMessage>();

			for(int i = 0; i<jsonArray.length(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				messageList.add(new GMessage(jsonObject.getString("message_text"), jsonObject.getString("movie_time"), jsonObject.getString("message_time"), jsonObject.getString("uname"), jsonObject.getString("groupname")));    
		}
			return messageList;

		} catch (UnirestException e) {
				e.printStackTrace();
		}
		return null;
		
	}
	static public void sendGroupMessage(String groupname, String uname, String message_text, String message_time, String movie_time){
		try {
			
			HttpResponse<String> responseString = Unirest.post("http://127.0.0.1:63342/OnlineCinema_Server/src/groups/sendGroupMessage.php")
					  .field("groupname", groupname)
					  .field("uname",uname)
					  .field("message_text", message_text)
					  .field("message_time", message_time)
					  .field("movie_time", movie_time)
					  .asString();
			System.out.println(responseString.getBody());
		}
		catch(UnirestException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		//System.out.println(getGroupsAndMovies("doge"));
//		System.out.println(getGroupMems("doge"));
//		JSONObject jsonObject= getGroupMems("doge");
//		ArrayList<String> groupNameArrayList = new ArrayList<String>();
//		for(Iterator iterator = jsonObject.keys(); iterator.hasNext();){
//			groupNameArrayList.add(iterator.next().toString());
//		}
//		System.out.println(groupNameArrayList);
//		for(String groupName:groupNameArrayList){
//			JSONObject groupJsonObject = jsonObject.getJSONObject(groupName);
//			JSONArray userJsonArray = groupJsonObject.getJSONArray("User");
//			JSONObject movieJsonObject = groupJsonObject.getJSONObject("movie");
//			
//			Movie movie = null;
//			if(!movieJsonObject.getString("movieName").equals("null")){
//				System.out.println(movieJsonObject.getString("movieName"));
//				System.out.println(movieJsonObject.getString("movieName").equals("null"));
//				movie = new Movie(movieJsonObject.getString("movieName"),
//						movieJsonObject.getString("owner"), 
//						movieJsonObject.getString("brief"),
//						movieJsonObject.getString("startTime"),
//						movieJsonObject.getString("endTime"));
//			}
//			
//			ArrayList<User> userArrayList = new ArrayList<User>();
//			for(int i=0;i<userJsonArray.length();i++){
//				JSONObject userJsonObject = userJsonArray.getJSONObject(i);
//				userArrayList.add(new User(userJsonObject.getString("uname"), userJsonObject.getString("ipAddr")));
//			}
//			
//			//System.out.println("movieFileName:" + movie.getMovieFileNameString());
//			Group group = new Group(groupName, userArrayList, movie);
//			//System.out.println(group);
//		}
		//System.out.println(System.currentTimeMillis());
		//getGroupMessage("group0", "2014-12-04 10:01:00");
		
		
		//System.out.println(System.currentTimeMillis());
		
		//Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
		//System.out.println(currentTimestamp.toString());
		GroupRequest.sendGroupMessage("group0", "cate", "So beautiful!", new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString(), "01:10:00");
		
	}
}
