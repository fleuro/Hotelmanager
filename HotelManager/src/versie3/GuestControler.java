/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package versie3;

import java.util.ArrayList;

/**
 *
 * @author fleuroudenampsen
 */
public class GuestControler {

	private String locGasten = "administratie/gasten/gastenbestand.txt";
	private String locBlacklist = "administratie/gasten/blacklist.txt";
	private String locPresentRes = "administratie/reserveringen/presentReserveringen.txt";

	public GuestControler(){

	}

	public Guest getSpecificGuest(String name) {
		String guestString = MyFileReader.searchLine(locGasten, name);
		Guest guest = null;
		if(guestString != null){
			String[] splitGuest;
			splitGuest = guestString.split(", ");
			boolean blackList = false;
			if(splitGuest[3].equals("true")){
				blackList = true;
			}
			guest = new Guest(splitGuest[0], splitGuest[1], splitGuest[2], blackList);
		}

		return guest;
	}

	public void addGuestToBlacklist( String name ){
		String blacklistGuest = "";
		String splittedString[] = null;
		for ( String guest : MyFileReader.readFromFile(locGasten) ){
			splittedString = guest.split(", ");
			if(splittedString[0].equals(name)){
				blacklistGuest = guest;
			}
		}
		if(blacklistGuest != null){
			//veranderingen in gastenbestand. gast op ongewenst zetten
			MyFileWriter.deleteLine(locGasten, name);
			splittedString = blacklistGuest.split(", ");
			String newLine = splittedString[0] + ", " + splittedString[1] + ", " + splittedString[2] + ", " + "true";
			MyFileWriter.insertLine(locGasten, newLine);

			//gast toevoegen aan de blacklist
			MyFileWriter.insertLine(locBlacklist, newLine);
		}
	}

	public boolean removeGuestFromBlacklist( String name ){
		String blacklistGuest = "";
		String splittedString[] = null;
		for ( String guest : MyFileReader.readFromFile(locGasten) ){
			splittedString = guest.split(", ");
			if(splittedString[0].equals(name)){
				blacklistGuest = guest;
			}
		}
		if(blacklistGuest != null){
			//veranderingen in gastenbestand. gast op ongewenst zetten
			MyFileWriter.deleteLine(locGasten, name);
			splittedString = blacklistGuest.split(", ");
			String newLine = splittedString[0] + ", " + splittedString[1] + ", " + splittedString[2] + ", " + "false";
			MyFileWriter.insertLine(locGasten, newLine);

			//gast verwijderen van de blacklist
			MyFileWriter.deleteLine(locBlacklist, name);
			return true;
		}
		return false;
	}

	public void printGuests(ArrayList<String> guests){
		System.out.printf("Naam %20s Adres %20s Email %20s Blacklist %n", " ", " ", " ");
		String splittedString[];
		for ( String guest : guests ){
			splittedString = guest.split(", ");
			System.out.printf(" %-25s %-22s %-30s %-33s%n", splittedString[0], splittedString[1], splittedString[2],
								splittedString[3]);
		}
	}

}