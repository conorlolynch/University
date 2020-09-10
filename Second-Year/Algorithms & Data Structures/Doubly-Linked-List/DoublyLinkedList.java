import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

// -------------------------------------------------------------------------
/**
 *  This class contains the methods of Doubly Linked List.
 *
 *  @author  Conor Loftus
 *  @version 09/10/18 11:13:22
 *  
 *  Time to complete: 6 Hours
 */


/**
 * Class DoublyLinkedList: implements a *generic* Doubly Linked List.
 * @param <T> This is a type parameter. T is used as a class name in the
 * definition of this class.
 *
 * When creating a new DoublyLinkedList, T should be instantiated with an
 * actual class name that extends the class Comparable.
 * Such classes include String and Integer.
 *
 * For example to create a new DoublyLinkedList class containing String data: 
 *    DoublyLinkedList<String> myStringList = new DoublyLinkedList<String>();
 *
 * The class offers a toString() method which returns a comma-separated sting of
 * all elements in the data structure.
 * 
 * This is a bare minimum class you would need to completely implement.
 * You can add additional methods to support your code. Each method will need
 * to be tested by your jUnit tests -- for simplicity in jUnit testing
 * introduce only public methods.
 */
class DoublyLinkedList<T extends Comparable<T>>
{

    /**
     * private class DLLNode: implements a *generic* Doubly Linked List node.
     */
    private class DLLNode
    {
        public final T data; // this field should never be updated. It gets its
                             // value once from the constructor DLLNode.
        public DLLNode next;
        public DLLNode prev;
    
        /**
         * Constructor
         * @param theData : data of type T, to be stored in the node
         * @param prevNode : the previous Node in the Doubly Linked List
         * @param nextNode : the next Node in the Doubly Linked List
         * @return DLLNode
         */
        public DLLNode(T theData, DLLNode prevNode, DLLNode nextNode) 
        {
          data = theData;
          prev = prevNode;
          next = nextNode;
        }
    }

    // Fields head and tail point to the first and last nodes of the list.
    private DLLNode head, tail;

    /**
     * Constructor of an empty DLL
     * @return DoublyLinkedList
     */
    public DoublyLinkedList() 
    {
      head = null;
      tail = null;
    }

    /**
     * Tests if the doubly linked list is empty
     * @return true if list is empty, and false otherwise
     *
     * Worst-case asymptotic running time cost: Theta(1)
     *
     * Justification:
     *  There are two comparisons made in this method with no loops. Comparisons run in constant time theta(1)
     */
    public boolean isEmpty()
    {
      return (head==null && tail==null);
    }

    /**
     * Inserts an element in the doubly linked list
     * @param pos : The integer location at which the new data should be
     *      inserted in the list. We assume that the first position in the list
     *      is 0 (zero). If pos is less than 0 then add to the head of the list.
     *      If pos is greater or equal to the size of the list then add the
     *      element at the end of the list.
     * @param data : The new data of class T that needs to be added to the list
     * @return none
     *
     * Worst-case asymptotic running time cost: Theta(N)
     *
     * Justification:
     *  In one of the conditions below there is a chance that the program could branch into a while loop which could iterate over the 
     *  DLLNode N times before completion. This gives a worse-case running time of theta(N)
     */
    public void insertBefore( int pos, T data ) 
    {
    	if(isEmpty()) //empty
		{
			head = new DLLNode(data, null, null);
			tail = head;
		}
		else if (head == tail && pos <= 0) //insert at start with one element
		{
			head = new DLLNode(data, null, tail);
			tail.prev = head;
		}
		else if (head == tail && pos > 0) //insert at end with one element
		{
			tail = new DLLNode(data, head, null);
			head.next = tail;
		}
		else if(pos <= 0) //insert at start otherwise
		{
			DLLNode oldHead = head;
			head = new DLLNode(data, null, oldHead);
			oldHead.prev = head;
		}
		else if(get(pos) == null) //insert at end otherwise
		{
			DLLNode oldTail = tail;
			tail = new DLLNode(data, oldTail, null);
			oldTail.next = tail;
		}
		else //insert in middle
		{
			int count = 0;
			DLLNode current = head;
			while(count <= pos)
			{
				if(count == pos-1) {
					DLLNode nextNode = current.next;
					DLLNode newNode = new DLLNode(data,current,current.next);
					current.next = newNode;
					nextNode.prev = newNode;
					break;
				}
				current = current.next;
				count++;
			}
		}
    }

