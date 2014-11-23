package model;

public class Movie {
	String movieNameString;
	String movieOwnerNameString;
	String movieBriefString;
	String startTString;
	String endTString;
	String imagePosterLocString;
	
	public Movie(String movieNameString,
	String movieOwnerNameString,
	String movieBriefString,
	String startTString,
	String endTString) {
		this.movieNameString = movieNameString;
		this.movieOwnerNameString = movieOwnerNameString;
		this.movieBriefString = movieBriefString;
		this.startTString = startTString;
		this.endTString = endTString;
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
	
}
