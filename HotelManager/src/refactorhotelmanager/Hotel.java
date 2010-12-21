package refactorhotelmanager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Hotel {

	private ArrayList<Guest> guestList = new ArrayList<Guest>();
	private ArrayList<Room> roomList = new ArrayList<Room>();
	private ArrayList<Reservation> futureReservationList = new ArrayList<Reservation>();
	private ArrayList<Reservation> presentReservationList = new ArrayList<Reservation>();
	private ArrayList<Reservation> pastReservationList = new ArrayList<Reservation>();

	public Hotel() {
		buildStandardHotel();
	}

	//////////////////////////////////////////////////////
	///////////////// CONTROL FUNCTIES ///////////////////
	//////////////////////////////////////////////////////
	private void buildStandardHotel(){
		addRoom("1.12", RoomType.normalSmall);
		addRoom("1.13", RoomType.luxurySmall);
		addRoom("2.12", RoomType.normalMedium);
		addRoom("2.13", RoomType.luxuryLarge);
		addRoom("0.12", RoomType.economySmall);

		addGuest("Gerd Vuuren", "Heuvelrug 28, 2385 BH, Boekelo", 733448,
				"G.Vuuren@gmail.com", true);
		addGuest("Jan de Sickenvas", "Dalstraat 11, 8365 WC, Amsterdam",
				233467447, "deJan@Sickenvas.net", false);
		addGuest("Arend Reiselief", "Hoogsingel 41, 2233 AN, Hengelo",
				88373662, "Arend.reisje@hotmail.com", false);
		addGuest("Toos Loemsant", "Tussenweg 3, 3345 ZG, Almelo", 223334,
				"Tooske142@gmail.com", false);
		addGuest("Frits de Keumus",
				"Zeewinkelstraat 78, 1110 AA, Katwijk aan Zee", 9900126,
				"KeumusFrits@live.nl", true);

//		checkIn("Toos Loemsant", RoomType.normalSmall, new Date(
//		"27 Nov 2010"), new Date("30 Nov 2010"), false);
//		checkIn("Jan de Sickenvas", RoomType.businessSmall, new Date(
//		"27 Nov 2010"), new Date("30 Nov 2010"), false);
	}

	public void checkIn(Reservation reservation, boolean print) {

		Room room = reservation.getRoom();
		room.setToOccupied();
		if(print){
						System.out.println(reservation.toString());
		}
		double costs = calculateRoomCosts(room.getRoomType().getDayPrice(),
						reservation.getStartDate(), reservation.getEndDate());
		addBill( reservation.getGuestName() ,"Room Bill", "Room Costs", costs, reservation.getStartDate());

		presentReservationList.add(reservation);
		futureReservationList.remove(reservation);
	}

	public String checkOut(String name) {
		Reservation foundReservation = null;
		for (Reservation reservation : presentReservationList) {
			// System.out.println( "check" + reservation.getGuestName()); // kan
			// je zien welke klant in de lijst zit.
			if (reservation.getGuestName().equals(name)) {
				foundReservation = reservation;
			}
		}
		if (foundReservation != null) {
			pastReservationList.add(foundReservation);
			presentReservationList.remove(foundReservation);
			foundReservation.getRoom().setToUnOccupied();
			return (name + "heeft uitgecheckt .");
		} else {
			return (name + " heeft al uitgecheckt of bestaat niet.");
		}
	}

	private double calculateRoomCosts(double dayprice, Date start, Date end){
		long days = (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
		return dayprice * days;
	}

	public void modificationToBillForSpecificGuest( String name, int number, String category, String newDescription, double newCosts, Date aDate){
    	for ( Guest guest : guestList){
    		if( guest.getName().equals(name) ){
    			guest.modifyBill(number, category, newDescription, newCosts, aDate );
    		}
	    }
	}

	//////////////////////////////////////////////////////
	///////////////// SEARCH FUNCTIES ////////////////////
	//////////////////////////////////////////////////////

	public Guest getSpecificGuest(String name) {
		Guest foundGuest = null;
		for (Guest guest : guestList) {
			if (guest.getName().equals(name)) {
				foundGuest = guest;
			}
		}
		return foundGuest;
	}

	private Room getAvailableRoom(RoomType roomType, Date startDate, Date endDate) {
		Room foundRoom = null;

		for (Room room : roomList) {
			if (room.getRoomType() == roomType) {
					foundRoom = room;
			}
		}
		return foundRoom;
	}

	private ArrayList<Room> getAvailableRooms(Date start, Date end){
		ArrayList<Room> posibilityRooms = new ArrayList<Room>();
		for(Room room : roomList){
			if(!alreadyReservation(room, start, end, presentReservationList) &&
							!alreadyReservation(room, start, end, futureReservationList)){
					if(!posibilityRooms.contains(room)){
						posibilityRooms.add(room);
					}
			}
		}
		return posibilityRooms;
	}

	public Reservation getReservation(String name) {
		Reservation foundReservation = null;
		boolean hasContent = false;
		for (Reservation reservation : futureReservationList) {
			if (reservation.getGuestName().equals(name)) {
					hasContent = true;
					foundReservation = reservation;
			}
		}
		if(!hasContent){
			System.out.println("Deze klant heeft (nog) geen geldige reservering.");
		}
		return foundReservation;
	}

	public Room getSpecificRoom(String roomNr) {
		Room foundRoom = null;
		for (Room room : roomList) {
			if (room.getRoomNr().equals(roomNr)){
				foundRoom = room;
			}
		}
		return foundRoom;
	}

	public String getGuestNameInReservation(String roomNr){
		String name = "";
		for( Reservation reservation : presentReservationList ){
			if(reservation.getRoom().getRoomNr().equals(roomNr)){
				name = reservation.getGuestName();
			}
		}
		return name;
	}


	//////////////////////////////////////////////////////
	///////////////// CHECK FUNCTIES /////////////////////
	//////////////////////////////////////////////////////

	public boolean isGuestOnBlacklist( String name ){
		for ( Guest guest : guestList ){
			if ( guest.getName().equals( name ) ){
				return guest.isBlackList();
			}
		}
		return false;
	}

	public boolean guestExists(String name) {
		for (Guest guest : guestList) {
			if (guest.getName().equals( name )) {
				return true;
			}
		}
		return false;
	}

	private boolean alreadyReservation(Room room, Date startDate, Date endDate, ArrayList<Reservation> list) {
		for (Reservation reservation : list) {
			if (reservation.getRoom() == room
					&& reservation.getStartDate().before(endDate)
					&& reservation.getEndDate().after(startDate)) {
				return true;
			}
		}
		return false;
	}

	
	//////////////////////////////////////////////////////
	/////////////////// ADD FUNCTIES /////////////////////
	//////////////////////////////////////////////////////

	public void addRoom(String roomNr, RoomType roomType) {
		roomList.add(new Room(roomNr, roomType));
	}

	public void addGuest(String name, String adres, int accountNr, String email, boolean blacklist) {
		guestList.add(new Guest(name, adres, accountNr, email, blacklist));
	}

	public void addFutureReservation(String name, RoomType roomtype, Date startDate, Date endDate){
		Guest guest = getSpecificGuest(name);
		Room room = getAvailableRoom(roomtype, startDate, endDate);
		Reservation reservation = new Reservation(guest, room, startDate, endDate);
		futureReservationList.add(reservation);
	}

	public void addBill(String name, String category, String description, double costs, Date aDate ) {
		if (guestExists(name)) {
			getSpecificGuest(name).addBill(category, description, costs, aDate);
		}
	}

	public void addBill(String name, String category, String description,double costs ) {
		if (guestExists(name)) {
			getSpecificGuest(name).addBill(category, description, costs);
		}
	}

	public void addBillForSpecificGuest( String name, String category, String description, double costs){
    	for ( Guest guest : guestList){
    		if( guest.getName().equals(name) ){
    			guest.addBill(category, description, costs );
    		}
	    }
	}

	public void addGuestToBlacklist( String name ){
		for ( Guest guest : guestList ){
			if ( guest.getName().equals( name ) ){
				guest.setBlacklist( true );
			}
		}
	}

	//////////////////////////////////////////////////////
	///////////////// REMOVE FUNCTIES ////////////////////
	//////////////////////////////////////////////////////

	public boolean removeBillForSpecificGuest( String name, int number){
    	for ( Guest guest : guestList){
    		if( guest.getName().equals(name) ){
    			return guest.removeBill( number );
    		}
	    }
    	return false;
	}

	public boolean removeGuestFromBlacklist( String name ){
		boolean exist = false;
		for ( Guest guest : guestList ){
			if ( guest.getName().equals( name ) ){
				if ( guest.isBlackList() ){
					guest.setBlacklist( false );
					exist = true;
				}
			}
		}
		return exist;
	}

	//////////////////////////////////////////////////////
	////////////////// PRINT FUNCTIES ////////////////////
	//////////////////////////////////////////////////////

	public void printAllGuests() {
		for ( Guest guest : guestList ){
			System.out.println(guest.toString());
		}
	}

	public void printBlacklist(){
		for (Guest guest : guestList) {
			if ( guest.isBlackList()){
				System.out.println( guest.toString() );
			}
		}
	}

	public void printAllBillsAllGuests() {
		for( Guest guest: guestList ){
			System.out.println( "\nDe kosten van " + guest.getName() + " zijn: " );
			guest.printAllBills();
		}
	}

	public void printAllRooms() {
		for (Room room : roomList) {
			System.out.println(room.toString());
		}
	}

	public void printAvailableRooms() {
		for (Room room : roomList) {
			if (room.IsOccupied() == false) {
				System.out.println(room.toString());
			}
		}
	}

	public void printRoomsPerType(Date startDate, Date endDate) {
		for (Room room : getAvailableRooms(startDate, endDate)) {
			System.out.printf( "%2d. : %10s \n", room.getRoomType().getOrder(), room.getRoomType().name() );
		}
	}

	public void printRoomInformation(String roomNr) {
		for (Room room : roomList) {
			if (room.getRoomNr().equals(roomNr)) {
				System.out.println(room.toString());
			}
		}
	}

	public void printAllCheckedInGuests() {
		for ( Reservation reservation : presentReservationList ){
			System.out.println( reservation.getGuest().toString() );
		}
	}

	public void printAllBillsSpecificGuest(String name) {
		if (guestExists(name)) {
			System.out.println( "\nDe kosten van " + name + " zijn: " );
			getSpecificGuest(name).printAllBills();
		} else {
			System.out.println( name + " bestaat niet." );
		}
	}

	public void printFutureReservations(){
		for(Reservation reservation : futureReservationList){
			System.out.println(reservation.printInfo());
		}
	}
	
	/*
	 *
	 * public String getReservation(String name) {
		Date date = Calendar.getInstance().getTime();
		ArrayList<Reservation> possibleReservations = new ArrayList<Reservation>();
		for (Reservation reservation : reservationList) {
			if (reservation.getGuestName().equals(name)) {
				if (reservation.getStartDate().after(date)
						|| reservation.getStartDate().compareTo(date) != 0) {
					possibleReservations.add(reservation);
				}
			}
		}
		if (possibleReservations.size() != 0) {
			for (Reservation reservation : possibleReservations) {
				return reservation.toString();
			}
		} else {
			return "Deze klant heeft (nog) geen geldige reservering.";
		}
		return null;
	}
	
	
	
	public void checkIn(String name, RoomType roomType, Date startDate, Date endDate, boolean print) {
		// check if guest already exist.
		Guest guest = getSpecificGuest(name);

		if (guest == null) {
			// guest doesn't exist. He is new in the hotel.
			// add him to the guestlist
			if(print){
				System.out.println("Gast bestaat niet, voeg deze gast toe aan de lijst.");
			}
			return;
		}

		// check if room is available.
		Room room = getAvailableRoom(roomType, startDate, endDate);
		if (room == null) {
			if(print){
				System.out.println("Geen kamer beschikbaar met deze type.");
			}
			checkAlternatives(roomType.dayPrice, startDate, endDate, print);
			return;
		}

		Reservation reservation = new Reservation(guest, room, startDate,
				endDate);
		reservationList.add(reservation);
		if(print){
						System.out.println("De reservering is gemaakt voor: "
				+ guest.getName() + " met kamer: " + room.getRoomType());
		}
		double costs = calculateRoomCosts(roomType.getDayPrice(), startDate, endDate);
		addBill( name ,"Room Bill", "Room Costs", costs, startDate );
		
	}

	

	private void checkAlternatives(double dayPrice, Date startDate, Date endDate, boolean print) {
		if(print){
			System.out.println("Vraag prijs " + dayPrice);
		}
		double maxPrice = dayPrice + 20;
		double minPrice = dayPrice - 20;
		double roomPrice;
		for (Room room : roomList) {
			if (alreadyReservation(room, startDate, endDate)) {
				roomPrice = room.getRoomType().getDayPrice();
				if (roomPrice > minPrice && roomPrice < maxPrice && print) {
					System.out.println(room.toString());
				}
			}
		}
	}

	private Room getAvailableRoomPerType(RoomType roomType) {
		Room foundRoom = null;

		for (Room room : roomList) {
			if (room.getRoomType() == roomType && room.IsOccupied() == false) {
				foundRoom = room;
			}
		}
		return foundRoom;
	}

	

	

	private Room getRoomPerType(RoomType roomType) {
		Room foundRoom = null;

		for (Room room : roomList) {
			if (room.getRoomType() == roomType) {
				foundRoom = room;
			}
		}

		return foundRoom;
	}
	
	public boolean isReservation(String name) {
		Date date = Calendar.getInstance().getTime();
		ArrayList<Reservation> possibleReservations = new ArrayList<Reservation>();
		for (Reservation reservation : reservationList) {
			if (reservation.getGuestName().equals(name)) {
				if (reservation.getStartDate().after(date)
						|| reservation.getStartDate().compareTo(date) != 0) {
					possibleReservations.add(reservation);
				}
			}
		}
		if (possibleReservations.size() != 0) {
			for (Reservation reservation : possibleReservations) {
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	

	
	
	public void printAllCheckedInGuests() {
		Date date = Calendar.getInstance().getTime();
		for ( Reservation reservation : reservationList ){
			if ( reservation.getStartDate().before(date)){
				if(reservation.getEndDate().after(date)){
					Guest guest = reservation.getGuest();
					System.out.println( guest.toString() );
				}
			}
		}
	}
	
	public void printSpecificGuestInformation(String name) {
		for (Guest guest : guestList) {
			if (guest.getName().equals(name)) {
				System.out.println(guest.toString());
			}
		}
	}
	
	
	
	public void printRoomsPerType() {
		for (Room room : roomList) {
			System.out.printf( "%2d. : %10s \n", room.getRoomType().getOrder(), room.getRoomType().name() );
		}
	}

	


	public ArrayList<Guest> getGuests(){
		return guestList;
	}
	
	
}
	 *
	 */
}