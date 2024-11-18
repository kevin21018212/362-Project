package users;

import java.util.HashMap;

public class Directory {
    private class TrieNodeData {
        private final String id;
        private final String name;
        private final String email;
        private final String dept;

        public TrieNodeData(String name, String id, String email, String dept) {
            this.name = name;
            this.id = id;
            this.email = email;
            this.dept = dept;
        }

        public String getName() {return name;}

        public String getId() {return id;}

        public String getDept() {return dept;}

        public String getEmail() {return email;}
    }


    private class TrieNode {
        private final HashMap<Character, TrieNode> children;
        private boolean isEndOfWord;


        public TrieNode() {
            this.children = new HashMap<>();
            this.isEndOfWord = false;
        }
    }
    private class Trie {
        private final TrieNode root;

        public Trie() {
            this.root = new TrieNode();
        }

        public void insert(String word) {
            TrieNode current = root;
            for (int i = 0; i < word.length(); i++) {
                char index = word.charAt(i);
                if (!current.children.containsKey(index)) {
                    current.children.put(index, new TrieNode());
                }
                current = current.children.get(index);
            }
            current.isEndOfWord = true;
        }

        public boolean search(String word) {
            TrieNode current = root;
            for (int i = 0; i < word.length(); i++) {
                char index = word.charAt(i);
                if (!current.children.containsKey(index)) {
                    return false;
                }
                current = current.children.get(index);
            }
            return current != null && current.isEndOfWord;
        }
//        public boolean search(String word) {
//            TrieNode current = root;
//            for (int i = 0; i < word.length(); i++) {
//                int index = word.charAt(i) - 'a';
//                if (current.children[index] == null) {
//                    return false;
//                }
//                current = current.children[index];
//            }
//            return current != null && current.isEndOfWord;
//        }
    }
    public final int ID_TRIE = 0;
    public final int NAME_TRIE = 1;
    public final int EMAIL_TRIE = 2;


    public Directory() {
        Trie trie = new Trie();
        trie.insert("Alice");
        trie.insert("Bob");
        trie.insert("Charlie");
        trie.insert("David");
        trie.insert("Eve");
        trie.insert("Frank");
        trie.insert("Grace");
    }
}
