// looked at https://github.com/gaylemcd/ctci/tree/master/java/CtCILibrary/CtCILibrary

import java.util.ArrayList;
import java.util.HashMap;

public class TrieNode {
	int level; // root is zero
	ArrayList<String> celebs;
	HashMap<Data.Phoneme, TrieNode> children;
	
	public TrieNode(int level) {
		// TODO Auto-generated constructor stub
		this.level = level;
		celebs = new ArrayList<String>();
		children = new HashMap<Data.Phoneme, TrieNode>();
	}
	
	public void addCeleb(String celebName, Data.Phoneme[] pronun) {
//		int L = level; // for debugging
		celebs.add(celebName);
//		TrieNode child = getChild(pronun[pronun.length - 1 - level]);
		if(pronun.length <= level + 1) {  // base case
			return;
		}
		Data.Phoneme phoneme = pronun[pronun.length - 1 - level];
		TrieNode child;
		if (!children.containsKey(phoneme)) {
			child = new TrieNode(level + 1);
			children.put(phoneme, child);
		} else {
			child = children.get(phoneme);
		}
		child.addCeleb(celebName, pronun);
		
	}
	public ArrayList<String> getCelebs(Data.Phoneme[] pronun) {
//		System.out.println("level " + level);
//		for(String celeb : celebs) {
//			System.out.println(celeb);
//		}
//		System.out.println("**********************************");
		if(pronun.length <= level) { // run out of phonemes
			return celebs;
		}
		// here, more phonemes to look at
		Data.Phoneme phoneme = pronun[pronun.length - 1 - level];
		if(children.containsKey(phoneme)) { // has appropriate child
			return children.get(phoneme).getCelebs(pronun);
		}
		if(level != 0) { // don't return entire list if no rhyme
			return celebs;
		}
		return null;
		
	}
	public ArrayList<String> getCelebs(Data.Phoneme[] pronun, boolean stopAtVowel) {
//		if(level != 0) {
//			Data.Phoneme cur = pronun[pronun.length - level];
//			System.out.print("Phoneme is " + cur + ".  It is ");
//			System.out.print(cur.isVowel() ? "" : "not ");
//			System.out.println("a vowel.");
//		}
//		System.out.println("level " + level);
//		for(String celeb : celebs) {
//			System.out.println(celeb);
//		}
//		System.out.println("**********************************");
		if(pronun.length <= level) { // run out of phonemes
			return celebs;
		}								// current Phoneme is a vowel
		if(level != 0 && stopAtVowel && pronun[pronun.length - level].isVowel()) {
			return celebs;
		}
		// here, more phonemes to look at
		Data.Phoneme nextPhoneme = pronun[pronun.length - 1 - level];
		if(children.containsKey(nextPhoneme)) { // has appropriate child
			return children.get(nextPhoneme).getCelebs(pronun, stopAtVowel);
		}
		if(level != 0) { // don't return entire list if no rhyme
			return celebs;
		}
		return null;
		
	}
	public ArrayList<String> getEndRhymes(Data.Phoneme[] pronun) {
		if(pronun.length < level) { // run out of phonemes
			return null;
		}				// current Phoneme is a vowel
		if(level != 0 && pronun[pronun.length - level].isVowel()) {
			return celebs;
		}
		// here, more phonemes to look at
		Data.Phoneme nextPhoneme = pronun[pronun.length - 1 - level];
		if(children.containsKey(nextPhoneme)) { // has appropriate child
			return children.get(nextPhoneme).getEndRhymes(pronun);
		}
		return null;		
	}
	public ArrayList<String> getDoubleRhymes(Data.Phoneme[] pronun) {
		return getMultiRhymes(pronun,2);
		//return getDoubleRhymes(pronun,false);
	}
	public ArrayList<String> getDoubleRhymes(Data.Phoneme[] pronun, boolean vowelSeen) {
		return getMultiRhymes(pronun,2);
		/*if(pronun.length < level) { // run out of phonemes
			return null;
		}				// current Phoneme is a vowel
		if(level != 0 && pronun[pronun.length - level].isVowel()) {
			if(vowelSeen) {
				return celebs;
			}
			vowelSeen = true;
		}
		if(pronun.length == level) {
			return null;
		}
		// here, more phonemes to look at
		Data.Phoneme nextPhoneme = pronun[pronun.length - 1 - level];
		if(children.containsKey(nextPhoneme)) { // has appropriate child
			return children.get(nextPhoneme).getDoubleRhymes(pronun,vowelSeen);
		}
		return null;	*/
	}
	public ArrayList<String> getTripleRhymes(Data.Phoneme[] pronun) {
		return getMultiRhymes(pronun,3);
	}
	public ArrayList<String> getTripleRhymes(Data.Phoneme[] pronun, int vowelsSeen) {
		if(pronun.length < level) { // run out of phonemes
			return null;
		}				// current Phoneme is a vowel
		if(level != 0 && pronun[pronun.length - level].isVowel()) {
			if(vowelsSeen == 2) {
				return celebs;
			}
			vowelsSeen++;
		}
		if(pronun.length == level) {
			return null;
		}
		// here, more phonemes to look at
		Data.Phoneme nextPhoneme = pronun[pronun.length - 1 - level];
		if(children.containsKey(nextPhoneme)) { // has appropriate child
			return children.get(nextPhoneme).getTripleRhymes(pronun,vowelsSeen);
		}
		return null;
	}
	public ArrayList<String> getMultiRhymes(Data.Phoneme[] pronun, int vowelsLeft) {
		if(pronun.length < level) { // run out of phonemes
			return null;
		}				// current Phoneme is a vowel
		if(level != 0 && pronun[pronun.length - level].isVowel()) {
			vowelsLeft--;
			if(vowelsLeft == 0) {
				return celebs;
			}
		}
		if(pronun.length == level) {
			return null;
		}
		// here, more phonemes to look at
		Data.Phoneme nextPhoneme = pronun[pronun.length - 1 - level];
		if(children.containsKey(nextPhoneme)) { // has appropriate child
			return children.get(nextPhoneme).getMultiRhymes(pronun,vowelsLeft);
		}
		return null;
	}
	public ArrayList<String> getConsonanceRhymes(Data.Phoneme[] pronun) {
		Data.Phoneme last = pronun[pronun.length-1];
		if(!last.isVowel()) { // if the final phoneme is a consonant
			return children.get(last).getCelebs();
		}
		return null;
	}

	public ArrayList<String> getCelebs() {
		// TODO Auto-generated method stub
		return celebs;
		
	}

}
