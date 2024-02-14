package com.dwag1n.app.common.trie;

/**
 * @author: Duo.Wang
 * @version: v1.0
 */
public class TrieNode {
    TrieNode[] children;
    int count;

    // Initialize your data structure here.
    public TrieNode() {
        children = new TrieNode[26];
        count = 0;
    }
}
