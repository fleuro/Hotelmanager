/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package refactorhotelmanager;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author fleuroudenampsen
 */
public class MyFileReader {

	private ArrayList<String> priceList = new ArrayList<String>();

	public MyFileReader(){
		readPriceListFromFile("administratie/prijslijst.txt");
		if(priceList == null){
			return;
		}
	}

	//deze moet weg, opschonen. gebruik de static. is beter
	private void readPriceListFromFile(String fileName) {
		try {
			BufferedReader file = new BufferedReader( new FileReader( fileName ) );
			while( file.ready() ) {
				String line = file.readLine();
				priceList.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> readFromFile(String fileName) {
		ArrayList<String> inputFile = new ArrayList<String>();
		try {
			BufferedReader file = new BufferedReader( new FileReader( fileName ) );
			while( file.ready() ) {
				String line = file.readLine();
				inputFile.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputFile;
	}

	public  ArrayList<String> getCategories(){
		ArrayList<String> categoryList = new ArrayList<String>();
		for(String line : priceList){
			String[] splitLine = line.split(", ");
			//format prijslijst:
			//prijslijst, category, item, price
			if(!categoryList.contains(splitLine[1])){
				categoryList.add(splitLine[1]);
			}
		}
		return categoryList;
	}

	public ArrayList<String> getItems(String category){
		ArrayList<String> itemList = new ArrayList<String>();
		for(String line : priceList){
			String[] splitLine = line.split(", ");
			if(splitLine[1].equals(category)){
				itemList.add(splitLine[2] + ": " + splitLine[3]);
			}
		}
		return itemList;
	}

	public static String searchLine(String fileName, String name){
		String foundLine = "";
		String[] splitLine;
		for(String line : readFromFile(fileName)){
			splitLine = line.split(", ");
			if(splitLine[0].equals(name)){
				foundLine = line;
			}
		}
		return foundLine;
	}
}