    /**
     * Returns the data stored at a particular position
     * @param pos : the position
     * @return the data at pos, if pos is within the bounds of the list, and null otherwise.
     *
     * Worst-case asymptotic running time cost: Theta(N)
     *
     * Justification:
     *  Worst case run time is the while loop iterating over the whole linked list which is N elements
     *
     */
    public T get(int pos) 
    {
      DLLNode currentNode = head;
      int count = 0;
      
      while (currentNode != null) {
    	  if (count == pos) {
    		  return currentNode.data;
    	  }
    	  currentNode = currentNode.next;
    	  count++;
      }
      return null;
    }

    /**
     * Deletes the element of the list at position pos.
     * First element in the list has position 0. If pos points outside the
     * elements of the list then no modification happens to the list.
     * @param pos : the position to delete in the list.
     * @return true : on successful deletion, false : list has not been modified. 
     *
     * Worst-case asymptotic running time cost: Theta(N)
     *
     * Justification:
     *  	In the worst case scenario we may have to iterate through N nodes to get to the desired one, or to reach the end of the DLL Node
     */
    public boolean deleteAt(int pos) 
    {
    	int position = 0;
    	DLLNode current = head;
    	
    	while (current != null) {
    		if (position == pos) {
    			if (position == 0 && current.next == null) {  // There's only one element in the list
    				head = null;
    				tail = null;
    			}
    			else if (position == 0) {  // Delete the first element in a list
    				head = current.next;
    				head.prev = null;
    			}
    			else if (current.next == null) {  // Deleting the last element element
    				tail = current.prev;
    				tail.next = null;
    			}
    			else { 	// Deleting a middle element
    				(current.next).prev = current.prev;
    				(current.prev).next = current.next;
    			}
    			return true;
    		}
    		current = current.next;
    		position++;
    	}
      return false;
    }

    /**
     * Reverses the list.
     * If the list contains "A", "B", "C", "D" before the method is called
     * Then it should contain "D", "C", "B", "A" after it returns.
     *
     * Worst-case asymptotic running time cost: Theta(N)
     *
     * Justification:
     *  Worse case scenario, the program will have to loop through all N elements in the DLL to reverse them 
     */
    public void reverse()
    {
      // To reverse all we have to do is switch the prev and next attributes of each node and reset head and tail
    	DLLNode current = head; 
    	DLLNode temp = null; 
		
		while (current != null) {
			temp = current.prev;
			current.prev = current.next;
			current.next = temp;
			if (temp == null) {
				tail = current;
			}
			current = current.prev;
		}
		
		if (temp != null) {
			head = temp.prev;
		}

    			
    	
    }

    /**
     * Removes all duplicate elements from the list.
     * The method should remove the _least_number_ of elements to make all elements uniqueue.
     * If the list contains "A", "B", "C", "B", "D", "A" before the method is called
     * Then it should contain "A", "B", "C", "D" after it returns.
     * The relative order of elements in the resulting list should be the same as the starting list.
     *
     * Worst-case asymptotic running time cost: Theta(N)
     *
     * Justification:
     *  After we initialize the HashSet which has a construction time complexity of Theta(N), the while loop will have to run through all N elements of the list which 
     *  has a worst case run time of Theta(N), the set.add() operation of a HashSet is Theta(1) and the set.contains() method also has a time run time cost of Theta(1) as it can access that specific item in the HashSet.
     *  So overall, the worst case run time is Theta(N).
     */
     public void makeUnique()
     {
    	 Set setA = new HashSet();		// We keep a counter for each unique node we have 
    	 DLLNode current = head;
    	 while (current != null) {
    		 if (setA.contains(current.data) ) {
    			 if (current.next == null) {		// If one of the duplicates is the last element
    				tail = current.prev;
    				tail.next = null;
    			 }
    			 else {								// If one of the duplicates is somewhere in the middle of the list
    				 (current.prev).next = current.next;
        			 (current.next).prev = current.prev; 
    			 }
    		 }
    		 else {
    			 setA.add(current.data);
    		 }
    		 
    		 current = current.next;
    	 }
    }


