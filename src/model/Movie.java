package model;

public class Movie {
	String movieNameString;
	String movieOwnerNameString;
	String movieBriefString;
	String startTString;
	String endTString;
	String imagePosterLocString;
	String movieOwnerIPString;
	int port = 0;
	int filesize = 0;
	
	public Movie(String movieNameString,
	String movieOwnerNameString,
	String movieBriefString,
	String startTString,
	String endTString,
	String movieOwnerIPString,
	int port,
	int filesize
	) {
		this.movieNameString = movieNameString;
		this.movieOwnerNameString = movieOwnerNameString;
		this.movieBriefString = movieBriefString;
		this.startTString = startTString;
		this.endTString = endTString;
		this.movieOwnerIPString = movieOwnerIPString;
		this.port = port;
		this.filesize = filesize;
	}
	
	public String getMovieOwnerIPString(){
		return movieOwnerIPString;
	}
	public String getMovieNameString(){
		return movieNameString;
	}
	
	public String getMovieFileNameString(){
		return movieNameString.toLowerCase().replaceAll("\\s+", "");
	}
	
	public String getOwnerNameString(){
		return movieOwnerNameString;
	}
	
	public String getMovieBriefString(){
		return movieBriefString;
	}
	
	public String getStartTimeString(){
		return startTString;
	}
	
	public String getEndTimeString(){
		return endTString;
	}
	
	public int getPort(){
		return port;
	}
	
	public int getFilesize(){
		return filesize;
	}
	
}
