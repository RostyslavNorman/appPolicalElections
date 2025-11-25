package algorithms;

import datastructures.DynamicArray;
import java.util.Comparator;
public class QuickSort {

    //Sorts DynamicArray using QuickSort with provided Comparator
    public static <T> void sort(DynamicArray<T> array, Comparator<T> comparator) {
        if (array == null || array.size() <= 1) {
            return;
        }

        //Convert to array for efficient sorting
        T[] arr = array.toArray();
        quickSort(arr, 0, arr.length - 1, comparator);

        //Update DynamicArray with sorted elements
        array.fromArray(arr);
    }
    //Sorts a regular array using quick sort
    public static <T> void sort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1) {
            return;
        }
        quickSort(array, 0, array.length - 1, comparator);
    }


    //recursive quick sort implementation
    private static <T> void quickSort(T[] array,int lowerIndex, int higherIndex, Comparator<T> comparator) {
        int leftIndex = lowerIndex;
        int rightIndex = higherIndex;

        // Choose pivot as middle element (better than first/last for sorted data)
        T pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];

        //partition phase
        while (leftIndex <= rightIndex) {
            while (comparator.compare(array[leftIndex], pivot) < 0) {
                leftIndex++;
            }

            //find element on right which should be left
            while (comparator.compare(array[rightIndex], pivot) > 0) {
                rightIndex--;
            }

            //swap elements and move indices
            if (leftIndex <= rightIndex) {
                swap(array, leftIndex, rightIndex);
                leftIndex++;
                rightIndex--;
            }
        }
        //recursive calls for sub arrays
        if (lowerIndex < rightIndex) {
            quickSort(array, lowerIndex, rightIndex, comparator);
        }
        if (leftIndex < higherIndex) {
            quickSort(array, leftIndex, higherIndex, comparator);
        }

    }

    public static <T> void swap(T[] array, int lowerIndex, int higherIndex) {
        T temp = array[lowerIndex];
        array[lowerIndex] = array[higherIndex];
        array[higherIndex] = temp;
    }

    //Sorts array in natural order (for Comparable types)
    public static <T extends Comparable<T>> void sortNatural(DynamicArray<T> array) {
        sort(array, (a,b) -> a.compareTo(b));
    }

    //Sorts in a reverse natural order
    public static <T extends Comparable<T>> void sort(DynamicArray<T> array) {
        sort(array, (a,b) -> b.compareTo(a));
    }


}
