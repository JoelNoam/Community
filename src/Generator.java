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
		
		

		Data d = new Data();
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
			String celebrity = getRhymingCeleb(phraseV, d.getCelebs());
			
			
			System.out.println(celebrity);
//			System.out.println("Again?");
//			String answer = kb.nextLine();
//			running =  answer.startsWith("Y") || answer.startsWith("y");
		}
			
	}
	private static String getRhymingCeleb(Data.Phoneme[] phraseV, Trie celebs) {
		// TODO Auto-generated method stub
		ArrayList<String> rhymes = celebs.getRhymes(phraseV);
		if(rhymes == null) { // no rhyme
			return null;
		}
//		for(String rhyme : rhymes) {
//			System.out.println(rhyme);
//		}
		Random r = new Random();
		int index = r.nextInt(rhymes.size());
		return rhymes.get(index);
	}
	private static String getRhymingCeleb(Data.Phoneme[] phraseV, LinkedHashMap<String, Data.Phoneme[]> celebs) {
		String line = "";
		String name = "";
		String pronun = "";
		String bestName = "";
		int maxRhyme = 0;
		Data.Phoneme[] celebV;
//		while(sc.hasNextLine()) { // for each celebrity
		for(java.util.Map.Entry<String, Data.Phoneme[]> entry : celebs.entrySet()) {
			
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
	private static int scoreRhyme(Data.Phoneme[] phraseV, Data.Phoneme[] celebV) {
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
