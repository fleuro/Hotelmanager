package versie3;
import java.util.*;

public class Reservation {
	
	private Date startDate;
	private Date endDate;
	private Guest guest;
	private String guestName;
	private String roomNr;
	private Room room;

	/*
	public Reservation(Guest guest, Room room,Date startDate, Date endDate){
		this.guest 		= guest;
		this.room 		= room;
		this.startDate 	= startDate;
		this.endDate 	= endDate;
	}
	 * 
	 */

	//overload
	public Reservation(String guestName, String roomNr, Date startDate, Date endDate){
		this.guestName = guestName;
		this.roomNr = roomNr;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Date getStartDate(){
		return startDate;
	}
	
	public Date getEndDate(){
		return endDate;
	}
	
	public Guest getGuest(){
		return guest;
	}
	
	public Room getRoom(){
		return room;
	}

	public String getRoomNr(){
		return roomNr;
	}

	public String getGuestName() {
		return guestName;
	}

	public String printDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int day = calendar.get(Calendar.DATE);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		return day + " " + decideMonthName(month) + " " + year;
	}

	private String decideMonthName(int monthNumber){
		switch(monthNumber){
			case 1: return "Jan";
			case 2: return "Feb";
			case 3: return "Mar";
			case 4: return "Apr";
			case 5: return "May";
			case 6: return "Jun";
			case 7: return "Jul";
			case 8: return "Aug";
			case 9: return "Sep";
			case 10: return "Oct";
			case 11: return "Nov";
			case 12: return "Dec";

			default: return "";
		}
	}

	public String toString() {
		return guestName + " verblijft in kamer " + roomNr +
			   " heeft ingecheckt op " + printDate(startDate) +
			   " en zal blijven tot " + printDate(endDate);
	}
}
