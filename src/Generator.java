import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map.*;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



public class Generator {
	private static final String DICT_FILE_NAME = "cmudict.dict";
	private static final String CELEBS_FILE_NAME = "celebs";

	public static void main(String[] args) {
		String phrase;
		Scanner kb = new Scanner(System.in);
		boolean running = true;
		
		
		
		while (running) {
			System.out.print("Enter a word or phrase: ");
			phrase = kb.nextLine();
			if(phrase.matches("-*[0-9]")) {
				break;
			}
//			String celebrity = getCelebrityFromPhrase(phrase);
			Data d = new Data();
			Data.Phonemes[] phraseV = d.getVector(phrase);
			String celebrity = getRhymingCeleb(phraseV, d.getCelebs());
			
			
			System.out.println(celebrity);
//			System.out.println("Again?");
//			String answer = kb.nextLine();
//			running =  answer.startsWith("Y") || answer.startsWith("y");
		}
			
	}
	private static String getRhymingCeleb(Data.Phonemes[] phraseV, LinkedHashMap<String, Data.Phonemes[]> celebs) {
		String line = "";
		String name = "";
		String pronun = "";
		String bestName = "";
		int maxRhyme = 0;
		Data.Phonemes[] celebV;
//		while(sc.hasNextLine()) { // for each celebrity
		for(java.util.Map.Entry<String, Data.Phonemes[]> entry : celebs.entrySet()) {
			
			name = entry.getKey();
			celebV = entry.getValue();
			int rhyme = scoreRhyme(phraseV, celebV);
			
			if(rhyme > maxRhyme) {
				maxRhyme = rhyme;
				bestName = name;
			}
		}
		return bestName;
		
	}
	private static int scoreRhyme(Data.Phonemes[] phraseV, Data.Phonemes[] celebV) {
		// TODO Auto-generated method stub
		int i=0;
		for(; i < Math.min(phraseV.length, celebV.length); i++) {
			if(phraseV[phraseV.length-i-1] != celebV[celebV.length-i-1]) {
				break;
			}
		}
		return i;
	}

	private static String getCelebrityFromVector(String[] vector) {  // TODO: rewrite to put all in memory first
		File f = new File(CELEBS_FILE_NAME);
		try {
			Scanner sc = new Scanner(f);
			String line = "";
			String name = "";
			String pronun = "";
			String bestName = "";
			int maxRhyme = 0;
			String[] celebV;
			while(sc.hasNextLine()) { // for each celebrity
				line = sc.nextLine();
				String[] words = line.split("\t");
				name = words[0];
				celebV = words[1].split(" ");
				int rhyme = scoreRhyme(vector, celebV);
				
				if(rhyme > maxRhyme) {
					maxRhyme = rhyme;
					bestName = name;
				}
			}
			return bestName;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int scoreRhyme(String[] vector, String[] celebV) {
		// TODO Auto-generated method stub
		int i=0;
		for(; i < Math.min(vector.length, celebV.length); i++) {
			if(!vector[vector.length-i-1].equals(celebV[celebV.length-i-1])) {
				break;
			}
		}
		return i;
	}





}
