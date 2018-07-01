package data;

import accounts.Student;
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

public class Database {
	static final ArrayList<Student> ACCOUNTS = null;
	private Log l = Log.instance(this.getClass(),"DatabaseReader");
	private String DBcreationTimeString;
	private String DBlastAccessTimeString;
	private static String filecontent = "";
	public boolean isReadyWriteable = false;
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
						String templine = null;
						BufferedWriter appendtext = Files.newBufferedWriter(databasefile.toPath(),defaultcharset,StandardOpenOption.SYNC);
						while ((templine = reader.readLine()) != null) {
							filecontent += templine;
						}
						l.log("Old file content:"+filecontent);
						Boolean fileNotEmpty = filecontent.equals("")?false:true;
						if(fileNotEmpty){
							fileObj = new JSONObject(filecontent);
							DBcreationTimeString = fileObj.getString("creationDate");
							DBlastAccessTimeString = fileObj.getString("lastAccess");
							l.log("updating lastAccess");
							writeDate("lastAccess", appendtext, false, fileObj);
						}
						else{
							l.log("Creating new Databasefile because file is empty...");
							createDBfile();
						}
						isReadyWriteable = true;
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
			isReadyWriteable = true;
			l.log("DatabasFile was created");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void writeDate(String keyName,BufferedWriter writer,Boolean newDBFile,JSONObject updatebleobj){
		Date dt = new Date();
		Calendar cldnow = GregorianCalendar.getInstance();
		cldnow.setTime(dt);
		String time = cldnow.get(Calendar.HOUR_OF_DAY)+":"+cldnow.get(Calendar.MINUTE);
		String datestringraw = cldnow.get(Calendar.DATE)+"-"+(cldnow.get(Calendar.MONTH)+1)+"-"+cldnow.get(Calendar.YEAR);
		String datestring = datestringraw+"_"+time;
		obj = newDBFile?new JSONObject():updatebleobj;
		l.log("obj old state:"+obj.toString());
		if(!newDBFile) {
			obj.put(keyName,datestring);
		}
		else {
			obj.put("creationDate", datestring);
			obj.put("lastAccess", datestring);
		}
		l.log("obj new state:"+obj.toString());
		obj.write(writer);
	}
	public String getTimeLastAccess() {
		return this.DBlastAccessTimeString;
	}
	public String getTimeCreationDate() {
		return this.DBcreationTimeString;
	}
	public Map<String,Integer> getGradesByPos(int pos) {
		Map returngrades = new HashMap<String,Integer>();
		return returngrades;
	}
	public boolean createStudent(String[] names,int age,Date birthday,String classname,Map<String,Integer> grades,long notes) {
		if(isReadyWriteable) {
			try(
			BufferedWriter writer = Files.newBufferedWriter(databasefile.toPath(),defaultcharset,StandardOpenOption.SYNC)
				){
			JSONObject newjson = new JSONObject();
			newjson.put("name",names);
			newjson.put("age", age);
			newjson.put("DateOfBirth", birthday.toString());
			newjson.put("className", classname);
			newjson.put("grades", grades);
			newjson.put("notes", notes);
			newjson.write(writer);
			return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		else {
			l.log("Couldn't write new students JSONObject to file: File not ready to write.");
			return false;
		}
	}
}
 