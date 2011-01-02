package refactorhotelmanager;
import java.util.Date;

public class Guest {

	private String name;
	private String adres;
	private int accountNr;
	private String email;
	private boolean blackList;
	private BillList billList;

	public Guest(String name, String adres, String email, boolean blackList) {
		this.name = name;
		this.adres = adres;
		//this.accountNr = accountNr;
		this.email = email;
		this.blackList = blackList;
		billList = new BillList();
	}

	public String getName() {
		return name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdres() {
		return adres;
	}

	public int getAccountNr() {
		return accountNr;
	}

	public String getEmail() {
		return email;
	}

	public boolean hasEmail(){
		if(email == null){
			return false;
		}
		return true;
	}
	
	public boolean isBlackList() {
		return blackList;
	}

	public void setBlacklist(boolean blacklist) {
		this.blackList = blacklist;
	}

	public String toString() {
		return "Name: \t\t\t" + name + "\nAdres: \t\t\t" + adres
				+ "\nAccount number: \t" + accountNr + "\nE-mail: \t\t" + email
				+ "\n";
	}

	public void addBill( String category, String description, double costs, int amount){
		addBill( category, description, costs, null, amount );
	}
	
	public void addBill(String category, String description, double costs, Date aDate, int amount) {
		billList.addBill(category, description, costs, aDate, amount);
	}

	public void printAllBills() {
		billList.printAllBills();
	}

	public void modifyBill(int number, String category, String description,
			double costs, Date aDate) {
		billList.modifyBill(number, category, description, costs, aDate);
	}

	public boolean checkFactuurNumber(int number) {
		return billList.isValidFactuurNumber(number);
	}

	public boolean removeBill(int number) {
		return billList.removeBill(number);
	}
}
