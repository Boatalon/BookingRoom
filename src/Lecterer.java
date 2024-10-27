

public class Lecterer extends User{
	final  private int maxDay = 7;
	final private int maxHrs = 5;
	
	public Lecterer(String username, String password) {
		super(username, password);
		super.maxDay = this.maxDay;
		super.maxHrs = this.maxHrs;
	}
}
