package model;

public class User {
	String bigImageLocString; 
	String smallImageLocString;
	String uname;
	String ipAddrString;
	
	public User(String uname, String ipAddrString){
		this.uname = uname;
		this.ipAddrString = ipAddrString;

	}
	
	public String getBigImageLocString(){
		return bigImageLocString;
	}
	public String getSmallImageLocString(){
		return smallImageLocString;
	}
	public String getUname(){
		return uname;
	}
}
