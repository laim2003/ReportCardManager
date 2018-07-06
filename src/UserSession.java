import accounts.Student;
import data.Database;
import intern.Log;

import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSession {

	public UserSession() {
		Log l = Log.instance(Main.class,"DataBaseAccess");
		Database data = new Database();
		l.log(data.getTimeCreationDate());
	}

}
