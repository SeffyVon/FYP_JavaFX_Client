package config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import model.Group;
import model.User;

public class Profile {
	public static User currentUser;
	public static Group currentGroup;
	public static HashMap<String, Group> groupMap = new HashMap<String, Group>();
	public static HashMap<String, User> userMap = new HashMap<String, User>();
	public static Set<String> gStringSet = new HashSet<String>();
	public static Set<String> uStringSet = new HashSet<String>();
	public static String privateKeyString = "";
}
