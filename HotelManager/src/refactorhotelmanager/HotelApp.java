package refactorhotelmanager;
import java.util.Calendar;
import java.util.Date;

public class HotelApp {
/*	public static void main(String[] args) {
		Hotel hotel = new Hotel();

		
		
		// verdieping.kamernummer, type, Occupied
		hotel.addRoom("1.12", RoomType.normalSmall, false);
		hotel.addRoom("1.13", RoomType.luxurySmall, false);
		hotel.addRoom("2.12", RoomType.normalMedium, false);
		hotel.addRoom("2.13", RoomType.luxuryLarge, false);
		hotel.addRoom("0.12", RoomType.economySmall, false);

		// naam, adres, rekeningnummer, e-mail, blacklist
		hotel.addGuest("Gerd Vuuren", "Heuvelrug 28, 2385 BH, Boekelo", 733448,
				"G.Vuuren@gmail.com", true);
		hotel.addGuest("Jan de Sickenvas", "Dalstraat 11, 8365 WC, Amsterdam",
				233467447, "deJan@Sickenvas.net", false);
		hotel.addGuest("Arend Reiselief", "Hoogsingel 41, 2233 AN, Hengelo",
				88373662, "Arend.reisje@hotmail.com", false);
		hotel.addGuest("Toos Loemsant", "Tussenweg 3, 3345 ZG, Almelo", 223334,
				"Tooske142@gmail.com", false);
		hotel.addGuest("Frits de Keumus",
				"Zeewinkelstraat 78, 1110 AA, Katwijk aan Zee", 9900126,
				"KeumusFrits@live.nl", true);

		Date date = Calendar.getInstance().getTime();
		System.out.println("Current date and time is: " + date);

		Date c = new Date("20 Jan 2011");
		System.out.println("nieuwe date " + c);

		// date format: " day Month year"
		// naam, kamernummer
		hotel.checkIn("Toos Loemsant", RoomType.normalSmall, new Date(
				"27 Nov 2010"), new Date("30 Nov 2010"), false);
		hotel.checkIn("Jan de Sickenvas", RoomType.businessSmall, new Date(
				"27 Nov 2010"), new Date("30 Nov 2010"), false);
		hotel.checkIn("Frits de Keumus", RoomType.normalSmall, new Date(
				"24 Nov 2010"), new Date("28 Nov 2010"), false);

		System.out
				.println("======================================================================");
		System.out.println("All guests:");
		hotel.printAllGuests();

		System.out
				.println("======================================================================");
		System.out.println("Guest information:");
		hotel.printSpecificGuestInformation("Frits de Keumus");

		System.out
				.println("======================================================================");
		System.out.println("All Rooms:");
		hotel.printAllRooms();

		System.out
				.println("======================================================================");
		System.out.println("Available Rooms:");
		hotel.printAvailableRooms();

		System.out
				.println("======================================================================");
		System.out.println("Gegevens van kamer 1.12:");
		hotel.printRoomInformation("1.12");

		// Bill
		System.out
				.println("======================================================================");
		hotel.addBill("Toos Loemsant", "Overige", "General Bill", 150.50, new Date( "12 Dec 2010" ) );
		hotel.addBill("Toos Loemsant", "Overige", "WC Bill", 300, new Date( "15 Dec 2010 "));
		hotel.printAllBillsSpecificGuest("Toos Loemsant");

		// Test of niet bestaande gast wordt aangemaakt.
		// hotel.printAllGuestBills(" Pietje ");
		// hotel.addBill( "Pietje", 0, "General Bill", 150);
		// hotel.addBill( "Pietje", 0, "-WC Bill", -300 );

		// naam
		hotel.checkOut("Toos Loemsant");
		hotel.checkOut("Toos Loemsant");

		// System.out.println("======================================================================");
		// System.out.println("Available Rooms:");
		// hotel.printAvailableRooms();
		// email check
		
		// check email
		//System.out.println("\n======================================================================\n");
		//hotel.printAllBillsAllGuests();

	}
	 try {    String str_date="11-June-07";
          DateFormat formatter ; 
      Date date ; 
          formatter = new SimpleDateFormat("dd-MMM-yy");
              date = (Date)formatter.parse(str_date); 
         Calendar cal=Calendar.getInstance();
       cal.setTime(date);
             System.out.println("Today is " +cal );
    }
  catch (ParseException e)
    {System.out.println("Exception :"+e);    }    
     
   }
} 
	*
	*
	*
	*
	*
	*/

}