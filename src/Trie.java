// looked at https://github.com/gaylemcd/ctci/tree/master/java/CtCILibrary/CtCILibrary

import java.util.ArrayList;
// suffix trie
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
	public ArrayList<String> getEndRhymes(Data.Phoneme[] pronun) {
		return root.getEndRhymes(pronun);
	}
	public ArrayList<String> getDoubleRhymes(Data.Phoneme[] pronun) {
		return root.getMultiRhymes(pronun,2);
	}
	public ArrayList<String> getTripleRhymes(Data.Phoneme[] pronun) {
		return root.getMultiRhymes(pronun,3);
	}
	public ArrayList<String> getNearRhymes(Data.Phoneme[] pronun) {
		return root.getNearRhymes(pronun);
	}
	public ArrayList<String> getRhymes(Data.Phoneme[] pronun, String rhymeType) {
		switch(rhymeType) { // works in java 7, should be lower case
			case "end":
				return root.getEndRhymes(pronun);
			case "double":
				return root.getDoubleRhymes(pronun);
			default:
				return root.getEndRhymes(pronun);
		}
		
	}
}
