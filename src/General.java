

public class General extends User{
	final  private int maxDay = 1;
	final private int maxHrs = 3;
	
	public General(String username, String password) {
		
		super(username, password);
		super.maxDay = this.maxDay;
		super.maxHrs = this.maxHrs;
	}
}
