/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package versie3;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author fleuroudenampsen
 */
public class MyFileWriter {

	public MyFileWriter(){

	}

	public static void newFile(String location){
		try{
			File billFile = new File(location);
			if(!billFile.exists()){
				billFile.createNewFile();
			}
		}catch(IOException e){

		}
	}

	public static void insertLine(String location, String newLine){
		//lees hele bestaande bestand uit
		//zet elke regel in een arrayList element
		ArrayList<String> file = MyFileReader.readFromFile(location);
		
		//voeg de nieuwe line als laatste toe
		file.add(newLine);
		//maak een writer

		writer(file, location);
	}

	private static void writer(ArrayList<String> file, String location){
		//maak een writer
		try{
			FileWriter fstream = new FileWriter(location);
			BufferedWriter out = new BufferedWriter(fstream);

			//schrijf zolang als de list is, elke keer een regel in het bestand
			for(String line : file){
				out.write(line + "\n");
			}
			//sluit readers en writers
			out.close();
    }catch (Exception e){//Catch exception if any
      System.err.println("Error: " + e.getMessage());
    }
	}

	public static void deleteLine(String location, String line){
		//lees bestand uit
		ArrayList<String> file = MyFileReader.readFromFile(location);
		String splittedLine[];
		int number = 0;
		int numberToDelete = 0;
		for(String fileLine : file){
			//eerste element is naam, daarmee gaan we vergelijken, dus split 'm
			splittedLine = fileLine.split(", ");
			if(splittedLine[0].equals(line)){
				numberToDelete = number;
			}
			number++;
		}
		//verwijder hele regel
		file.remove(numberToDelete);
		//schrijf nieuwe file weg
		writer(file, location);
	}

}