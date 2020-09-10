import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//-------------------------------------------------------------------------
/**
 *  Test class for Doubly Linked List
 *
 *  @version 3.1 09/11/15 11:32:15
 *
 *  @author  Conor Loftus 
 */

@RunWith(JUnit4.class)
public class BSTTest
{
  
  @Test 
  public void testHeight(){
	  BST<Integer, Integer> bst = new BST<Integer, Integer>();
	  assertEquals("Checking height of empty tree",-1, bst.height());
	  bst.put(7, 7);
	  assertEquals("Checking height of tree with one element",0,bst.height());
	  bst.put(8, 8);
	  assertEquals("Checking height of tree with two elements",1,bst.height());
	  bst.put(3, 3);
	  bst.put(1, 1);
	  assertEquals("Checking height of tree with four elements, three on left side",2,bst.height());
	  bst.put(2, 2);
	  assertEquals("Checking height of tree with five elements, four on left side",3,bst.height());
	  bst.put(6,6);
	  bst.put(4,4);
	  bst.put(5,5);
	  assertEquals("Checking height of tree with eight elements",4,bst.height());
  }
  
  
  @Test
  public void testMedian() {
	  BST<Integer, Integer> bst = new BST<Integer, Integer>();
	  assertEquals("Checking median of empty tree",null,bst.median());
	  bst.put(7,7);
	  assertEquals("Checking median on tree with one element","7",bst.median().toString());
	  bst.put(8,8);
	  assertEquals("Checking median on tree with two elements","7",bst.median().toString());
	  bst.put(3,3);
	  bst.put(1,1);
	  assertEquals("Checking median on tree with four elements","3",bst.median().toString());
	  bst.put(2,2);
	  assertEquals("Checking median on tree with five elements","3",bst.median().toString());
	  bst.put(6,6);
	  bst.put(4,4);
	  bst.put(5,5);
	  assertEquals("Checking median on tree with eight elements","4",bst.median().toString());
	  bst = new BST<Integer, Integer>();
	  bst.put(7,7);
	  bst.put(8,8);
	  bst.put(9,9);
	  bst.put(12,12);
	  bst.put(13,13);
	  assertEquals("Checking median on tree with elements all on right side","9",bst.median().toString());
	  bst.put(10,10);
	  assertEquals("Checking median on tree with elements all on right side with one left","9",bst.median().toString());
  }
  
  @Test 
  public void testPrintKeysInOrder() {
	  BST<Integer, Integer> bst = new BST<Integer, Integer>();
	  assertEquals("Checking printKeysInOrder on empty BST","()",bst.printKeysInOrder());
	  bst.put(7,7);
	  assertEquals("Checking printKeysInOrder on tree with one element","(()7())",bst.printKeysInOrder());
	  bst.put(8,8);
	  assertEquals("Checking printKeysInOrder on tree with two elements","(()7(()8()))",bst.printKeysInOrder());
	  bst.put(3,3);
	  assertEquals("Checking printKeysInOrder on tree with three elements","((()3())7(()8()))",bst.printKeysInOrder());
	  bst.put(1,1);
	  bst.put(2,2);
	  bst.put(6,6);
	  assertEquals("Checking printKeysInOrder on tree with three elements","(((()1(()2()))3(()6()))7(()8()))",bst.printKeysInOrder());
	  bst.put(10,10);
	  bst.put(9,9);
	  assertEquals("Checking printKeysInOrder on tree with three elements","(((()1(()2()))3(()6()))7(()8((()9())10())))",bst.printKeysInOrder());
	  
  }
  
  /** <p>Test {@link BST#prettyPrintKeys()}.</p> */
  @Test
  public void testPrettyPrint() {
     BST<Integer, Integer> bst = new BST<Integer, Integer>();
     assertEquals("Checking pretty printing of empty tree",
             "-null\n", bst.prettyPrintKeys());
      
                          //  -7
                          //   |-3
                          //   | |-1
                          //   | | |-null
     bst.put(7, 7);       //   | |  -2
     bst.put(8, 8);       //   | |   |-null
     bst.put(3, 3);       //   | |    -null
     bst.put(1, 1);       //   |  -6
     bst.put(2, 2);       //   |   |-4
     bst.put(6, 6);       //   |   | |-null
     bst.put(4, 4);       //   |   |  -5
     bst.put(5, 5);       //   |   |   |-null
                          //   |   |    -null
                          //   |    -null
                          //    -8
                          //     |-null
                          //      -null
     
     String result = 
      "-7\n" +
      " |-3\n" + 
      " | |-1\n" +
      " | | |-null\n" + 
      " | |  -2\n" +
      " | |   |-null\n" +
      " | |    -null\n" +
      " |  -6\n" +
      " |   |-4\n" +
      " |   | |-null\n" +
      " |   |  -5\n" +
      " |   |   |-null\n" +
      " |   |    -null\n" +
      " |    -null\n" +
      "  -8\n" +
      "   |-null\n" +
      "    -null\n";
     assertEquals("Checking pretty printing of non-empty tree", result, bst.prettyPrintKeys());
     }

  
     /** <p>Test {@link BST#delete(Comparable)}.</p> */
     @Test
     public void testDelete() {
         BST<Integer, Integer> bst = new BST<Integer, Integer>();
         bst.delete(1);
         assertEquals("Deleting from empty tree", "()", bst.printKeysInOrder());
         
         bst.put(7, 7);   //        _7_
         bst.put(8, 8);   //      /     \
         bst.put(3, 3);   //    _3_      8
         bst.put(1, 1);   //  /     \
         bst.put(2, 2);   // 1       6
         bst.put(6, 6);   //  \     /
         bst.put(4, 4);   //   2   4
         bst.put(5, 5);   //        \
                          //         5
         
         assertEquals("Checking order of constructed tree","(((()1(()2()))3((()4(()5()))6()))7(()8()))", bst.printKeysInOrder());
         
         bst.delete(9);
         assertEquals("Deleting non-existent key","(((()1(()2()))3((()4(()5()))6()))7(()8()))", bst.printKeysInOrder());
         
         bst.delete(8);
         assertEquals("Deleting leaf", "(((()1(()2()))3((()4(()5()))6()))7())", bst.printKeysInOrder());
 
         bst.delete(6);
         assertEquals("Deleting node with single child","(((()1(()2()))3(()4(()5())))7())", bst.printKeysInOrder());
 
         System.out.println(bst.prettyPrintKeys());
         bst.delete(3);
         System.out.println(bst.prettyPrintKeys());
         assertEquals("Deleting node with two children","(((()1())2(()4(()5())))7())", bst.printKeysInOrder());
     }
     
}