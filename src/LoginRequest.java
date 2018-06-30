
public class LoginRequest {

	private String inputname;
	private String inputpassword;
	
	public LoginRequest(String username,String password) {
		this.inputname = username;
		this.inputpassword = password;
	}
	public boolean getValid(String reqtype) {
		if(reqtype.equals("username")) {
		if(!inputname.equals("laim2003")) {
		return false;
		}else {
		return true;
		}
		}
		else {
		if(!inputpassword.equals("13L06F03")) {
			return false;
		}else {
			return true;
		}
		}
	}

}
