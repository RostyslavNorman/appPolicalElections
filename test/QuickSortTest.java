import algorithms.QuickSort;
import datastructures.DynamicArray;
import models.*;
import java.util.Comparator;
import java.util.Comparator.*;
import org.junit.jupiter.api.Test;
import utils.Comparators;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

//JUnit tests for QuickSort

public class QuickSortTest {

    //Test 1. QuickSort with integers
    @Test
    public void testQuickSortIntegers(){
        DynamicArray<Integer> array = new DynamicArray<>();
        array.add(5);
        array.add(2);
        array.add(8);
        array.add(1);
        array.add(9);
        array.add(3);

        QuickSort.sort(array, (a, b) -> a - b);  // Ascending

        assertEquals(1, array.get(0));
        assertEquals(2, array.get(1));
        assertEquals(3, array.get(2));
        assertEquals(5, array.get(3));
        assertEquals(8, array.get(4));
        assertEquals(9, array.get(5));

        QuickSort.sort(array, Comparator.reverseOrder()); //Descending

        assertEquals(9, array.get(0));
        assertEquals(8, array.get(1));
        assertEquals(5, array.get(2));
        assertEquals(3, array.get(3));
        assertEquals(2, array.get(4));
        assertEquals(1, array.get(5));

    }

    @Test
    public void testQuickSortStrings(){
        DynamicArray<String> array = new DynamicArray<>();
        array.add("Charlie");
        array.add("Alice");
        array.add("Bob");
        array.add("David");

        QuickSort.sort(array, (a, b) -> a.compareToIgnoreCase(b));
        assertEquals("Alice", array.get(0));
        assertEquals("Bob", array.get(1));
        assertEquals("Charlie", array.get(2));
        assertEquals("David", array.get(3));
    }
    @Test
    public void testSortPoliticiansByName(){
        DynamicArray<Politician> array = createSamplePoliticians();

        QuickSort.sort(array, Comparators.BY_NAME);

        // Fianna Fail, Fine Gael, Sinn Fein, then Independent (alphabetical, Ind last)
        assertEquals("Fianna Fail", array.get(0).getPoliticalParty());
        assertEquals("Fine Gael", array.get(1).getPoliticalParty());
        assertEquals("Sinn Fein", array.get(2).getPoliticalParty());
        assertEquals("Independent", array.get(3).getPoliticalParty());
    }




    // helper methods
    private DynamicArray<Politician> createSamplePoliticians(){
        DynamicArray<Politician> politicians = new DynamicArray<>();

        politicians.add(new Politician("Charlie Brown", "1970-01-01", "Sinn Fein", "Cork", ""));
        politicians.add(new Politician("Alice Johnson", "1975-01-01", "Fianna Fail", "Dublin", ""));
        politicians.add(new Politician("Bob Smith", "1980-01-01", "Fine Gael", "Galway", ""));
        politicians.add(new Politician("David Murphy", "1985-01-01", "Independent", "Kerry", ""));

        return politicians;
    }

    private DynamicArray<Election> createSampleElections() {
        DynamicArray<Election> elections = new DynamicArray<>();

        elections.add(new Election(ElectionType.GENERAL, "Dublin", "01/01/2011", 3));
        elections.add(new Election(ElectionType.LOCAL, "Cork", "01/01/2024", 5));
        elections.add(new Election(ElectionType.EUROPEAN, "Ireland South", "01/01/2016", 4));
        elections.add(new Election(ElectionType.PRESIDENTIAL, "Ireland", "01/01/2020", 1));

        return elections;
    }

}
