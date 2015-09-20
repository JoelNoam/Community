import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
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
			String celebrity = getCelebrityFromPhrase(phrase);
			System.out.println(celebrity);
//			System.out.println("Again?");
//			String answer = kb.nextLine();
//			running =  answer.startsWith("Y") || answer.startsWith("y");
		}
			
	}
	private static String getCelebrityFromPhrase(String phrase) {
		String[] words = phrase.split(" ");
		String lastWord = words[words.length - 1];
		String[] vector = getVector(lastWord.replaceAll("[^a-zA-Z]", ""));			
		if (vector == null) {
			return null;
		}
		return getCelebrityFromVector(vector);
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

	private static String[] getVector(String word) {
		String pronun = getPronun(word);
//		String pronun = getPronunInet(word);
		if(pronun == null) {
			return null;
		}
//		ArrayList<String> vector = new ArrayList<String>();
		String[] parts = pronun.split(" ");
		return parts;
//		vector.addAll(Arrays.asList(parts));
//		return vector;
	}
	private static String getPronun(String word) {
		String prefix = word.toLowerCase() + " "; //TODO: deal w/ multiple pronunciations
		File f = new File(DICT_FILE_NAME);
		try {
			Scanner sc = new Scanner(f);
			String line = "";
//			System.out.println("prefix: " + prefix);
			while(sc.hasNextLine()) {
				line = sc.nextLine();
				if(line.startsWith(prefix)) {
//					System.out.println(line);
					String pronun = line.replaceFirst(prefix, "")
							.replaceAll("[0-9]", ""); // strips accent
					return pronun;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		return null;
	}

	private static String getPronunInet(String word) {
		String html = null;
		URLConnection connection = null;
		try {
		  connection =  new URL("http://www.speech.cs.cmu.edu/cgi-bin/cmudict?in=" + word).openConnection();
//		  System.out.println("Connected");
		  Scanner scanner = new Scanner(connection.getInputStream());
		  scanner.useDelimiter("\\Z");
		  html = scanner.next();
		}catch ( Exception ex ) {
		    ex.printStackTrace();
		}
		Document doc = Jsoup.parse(html);
		Elements pronunEl = doc.select("body > div > tt:nth-child(5)");
		return pronunEl.text();
		
	}
}
