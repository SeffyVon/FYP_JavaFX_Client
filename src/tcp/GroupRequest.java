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

import config.Config;

public class GroupRequest {
	
	String hostAddrString;
	public GroupRequest() {
	    hostAddrString = "http://127.0.0.1:63342/OnlineCinema_Server/src/";
		//System.out.println(hostAddrString);
	}
	

	
	public JSONObject getGroupMems(String uname){
		try {
			HttpResponse<JsonNode> responseString = Unirest.post(hostAddrString + "groups/getGroupMems.php")
			  .field("uname",uname)
			  .asJson();
			if(!responseString.getBody().toString().equals("{}"))
				return responseString.getBody().getObject();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	

	
	public static void main(String[] args) {
//		//System.out.println(getGroupsAndMovies("doge"));
		System.out.println(new GroupRequest().getGroupMems("doge"));
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
		//new GroupRequest().sendGroupMessage("group0", "cate", "So beautiful!", new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString(), "01:10:00");
		
	}
}
