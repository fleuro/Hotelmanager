package refactorhotelmanager;
import java.util.Date;
public class SubMenu_Overview {

    //Atributen
    private MenuChooser  menu;  
    private HotelControler        hotel;

    
    //Constructor
    public SubMenu_Overview(HotelControler aHotel) {
    	hotel = aHotel;
        createMenu();
    }

    //Methoden
    /**
     *  Hulpmethode om het menu aan te maken
     *  Deze wordt aangeroepen via de constructor
    */
    private void createMenu() {
        menu = new MenuChooser( "Overzichten", "maak uw keuze"       );
        menu.addItem    ( "Overzicht klant m.b.t. Factuur"           );
        menu.addItem    ( "Overzicht ingecheckte gasten"             );
        menu.addItem    ( "Overzicht alle gasten"                    );
        menu.addItem    ( "Overzicht blacklist"                      );
				menu.addItem    ( "Overzicht alle kamers"                    );
				menu.addItem    ( "Overzicht alle  beschikbare kamers"       );
				menu.addItem    ( "Overzicht toekomstige reserveringen"      );
				menu.addItem    ( "Overzicht geschiedenis reserveringen"     );
				menu.addItem    ( "Overzicht heden reserveringen"					   );
        menu.addStopItem( "Vorig Menu"                               );
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
            	String name = TuiHelper.askQuestionWithTextAnswer( "Naam:" , true);
            	if( hotel.getSpecificGuest(name) != null){
            		String ja   = "";
            		while ( !ja.equals("Ja")){
            			hotel.printAllBillsSpecificGuest( name );
            			ja   = TuiHelper.askQuestionWithTextAnswer( "Kloppen alle gegevens? (Ja of Nee)", true);
            			if ( ja.equals("Nee")){
            				//modifyBill( name );
            			} 
            			TuiHelper.hitEnterWaitForEnter();
            		}
            	} else{
            		System.err.println(" De opgegeven naam staat niet in de database");
            	}
            	
            break;
            
            case 2:
            	hotel.printAllCheckedInGuests();
            	TuiHelper.hitEnterWaitForEnter();
            break;
            
            case 3:
            	hotel.printAllGuests();
            	TuiHelper.hitEnterWaitForEnter();
            break;
            
            case 4:
            	hotel.printBlacklist();
            	TuiHelper.hitEnterWaitForEnter();
            break;

						case 5:
							hotel.printAllRooms();
							TuiHelper.hitEnterWaitForEnter();
            break;

						case 6:
							hotel.printAvailableRooms();
							TuiHelper.hitEnterWaitForEnter();
            break;

						case 7:
							hotel.printFutureReservations();
							TuiHelper.hitEnterWaitForEnter();
            break;

						case 8:
							hotel.printPastReservations();
							TuiHelper.hitEnterWaitForEnter();
            break;

						case 9:
							hotel.printPresentReservations();
							TuiHelper.hitEnterWaitForEnter();
            break;

            default: 
            break;
            }
        }
    }
    /*
    public void modifyBill( String name ){
    	System.out.println();
    	int number  			= TuiHelper.askQuestionWithNumberAnswer("Factuurnummer: ");;
    	while( !(hotel.getSpecificGuest(name).checkFactuurNumber( number )) ){
    		number  			= TuiHelper.askQuestionWithNumberAnswer("Factuurnummer: ");
    	}
    	System.out.println();
    	
    	String category 			= TuiHelper.askQuestionWithTextAnswer( "Categorie: ", true);
    	System.out.println();
    	
    	String newDescription 	= TuiHelper.askQuestionWithTextAnswer( "Nieuwe beschrijving: ", true);
    	System.out.println();
    	
    	double newCosts 		= TuiHelper.askQuestionWithNumberAnswer( "Nieuwe kosten: ", true);
    	System.out.println();
    	String aDate            = TuiHelper.askQuestionWithTextAnswer("Datum: ", true);
    	//System.out.println("Nieuwe kosten � " + newCosts);
    	hotel.modificationToBillForSpecificGuest( name, number, category, newDescription, newCosts,new Date( aDate ));
    	System.out.println( "De wijzigingen zijn toegebracht." );
    }
		 * 
		 */
}