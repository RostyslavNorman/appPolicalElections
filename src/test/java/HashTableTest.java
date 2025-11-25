

import datastructures.HashTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * JUnit Tests for HashTable Implementation
 * Tests hashing functionality, collision resolution, and basic operations
 */
public class HashTableTest {

    private HashTable<String, String> table;
    @BeforeEach
    public void setUp() {
        //create small table to force collisions for testing
        table = new HashTable<>(10);
    }

    @Test
    public void testBasicPutAndGet() {
        table.put("John", "Politician1");
        table.put("Mary", "Politician2");
        table.put("James", "Politician3");

        assertEquals("Politician1", table.get("John") );
        assertEquals("Politician2", table.get("Mary") );
        assertEquals("Politician3", table.get("James") );

        assertNull(table.get("Non"));
    }

    @Test
    public void testCollisionHandling() {

        table.put("CAT", "Animal1");
        table.put("ACT", "Animal2");//same character sum as CAT
        table.put("TAC", "Animal3");//same character sum as CAT

        //All should be retrievable despite collisions
        assertEquals("Animal1", table.get("CAT") );
        assertEquals("Animal2", table.get("ACT") );
        assertEquals("Animal3", table.get("TAC") );

        assertEquals(3, table.size());

    }

    @Test
    public void testUpdateExistingKey(){
        table.put("John", "OldValue");
        assertEquals("OldValue", table.get("John") );
        assertEquals(1, table.size());

        //update same key
        table.put("John", "NewValue");
        assertEquals("NewValue", table.get("John") );
        assertEquals(1, table.size());
    }

    @Test
    public void testRemove(){
        table.put("John", "Value1");
        table.put("Mary", "Value2");
        table.put("James", "Value3");

        assertEquals(3, table.size());

        //remove middle entry
        String removed = table.remove("Mary");
        assertEquals("Value2", removed);
        assertEquals(2, table.size());

        //varify Mary is gone but others remain
        assertNull(table.get("Mary"));
        assertEquals("Value3", table.get("James"));
        assertEquals("Value1", table.get("John"));

        assertNull(table.remove("NOn"));
        assertEquals(2, table.size());
    }

    @Test
    public void testContainsKey() {
        table.put("John", "Value1");
        table.put("Mary", "Value2");

        assertTrue(table.containsKey("John"));
        assertTrue(table.containsKey("Mary"));
        assertFalse(table.containsKey("NonExistent"));

        table.remove("John");
        assertFalse(table.containsKey("John"));
    }

    //test that the table grows when load factor exceeds threshold
    @Test
    public void testLoadFactorAndRehashing() {
        HashTable<String, Integer> smallTable = new HashTable<>(5);

        double initialLoadFactor = smallTable.getLoadFactor();
        assertEquals(0.0, initialLoadFactor, 0.001);

        // Add elements to increase load factor
        for (int i = 0; i < 10; i++) {
            smallTable.put("Key" + i, i);
        }

        // All elements should still be retrievable after potential rehashing
        for (int i = 0; i < 10; i++) {
            assertEquals(i, smallTable.get("Key" + i));
        }

        assertEquals(10, smallTable.size());
    }

    @Test
    public void testKeysAndValues() {
        table.put("John", "Value1");
        table.put("Mary", "Value2");
        table.put("James", "Value3");

        var keys = table.keys();
        var values = table.values();

        assertEquals(3, keys.size());
        assertEquals(3, values.size());

        // Check all keys are present
        assertTrue(containsInArray(keys, "John"));
        assertTrue(containsInArray(keys, "Mary"));
        assertTrue(containsInArray(keys, "James"));

        // Check all values are present
        assertTrue(containsInArray(values, "Value1"));
        assertTrue(containsInArray(values, "Value2"));
        assertTrue(containsInArray(values, "Value3"));
    }
    @Test
    public void testClear() {
        table.put("John", "Value1");
        table.put("Mary", "Value2");
        table.put("James", "Value3");

        assertEquals(3, table.size());
        assertFalse(table.isEmpty());

        table.clear();

        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
        assertNull(table.get("John"));
    }

    // Helper method
    private <T> boolean containsInArray(datastructures.DynamicArray<T> array, T item) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).equals(item)) {
                return true;
            }
        }
        return false;
    }
}
