package intern;

public class Log {
	String className;
	String tag;
	protected Log(Class c,String tag) {
		this.className = c.getName();
		this.tag = tag;
	}
	public void log(String msg) {
		System.out.println(className+"/"+tag+": "+msg);
	}
	public static Log instance(Class c,String tag) {
		return new Log(c,tag);
	}
}
