package com.app.pictolike.mysql;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class MySQLConnect {

	public static final int CMD_SIGNIN = 1;
	public static final int CMD_SIGNUP = 2;
	public static final int CMD_SAVEFILE = 2;

	
	public static final int ERR_NONE = 0;
	public static final int ERR_CONNECTION_FAILED = -1;
	public static final int ERR_LOAD_FAILED = -2;
	public static final int ERR_USER_EXISTS = -3;
	public static final int ERR_INSERT_FAILED = -4;
	public static final int ERR_PARSE_FAILED = -5;
	
	public static HttpClient HTTP_CLIENT = new DefaultHttpClient(); 
	
	public static final String LINK_SIGNIN = "http://10.0.0.4:8888/signin.php";
	public static final String LINK_SIGNUP = "http://10.0.0.4:8888/signup.php";
	public static final String LINK_SENTFILE = "http:/10.0.0.4:8888/newfile.php";
	
	public static final String USER_NAME = "username";
	public static final String FIELD_NAME = "filename";
	public static final String FIELD_EMAIL = "email";
	public static final String FIELD_PASSWORD = "password";

	public static final String FILE_NAME = "filename";
	public static final String DATE_CREATED = "datecreated";
	public static final String LOCATION_CREATED = "locationcreated";
	
	public static final String MSG_USER_EXISTS = "user exists";
	public static final String MSG_INSERT_FAILED = "insert failed";
	public static final String FIELD_BIRTHDAY = "birthday";
	public static final String FIELD_GENDER = "gender";
	
	public static final String DEVICEID = "deviceID";
	public static final String IMG221 = "img221B";
	public static final String USERAGE = "usrage";
	
	
	
	static public void signin(String name, String password, MySQLCommand.OnCompleteListener listener) {
		SigninCommand cmd = new SigninCommand(name, password);
		if (listener != null)
			cmd.setOnCompleteListener(listener);
		run_command(cmd);
	}



	
	static public void savefile(String username,String filename, String datecreated, String locationcreated,  String deviceID, String userage, String gender, MySQLCommand.OnCompleteListener listener) {
		SaveFileCommand cmd = new SaveFileCommand(username,filename, datecreated, locationcreated, deviceID, userage, gender);
		if (listener != null)
			cmd.setOnCompleteListener(listener);
		run_command(cmd);
	}

	static private void run_command(MySQLCommand cmd) {
		new Thread(cmd).start();
	}
}
