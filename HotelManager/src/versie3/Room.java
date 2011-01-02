package versie3;

public class Room {

	private String 		roomNr;
	private RoomType 	roomType;
	private boolean occupied;
	
	public Room(String roomNr, RoomType roomType){
		this.roomNr			= roomNr;
		this.roomType		= roomType;
	}

	public String getRoomNr(){
		return roomNr;
	}

	public void setToOccupied(){
		occupied = true;
	}

	public void setToUnOccupied(){
		occupied = false;
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
