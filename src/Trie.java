// looked at https://github.com/gaylemcd/ctci/tree/master/java/CtCILibrary/CtCILibrary

import java.util.ArrayList;

public class Trie {
	TrieNode root;
	
	public Trie() {
		root = new TrieNode(0);
	}
	
	public TrieNode getRoot() {
		return root;
	}
	public void addCeleb(String celebName, Data.Phoneme[] pronun) {
		root.addCeleb(celebName, pronun);
	}
	public void put(String celebName, Data.Phoneme[] pronun) { // works with existing code
		root.addCeleb(celebName, pronun);
	}
	public ArrayList<String> getRhymes(Data.Phoneme[] pronun) {
		return root.getCelebs(pronun);
	}
	public ArrayList<String> getRhymes(Data.Phoneme[] pronun, boolean stopAtVowel) {
		return root.getCelebs(pronun, stopAtVowel);
	}
}
