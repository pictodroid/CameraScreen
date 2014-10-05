package com.app.pictolike.mysql;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.app.pictolike.data.MyPeople;

class SigninCommand extends MySQLCommand {
	String m_strUserName;
	String m_strPassword;
	
	SigninCommand(String name, String password) {
		m_strUserName = name;
		m_strPassword = password;
	}

	@Override
	void command() {
		ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		InputStream is;
		String result = "";
		
		// connect server.
		try {
			HttpPost httpPost = new HttpPost(MySQLConnect.LINK_SIGNIN);
			nameValuePair.add(new BasicNameValuePair(MySQLConnect.USER_NAME, m_strUserName));
			nameValuePair.add(new BasicNameValuePair(MySQLConnect.FIELD_PASSWORD, m_strPassword));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			HttpResponse response = MySQLConnect.HTTP_CLIENT.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			setErrorCode(MySQLConnect.ERR_CONNECTION_FAILED);
			e.printStackTrace();
			return;
		}
		
		// convert response to string.
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");
			result = sb.toString();
		} catch (Exception e) {
			setErrorCode(MySQLConnect.ERR_LOAD_FAILED);
			e.printStackTrace();
			return;
		}

		// parsing json data		
		try {
			JSONArray jarray = new JSONArray(result);
			JSONObject json_data = jarray.getJSONObject(0);
			MyPeople people = new MyPeople();
			
			people.name = json_data.getString(MySQLConnect.USER_NAME);
	
			people.password = json_data.getString(MySQLConnect.FIELD_PASSWORD);
			
			setResult(people);			
		} catch (Exception e) {
			setErrorCode(MySQLConnect.ERR_PARSE_FAILED);
			e.printStackTrace();
			return;
		}
	}
}