    /*----------------------- STACK API 
     * If only the push and pop methods are called the data structure should behave like a stack.
     */

    /**
     * This method adds an element  to the data structure.
     * How exactly this will be represented in the Doubly Linked List is up to the programmer.
     * @param item : the item to push on the stack
     *
     * Worst-case asymptotic running time cost: Theta(1)
     *
     * Justification:
     *  As there is no while or for loops, the run time is constant as the insertion requires basic operations and comparisons
     */
    public void push(T item) 
    {
      insertBefore(0, item);
    }

    /**
     * This method returns and removes the element that was most recently added by the push method.
     * @return the last item inserted with a push; or null when the list is empty.
     *
     * Worst-case asymptotic running time cost: Theta(1)
     *
     * Justification:
     *  The get() and deleteAt() methods terminate after a single iteration as we are only interested in the head of the list
     *  Therefore the get() and deleteAt() methods have a run time of Theta(1) due to a single iteration each. All the remaining
     *  operations in this method such as comparisons and assignments have constant run time.
     */
    public T pop() 
    {
    	if (isEmpty()) {
    		return null;
    	}
    	
    	T item = get(0);
    	deleteAt(0);
    	return item;
    		
    }

    /*----------------------- QUEUE API
     * If only the enqueue and dequeue methods are called the data structure should behave like a FIFO queue.
     */
 
    /**
     * This method adds an element to the data structure.
     * How exactly this will be represented in the Doubly Linked List is up to the programmer.
     * @param item : the item to be enqueued to the stack
     *
     * Worst-case asymptotic running time cost: Theta(1)
     *
     * Justification:
     *  We insert the item into the first node of the list, no loops occur and a minimum amount of comparisons and operations occur
     *  which have constant run times of Theta(1).
     */
    public void enqueue(T item) 
    {
      insertBefore(0, item);
    }

     /**
     * This method returns and removes the element that was least recently added by the enqueue method.
     * @return the earliest item inserted with an enqueue; or null when the list is empty.
     *
     * Worst-case asymptotic running time cost: Theta(1)
     *
     * Justification:
     *  There are no loops run in this method, only comparisons and assignments are used which both are constant run time of Theta(1)
     */
    public T dequeue() 
    {
      if (isEmpty()) {			// If queue is empty
    	  return null;
      }
      else if (head == tail) {	// If queue has one element
    	  DLLNode node = tail;
    	  head = null;
    	  tail = null;
    	  return node.data;
      }
      else {					// Remove the first element that was put in (the tail of the list)
    	  DLLNode oldTail = tail;
          tail = tail.prev;
          tail.next = null;
          return oldTail.data;  
      }
    }
 

    /**
     * @return a string with the elements of the list as a comma-separated
     * list, from beginning to end
     *
     * Worst-case asymptotic running time cost:   Theta(n)
     *
     * Justification:
     *  We know from the Java documentation that StringBuilder's append() method runs in Theta(1) asymptotic time.
     *  We assume all other method calls here (e.g., the iterator methods above, and the toString method) will execute in Theta(1) time.
     *  Thus, every one iteration of the for-loop will have cost Theta(1).
     *  Suppose the doubly-linked list has 'n' elements.
     *  The for-loop will always iterate over all n elements of the list, and therefore the total cost of this method will be n*Theta(1) = Theta(n).
     */
    public String toString() 
    {
      StringBuilder s = new StringBuilder();
      boolean isFirst = true; 

      // iterate over the list, starting from the head
      for (DLLNode iter = head; iter != null; iter = iter.next)
      {
        if (!isFirst)
        {
          s.append(",");
        } else {
          isFirst = false;
        }
        s.append(iter.data.toString());
      }

      return s.toString();
    }


}





