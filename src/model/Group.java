package model;

import java.awt.List;
import java.util.ArrayList;

import javafx.scene.image.ImageView;

public class Group {
	String groupName;
	ArrayList<User> userList;
	Movie groupMovie;
	
	Group(String groupName, ArrayList<User> userList){
		this.groupName = groupName;
		this.userList = userList;
		this.groupMovie = groupMovie;
	}
	
	ImageView groupPic(){
		ImageView groupImageView = new ImageView();
		return groupImageView;
	}
	
}
