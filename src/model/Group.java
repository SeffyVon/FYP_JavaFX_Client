package model;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;

public class Group {
	String groupName;
	ArrayList<User> userList;
	Movie groupMovie;
	ObservableList<GMessage> gMessages = null;
	String lastMessageTimeString = "2000-01-01 00:00:00";
	
	public Group(String groupName, ArrayList<User> userList, Movie groupMovie){
		this.groupName = groupName;
		this.userList = userList;
		this.groupMovie = groupMovie;
		this.gMessages = FXCollections.observableArrayList(GMessage.extractor());
	}
	public String getGroupName(){
		return groupName;
	}
	ImageView groupPic(){
		ImageView groupImageView = new ImageView();
		return groupImageView;
	}
	
	public ArrayList<User> getUserList(){
		return userList;
	}
	
	public Movie getMovie(){
		return groupMovie;
	}
	
	public ObservableList<GMessage> getList(){
		return gMessages;
	}
	
	public void setList(ObservableList<GMessage> newList){
		System.out.println("newList in Group "+ newList + newList.size());
		CopyOnWriteArrayList<GMessage> copyList = new CopyOnWriteArrayList<GMessage>();
		copyList.addAll(newList);
		gMessages.setAll(copyList);
		System.out.println("gMessages in Group "+ gMessages + "gMessage time" + lastMessageTimeString);
	}
	
	public void setLastMessageTime(String lastMessageTimeString){
		this.lastMessageTimeString = lastMessageTimeString;
	}
	
	public String getLastMessageTime(){
		return lastMessageTimeString;
	}
}
