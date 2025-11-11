package datastructures;

/**
 * Implementation of linked list
 */
public class CustomLinkedList<T> {

    private Node<T> head;
    private int size;

    public CustomLinkedList() {
        head = null;
        size = 0;
    }
    public int size() {
        return size;
    }
    // adds an element to the end of the list
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        }
        else {
            //traverse to the last node
            Node<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            //link the last node to the new one
            current.setNext(newNode);
        }
        size++; // increment size
    }
    //add an element at a specific index in the list
    public void add(int index, T data) {
        // Validation: index must be 0 to size (inclusive)
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<T> newNode = new Node<>(data);

        // Insert at beginning
        if (index == 0) {
            newNode.setNext(head);  // New node points to old head
            head = newNode;          // New node becomes head
        }
        //Insert in middle or end
        else {
            // Traverse to the node BEFORE the insertion point
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            // Insert new node between current and current.next
            newNode.setNext(current.getNext());
            current.setNext(newNode);
        }

        size++;
    }
    // get an element at a specific index
    public T get(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    public T remove(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T removedData;
        if (index == 0) {
            removedData = head.getData();
            head = head.getNext(); //second node becomes new head
        }
        else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            //save the data to return
            removedData = current.getNext().getData();
            // skip over the node to remove
            current.setNext(current.getNext().getNext());

        }
        size--;
        return removedData;
    }
    //removes the first occurrence of the specific elements
    //return true if element was found and removed, false otherwise
    public boolean remove(T data) {

        if (head == null) {
            return false;
        }
        if (head.getData().equals(data)) {
            head = head.getNext();
            size--;
            return true;
        }
        Node<T> current = head;
        while (current.getNext() != null) {
            if (current.getNext().getData().equals(data)) {
                //found it and skip over it
                current.setNext(current.getNext().getNext());
                size--;
                return true;
            }
            current = current.getNext();
        }
        return false;
    }
    //============SEARCH METHODS================

    //checks if the list contains the specific element
    //@return true if found, false otherwise
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.getData().equals(data)) {
                return true;
            }
            current = current.getNext();
        }
        return false;

    }

    //helper methods
    //isEmpty method
    public boolean isEmpty() {
        return size == 0;
    }
    //removes all elements from the list
    public void clear() {
        head = null;
        size = 0;
    }
    //toString
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        String output = "[";
        Node<T> current = head;
        while (current != null) {
            output += current.getData();
            if (current.getNext() != null) {
                output += ", ";
            }
            current = current.getNext();
        }
        output += "]";
        return output;
    }
    //additional methods
    //returns first element of the list
    public T getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return head.getData();
    }
    //returns last element
    public T getLast() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        Node<T> current = head;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        return current.getData();
    }

}
