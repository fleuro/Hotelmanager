package refactorhotelmanager;

public class Room {

	private String 		roomNr;
	private RoomType 	roomType;
	private boolean 	occupied;
	
	public Room(String roomNr, RoomType roomType, boolean occupied){
		this.roomNr			= roomNr;
		this.roomType		= roomType;
		this.occupied		= occupied;
	}

	public String getRoomNr(){
		return roomNr;
	}
	
	public RoomType getRoomType(){
		return roomType;
	}
	
	public boolean IsOccupied(){
		return occupied;
	}
	
	public String toString(){
		String taken = "";
		if ( occupied ){
			taken = "Yes";
		} else{
			taken = "No";
		}
		
		return "Roomnumber: \t\t" + roomNr
			   + "\nRoom type: \t\t" + roomType
			   + "\n";
	}
}
