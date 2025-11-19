import algorithms.QuickSort;
import datastructures.DynamicArray;
import models.*;
//import utils.Comparators;
import org.junit.jupiter.api.Test;
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

        QuickSort.sort(array, (a, b) -> a - b);  // Ascending //Ascending

        assertEquals(1, array.get(0));
        assertEquals(2, array.get(1));
        assertEquals(3, array.get(2));
        assertEquals(5, array.get(3));
        assertEquals(8, array.get(4));
        assertEquals(9, array.get(5));
    }

}
