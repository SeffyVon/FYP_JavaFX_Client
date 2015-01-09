package model;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class User {
	String bigImageLocString; 
	String smallImageLocString;
	String uname;
	String ipAddrString;
	File file = null ;
	Image middleImage = null;
	ImageView smallImageView = null;
	public User(String uname, String ipAddrString){
		this.uname = uname;
		this.ipAddrString = ipAddrString;
		file = new File("resources/pic/users/"+uname+".jpg");
		middleImage = new Image(file.toURI().toString());
		smallImageView = new ImageView(middleImage);
		smallImageView.setFitHeight(20);
		smallImageView.setFitWidth(20);
	}
	public String getIpAddrString(){
		return ipAddrString;
	}
	
	public Image getMiddleImage(){
		return middleImage;
	}
	
	public ImageView getSmallImageView(){	
		return smallImageView;
	}
	public String getUname(){
		return uname;
	}
	public Image getSmallImage() {
		return smallImageView.getImage();
	}
}
