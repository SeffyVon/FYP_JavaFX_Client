package config;

public class Config {
	public static String macAddrString = "127.0.0.1";//"10.115.217.178";//"172.20.10.2";//"172.20.10.2";//"10.8.40.49"; //"10.115.193.30";
	public static String linuxAddrString = "172.20.10.3";//"10.115.209.74";
	public static String hostAddrString = "http://127.0.0.1:63342/OnlineCinema_Server/src/";
	
	public static int macPort = 15126;
	public static int linuxPort = 15124;
	public static int localPort = 15126;
	public static int otherPort = 15124;
	public static String localAddrString = "172.20.10.2"; //sender
	public static String otherAddrString = "172.20.10.3";
	
	public static int RefreshGroupMessageRate = 2000; // 2000 milliseconds
}
