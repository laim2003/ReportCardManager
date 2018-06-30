import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class ReportCard {
private HashMap<Integer,String> names = new HashMap<Integer,String>();
private String nsname;
private int grade;
private HashMap<HashMap<String,String>,Integer> grades;
private Map<String,Integer> returned = new HashMap<String,Integer>();
	public ReportCard(String fname,String sname,int grade,Map grades,int id) {
		//Adding created student to 'names' e.g. <1,Lukas_Faber>
		names.put(id,fname+"_"+sname);
		this.grades = (HashMap) grades;
	}
	public Map<String,Integer> requestGradesFromCard(int id){
		returned.clear();
	    Iterator it = grades.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        returned.put(pair.getKey().toString(),(int)pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		return returned;
	}
	public String searchCard() {
		return "Hello World";
	}
	public void deleteCard(int id) {
		names.remove(id);
		grades.remove(id);
	}
}