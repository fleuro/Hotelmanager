package refactorhotelmanager;
import java.util.Scanner;

public class TuiHelper{

    static Scanner scan = new Scanner( System.in );

    public static String askQuestionWithTextAnswer( String question, boolean checkNumber){
        return askQuestionWithTextAnswer( question, null, checkNumber );
    }

    public static String askQuestionWithTextAnswer( String question, String header, boolean checkNumber ){
    	String respons = "";
		if ( header != null ) {
			System.out.println( "========================" );
			System.out.println( header                     );
		}
		System.out.println( question );

		respons = scan.nextLine();
		if(respons.equals(" ") || checkIfNumber(respons) && checkNumber){
			System.err.println( "Foute invoer. Geen lege waarden of cijfers" );
			TuiHelper.wait( 1000 );
			return askQuestionWithTextAnswer( question, header, checkNumber);
		}
		return respons;
    }
    
    private static boolean checkIfNumber(String in) {
        char[] c = in.toCharArray();
		for(int i=0; i < in.length(); i++){
			if ( !Character.isDigit(c[i])){
				return false;
			}
		 }
		return true;
    }


    public static int askQuestionWithNumberAnswer( String question ){
        return askQuestionWithNumberAnswer( question, null, Integer.MIN_VALUE, Integer.MAX_VALUE );
    }
    public static double askQuestionWithNumberAnswer( String question, boolean spec ){
        return askQuestionWithNumberAnswer( question, null, Double.MIN_VALUE, Double.MAX_VALUE, spec );
    }
    public static double askQuestionWithNumberAnswer( String question, String header, double lowestPossibleValue, double highestPossibleValue, boolean spec ){
        try {
            double answer = Double.parseDouble( askQuestionWithTextAnswer( question, header, false ) );
            if ( answer < lowestPossibleValue || answer > highestPossibleValue ){
                System.err.println( "getal" + answer + "niet tussen " + lowestPossibleValue + " en " + highestPossibleValue );
                TuiHelper.wait( 1000 );
                return askQuestionWithNumberAnswer( question, header, lowestPossibleValue,  highestPossibleValue, spec );
            } else {
                return answer;
            }
        } catch ( NumberFormatException nfe ){
            System.err.println( "Fout: geen getal, voer een getal in" );
            TuiHelper.wait( 1000 );
            return askQuestionWithNumberAnswer( question, header, lowestPossibleValue,  highestPossibleValue, spec );
        }
    }

    public static int askQuestionWithNumberAnswer( String question, String header, int lowestPossibleValue, int highestPossibleValue ){
        try {
            int answer = Integer.parseInt( askQuestionWithTextAnswer( question, header, false ) );
            if ( answer < lowestPossibleValue || answer > highestPossibleValue ){
                System.err.println( "getal" + answer + "niet tussen " + lowestPossibleValue + " en " + highestPossibleValue );
                TuiHelper.wait( 1000 );
                return askQuestionWithNumberAnswer( question, header, lowestPossibleValue,  highestPossibleValue );
            } else {
                return answer;
            }
        } catch ( NumberFormatException nfe ){
            System.err.println( "Fout: geen getal, voer een getal in" );
            TuiHelper.wait( 1000 );
            return askQuestionWithNumberAnswer( question, header, lowestPossibleValue,  highestPossibleValue );
        }
    }
   
    public static void hitEnterWaitForEnter(){
         System.out.println( "\n\n<-- Druk op enter om terug te gaan -->" );
        scan.nextLine();
    }

    public static void wait( int milliseconds ){
        try {
            Thread.sleep( milliseconds );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }
    
    public static void clearScreen(){
        System.out.print('\u000C');
    }
    
    public boolean maxSizeString( String string ){
		if ( string.length() < 100 ){
			return true;
		}
		return false;
	}
    
}