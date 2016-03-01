import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Scanner;





public class Generator {
	private static final String DICT_FILE_NAME = "cmudict.dict";
	private static final String CELEBS_FILE_NAME = "celebs";

	public static void main(String[] args) {
		String phrase;
		Scanner kb = new Scanner(System.in);
		boolean running = true;
		
		Runtime runtime = Runtime.getRuntime();
		System.out.println("max: " + runtime.maxMemory()/(1024*1024));
		System.out.println("used: " + (runtime.totalMemory()-runtime.freeMemory())/(1024*1024));
		
		long sTime = System.nanoTime();
		Data d = new Data();
		long eTime = System.nanoTime();
		System.out.println("Time to load: " + (eTime - sTime)/1e9);
		System.out.println("used: " + (runtime.totalMemory()-runtime.freeMemory())/(1024*1024));
		System.out.print("Enter rhyme type: ");
		String rhymeType = kb.nextLine();
		while (running) {
			System.out.print("Enter a word or phrase: ");
			phrase = kb.nextLine();
			if(phrase.matches("-*[0-9]*")) {
				break;
			}
//			String celebrity = getCelebrityFromPhrase(phrase);
			Data.Phoneme[] phraseV = d.getVector(phrase);
//			System.out.println(d.getCelebs().getRoot().getCelebs(phraseV).get(3));
			if(phraseV == null) {
				System.out.println("Last word of phrase not found in dictionary");
				kb.nextLine();
				continue;
			}
			//String celebrity = getRhymingCeleb(phraseV, d.getCelebs());
			ArrayList<String> rhymes = d.getRhymes(phraseV,rhymeType);
			if(null != rhymes && !rhymes.isEmpty()) {
				for(String r : rhymes) {
					System.out.println(r);
				}
			} else {
				System.out.println("no rhymes found");
			}
			
			
			//System.out.println(celebrity);
//			System.out.println("Again?");
//			String answer = kb.nextLine();
//			running =  answer.startsWith("Y") || answer.startsWith("y");
		}
			
	}
	/*private static String getRhymingCeleb(Data.Phoneme[] phraseV, Trie celebs) {
		// TODO Auto-generated method stub
		ArrayList<String> rhymes = celebs.getRhymes(phraseV,true);
		if(rhymes == null) { // no rhyme
			return null;
		}
		for(String rhyme : rhymes) {
			System.out.println(rhyme);
		}
		Random r = new Random();
		int index = r.nextInt(rhymes.size());
		return rhymes.get(index);
	}*/

}
