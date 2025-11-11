package datastructures;


/**
 * Node class - the building block of linked list
 */
public class Node<T> {
    private T data; //data stored in node
    private Node<T> next; // creates chain

    public Node(T data){
        this.data = data;
        this.next = null;
    }

    public T getData(){
        return this.data;
    }
    public Node<T> getNext(){
        return this.next;
    }

    public void setNext(Node<T> next){
        this.next = next;
    }
    public void setData(T data){
        this.data = data;
    }

    public boolean hasNext(){
        return this.next != null;
    }
    @Override
    public String toString(){
        return "Node {data= " + data + "}";
    }


}
