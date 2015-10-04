import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Data {
	
	
	private static final String DICT_FILE_NAME = "cmudict.dict";
	private static final String CELEBS_FILE_NAME = "celebs";
	public enum Phonemes { AA, AE, AH, AO, AW, AY, B, CH, D, DH, EH, ER, EY, F, G,
		HH, IH, IY, JH, K, L, M, N, NG, OW, OY, P, R, S, SH, T, TH, UH, UW, V, W, Y, Z, ZH}
	
//	private char[][] words;  // should eventually be a trie(?)
//	private char[][] celebs;  // should eventually be a trie
	private LinkedHashMap<String, Phonemes[]> celebs;  // key is name, String 
	// phonemes are stored front to back
	
	public Data() {
		File celebsFile = new File(CELEBS_FILE_NAME);
		celebs = new LinkedHashMap<String, Phonemes[]>();
		try {
			Scanner sc = new Scanner(celebsFile);
			String line;
			String name;
			String[] celebStrings;
			Phonemes[] celebV;
			while(sc.hasNextLine()) { // for each celebrity
				line = sc.nextLine();
				String[] words = line.split("\t");
				name = words[0];
				celebStrings = words[1].split(" ");
				celebV = new Phonemes[celebStrings.length];
				for(int i = 0; i < celebStrings.length; i++) {
					celebV[i] = Phonemes.valueOf(celebStrings[i]);
				}
				celebs.put(name, celebV);
			}
				
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public LinkedHashMap<String, Phonemes[]> getCelebs() {
		return celebs;
	}
	public Phonemes[] getCelebV(String name) {
		return celebs.get(name);
		
	}

	public Phonemes[] getVector(String phrase) {
		// TODO Auto-generated method stub
		String[] words = phrase.split(" ");
		String word = words[words.length-1];
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
					String[] parts = pronun.split(" ");
					Phonemes[] vector = new Phonemes[parts.length];
					for(int i = 0; i < parts.length; i++) {
						vector[i] = Phonemes.valueOf(parts[i]);
					}
					return vector;
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
