package laravel;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;

public class Test {
	public static void main(String[] args) {
		try {
			GetRequest responseString = Unirest.get("http://localhost:63342/PHP_FYP_Server/public/index.php");
			System.out.println(responseString.getHeaders());
		}catch( Exception e){
			e.printStackTrace();
		}
	}
}
