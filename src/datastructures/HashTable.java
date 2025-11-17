package datastructures;

/**
 * Generic Hash Table Implementation using Separate Chaining
 * Uses CustomLinkedList for collision resolution
 * Key Features:
 * - Custom hash function for String keys
 * - Separate chaining for collision resolution
 * - Load factor monitoring
 * - Generic key-value storage
 * K-> key type
 * V-> value type
*/
public class HashTable<K, V> {
    //inner class to store key-value pairs
    private class Entry {
        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private CustomLinkedList<Entry>[] table;
    private int size; //number of entries
    private int capacity; //table size
    private static final int DEFAULT_CAPACITY = 101;
    private static final double MAX_LOAD_FACTOR = 0.7;

    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = new CustomLinkedList[capacity];
        this.size = 0;
        initializeTable();
    }

    //initialize all slots with empty linked lists
    private void initializeTable() {
        for (int i = 0; i < capacity; i++) {
            table[i] = new CustomLinkedList<>();
        }
    }

    /**
     * Custom hash function for any object key
     * For Strings: sums character codes
     * For other objects: uses hashCode()
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int hashValue;

        //Custom hash for String keys
        if (key instanceof String) {
            String strKey = (String) key;
            int total = 0;
            for (int i = 0; i < strKey.length(); i++) {
                total += strKey.charAt(i);
            }
            hashValue = Math.abs(total % capacity);
        } else {
            hashValue = Math.abs(key.hashCode() % capacity);
        }
        return hashValue;
    }

    /**
     * Inserts or updates a key-value pair
     * If key exists, updates value
     * If key doesn't exist, inserts new entry
     */

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = hash(key);
        CustomLinkedList<Entry> chain = table[index];
        //check if key already exists
        Node<Entry> current = chain.getHead();
        while (current != null) {
            if (current.getData().equals(key)) {
                //update existing value
                current.getData().value = value;
                return;
            }
            current = current.getNext();
        }
        //key does not exist insert new entry (head insertion for efficiency)
        Entry newEntry = new Entry(key, value);
        chain.addFirst(newEntry);
        size++;

        if (getLoadFactor()>MAX_LOAD_FACTOR){
            rehash();
        }
    }

    public V get(K key) {
        if (key == null) {
            return null;
        }
        int index = hash(key);
        CustomLinkedList<Entry> chain = table[index];

        //Search through chain
        Node<Entry> current = chain.getHead();
        while (current != null) {
            if (current.getData().equals(key)) {
                return current.getData().value;
            }
            current = current.getNext();
        }
        return null; //key not found
    }
    //checks if key exists in a table
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V remove(K key) {
        if (key == null) {
            return null;
        }
        int index = hash(key);
        CustomLinkedList<Entry> chain = table[index];

        //search and remove from chain
        Node<Entry> current = chain.getHead();
        int position =0;
        while (current != null) {
            if (current.getData().equals(key)) {
                V removedValue = current.getData().value;
                chain.remove(position);
                size--;
                return removedValue;
            }
            current = current.getNext();
            position++;
        }
        return null; //key not found
    }

    public double getLoadFactor() {
        return (double) size / capacity;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void clear(){
        initializeTable();
        size = 0;
    }
    /**
     * Rehashes table when load factor exceeds threshold
     * Creates new larger table and rehashes all entries
     */
    private void rehash() {
        int newCapacity = capacity * 2 + 1; //keep odd for better distribution
        CustomLinkedList<Entry>[] oldTable = table;

        //create new larger table
        table = new CustomLinkedList[newCapacity];
        capacity = newCapacity;
        size = 0;
        initializeTable();

        //rehash all entries
        for (CustomLinkedList<Entry> chain : oldTable) {
            Node<Entry> current = chain.getHead();
            while (current != null) {
                put(current.getData().key, current.getData().value);
                current = current.getNext();
            }
        }
    }

    //returns all keys in the hash table
    public DynamicArray<K> keys() {
        DynamicArray<K> keys = new DynamicArray<>();
        for (CustomLinkedList<Entry> chain : table) {
            Node<Entry> current = chain.getHead();
            while (current != null) {
                keys.add(current.getData().key);
                current = current.getNext();
            }
        }
        return keys;
    }

    //returns all values in the hash table
    public DynamicArray<V> values() {
        DynamicArray<V> values = new DynamicArray<>();
        for (CustomLinkedList<Entry> chain : table) {
            Node<Entry> current = chain.getHead();
            while (current != null) {
                values.add(current.getData().value);
                current = current.getNext();
            }
        }
        return values;
    }


}

