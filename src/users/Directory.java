package users;

import java.util.HashMap;

public class Directory {
    private class EndOfWordData {
        private final String id;
        private final String name;
        private final String email;
        private final String dept;

        public EndOfWordData(String name, String id, String email, String dept) {
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
        private EndOfWordData data;


        public TrieNode() {
            this.children = new HashMap<>();
            this.isEndOfWord = false;
            this.data = null;
        }
    }

    private final TrieNode root;

    public Directory() {
        this.root = new TrieNode();
    }

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
}
