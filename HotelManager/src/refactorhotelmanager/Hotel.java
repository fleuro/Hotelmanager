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
	private MyFileReader myReader = new MyFileReader();

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

		addGuest("Gerd Vuuren", "Heuvelrug 28", "G.Vuuren@gmail.com", true);
		addGuest("Jan de Sickenvas", "Dalstraat 11", "deJan@Sickenvas.net", false);
		addGuest("Arend Reiselief", "Hoogsingel 41", "Arend.reisje@hotmail.com", false);
		addGuest("Toos Loemsant", "Tussenweg 3", "Tooske142@gmail.com", false);
		addGuest("Frits de Keumus","Zeewinkelstraat 78","KeumusFrits@live.nl", true);
//		checkIn("Toos Loemsant", RoomType.normalSmall, new Date(
	//	"24 Dec 2010"), new Date("20 Jan 2011"), false);
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
		addBill( reservation.getGuestName() ,"Room Bill", "Room Costs", costs, reservation.getStartDate(), 1);

		presentReservationList.add(reservation);
		removeFutureReservation(reservation.getGuestName());
		String billLocation = "administratie/rekeningen/" + reservation.getGuestName() +
						", " + reservation.getRoom().getRoomNr() + ", " + reservation.printDate(reservation.getStartDate()) + ".txt";
		//maak een nieuw bill file aan.
		MyFileWriter.newFile(billLocation);
		MyFileWriter.insertLine(billLocation, "verblijf, "+ "kamerkosten, " + reservation.printDate(reservation.getStartDate()) + ", 1, " + costs);

		//voeg gast toe aan ingecheckte gasten
		String guestLocation = "administratie/gasten/ingecheckteGastenbestand.txt";
		Guest guest = reservation.getGuest();
		MyFileWriter.insertLine(guestLocation, guest.getName() + ", " + guest.getAdres() + ", " +
						guest.getEmail() + ", " + guest.isBlackList());
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
			String guestLocation = "administratie/gasten/ingecheckteGastenbestand.txt";
			MyFileWriter.deleteLine(guestLocation, foundReservation.getGuestName());
			pastReservationList.add(foundReservation);
			presentReservationList.remove(foundReservation);
			foundReservation.getRoom().setToUnOccupied();
			return (name + "heeft uitgecheckt.");
		} else {
			return (name + " heeft al uitgecheckt of bestaat niet.");
		}
	}

	private double calculateRoomCosts(double dayprice, Date start, Date end){
		long days = (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
		return dayprice * days;
	}

	public void modificationToBillForSpecificGuest( String name, int number, String category, String newDescription, double newCosts, Date aDate){
		Reservation res = getPresentReservation(name);
		String location = "administratie/rekeningen/" + name + ", " + res.getRoom() + ", " + res.printDate(res.getStartDate()) + ".txt";
		ArrayList<String> bill = MyFileReader.readFromFile(location);
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

	public Reservation getFutureReservation(String name) {
		String reservation = MyFileReader.searchLine("administratie/reserveringen/futureReserveringen", name);
		String[] splittedReservation = reservation.split(", ");
		Reservation reservation = new Reservation(splittedReservation[0],splittedReservation[1],splittedReservation[2],splittedReservation[3]);

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

	public Reservation getPresentReservation(String guestName){
		Reservation foundReservation = null;
		boolean hasContent = false;
		for (Reservation reservation : presentReservationList) {
			if (reservation.getGuestName().equals(guestName)) {
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

	public void addGuest(String name, String adres, String email, boolean blacklist) {
		guestList.add(new Guest(name, adres, email, blacklist));
	}

	public void addFutureReservation(String name, RoomType roomtype, Date startDate, Date endDate){
		Guest guest = getSpecificGuest(name);
		Room room = getAvailableRoom(roomtype, startDate, endDate);
		Reservation reservation = new Reservation(guest, room, startDate, endDate);
		String location = "administratie/reserveringen/futureReserveringen.txt";
		String line = guest.getName() + ", " + room.getRoomNr() + ", " + reservation.printDate(startDate) + ", " + reservation.printDate(endDate);
		MyFileWriter.insertLine(location, line);
	}

	public void addBill(String name, String category, String description, double costs, Date aDate, int amount ) {
		if (guestExists(name)) {
			getSpecificGuest(name).addBill(category, description, costs, aDate, amount);
		}
	}

	public void addBill(String name, String category, String description,double costs, int amount ) {
		if (guestExists(name)) {
			getSpecificGuest(name).addBill(category, description, costs, amount);
		}
	}

	public void addBillForSpecificGuest( String name, String category, String description, double costs, int amount){
    	for ( Guest guest : guestList){
    		if( guest.getName().equals(name) ){
    			guest.addBill(category, description, costs, amount );
    		}
	    }
	}

	public void addGuestToBlacklist( String name ){
		String blacklistGuest = "";
		String splittedString[] = null;
		for ( String guest : MyFileReader.readFromFile("administratie/gasten/gastenbestand.txt") ){
			splittedString = guest.split(", ");
			if(splittedString[0].equals(name)){
				blacklistGuest = guest;
			}
		}
		if(blacklistGuest != null){
			//veranderingen in gastenbestand. gast op ongewenst zetten
			MyFileWriter.deleteLine("administratie/gasten/gastenbestand.txt", name);
			splittedString = blacklistGuest.split(", ");
			String newLine = splittedString[0] + ", " + splittedString[1] + ", " + splittedString[2] + ", " + "true";
			MyFileWriter.insertLine("administratie/gasten/gastenbestand.txt", newLine);

			//gast toevoegen aan de blacklist
			MyFileWriter.insertLine("administratie/gasten/blacklist.txt", newLine);
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
		String blacklistGuest = "";
		String splittedString[] = null;
		for ( String guest : MyFileReader.readFromFile("administratie/gasten/gastenbestand.txt") ){
			splittedString = guest.split(", ");
			if(splittedString[0].equals(name)){
				blacklistGuest = guest;
			}
		}
		if(blacklistGuest != null){
			//veranderingen in gastenbestand. gast op ongewenst zetten
			MyFileWriter.deleteLine("administratie/gasten/gastenbestand.txt", name);
			splittedString = blacklistGuest.split(", ");
			String newLine = splittedString[0] + ", " + splittedString[1] + ", " + splittedString[2] + ", " + "false";
			MyFileWriter.insertLine("administratie/gasten/gastenbestand.txt", newLine);

			//gast verwijderen van de blacklist
			MyFileWriter.deleteLine("administratie/gasten/blacklist.txt", name);
			return true;
		}
		return false;
	}

	public void removeFutureReservation(String name){
		MyFileWriter.deleteLine("administratie/reserveringen/futureReserveringen.txt", name);
	}

	//////////////////////////////////////////////////////
	////////////////// PRINT FUNCTIES ////////////////////
	//////////////////////////////////////////////////////

	private void printGuests(ArrayList<String> guests){
		System.out.printf("Naam %20s Adres %20s Email %20s Blacklist %n", " ", " ", " ");
		String splittedString[];
		for ( String guest : guests ){
			splittedString = guest.split(", ");
			System.out.printf(" %-25s %-22s %-30s %-33s%n", splittedString[0], splittedString[1], splittedString[2],
								splittedString[3]);
		}
	}

	public void printAllGuests() {
		printGuests(MyFileReader.readFromFile("administratie/gasten/gastenbestand.txt"));
	}

	public void printAllCheckedInGuests() {
		printGuests(MyFileReader.readFromFile("administratie/gasten/ingecheckteGastenbestand.txt"));
	}
	
	public void printBlacklist(){
		printGuests(MyFileReader.readFromFile("administratie/gasten/blacklist.txt"));
	}

	public void printAllBillsSpecificGuest(String name) {
		Reservation reservation = getPresentReservation(name);
		if (reservation != null) {
			System.out.println( "\nDe kosten van " + name + " zijn: " );
			System.out.printf("Omschrijving %20s Datum %20s Aantal %20s Prijs %n", " ", " ", " ");
			double costs = 0;
			String splittedString[];
			String location = "administratie/rekeningen/" + reservation.getGuestName() +
						", " + reservation.getRoom().getRoomNr() + ", " + reservation.printDate(reservation.getStartDate()) + ".txt";

			for (String line : MyFileReader.readFromFile(location)) {
				splittedString = line.split(", ");

				costs += Double.parseDouble(splittedString[4]);
				System.out.printf(" %-32s %-31s %-22s %-33s%n", splittedString[0] + " " + splittedString[1], splittedString[2],
								splittedString[3], Double.parseDouble(splittedString[4]));
			}
			System.out.println();
			System.out.printf("Totale kosten: "+"�"+" %.2f%n", costs);

		} else {
			System.out.println( name + " bestaat niet." );
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
			System.out.printf( "%2d. : %10s \n", room.getRoomType().getOrder(), room.getRoomType().name());
		}
	}

	public void printRoomInformation(String roomNr) {
		for (Room room : roomList) {
			if (room.getRoomNr().equals(roomNr)) {
				System.out.println(room.toString());
			}
		}
	}

	private void printReservation(ArrayList<String> reservation){
		System.out.printf("Naam %20s Kamernr %20s Van %20s Tot %n", " ", " ", " ");
		String splittedString[];
		for ( String guest : reservation ){
			splittedString = guest.split(", ");
			System.out.printf(" %-25s %-22s %-30s %-33s%n", splittedString[0], splittedString[1], splittedString[2],
								splittedString[3]);
		}
	}

	public void printFutureReservations(){
		printReservation(MyFileReader.readFromFile("administratie/reserveringen/futureReserveringen.txt"));
	}

	public void printPresentReservations(){
		printReservation(MyFileReader.readFromFile("administratie/reserveringen/presentReserveringen.txt"));
	}

	public void printPastReservations(){
		printReservation(MyFileReader.readFromFile("administratie/reserveringen/pastReserveringen.txt"));
	}

	public void printCategories(){
		ArrayList<String> categories = myReader.getCategories();
		int teller = 1;
		for(String category : categories){
			System.out.println(teller + ": " + category);
			teller++;
		}
	}

	public void printItemsPerCategory(String category){
		ArrayList<String> itemList = myReader.getItems(category);
		int teller = 1;
		for(String item : itemList){
			System.out.println(teller + ": " + item);
			teller++;
		}
	}

	public String getItemByNumber(String category, int itemNumber){
		return myReader.getItems(category).get(itemNumber-1);
	}

	public String getCategoryByNumber(int categoryNumber){
		return myReader.getCategories().get(categoryNumber-1);
	}
	/*
	 *
	 * public String getFutureReservation(String name) {
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