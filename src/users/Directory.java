package users;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Trie-based directory implementation for efficient string searching and storage.
 * Supports exact and partial matching of strings with associated data.
 */
public class Directory {
    /**
     * Data class to store information associated with each directory entry.
     */
    public static class EndOfWordData {
        private final String id;
        private final String name;
        private final String email;
        private final String dept;

        /**
         * Constructs a new EndOfWordData object.
         * @param name The name of the person
         * @param id The ID of the person
         * @param email The email address of the person
         * @param dept The department of the person
         */
        public EndOfWordData(String name, String id, String email, String dept) {
            this.name = name;
            this.id = id;
            this.email = email;
            this.dept = dept;
        }

        /** @return The name of the person */
        public String getName() {return name;}

        /** @return The ID of the person */
        public String getId() {return id;}

        /** @return The department of the person */
        public String getDept() {return dept;}

        /** @return The email address of the person */
        public String getEmail() {return email;}
    }

    /**
     * Node class for the Trie data structure.
     * Each node contains references to child nodes and optional end-of-word data.
     */
    private class TrieNode {
        private final HashMap<Character, TrieNode> children;
        private boolean isEndOfWord;
        private EndOfWordData data;

        /**
         * Constructs a new TrieNode with empty children and no data.
         */
        public TrieNode() {
            this.children = new HashMap<>();
            this.isEndOfWord = false;
            this.data = null;
        }
    }

    private final TrieNode root;

    /**
     * Constructs a new Directory with an empty root node.
     */
    public Directory() {
        this.root = new TrieNode();
    }

    /**
     * Inserts a word and its associated data into the directory.
     * @param word The word to insert
     * @param data The associated data to store with the word
     */
    public void insert(String word, EndOfWordData data) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char index = word.charAt(i);
            if (!current.children.containsKey(index)) {
                current.children.put(index, new TrieNode());
            }
            current = current.children.get(index);
        }
        current.isEndOfWord = true;
        current.data = data;
    }

    /**
     * Searches for an exact match of a word in the directory.
     * @param word The word to search for
     * @return The associated EndOfWordData if found, null otherwise
     */
    public EndOfWordData search(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char index = word.charAt(i);
            if (!current.children.containsKey(index)) {
                return null;
            }
            current = current.children.get(index);
        }
        if (current != null && current.isEndOfWord) {
            return current.data;
        } else {
            return null;
        }
    }

    /**
     * Performs a partial search in the directory for words starting with the given prefix.
     * Only processes prefixes longer than 2 characters.
     * @param word The prefix to search for
     * @return ArrayList of EndOfWordData objects matching the prefix
     */
    public ArrayList<EndOfWordData> searchPartial(String word) {
        if (word == null || word.length() <= 2) {
            return new ArrayList<>();
        }

        ArrayList<EndOfWordData> results = new ArrayList<>();
        TrieNode current = root;

        // Traverse to the node corresponding to the word
        for (int i = 0; i < word.length(); i++) {
            char index = word.charAt(i);
            if (!current.children.containsKey(index)) {
                return results;
            }
            current = current.children.get(index);
        }

        // If we reached here, collect all EndOfWordData from this point
        collectAllEndOfWordData(current, results);
        return results;
    }

    /**
     * Helper method to recursively collect all EndOfWordData objects from a given node.
     * @param node The starting node
     * @param results ArrayList to store the collected results
     */
    private void collectAllEndOfWordData(TrieNode node, ArrayList<EndOfWordData> results) {
        if (node == null) {
            return;
        }

        if (node.isEndOfWord && node.data != null) {
            results.add(node.data);
        }

        // Recursively collect from all children
        for (TrieNode child : node.children.values()) {
            collectAllEndOfWordData(child, results);
        }
    }
}