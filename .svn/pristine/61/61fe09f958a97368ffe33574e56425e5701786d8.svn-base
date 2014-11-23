package tcp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.JSONArray;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import config.Config;

public class UserRequest {  // userLogin and userRegister
	String hostAddrString;
	public UserRequest() {
	    hostAddrString = "http://"+Config.macAddrString+":63342/OnlineCinema_Server/src/";
		System.out.println(hostAddrString);
	}
	public boolean userLogin(String uname, String hashOfPw, String ipAddr, boolean ipAddrUpdate){
		try {
			HttpResponse<String> responseString = Unirest.post(hostAddrString+"users/userLogin.php")
			  .field("uname",uname)
			  .field("hashOfPw", hashOfPw)
			  .field("ipAddr", ipAddr)
			  .field("ipAddrUpdate", ipAddrUpdate)
			  .asString();
			System.out.println(responseString.getBody());

			if(responseString.getBody().toString().contains("OK"))
				return true;
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean userRegister(String uname, String hashOfPw){
		try {
			HttpResponse<String> responseJson = Unirest.post(hostAddrString+"users/userRegister.php")
			  .field("uname",uname)
			  .field("hashofpw", hashOfPw)
			  .asString();
			System.out.println(responseJson.getHeaders());
			System.out.println(responseJson.getBody());
			return true;
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	public boolean userPicUpload(String uname, String picname){
		try {
			HttpResponse<String> responseJson = Unirest.post(hostAddrString+"/users/fileUpload.php")
			  .field("uploadFile", new File(picname))
			  .field("uname", uname)
			  .asString();
		
			//System.out.println(responseJson.getHeaders());
			System.out.println(responseJson.getBody());
			return true;
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean userPicDownload(String uname){
		try {
			HttpResponse<InputStream> responseBinary = Unirest.post(hostAddrString+"users/fileDownload.php")
			  .field("uname", uname)
			  .asBinary();
	
			InputStream inputStream = responseBinary.getBody();
			
			byte[] buffer = new byte[inputStream.available()];
		    inputStream.read(buffer);
		 
		    File targetFile = new File("resources/pic/users/"+uname + ".jpg");
		    OutputStream outStream = new FileOutputStream(targetFile);
		    outStream.write(buffer);
		    
		
 
			return true;
		} catch (UnirestException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public String userPicUpdate(String uname, String pword){
		try {
			HttpResponse<String> responseJson = Unirest.post(hostAddrString+"users/picFileUpdate.php")
			  .field("uname", uname)
			  .field("hashOfPw", pword)
			  .asString();
			//System.out.println(responseJson.getBody());
			return responseJson.getBody();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		
		
		try {
			Config.localAddrString = InetAddress.getLocalHost().getHostAddress();
			if(System.getProperty("os.name").equals("Mac OS X")){
				Config.macAddrString = Config.localAddrString;
			}else{
				Config.linuxAddrString = Config.localAddrString;
			}
			System.out.println("local IP addr:"+Config.macAddrString);
			
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		UserRequest userRequest= new UserRequest();
		//System.out.println(userRequest.userLogin("doge","hello",Config.localAddrString));
		//System.out.println(userRequest.userRegister("cate2","hello"));
		//System.out.println(userRequest.userPicDownload("user_pic"));
		String unameString = "user5";
		userRequest.userPicUpload(unameString, "resources/pic/test/alpacas.jpg");
		System.out.println(userRequest.userPicUpdate(unameString,"hello"));
		System.out.println(userRequest.userPicDownload(unameString));
	}
}
