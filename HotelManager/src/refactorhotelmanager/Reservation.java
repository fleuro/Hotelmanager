package refactorhotelmanager;
import java.util.*;

public class Reservation {
	
	private Date startDate;
	private Date endDate;
	private Guest guest;
	private Room room;
	
	public Reservation(Guest guest, Room room,Date startDate, Date endDate){
		this.guest 		= guest;
		this.room 		= room;
		this.startDate 	= startDate;
		this.endDate 	= endDate;
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
	
	public String getGuestName() {
		return guest.getName();
	}

	public String printInfo(){
		return guest + " " + room;
	}
	
	public String toString() {
		return guest + " stays in room " + room +
			   " has checked in on " + startDate +
			   " will stay till " + endDate; 
	}
}
