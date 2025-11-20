

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


}
