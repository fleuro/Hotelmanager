package hotelmanager;
public class Apl {

	/**
	 * Opstart punt voor de applicatie. Maak de basisclasses aan en maak
	 * vervolgens het hoofdmenu zichtbaar
	 */
	public static void main( String[] args ) {
		try {
			MainMenu menu = new MainMenu();
			menu.show();
		} catch ( Exception exception ) {
			exception.printStackTrace();
		}
	}
}