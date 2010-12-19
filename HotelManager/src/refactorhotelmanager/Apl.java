/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package refactorhotelmanager;

/**
 *
 * @author fleuroudenampsen
 */
public class Apl {
	public static void main( String[] args ) {
		try {
			MainMenu menu = new MainMenu();
			menu.show();
		} catch ( Exception exception ) {
			exception.printStackTrace();
		}
	}
}
