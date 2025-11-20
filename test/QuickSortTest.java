import algorithms.QuickSort;
import datastructures.DynamicArray;
import models.*;
import java.util.Comparator;
import java.util.Comparator.*;
import org.junit.jupiter.api.Test;
import utils.Comparators;
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

        assertEquals("Alice Johnson", array.get(0).getName());
        assertEquals("Bob Smith", array.get(1).getName());
        assertEquals("Charlie Brown", array.get(2).getName());
        assertEquals("David Murphy", array.get(3).getName());
    }

    @Test
    public void testSortPoliticiansByParty(){
        DynamicArray<Politician> array = createSamplePoliticians();
        QuickSort.sort(array, Comparators.BY_PARTY);

        assertEquals("Fianna Fail", array.get(0).getPoliticalParty());
        assertEquals("Fine Gael", array.get(1).getPoliticalParty());
        assertEquals("Sinn Fein", array.get(2).getPoliticalParty());
        assertEquals("Independent", array.get(3).getPoliticalParty());

    }

    @Test
    public void testSortElectionsByYear(){
        DynamicArray<Election> array = createSampleElections();
        QuickSort.sort(array, Comparators.BY_YEAR_DESC);

        assertEquals("2024", array.get(0).getYear());
        assertEquals("2020", array.get(1).getYear());
        assertEquals("2016", array.get(2).getYear());
        assertEquals("2011", array.get(3).getYear());
    }

    @Test
    public void testSortCandidatesByVotes() {
        // Create election and candidates
        Election election = new Election(ElectionType.GENERAL, "Dublin", "2024-01-01", 3);

        Politician p1 = new Politician("John", "1970-01-01", "Fine Gael", "Dublin", "");
        Politician p2 = new Politician("Mary", "1975-01-01", "Fianna Fail", "Dublin", "");
        Politician p3 = new Politician("Pat", "1980-01-01", "Sinn Fein", "Dublin", "");
        Politician p4 = new Politician("Ann", "1985-01-01", "Green Party", "Dublin", "");

        Candidate c1 = new Candidate(p1, election, 5000, "Fine Gael");
        Candidate c2 = new Candidate(p2, election, 8000, "Fianna Fail");
        Candidate c3 = new Candidate(p3, election, 6500, "Sinn Fein");
        Candidate c4 = new Candidate(p4, election, 3000, "Green Party");

        DynamicArray<Candidate> candidates = new DynamicArray<>();
        candidates.add(c1);
        candidates.add(c2);
        candidates.add(c3);
        candidates.add(c4);

        // Sort by votes (descending)
        QuickSort.sort(candidates, Comparators.BY_VOTES);

        assertEquals(8000, candidates.get(0).getVotes());  // Mary
        assertEquals(6500, candidates.get(1).getVotes());  // Pat
        assertEquals(5000, candidates.get(2).getVotes());  // John
        assertEquals(3000, candidates.get(3).getVotes());  // Ann

        assertEquals("Mary", candidates.get(0).getPoliticianName());
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

        elections.add(new Election(ElectionType.GENERAL, "Dublin", "2011-01-01", 3));
        elections.add(new Election(ElectionType.LOCAL, "Cork", "2024-01-01", 5));
        elections.add(new Election(ElectionType.EUROPEAN, "Ireland South", "2016-01-01", 4));
        elections.add(new Election(ElectionType.PRESIDENTIAL, "Ireland", "2020-01-01", 1));

        return elections;
    }

}
