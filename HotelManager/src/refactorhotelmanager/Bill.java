package refactorhotelmanager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class Bill {

	private double costs;
	private String description;
	private String category;
	private int number;
	private Date aDate;
	
	public Bill( String category, String description, double costs, int number ) {
		this(category, description, costs, number, null);
	}

	public Bill( String category, String description, double costs, int number, Date aDate ) {
		this.costs       = costs;
		this.description = description;
		this.category 	 = category;
		this.number 	 = number;
		this.aDate 		 = new Date("16 Dec 2010");
	}
	
	public double getCosts() {
		return costs;
	}

	public int getNumber(){
		return number;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Date getDate() {
		return aDate;
	}
	
	public void setDate( Date aDate) {
		this.aDate = aDate;
	}

	public void print() {
		//		System.out.printf("Omschrijving %20s Datum %20s Aantal %20s Prijs %n", " ", " ", " ");
		/*
		DateFormat formatter;
        formatter = new SimpleDateFormat("dd-MMM-yy");
            aDate = (Date)formatter.parse(str_date); 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(aDate);
        */
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(aDate);
  
		if( aDate != null ) {
		String aString = "� " + Double.toString( costs ); 
		Date date = calendar.getTime();
		int day = calendar.get(Calendar.DATE);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		System.out.printf(" %-32s %-31s %-22s %-33s%n", description, day + "-" + month + "-" + year , "1", aString);
			}
		/*System.out.println("\nFactuurnummer: ( " + number + " )" );
		System.out.println("Categorie: " + category );
		System.out.println("Beschrijving: " + description);
		System.out.printf("Kosten: � %.2f%n", costs);
	*/}
}
