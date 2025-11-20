package datastructures;

/**
 * Generic Hash Table Implementation using Separate Chaining
 * Uses CustomLinkedList for collision resolution
 * Key Features:
 * - Custom hash function for String keys
 * - Separate chaining for collision resolution
 * - Load factor monitoring
 * - Generic key-value storage
 *
 * @param <K> Key type (must have meaningful hashCode and equals)
 * @param <V> Value type
 */
public class HashTable<K, V> {

    /**
     * Inner class to store key-value pairs
     */
    private class Entry {
        K key;
        V value;

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
    private int size; // Number of entries
    private int capacity; // Table size
    private static final int DEFAULT_CAPACITY = 101; // Prime number
    private static final double MAX_LOAD_FACTOR = 0.7;

    /**
     * Constructor with default capacity
     */
    @SuppressWarnings("unchecked")
    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = new CustomLinkedList[capacity];
        this.size = 0;
        initializeTable();
    }

    /**
     * Constructor with specified capacity
     * @param capacity Initial capacity (should be prime for better distribution)
     */
    @SuppressWarnings("unchecked")
    public HashTable(int capacity) {
        this.capacity = capacity;
        this.table = new CustomLinkedList[capacity];
        this.size = 0;
        initializeTable();
    }

    /**
     * Initialize all slots with empty linked lists
     */
    private void initializeTable() {
        for (int i = 0; i < capacity; i++) {
            table[i] = new CustomLinkedList<>();
        }
    }

    /**
     * Custom hash function for any object key
     * For Strings: sums character codes
     * For other objects: uses hashCode()
     *
     * @param key Key to hash
     * @return Hash value (index in table)
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int hashValue;

        // Custom hash for String keys (as per course notes)
        if (key instanceof String) {
            String strKey = (String) key;
            int total = 0;
            for (int i = 0; i < strKey.length(); i++) {
                total += strKey.charAt(i);
            }
            hashValue = Math.abs(total % capacity);
        } else {
            // Use built-in hashCode for other types
            hashValue = Math.abs(key.hashCode() % capacity);
        }

        return hashValue;
    }

    /**
     * Inserts or updates a key-value pair
     * If key exists, updates value
     * If key doesn't exist, inserts new entry
     *
     * @param key Key to insert/update
     * @param value Value to store
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = hash(key);
        CustomLinkedList<Entry> chain = table[index];

        // Check if key already exists (update scenario)
        Node<Entry> current = chain.getHead();
        while (current != null) {
            if (current.getData().key.equals(key)) {
                // Update existing value
                current.getData().value = value;
                return;
            }
            current = current.getNext();
        }

        // Key doesn't exist, insert new entry (head insertion for efficiency)
        Entry newEntry = new Entry(key, value);
        chain.addFirst(newEntry);
        size++;

        // Check load factor
        if (getLoadFactor() > MAX_LOAD_FACTOR) {
            rehash();
        }
    }

    /**
     * Retrieves value for given key
     *
     * @param key Key to search for
     * @return Value associated with key, or null if not found
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }

        int index = hash(key);
        CustomLinkedList<Entry> chain = table[index];

        // Search through chain
        Node<Entry> current = chain.getHead();
        while (current != null) {
            if (current.getData().key.equals(key)) {
                return current.getData().value;
            }
            current = current.getNext();
        }

        return null; // Key not found
    }

    /**
     * Checks if key exists in table
     *
     * @param key Key to check
     * @return true if key exists
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Removes entry with given key
     *
     * @param key Key to remove
     * @return Value that was removed, or null if key not found
     */
    public V remove(K key) {
        if (key == null) {
            return null;
        }

        int index = hash(key);
        CustomLinkedList<Entry> chain = table[index];

        // Search and remove from chain
        Node<Entry> current = chain.getHead();
        int position = 0;

        while (current != null) {
            if (current.getData().key.equals(key)) {
                V removedValue = current.getData().value;
                chain.remove(position);
                size--;
                return removedValue;
            }
            current = current.getNext();
            position++;
        }

        return null; // Key not found
    }

    /**
     * Returns current load factor
     * Load factor = number of entries / table capacity
     *
     * @return Load factor (0.0 to 1.0+)
     */
    public double getLoadFactor() {
        return (double) size / capacity;
    }

    /**
     * Returns number of entries in table
     *
     * @return Number of key-value pairs
     */
    public int size() {
        return size;
    }

    /**
     * Checks if table is empty
     *
     * @return true if no entries
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all entries from table
     */
    public void clear() {
        initializeTable();
        size = 0;
    }

    /**
     * Rehashes table when load factor exceeds threshold
     * Creates new larger table and rehashes all entries
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        int newCapacity = capacity * 2 + 1; // Keep odd for better distribution
        CustomLinkedList<Entry>[] oldTable = table;

        // Create new larger table
        table = new CustomLinkedList[newCapacity];
        capacity = newCapacity;
        size = 0;
        initializeTable();

        // Rehash all entries
        for (CustomLinkedList<Entry> chain : oldTable) {
            Node<Entry> current = chain.getHead();
            while (current != null) {
                put(current.getData().key, current.getData().value);
                current = current.getNext();
            }
        }
    }

    /**
     * Returns all keys in the hash table
     * Useful for iteration and searches
     *
     * @return DynamicArray of all keys
     */
    public DynamicArray<K> keys() {
        DynamicArray<K> keyList = new DynamicArray<>();
        for (CustomLinkedList<Entry> chain : table) {
            Node<Entry> current = chain.getHead();
            while (current != null) {
                keyList.add(current.getData().key);
                current = current.getNext();
            }
        }
        return keyList;
    }

    /**
     * Returns all values in the hash table
     *
     * @return DynamicArray of all values
     */
    public DynamicArray<V> values() {
        DynamicArray<V> valueList = new DynamicArray<>();
        for (CustomLinkedList<Entry> chain : table) {
            Node<Entry> current = chain.getHead();
            while (current != null) {
                valueList.add(current.getData().value);
                current = current.getNext();
            }
        }
        return valueList;
    }

    /**
     * Display hash table structure (for debugging)
     * Shows which slots have entries and chain lengths
     */
    public void displayStructure() {
        System.out.println("\n=== Hash Table Structure ===");
        System.out.println("Capacity: " + capacity);
        System.out.println("Size: " + size);
        System.out.println("Load Factor: " + String.format("%.2f", getLoadFactor()));
        System.out.println("\nChain Lengths:");

        int emptySlots = 0;
        int maxChainLength = 0;

        for (int i = 0; i < capacity; i++) {
            int chainLength = table[i].size();
            if (chainLength == 0) {
                emptySlots++;
            } else {
                maxChainLength = Math.max(maxChainLength, chainLength);
                System.out.println("Slot " + i + ": " + chainLength + " entries");
            }
        }

        System.out.println("\nEmpty slots: " + emptySlots);
        System.out.println("Max chain length: " + maxChainLength);
        System.out.println("===========================\n");
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";

        StringBuilder sb = new StringBuilder("{\n");
        for (CustomLinkedList<Entry> chain : table) {
            Node<Entry> current = chain.getHead();
            while (current != null) {
                sb.append("  ").append(current.getData()).append("\n");
                current = current.getNext();
            }
        }
        sb.append("}");
        return sb.toString();
    }
}