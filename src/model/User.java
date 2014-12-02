package model;

import java.io.File;

import javafx.scene.image.Image;

public class User {
	String bigImageLocString; 
	String smallImageLocString;
	String uname;
	String ipAddrString;
	File file = null ;
	Image smallImage = null;
	
	public User(String uname, String ipAddrString){
		this.uname = uname;
		this.ipAddrString = ipAddrString;
		file = new File("resources/pic/users/"+uname+".jpg");
		smallImage = new Image(file.toURI().toString());
	}
	public String getIpAddrString(){
		return ipAddrString;
	}
	
//	public Image getMiddleImage(){
//		return image;
//	}
	
	public Image getSmallImage(){	
		return smallImage;
	}
	public String getUname(){
		return uname;
	}
}
