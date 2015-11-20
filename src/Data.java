import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Data {
		
	private static final String DICT_FILE_NAME = "cmudict.dict";
	private static final String CELEBS_FILE_NAME = "celebs";
	public enum Phoneme { AA, AE, AH, AO, AW, AY, B, CH, D, DH, EH, ER, EY, F, G,
		HH, IH, IY, JH, K, L, M, N, NG, OW, OY, P, R, S, SH, T, TH, UH, UW, V, W, Y, Z, ZH;
		public boolean isVowel() {
	        switch(this) {
	                case AA:case AE:case AH:case AO:case AW:case AY:case EH:case ER:
	                case EY:case IH:case IY:case OW:case OY:case UH:case UW:
	                        return true;
	                default:
	                        return false;
	        }
		}
	}
	
	private HashMap<String,Data.Phoneme[]> words;
	private Trie celebs;
	// phonemes are stored front to back
	
	public Data() {
		File celebsFile = new File(CELEBS_FILE_NAME);
		File wordsFile = new File(DICT_FILE_NAME);
		celebs = new Trie();
		try {
			Scanner sc = new Scanner(celebsFile);
			String line;
			String name;
			String[] celebStrings;
			Phoneme[] celebV;
			while(sc.hasNextLine()) { // for each celebrity
				line = sc.nextLine();
				String[] words = line.split("\t");
				name = words[0];
				celebStrings = words[1].split(" ");
				celebV = new Phoneme[celebStrings.length];
				for(int i = 0; i < celebStrings.length; i++) {
					celebV[i] = Phoneme.valueOf(celebStrings[i]);
				}
				celebs.put(name, celebV);
			}
			sc = new Scanner(wordsFile);
			line = "";
//			System.out.println("prefix: " + prefix);
			words = new HashMap<String, Phoneme[]>();
			while(sc.hasNextLine()) {
				line = sc.nextLine();
				if(line.matches(".*\\(\\d+\\).*")) {
					continue; 	// discard alternative pronunciations
				}
				
//					System.out.println(line);
				line = line.replaceAll("#.*", "").trim(); // get rid of comments
				String[] parts = line.replaceAll("[0-9]", "").split(" "); // strips accent
				String word = parts[0];
				Phoneme[] pronun = new Phoneme[parts.length-1]; // don't include word itself
				for(int i = 0; i < pronun.length; i++) {
					pronun[i] = Phoneme.valueOf(parts[i+1]);
				}
				words.put(word, pronun);
			}
//			System.out.println("words.size() == " + words.size());	
//			System.out.println(words.get("tuple"));
			
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Trie getCelebs() {
		return celebs;
	}

	public Phoneme[] getVector(String phrase) {
		String[] parts = phrase.split(" ");
		String word = parts[parts.length-1].toLowerCase();
		return words.get(word);
	
	}


	
}
