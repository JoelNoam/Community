
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;



public class Data {
		
	private static final String DICT_FILE_NAME = "cmudict.dict";
	private static final String CELEBS_FILE_NAME = "names1.dict";
//	private static final String CELEBS_FILE_NAME = "test.dict";
//	private static final String CELEBS_FILE_NAME = "names.dict"; // based on pantheon
	
//	private static final String CELEBS_FILE_NAME = "pantheon_all.dict";
//	private static final String CELEBS_FILE_NAME = "celebs";
	public enum Phoneme { AA, AE, AH, AH0, AO, AW, AY, B, CH, D, DH, EH, ER, EY, F, G,
		HH, IH, IY, JH, K, L, M, N, NG, OW, OY, P, R, S, SH, T, TH, UH, UW, V, W, Y, Z, ZH;
		public boolean isVowel() {
	        switch(this) {
	        		case AH0:
	                case AA:case AE:case AH:case AO:case AW:case AY:case EH:case ER:
	                case EY:case IH:case IY:case OW:case OY:case UH:case UW:
	                        return true;
	                default:
	                        return false;
	        }
		}
		public boolean isConsonant() {
			return !isVowel();
		}
		public Phoneme[] consonants() {
			return new Phoneme[]{B,CH,D,DH,F,G,HH,JH,K,L,M,N,NG,P,R,S,SH,T,TH,V,W,Y,Z,ZH};
		}
		public Phoneme[] similars() { // doesn't return itself
			switch(this) {
			//labial
			case P: return new Phoneme[]{B,F};
			case B: return new Phoneme[]{P,V};
			case F: return new Phoneme[]{P,V};
			case V: return new Phoneme[]{B,F,W};
			case M: return new Phoneme[]{N}; // nasal
			case W: return new Phoneme[]{V};
			//dent/alv
			case TH: return new Phoneme[]{DH,T,S};
			case DH: return new Phoneme[]{TH,D,Z};
			case T: return new Phoneme[]{TH,D};
			case D: return new Phoneme[]{DH,T};
			case S: return new Phoneme[]{TH,Z};
			case Z: return new Phoneme[]{DH,S};
			case N: return new Phoneme[]{M,NG};
			case L: return new Phoneme[]{R};
			case R: return new Phoneme[]{L};
			//pal
			case CH: return new Phoneme[]{JH,SH,ZH};
			case JH: return new Phoneme[]{CH,SH,ZH};
			case SH: return new Phoneme[]{CH,JH,ZH};
			case ZH: return new Phoneme[]{CH,JH,SH};
			case Y: return new Phoneme[]{};
			//vel
			case K: return new Phoneme[]{G};
			case G: return new Phoneme[]{K,NG};
			case NG: return new Phoneme[]{N,G};
			default: return new Phoneme[]{};
			}
		}
	}
	
	private HashMap<String,Data.Phoneme[]> words;
	private Trie suffixTrie;
	//private PrefixTrie prefixTrie;
	private HashMap<Phoneme,ArrayList<String>> finalVowelTable;
	// phonemes are stored front to back
	
