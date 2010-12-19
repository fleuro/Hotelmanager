package refactorhotelmanager;
import java.util.ArrayList;

public class MenuChooser {
    //Attributen
    private String title;
    private String inputQuestion;
    private ArrayList<String> menuItems;
    private boolean stopItemAdded = false;
    
    //Constructor
    public MenuChooser( String title, String inputQuestion ){
        menuItems = new ArrayList<String>();
        this.title         = title;
        this.inputQuestion    = inputQuestion;
    }
    
    public void addItem( String item ){
        menuItems.add( item );
    }
    
    public void addStopItem( String item ){
        menuItems.add( 0, item );
        stopItemAdded = true;
    }
    
    public int getMenuChoice(){
        int choice = -1;
        while( choice == -1 ){
            showMenu();
            choice = TuiHelper.askQuestionWithNumberAnswer( inputQuestion );
        }
        return choice;
    }
    
    private void showMenu(){
        if( !stopItemAdded ){
            addStopItem( "(FOUT!!! Er is nog geen stop item toegevoegd aan dit menu)" );
        }
        
		System.out.println( "==================================" );
		System.out.printf ( "=  %-30s=\n", title                 );
		System.out.println( "==================================" );
		
		// druk eerst alle gewone items af en als laatste het stopitem
		for ( int i = 1; i < menuItems.size(); i++ ) {
			System.out.printf( "%3d. %s\n", i, menuItems.get( i ) );
		}
		System.out.printf( "%3d. %s\n", 0, menuItems.get( 0 ) );
	}
	
	//Leesbaarheid
	public String getMenuChoiceString(){
	    int choice = getMenuChoice();
	    return menuItems.get( choice );
	}
}
   
    
        