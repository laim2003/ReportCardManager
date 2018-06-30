package data;

import accounts.Account;
import intern.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Calendar.Builder;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
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

public class Database {
	static final ArrayList<Account> ACCOUNTS = null;
	private Log l = Log.instance(this.getClass(),"DatabaseReader");
	private static String DBcreationTimeString;
	private static String DBlastAccessTimeString;
	private static JSONObject obj;
	private static JSONObject fileObj;
	private static File databasefile = new File("C:/Users/Lukas/eclipse-workspace/ReportCard/src/data/datafiles/data.json");
	Charset defaultcharset = Charset.forName("UTF-8");
	private void init(){
			if(databasefile.exists()) {
				try(
						BufferedReader reader = Files.newBufferedReader(databasefile.toPath(), defaultcharset)
				){
						l.log("DatabaseFile exists as "+databasefile.getName());
						String line = "";
						String templine = null;
						BufferedWriter appendtext = Files.newBufferedWriter(databasefile.toPath(),defaultcharset,StandardOpenOption.WRITE);
						while ((templine = reader.readLine()) != null) {
							line += templine;
						}
						l.log("Old file content:"+line);
						Boolean fileNotEmpty = line.equals("")?false:true;
						if(fileNotEmpty){
							fileObj = new JSONObject(line);
							this.DBcreationTimeString = fileObj.getString("creationDate");
							this.DBlastAccessTimeString = fileObj.getString("lastAccess");
							writeDate("lastAccess", appendtext, false, fileObj);
						}
						else{
							createDBfile();
						}
						l.log("Database created at:"+fileObj.get("creationDate"));
						appendtext.close();
						reader.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
		}
			else {
				createDBfile();
		}
			
		}
	public Database(){
		init();
	}
	private void createDBfile() {
		try(BufferedWriter writer = Files.newBufferedWriter(databasefile.toPath(), defaultcharset)){
			databasefile.createNewFile();
			writeDate(null,writer,true,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		l.log("DatabasFile was created");
	}
	private void writeDate(String keyName,BufferedWriter writer,Boolean newDBFile,JSONObject updatebleobj){
		Date dt = new Date();
		Calendar cldnow = GregorianCalendar.getInstance();
		cldnow.setTime(dt);
		String time = cldnow.get(Calendar.HOUR_OF_DAY)+":"+cldnow.get(Calendar.MINUTE);
		String datestringraw = cldnow.get(Calendar.DATE)+"-"+(cldnow.get(Calendar.MONTH)+1)+"-"+cldnow.get(Calendar.YEAR);
		String datestring = datestringraw+"_"+time;
		obj = newDBFile?new JSONObject():updatebleobj;
		if(!newDBFile) {
			obj.put(keyName,datestring);
			l.log(this.DBlastAccessTimeString);
		}
		else {
			obj.put("creationDate", datestring);
			obj.put("lastAccess", datestring);
		}
		obj.write(writer,0,3);
	}
	public String getTimeLastAccess() {
		return this.DBlastAccessTimeString;
	}
	public String getTimeCreationDate() {
		return this.DBcreationTimeString;
	}
}
 