	public Data() {
		File celebsFile = new File(CELEBS_FILE_NAME);
		File wordsFile = new File(DICT_FILE_NAME);
		suffixTrie = new Trie();
		finalVowelTable = new HashMap<Phoneme,ArrayList<String>>();
		try {
			Scanner sc = new Scanner(celebsFile);
			String line;
			String name;
			String[] celebStrings; // each element is an arpabet symbol
			Phoneme[] celebV;
			while(sc.hasNextLine()) { // for each celebrity
				line = sc.nextLine();
				line = line.replaceAll("#.*", "").trim(); // get rid of comments
				String[] words = line.split("\t");
				name = words[0];
				System.out.println(name);
				celebStrings = words[1].split(" ");
				celebV = new Phoneme[celebStrings.length];
				for(int i = 0; i < celebStrings.length; i++) {
//					System.out.println(celebStrings[i]);
					try {
						celebV[i] = Phoneme.valueOf(celebStrings[i]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.err.println(celebStrings[i] + " was problem");
					}
//					if(celebStrings[i].contains("0")) {
//						celebV[i] = Phoneme.AH0;
//					} else {						
//						celebV[i] = Phoneme.valueOf(celebStrings[i].replaceAll("[0-9]", ""));
//					}
				}
				suffixTrie.put(name, celebV);
				//prefixTrie.put(name, celebV);
				for(int i = celebV.length - 1; i >= 0; i--) {
					if(celebV[i].isVowel()) {
						ArrayList<String> L = finalVowelTable.get(celebV[i]);
						if(L == null) {
							L = new ArrayList<String>();
							finalVowelTable.put(celebV[i], L);
						}
						L.add(name);
						break;
					}
				}
			}
			//System.out.println(finalVowelTable);
			
			
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
				//String[] parts = line.replaceAll("[0-9]", "").split(" "); // strips accent
				String[] parts = line.split(" ");
				String word = parts[0];
				Phoneme[] pronun = new Phoneme[parts.length-1]; // don't include word itself
				for(int i = 0; i < pronun.length; i++) {
//					if(parts[i+1].contains("0")) {
//						pronun[i] = Phoneme.AH0;
//					} else {						
						pronun[i] = Phoneme.valueOf(parts[i+1].replaceAll("[0-9]", ""));
//					}
				}
				words.put(word, pronun);
			}
//			System.out.println("words.size() == " + words.size());
//			System.out.println(words.get("tuple"));
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	// THIS IS THE MAIN THING
	public ArrayList<String> getRhymes(Phoneme[] pronun, String rhymeType, int vowels, boolean near) {
		switch(rhymeType) {
			case "end":
				return suffixTrie.getMultiRhymes(pronun, vowels, near);
			case "assonance": // TODO make vowel trie, or do something for more than 1 vowel
				/*Phoneme p = null;
				for(int i = pronun.length - 1; i >= 0; i--) {
					if(pronun[i].isVowel()) {
						p = pronun[i];
						break;
					}
				}
				return finalVowelTable.get(p);*/
				return suffixTrie.getAssonanceRhymes(pronun,vowels);
			case "consonance":
				return suffixTrie.getConsonanceRhymes(pronun);
			default:
				return suffixTrie.getMultiRhymes(pronun, vowels, near);
		}
	}
	
	/**
	 * Returns an ArrayList<String> of celebrity names (all caps)
	 * 
	 * @param pronun pronunciation of the word/phrase to be rhymed
	 * @param rhymeType possibilities are "end" (default), "double", "triple", "assonance"
	 */
	public ArrayList<String> getRhymes(Phoneme[] pronun, String rhymeType) {
		switch(rhymeType) { // works in java 7, should be lower case
			case "end":
				return suffixTrie.getEndRhymes(pronun);
			case "double":
				return suffixTrie.getDoubleRhymes(pronun);
			case "triple":
				return suffixTrie.getTripleRhymes(pronun);
			case "assonance":
				Phoneme p = null;
				for(int i = pronun.length - 1; i >= 0; i--) {
					if(pronun[i].isVowel()) {
						p = pronun[i];
						break;
					}
				}
				return finalVowelTable.get(p);
			case "near":
				return suffixTrie.getNearRhymes(pronun);
			case "test":
				Scanner kb = new Scanner(System.in);
				System.out.println("How many vowels? near?");
				int vowels = kb.nextInt();
				String near = kb.next();
				if("y".equals(near)) {
					return(suffixTrie.getMultiRhymes(pronun,vowels,true));
				} else {
					return(suffixTrie.getMultiRhymes(pronun,vowels,false));					
				}
			default:
				return suffixTrie.getEndRhymes(pronun);
		}
	}
	
	/*public Trie getCelebs() {
		return celebs;
	}*/
	/*public PrefixTrie getPrefixTrie() {
		return prefixTrie;
	} */

	public Phoneme[] getVector(String phrase) {
		String[] parts = phrase.split(" ");
		String word = parts[parts.length-1].toLowerCase();
		return words.get(word);
	
	}
}
