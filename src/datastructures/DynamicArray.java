package datastructures;
/**
 * Generic Dynamic Array Implementation
 * Automatically resizes when capacity is reached
 * Used for storing variable-size collections (candidates, search results, etc.)
 * E -> type of elements stored in the array
 */
public class DynamicArray<E> {
    private E[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    private static final double GROWTH_FACTOR = 1.5;

    public DynamicArray() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }
    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        data = (E[]) new Object[initialCapacity];
        size = 0;
    }
    public void add(E element) {
        if (size == data.length){
            resize();
        }
        data[size++] = element;
    }
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of range");
        }
        if (size == data.length) {
            resize();
        }
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = element;
        size++;
    }
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of range");
        }
        return data[index];
    }
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of range");
        }
        E old = data[index];
        data[index] = element;
        return old;
    }
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of range");
        }
        E result = data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null;
        return result;
    }
    //first occurrence of the specific element
    public boolean remove(E element) {
        for (int i = 0; i < size; i++) {
            if (data[i] == null ? element == null : data[i].equals(element)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(E element) {
        return indexOf(element) >=0;
    }

    public int indexOf(E element) {
        for (int i = 0; i < size; i++) {
            if (data[i]==null ? element==null : data[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    public void clear(){
        data = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Resizes the internal array when capacity is reached
     * Creates new array with increased capacity and copies elements
     */
    public void resize(){
        int newCapacity = (int) (data.length * GROWTH_FACTOR);
        E[] newData = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    public E[] toArray() {
        E[] array = (E[]) new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = data[i];
        }
        return array;
    }

    //crates array from sorted array
    public void fromArray(E[] sortedArray) {
        // Simply copy sorted elements back
        for (int i = 0; i < sortedArray.length && i < size; i++) {
            data[i] = sortedArray[i];
        }
    }


}
