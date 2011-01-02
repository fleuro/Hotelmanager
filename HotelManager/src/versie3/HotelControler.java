package versie3;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HotelControler {

	private ArrayList<Guest> guestList = new ArrayList<Guest>();
	private String locIngecheckteGasten = "administratie/gasten/ingecheckteGastenbestand.txt";
	private String locGasten = "administratie/gasten/gastenbestand.txt";
	private String locKamers = "administratie/kamers.txt";
	private String locPresentRes = "administratie/reserveringen/presentReserveringen.txt";
	private String locPastRes = "administratie/reserveringen/pastReserveringen.txt";
	private String locFutureRes = "administratie/reserveringen/futureReserveringen.txt";
	private String locBlacklist = "administratie/gasten/blacklist.txt";
	private MyFileReader myReader = new MyFileReader();
	private GuestControler guestControler = new GuestControler();

	public HotelControler() {
	}

	//////////////////////////////////////////////////////
	///////////////// CONTROL FUNCTIES ///////////////////
	//////////////////////////////////////////////////////

	public void checkIn(Reservation reservation) {
		Room room = getSpecificRoom(reservation.getRoomNr());
		room.setToOccupied();

		System.out.println(reservation.toString());

		double costs = calculateRoomCosts(room.getRoomType().getDayPrice(),
						reservation.getStartDate(), reservation.getEndDate());
		addBill( reservation.getGuestName() ,"Room Bill", "Room Costs", costs, reservation.getStartDate(), 1);

		//presentReservationList.add(reservation);
		addPresentReservation(reservation.getGuestName(), room.getRoomType(), reservation.getStartDate(), reservation.getEndDate());
		removeReservation(reservation.getGuestName(), "future");
		String billLocation = "administratie/rekeningen/" + reservation.getGuestName() +
						", " + reservation.getRoomNr() + ", " + reservation.printDate(reservation.getStartDate()) + ".txt";
		//maak een nieuw bill file aan.
		MyFileWriter.newFile(billLocation);
		MyFileWriter.insertLine(billLocation, "verblijf, "+ "kamerkosten, " + reservation.printDate(reservation.getStartDate()) + ", 1, " + costs);

		//voeg gast toe aan ingecheckte gasten
		String guestLocation = locIngecheckteGasten;
		String guest = MyFileReader.searchLine(locGasten, reservation.getGuestName());
		MyFileWriter.insertLine(guestLocation, guest);
	}

	public String checkOut(String name) {
		String foundReservation = MyFileReader.searchLine(locPresentRes, name);
		if(foundReservation == null){
			return (name + " heeft al uitgecheckt of bestaat niet.");
		}
		MyFileWriter.deleteLine(locIngecheckteGasten, name);
		MyFileWriter.insertLine(locPastRes, foundReservation);
		
		return (name + " heeft uitgecheckt.");
	}

	private double calculateRoomCosts(double dayprice, Date start, Date end){
		long days = (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
		return dayprice * days;
	}

	/*
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
	 * 
	 */

	//////////////////////////////////////////////////////
	///////////////// SEARCH FUNCTIES ////////////////////
	//////////////////////////////////////////////////////

	public Guest getSpecificGuest(String name) {
		return guestControler.getSpecificGuest(name);
	}

	private Room getAvailableRoom(RoomType roomType) {
		Room foundRoom = null;
		ArrayList<String> rooms = MyFileReader.readFromFile(locKamers);
		String[] splittedRoom;
		for(String room : rooms){
			splittedRoom = room.split(", ");
			if(RoomType.valueOf(splittedRoom[1]).equals(roomType)){
				foundRoom = new Room(splittedRoom[0], RoomType.valueOf(splittedRoom[1]));
			}
		}
		return foundRoom;
	}

	private ArrayList<Room> getAvailableRooms(Date start, Date end){
		ArrayList<Room> posibilityRooms = new ArrayList<Room>();
		for(Room room : setRoomList()){
			if(!alreadyReservation(room, start, end, MyFileReader.readFromFile(locPresentRes)) &&
							!alreadyReservation(room, start, end, MyFileReader.readFromFile(locFutureRes))){
					if(!posibilityRooms.contains(room)){
						posibilityRooms.add(room);
					}
			}
		}
		return posibilityRooms;
	}

	private ArrayList<Room> setRoomList(){
		ArrayList<String> allRooms = MyFileReader.readFromFile(locKamers);
		ArrayList<Room> roomList = new ArrayList<Room>();
		String[] splitRooms;
		for(String room : allRooms){
			splitRooms = room.split(", ");
			Room newRoom = new Room(splitRooms[0], RoomType.valueOf(splitRooms[1]));
			roomList.add(newRoom);
		}
		return roomList;
	}

	public Reservation getReservation(String name, String location){
		String reservation = MyFileReader.searchLine(location, name);
		if(reservation == null){
			System.out.println("Deze klant heeft (nog) geen geldige reservering.");
			return null;
		}
		String[] splittedReservation = reservation.split(", ");
		Reservation foundReservation = new Reservation(splittedReservation[0],splittedReservation[1],
						new Date(splittedReservation[2]), new Date(splittedReservation[3]));
		return foundReservation;
	}

	public ArrayList<Reservation> getAllReservations(String name, String location){
		ArrayList<String> file = MyFileReader.readFromFile(location);
		ArrayList<Reservation> resList = new ArrayList<Reservation>();
		String[] split;
		for(String res : file){
			split = res.split(", ");
			if(split[0].equals(name)){
				Reservation foundReservation = new Reservation(split[0],split[1],
						new Date(split[2]), new Date(split[3]));
				resList.add(foundReservation);
			}
		}

		if(resList == null){
			System.out.println("Deze klant heeft (nog) geen geldige reservering.");
			return null;
		}
		return resList;
	}

	public Room getSpecificRoom(String roomNr) {
		String room = MyFileReader.searchLine(locKamers, roomNr);
		if(room == null){
			System.out.println("room niet gevonden");
			return null;
		}
		String[] splittedRoom = room.split(", ");
		Room foundRoom = new Room(splittedRoom[0], RoomType.valueOf(splittedRoom[1]));
		return foundRoom;
	}

	public String getGuestNameInReservation(String roomNr){
		String name = "";
		String[] splittedLine;
		for(String line : MyFileReader.readFromFile(locPresentRes)){
			splittedLine = line.split(", ");
			if(splittedLine[1].equals(roomNr)){
				name = splittedLine[0];
			}
		}
		return name;
	}


	//////////////////////////////////////////////////////
	///////////////// CHECK FUNCTIES /////////////////////
	//////////////////////////////////////////////////////

	public boolean isGuestOnBlacklist( String name ){
		String guest = MyFileReader.searchLine(locBlacklist, name);
		if(guest != null){
			return true;
		}
		return false;
	}

	private boolean alreadyReservation(Room room, Date startDate, Date endDate, ArrayList<String> list){
		String[] splittedReservation;
		for (String reservation : list) {
			splittedReservation = reservation.split(", ");
			Date start = new Date(splittedReservation[2]);
			Date end = new Date(splittedReservation[3]);
			if (splittedReservation[1].equals(room.getRoomNr())){
				if(start.before(endDate) && end.after(startDate)){
					return true;
				}
			}
		}
		return false;
	}

	
	//////////////////////////////////////////////////////
	/////////////////// ADD FUNCTIES /////////////////////
	//////////////////////////////////////////////////////

	public void addBill(String name, String category, String description, double costs, Date aDate, int amount ) {
		Guest guest = getSpecificGuest( name );
		if ( guest != null) {
			guest.addBill(category, description, costs, aDate, amount);
		}
	}

	public void addBill(String name, String category, String description,double costs, int amount ) {
		Guest guest = getSpecificGuest( name );
		if ( guest != null) {
			guest.addBill(category, description, costs, amount);
		}
	}

	public void addBillForSpecificGuest( String name, String category, String description, double costs, int amount){
    	for ( Guest guest : guestList){
    		if( guest.getName().equals(name) ){
    			guest.addBill(category, description, costs, amount );
    		}
	    }
	}

	public void addReservation(String name, RoomType roomtype, Date startDate, Date endDate, String type){
		Room room = getAvailableRoom(roomtype);
		Reservation reservation = new Reservation(name, room.getRoomNr(), startDate, endDate);
		String location = "administratie/reserveringen/" + type + "Reserveringen.txt";
		String line = name + ", " + room.getRoomNr() + ", " + reservation.printDate(startDate) + ", " + reservation.printDate(endDate);
		MyFileWriter.insertLine(location, line);
	}

	public void addFutureReservation(String name, RoomType roomtype, Date startDate, Date endDate){
		addReservation(name, roomtype, startDate, endDate, "future");
	}

	public void addPresentReservation(String name, RoomType roomtype, Date startDate, Date endDate){
		addReservation(name, roomtype, startDate, endDate, "present");
	}

	public void addPastReservation(String name, RoomType roomtype, Date startDate, Date endDate){
		addReservation(name, roomtype, startDate, endDate, "past");
	}

	public void addGuestToBlacklist( String name ){
		guestControler.addGuestToBlacklist(name);
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
		return guestControler.removeGuestFromBlacklist(name);
	}

	public void removeReservation(String name, String type){
		MyFileWriter.deleteLine("administratie/reserveringen/" + type + "Reserveringen.txt", name);
	}

	//////////////////////////////////////////////////////
	////////////////// PRINT FUNCTIES ////////////////////
	//////////////////////////////////////////////////////

	private void printGuests(ArrayList<String> guests){
		guestControler.printGuests(guests);
	}

	public void printAllGuests() {
		printGuests(MyFileReader.readFromFile(locGasten));
	}

	public void printAllCheckedInGuests() {
		printGuests(MyFileReader.readFromFile(locIngecheckteGasten));
	}
	
	public void printBlacklist(){
		printGuests(MyFileReader.readFromFile(locBlacklist));
	}

	public void printAllBillsSpecificGuest(String name) {
		Reservation reservation = getReservation(name, locPresentRes);
		String foundReservation = MyFileReader.searchLine(locPresentRes, name);
		MyFileWriter.deleteLine(locPresentRes, foundReservation);
		if(reservation == null){
			return;
		}
		if (reservation != null) {
			System.out.println( "\nDe kosten van " + name + " zijn: " );
			System.out.printf("Omschrijving %20s Datum %20s Aantal %20s Prijs %n", " ", " ", " ");
			double costs = 0;
			String splittedString[];
			String location = "administratie/rekeningen/" + reservation.getGuestName() +
						", " + reservation.getRoomNr() + ", " + reservation.printDate(reservation.getStartDate()) + ".txt";

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

	private void printRoom(ArrayList<String> rooms){
		System.out.printf("Order %20s Kamernr %15s KamerType %20s Prijs %n", " ", " ", " ");
		String splittedString[];
		for ( String room : rooms ){
			splittedString = room.split(", ");
			System.out.printf(" %-25s %-22s %-30s %-33s%n", RoomType.valueOf(splittedString[1]).getOrder(),
							splittedString[0], RoomType.valueOf(splittedString[1]), RoomType.valueOf(splittedString[1]).getDayPrice());
		}
	}

	public void printAllRooms() {
		printRoom(MyFileReader.readFromFile(locKamers));
	}

	public void printAvailableRooms() {
		ArrayList<String> presentReservations = MyFileReader.readFromFile(locPresentRes);
		ArrayList<String> occupiedRooms = new ArrayList<String>();
		ArrayList<String> availableRooms = new ArrayList<String>();
		String[] splitReservation;
		for(String res : presentReservations){
			splitReservation = res.split(", ");
			occupiedRooms.add(splitReservation[1]);
		}
		for (String room : MyFileReader.readFromFile(locKamers)) {
			if(!occupiedRooms.contains(room)){
				availableRooms.add(room);
			}
		}
		printRoom(availableRooms);
	}

	public void printRoomsPerType(Date startDate, Date endDate) {
		ArrayList<String> roomList = new ArrayList<String>();
		for(Room room : getAvailableRooms(startDate, endDate)){
			roomList.add(room.getRoomNr() + ", " + room.getRoomType().toString());
		}
		printRoom(roomList);
	}

	public void printRoomInformation(String roomNr) {
	  ArrayList<String> temp = new ArrayList<String>();
		temp.add(MyFileReader.searchLine(locKamers, roomNr));
		printRoom(temp);
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
		printReservation(MyFileReader.readFromFile(locFutureRes));
	}

	public void printPresentReservations(){
		printReservation(MyFileReader.readFromFile(locPresentRes));
	}

	public void printPastReservations(){
		printReservation(MyFileReader.readFromFile(locPastRes));
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
}