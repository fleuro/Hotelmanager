package refactorhotelmanager;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu {
    //Attributen
    private MenuChooser         menu;
    private SubMenu_Overview    submenu_overview;
    private Hotel               hotel;
    
    //Constructor
    public MainMenu(){
        maakMenu();  
        hotel			       = new Hotel();
        submenu_overview       = new SubMenu_Overview(hotel);
    }
        
    //Methoden
    /**
     *  Hulpmethode om het menu aan te maken
     *  Deze wordt aangeroepen via de constructor
    */
    private void maakMenu() {
        menu = new MenuChooser( "Hoofdmenu", "maak uw keuze"  );
        menu.addItem         ( "Check gast in"                );
        menu.addItem         ( "Maak reservering"             );
        menu.addItem         ( "Check out"                    );
        menu.addItem         ( "Voeg rekening toe aan klant"  );
        menu.addItem         ( "Verwijder rekening van klant" );
        menu.addItem         ( "Voeg klant toe aan blacklist" );
        menu.addItem         ( "Verwijder klant van blacklist");
        menu.addItem         ( "Overzichten"                  );
				menu.addItem				 ( "print future reserveringen"		);
        menu.addStopItem     ( "Stoppen"                      );
    }

    /**
     * Zorgt ervoor dat het menu aan de gebruiker wordt getoont.
     * Afhankelijk van de keuze wordt goede methode aangeroepen.
     */
    public void show() {
        int choice = -1;
        while ( choice != 0 ) {
            choice = menu.getMenuChoice();
            switch ( choice ) {
            
            case 1:
            	checkIn();
            break;
            
            case 2:
							register(null,true);
						break;
            
            case 3:
            	checkOut();
            break;
            
            case 4:
            	addBill();
            break;
            
            case 5:
            	removeBill();
            break;
            
            case 6:
            	addToBlacklist();
            break;
            
            case 7:
            	deleteFromBlacklist();
            break;

						case 8:
            	startSubMenu_Overview();
						break;
						case 9:
            	hotel.printFutureReservations();
						break;
						
            default:
            break;
            }
        }
        System.out.println( "Bedankt en graag tot ziens!" );
    }
    
    public void startSubMenu_Overview(){
        submenu_overview.show();
    }
    
    public void addToBlacklist(){
    	boolean exist = false;
    	while ( !exist ){
    		String name = TuiHelper.askQuestionWithTextAnswer("Naam:", "Controleer of de klant bestaat", true);
    		if(name == null){
    			System.err.println( "Voer een gast in\n" );
    		}
    		if ( !hotel.guestExists( name )){
    			System.err.println( "De klant bevindt zich niet in de database\n" );
    			TuiHelper.wait( 500 );
    		} else{
    			hotel.addGuestToBlacklist( name );
    			exist = true;
    			System.out.println( name + " is succesvol toegevoegd aan de blacklist.");
    			System.out.println();
    		}
    	}
    }
    
    public void deleteFromBlacklist(){
    	boolean exist = false;
    	while ( !exist ){
    		String name = TuiHelper.askQuestionWithTextAnswer("Naam:", "Controleer of de klant bestaat", true);
    		if ( !hotel.guestExists( name )){
    			System.err.println( "De klant bevindt zich niet in de database\n" );
    			TuiHelper.wait( 500 );
    		} else{
    			if ( hotel.removeGuestFromBlacklist( name ) ){
    				exist = true;
    				System.out.println( name + " is succesvol verwijderd van de blacklist.");
    				System.out.println();
    			} else {
    				System.err.println( "De klant bevindt zich niet op de blacklist.");
    			}
    		}
    	}
    }

		public void register(String name, boolean checkName){
			String aName;
			if(checkName){
			 aName = TuiHelper.askQuestionWithTextAnswer( "Naam:", "Controleer of de klant bestaat", true );
			}else{
				aName = name;
			}
        if ( hotel.getSpecificGuest( aName ) == null ){
            System.err.println( "De klant bevindt zich nog niet in de database\n" );
            TuiHelper.wait( 500 );
            registerForm( aName );
        } else if ( hotel.isGuestOnBlacklist( aName ) ){
        	System.err.println( "De klant bevindt zich op de blacklist en kan daarom niet inchecken\n");
        	TuiHelper.wait( 500 );
        	return;
        }
			makeReservation(aName);
		}
    
    public void checkIn(){
        String aName = TuiHelper.askQuestionWithTextAnswer( "Naam:", "Controleer of de klant bestaat", true );
        if ( hotel.getSpecificGuest( aName ) == null ){
            System.err.println( "De klant bevindt zich nog niet in de database\n" );
            TuiHelper.wait( 500 );
            register(aName, false);
        } else if ( hotel.isGuestOnBlacklist( aName ) ){
        	System.err.println( "De klant bevindt zich op de blacklist en kan daarom niet inchecken\n");
        	TuiHelper.wait( 500 );
        	return;
        }
				Reservation reservation = hotel.getFutureReservation( aName );
				Date today = Calendar.getInstance().getTime();
				if(reservation.getStartDate().before(today)){
					hotel.checkIn(reservation, true);
					System.out.println(reservation.printInfo());
				}else{
					System.err.println("De reserveringsdatum begint niet vandaag. Gast kan nu niet worden ingecheckt." +
									" De reservering is wel toegevoegd.");
					TuiHelper.wait( 500 );
				}
    }
    
    public void checkOut(){
        boolean exists = false;
        while ( !exists ){
            String aName = TuiHelper.askQuestionWithTextAnswer( "Naam:", "Controleer of de klant bestaat", true );
            if ( !hotel.guestExists( aName )){
                System.err.println( "De klant bevindt zich niet in de database" );
            } else{
                exists = true;
                System.out.println( hotel.checkOut( aName ) );
            }
            hotel.printAllBillsSpecificGuest( aName );
            TuiHelper.hitEnterWaitForEnter();
        }
    }

		public void addBill(){
    	String roomNumber = TuiHelper.askQuestionWithTextAnswer("Kamer nummer: ", false);
			String name = getGuestNamePerRoomNumber(roomNumber);
    	System.out.println();
    	if( hotel.guestExists( name )){
				hotel.printCategories();

    		int categoryNumber = TuiHelper.askQuestionWithNumberAnswer( "Categorie: ");
				String category = hotel.getCategoryByNumber(categoryNumber);
				hotel.printItemsPerCategory(category);

				int itemNumber = TuiHelper.askQuestionWithNumberAnswer( "Item: ");
				String item = hotel.getItemByNumber(category, itemNumber);

				String[] splittedItem = item.split(": ");

    		System.out.println();

				int amount = TuiHelper.askQuestionWithNumberAnswer( "Aantal: ");
				double costs = Double.parseDouble(splittedItem[1]);
				if(amount > 1){
					costs = calculateCosts(amount, costs);
				}
				System.out.println();
				
    		hotel.addBillForSpecificGuest( name, category, splittedItem[0], costs, amount );
				Reservation reservation = hotel.getPresentReservation(name);

				String location = "administratie/rekeningen/" + reservation.getGuestName() +
						", " + reservation.getRoom().getRoomNr() + ", " + reservation.printDate(reservation.getStartDate()) + ".txt";
				
				MyFileWriter.insertLine(location, category + ", " + splittedItem[0] + ", " + giveProperToday() + ", " + amount + ", " + costs);
    		System.out.println( "De rekening voor " + name + " is toegevoegd" );
    		TuiHelper.hitEnterWaitForEnter();
    	} else{
    		System.err.println( "De opgegeven naam staat niet in onze database" );
    	}
		}

		private String giveProperToday(){
			Date today = Calendar.getInstance().getTime();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);
			int day = calendar.get(Calendar.DATE);
			int month = calendar.get(Calendar.MONTH) + 1;
			int year = calendar.get(Calendar.YEAR);

			return day + " " + month + " " + year;
		}

		private double calculateCosts(int amount, double costs){
			return amount * costs;
		}

		/*
    public void addBill(){
    	String roomNumber = TuiHelper.askQuestionWithTextAnswer("Kamer nummer: ", false);
			String name = getGuestNamePerRoomNumber(roomNumber);
    	System.out.println();
    	if( hotel.guestExists( name )){

    		String category 			= TuiHelper.askQuestionWithTextAnswer( "Categorie: ", true);
    		System.out.println();
    	
    		String newDescription 	= TuiHelper.askQuestionWithTextAnswer( "Nieuwe beschrijving: ", true);
    		System.out.println();
    	
    		double newCosts 		= ( TuiHelper.askQuestionWithNumberAnswer( "Nieuwe kosten: ", true ));
    		System.out.println();
    	
    		hotel.addBillForSpecificGuest( name, category, newDescription, newCosts );
    		System.out.println( "De rekening is toegevoegd" );
    		TuiHelper.hitEnterWaitForEnter();
    	} else{
    		System.err.println( "De opgegeven naam staat niet in onze database" );
    	}
    }
    */
		
	private String getGuestNamePerRoomNumber(String roomNumber){
		if(hotel.getSpecificRoom(roomNumber) != null){
			return hotel.getGuestNameInReservation(roomNumber);
		}
		return null;
	}
    
	public void removeBill(){
		String name = TuiHelper.askQuestionWithTextAnswer( "Naam:", "Controleer of de klant bestaat", true );
			if ( !hotel.guestExists( name )){
					System.err.println( "De klant bevindt zich niet in de database" );
			} else{
				hotel.printAllBillsSpecificGuest( name );
				System.out.println();
				int billNr   = TuiHelper.askQuestionWithNumberAnswer( "Vul het factuurnummer in van de rekening die je wilt verwijderen:");
				if ( hotel.removeBillForSpecificGuest( name, billNr ) ){
					System.out.println( "De rekening is succesvol verwijderd." );
				} else{
					System.err.println( "Het ingevoerde factuurnummer is fout.");
				}
			}
    }
    
    public void registerForm( String name ){
    	System.out.println( "Registratieformulier:" );
			System.out.println( "=====================" );
			System.out.println();

			System.out.println( "Naam:         (voornaam + achternaam)");
			System.out.println( name );
			System.out.println();

			String address = registerAddress();
			//int accountNr = registerAccountNr();
			String email = registerEmail();

			System.out.println();

			boolean blacklist = false;
			hotel.addGuest( name, address, email, blacklist );
			String guestLocation = "administratie/gasten/gastenbestand.txt";
			MyFileWriter.insertLine(guestLocation, name + ", " + address + ", " +
						email + ", " + blacklist);
			System.out.println( "Gegevens succesvol opgeslagen" );
			TuiHelper.hitEnterWaitForEnter();
    }

		private String registerAddress(){
			String address = "";
			boolean valid = false;
			while (!valid){
				address = TuiHelper.askQuestionWithTextAnswer( "Adresgegevens:   voorbeeldstraat 84" , false);
				System.out.println();
				if(containsNumber(address)){
					if ( address != null && !address.equals("") && !address.equals(" ")){
						valid = true;
					}
				} else{
							System.err.println( "U heeft geen geldig adres ingevoerd\n");
						}
					}
			return address;
		}

		private int registerAccountNr(){
			boolean valid = false;
			int accountNr = 0;
			while(!valid){
				accountNr = TuiHelper.askQuestionWithNumberAnswer( "Rekeningnummer:" );
				if(isValidAccountNumber(accountNr)){
					valid = true;
				}else{
					System.err.println( "Het rekeningnummer moet groter zijn dan 7 en kleiner dan 11.");
					System.out.println();
				}
			}
			System.out.println();
			return accountNr;
		}

		private String registerEmail(){
			String email = "";
			boolean valid = false;
			while (!valid){
				email   = TuiHelper.askQuestionWithTextAnswer( "E-mail (optioneel):          voorbeeld@example.com" , false);
				if ( email.equals("") ){
					valid = true;
				}else if ( isValidEmail( email ) ){
					valid = true;
				} else{
					System.err.println( "Het opgegeven email adres voldoet niet aan de eisen.");
					System.out.println();
				}
			}
			return email;
		}

		private boolean isValidEmail( String email ) {
			String expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
			CharSequence inputStr = email;
			Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(inputStr);

			return matcher.matches();
		}

    private boolean containsNumber(String stringToCheck){
		char[] c = stringToCheck.toCharArray();
		for(int i=0; i < stringToCheck.length(); i++){
			if ( Character.isDigit(c[i])){
				return true;
			}
		 }
		return false;
	}
    
    private boolean isValidAccountNumber(int accountNr){
		String lengthChecker = String.valueOf(accountNr);
		if(lengthChecker.length() >= 7){
			if(lengthChecker.length() <= 11){
				return true;
			}
		}
		return false;
	}
    
    public boolean isDate( String date ){
    	try{
    		Date aDate = new Date(date);
    		//System.out.println( aDate );
    		return true;

    	} catch( Exception e ){
    		return false;
    	}
    }
    
    private void makeReservation(String name){
			System.out.println( "Reservering maken:" );
			System.out.println( "=====================" );

			boolean finished = false;
			Date startDate = null;
			Date endDate = null;

			while( !finished ){
				System.out.println();
				System.out.println( "Datum format: 01 Jan 2010" );

				startDate = registerStartdate();
				endDate = registerEnddate();

				if( !invalidDates(startDate, endDate) ){
					finished = true;
				}
			}

			hotel.printRoomsPerType(startDate, endDate);
			RoomType roomtype = registerRoomType();

			String continueReservation  = TuiHelper.askQuestionWithTextAnswer( "Maak reservering? Ja/Nee" , true);
			if(continueReservation.equals("Ja")){
				hotel.addFutureReservation(name, roomtype, startDate, endDate);
				//hotel.checkIn(name, roomtype, startDate, endDate, true);
			}else{
			}
	}

		private Date registerStartdate(){
			Date startDate = null;
			String start = "";
			boolean valid = false;
			while ( !valid ){
				start    = TuiHelper.askQuestionWithTextAnswer( "Begin datum:" , false);
				if ( isDate( start ) ){
					startDate = new Date(start);
					System.out.println();
					valid = true;
				} else {
					System.err.println( "Voer een geldige datum in\n");
				}
			}
			return startDate;
		}

		private Date registerEnddate(){
			Date endDate = null;
			String end = "";
			boolean valid = false;
			while( !valid ){
				end = TuiHelper.askQuestionWithTextAnswer( "Eind datum:" , false);
				if ( isDate( end ) ){
					endDate = new Date(end);
					System.out.println();
					valid = true;
				} else {
					System.err.println( "Voer een geldige datum in\n");
				}
			}
			return endDate;
		}

		private RoomType registerRoomType(){
			int type  = 0;
			RoomType roomtype = null;
			boolean valid = false;
			while (!valid){
				type  = TuiHelper.askQuestionWithNumberAnswer( "Kamer type:" );
				roomtype = getRoomPerOrder(type);
				if(roomtype == null){
					System.err.println("Kamertype bestaat niet.");
				} else {
					valid = true;
				}
				System.out.println();
			}
			return roomtype;
		}
    
    private RoomType getRoomPerOrder(int order){
			for (RoomType type : RoomType.values()) {
				if (type.getOrder() == order) {
					return type;
				}
			}
			return null;
		}
    
    private boolean invalidDates(Date start, Date end){
		// check met datum van vandaag. beiden moeten daarna liggen
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE,-1);

		if(start.before(today.getTime()) || end.before(today.getTime())){
			System.err.println("Begin datum en/of eind datum liggen voor huidige datum");
			return true;
		}
		// check of start voor end ligt
		if(start.after(end)){
			System.err.println("Begin datum is later dan de eind datum. Dit is niet mogelijk.");
			return true;
		}
		return false;
	}
    
    private RoomType roomTypeExists(String roomtype) {
		if (roomtype != null) {
			for (RoomType type : RoomType.values()) {
				if (roomtype.equalsIgnoreCase(type.name())) {
					return type;
				}
			}
		}
		return null;
	}
}