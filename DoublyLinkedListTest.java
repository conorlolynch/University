import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//-------------------------------------------------------------------------
/**
 *  Test class for Doubly Linked List
 *
 *  @author Conor Loftus 
 *  @version 13/10/16 18:15
 */
@RunWith(JUnit4.class)
public class DoublyLinkedListTest
{
    //~ Constructor ........................................................
    @Test
    public void testConstructor()
    {
      new DoublyLinkedList<Integer>();
    }

    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Check if the insertBefore works
     */
    @Test
    public void testInsertBefore()
    {
        // test non-empty list
        DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
        testDLL.insertBefore(0,1);
        testDLL.insertBefore(1,2);
        testDLL.insertBefore(2,3);

        testDLL.insertBefore(0,4);
        assertEquals( "Checking insertBefore to a list containing 3 elements at position 0", "4,1,2,3", testDLL.toString() );
        testDLL.insertBefore(1,5);
        assertEquals( "Checking insertBefore to a list containing 4 elements at position 1", "4,5,1,2,3", testDLL.toString() );
        testDLL.insertBefore(2,6);       
        assertEquals( "Checking insertBefore to a list containing 5 elements at position 2", "4,5,6,1,2,3", testDLL.toString() );
        testDLL.insertBefore(-1,7);        
        assertEquals( "Checking insertBefore to a list containing 6 elements at position -1 - expected the element at the head of the list", "7,4,5,6,1,2,3", testDLL.toString() );
        testDLL.insertBefore(7,8);     
        assertEquals( "Checking insertBefore to a list containing 7 elemenets at position 8 - expected the element at the tail of the list", "7,4,5,6,1,2,3,8", testDLL.toString() );
        testDLL.insertBefore(700,9);        
        assertEquals( "Checking insertBefore to a list containing 8 elements at position 700 - expected the element at the tail of the list", "7,4,5,6,1,2,3,8,9", testDLL.toString() );

        // test empty list
        testDLL = new DoublyLinkedList<Integer>();
        testDLL.insertBefore(0,1);        
        assertEquals( "Checking insertBefore to an empty list at position 0 - expected the element at the head of the list", "1", testDLL.toString() );
        testDLL = new DoublyLinkedList<Integer>();
        testDLL.insertBefore(10,1);        
        assertEquals( "Checking insertBefore to an empty list at position 10 - expected the element at the head of the list", "1", testDLL.toString() );
        testDLL = new DoublyLinkedList<Integer>();
        testDLL.insertBefore(-10,1);        
        assertEquals( "Checking insertBefore to an empty list at position -10 - expected the element at the head of the list", "1", testDLL.toString() );
     }

    @Test
    public void testIsEmpty() {
    	DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
    	assertEquals(true, testDLL.isEmpty());
    	testDLL.insertBefore(0,1);
    	assertEquals(false, testDLL.isEmpty());
    }
    
    @Test
    public void testDeleteAt() {
    	DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
    	assertEquals("Checking deleteAt on an empty list", false, testDLL.deleteAt(0));
    	
    	testDLL.insertBefore(0,1);
    	testDLL.deleteAt(0);
    	assertEquals("Checking deleteAt on an list with one element", "", testDLL.toString());
    	testDLL.insertBefore(0,2);
    	testDLL.insertBefore(1,3);
    	testDLL.insertBefore(2,4);
    	testDLL.deleteAt(0);
    	assertEquals("Checking deleteAt on an list with three elements deleting first element", "3,4", testDLL.toString());
    	testDLL.deleteAt(1);
    	assertEquals("Checking deleteAt on an list with two elements deleting last element", "3", testDLL.toString());
    	testDLL.insertBefore(1,4);
    	testDLL.insertBefore(2,5);
    	testDLL.insertBefore(3,6);
    	testDLL.deleteAt(2);
    	assertEquals("Checking deleteAt on an list with four elements deleting middle element", "3,4,6", testDLL.toString());
    	testDLL.deleteAt(20);
    	assertEquals("Checking deleteAt at an index outside of range ","3,4,6",testDLL.toString());
    }
    
    @Test
    public void testReverse() {
    	DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
    	testDLL.reverse();
    	assertEquals("Checking Reverse on empty list: ","",testDLL.toString());
    	testDLL.insertBefore(0,1);
    	testDLL.reverse();
    	assertEquals("Checking Reverse on list with one element: ","1",testDLL.toString());
    	testDLL.insertBefore(1,2);
    	testDLL.reverse();
    	assertEquals("Checking Reverse on list with two elements: ","2,1",testDLL.toString());
    	testDLL.insertBefore(2,3);
    	testDLL.insertBefore(3,4);
    	testDLL.reverse();
    	assertEquals("Checking reverse on list with four elements: ","4,3,1,2",testDLL.toString());
    }
    
    @Test 
    public void testMakeDuplicate() {
    	DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
    	testDLL.makeUnique();
    	assertEquals("Checking makeDuplicate on empty list: ","",testDLL.toString());
    	testDLL.insertBefore(0,1);
    	assertEquals("Checking makeDuplicate on list with one element: ","1",testDLL.toString());
    	testDLL.insertBefore(1,1);
    	testDLL.makeUnique();
    	assertEquals("Checking makeDuplicate on list with two duplicate element: ","1",testDLL.toString());
    	testDLL.insertBefore(1,2);
    	testDLL.insertBefore(2,2);
    	testDLL.makeUnique();
    	assertEquals("Checking makeDuplicate on list with three elements, two duplicate element: ","1,2",testDLL.toString());
    	testDLL.insertBefore(2,3);
    	testDLL.insertBefore(3,3);
    	testDLL.insertBefore(4,4);
    	testDLL.makeUnique();
    	assertEquals("Checking makeDuplicate on list with five elements, two duplicates: ","1,2,3,4",testDLL.toString());
    }
    
    @Test 
    public void testPush() {
    	DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
    	testDLL.push(1);
    	assertEquals("Checking push on empty stack: ","1",testDLL.toString());
    	testDLL.push(2);
    	assertEquals("Checking push on stack with elements: ","2,1",testDLL.toString());
    	
    }
    
    @Test
    public void testPop() {
    	DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
    	testDLL.pop();
    	assertEquals("Checking pop on empty stack: ",null,testDLL.pop());
    	testDLL.push(1);
    	Integer intExpected = 1;
    	assertEquals("Checking pop on stack with one element: ",intExpected,testDLL.pop());
    	testDLL.push(1);
    	testDLL.push(2);
    	intExpected = 2;
    	assertEquals("Checking pop on stack with multiple elements: ",intExpected,testDLL.pop());

    }
    
    @Test 
    public void testEnqueue() {
    	DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
    	testDLL.enqueue(1);
    	assertEquals("Checking enqueue on empty queue: ","1",testDLL.toString());
    	testDLL.enqueue(2);
    	assertEquals("Checking enqueue on queue with many elements: ","2,1",testDLL.toString());

    }
    
    @Test
    public void testDequeue() {
    	DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
    	Integer intExpected = null;
    	assertEquals("Checking dequeue on empty queue: ",null,testDLL.dequeue());
    	testDLL.enqueue(1);
    	intExpected = 1;
    	assertEquals("Checking enqueue on queue with one element: ",intExpected,testDLL.dequeue());
    	testDLL.enqueue(1);
    	testDLL.enqueue(2);
    	intExpected = 1;
    	assertEquals("Checking enqueue on queue with many elements: ",intExpected,testDLL.dequeue());
    	intExpected = 2;
    	assertEquals("Checking enqueue on queue one element again: ",intExpected,testDLL.dequeue());

    }

}



