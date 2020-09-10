import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//-------------------------------------------------------------------------
/**
 *  Test class for SortComparison.java
 *
 *  @author
 *  @version HT 2020
 */
@RunWith(JUnit4.class)
public class SortComparisonTest
{
	
	/**
	 *  Run times (in milliseconds) for each algorithm
	 *
	|         	  			| Insert  | Selection | Merge Recursive| Merge Iterative |  Quick  |
	|-----------------------|---------|-----------|----------------|-----------------|---------|
	| 10 Random  			| 0.0053  |   0.0106  |    0.0177      |      0.0404     | 0.0110  |
	| 100 Random  			| 0.0755  |   0.0690  |    0.0735      | 	  0.0512     | 0.0289  | 
	| 1000 Random           | 5.2154  |   3.5024  |    0.9059      | 	  0.6801     | 0.4026  |
	| 1000 few unique       | 1.4070  |   1.9019  |    0.2761      | 	  0.2770     | 0.1348  |
	| 1000 nearly ordered   | 0.1200  |   1.2684  |    0.1465      | 	  0.1799     | 0.1302  |
	| 1000 reverse order    | 1.0421  |   0.8731  |    0.1203      | 	  0.1454     | 0.4293  |
	| 1000 sorted   		| 0.0090  |   0.3430  |    0.1304      |      0.1792     | 0.6108  |
	 
	Questions:
	
	a. Which of the sorting algorithms does the order of input have an impact on? Why?
	
			Quick sort seems to be affected the most by the order of input as it performs quite poorly when the input array is sorted
			or reverse sorted. This is because the algorithm is at risk of having a poor pivot chosen where one partition is a 
			single element and the other partition contains the remaining elements of that array making this algorithm become a n^2 one. 
			This occurs mostly when the input array is sorted in reverse or fully sorted and the pivot is set as the first or last index 
			of that sorted array. These issues can be avoided by making changes to the algorithm such picking a random element as the pivot 
			or getting the average of the first, middle and last elements of the input array and setting this as the pivot. 
	
	
	b. Which algorithm has the biggest difference between the best and worst performance, based
		on the type of input, for the input of size 1000? Why?
		
			Insertion Sort seems to have the biggest difference between best and worst case performance. When a large array is already sorted
			insertion sort becomes a O(n) sorting algorithm and can quickly iterate through all the elements to finish sorting.
			This is quite the opposite when a large array is unsorted, insertion sort becomes an O(n^2) sorting algorithm with many comparisons and 
			element switches. From my table insertion sort was roughly 578 times faster at sorting an already sorted array than the unsorted array 
			which both had 1000 elements. No other sorting algorithm has this large of a performance gap.
		
	c. Which algorithm has the best/worst scalability, i.e., the difference in performance time
		based on the input size? Please consider only input files with random order for this answer.
		
			Merge Sort Iterative has the best scalability from the table above. Sorting 100 elements only takes 1.3 times as long as sorting 10 elements
			and sorting 1000 random elements only takes 13 times as long as sorting 100 random elements. These are the best differences in 
			performance time based on input size of all the sorting algorithms tested but quick sort is a close second to this kind of scalabilty. 
			
			Insertion sort has the worst scalability. Sorting 100 elements takes 14 times longer than sorting 10 elements and sorting 1000 elements
			takes 70 times longer to sort than 100 elements. These are the worst performance differences based on input size for all the algorithms tested.
		
	d. Did you observe any difference between iterative and recursive implementations of mergesort?
			
			Iterative Mergesort seems to perform better on the 100 and 1000 random elements files as these require less stack function calls that 
			Recursive Mergesirt would be making. The recursive implementation of mergesort was a lot easier to implement and understand. 
			Iterative mergesort takes a bottom up approach to solving the problem while recursive mersort takes a top to bottom approach.
		
	e. Which algorithm is the fastest for each of the 7 input files?
		
			Fastest for 10 Random: 		 Insertion Sort
			Fastest for 100 Random: 	 Quick Sort
			Fastest for 1000 Random: 	 Quick Sort
			Fastest 1000 few unique: 	 Quick Sort
			Fastest 1000 nearly ordered: Insertion Sort
			Fastest 1000 reverse order:  Mergesort Recursive
			Fastest 1000 sortedL		 Instertion Sort
	*/
	
	
    //~ Constructor ........................................................
    @Test
    public void testConstructor()
    {
        new SortComparison();
    }

    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Check that the methods work for empty arrays
     */
    @Test
    public void testEmpty()
    {
    	double[] emptyArr = new double[0];
    	assertEquals("Checking insertion sort with empty array",SortComparison.insertionSort(emptyArr), emptyArr);
    	assertEquals("Checking selection sort with empty array",SortComparison.selectionSort(emptyArr), emptyArr);
    	assertEquals("Checking quick sort with empty array",SortComparison.quickSort(emptyArr), emptyArr);
    	assertEquals("Checking merge sort recursive with empty array",SortComparison.mergeSortRecursive(emptyArr), emptyArr);
    	assertEquals("Checking merge sort iterative with empty array",SortComparison.mergeSortIterative(emptyArr), emptyArr);
    }
    
    @Test
    public void testInsertionSort() {
    	double[] oneElementArray = new double[] {1};
    	double[] twoElementArray = new double[] {2,1};
    	double[] threeElementArray = new double[] {2,3,1};
    	assertEquals("Checking insertion sort with 1 element array",Arrays.toString(SortComparison.insertionSort(oneElementArray)), "[1.0]");
    	assertEquals("Checking insertion sort with 2 element array",Arrays.toString(SortComparison.insertionSort(twoElementArray)), "[1.0, 2.0]");
    	assertEquals("Checking insertion sort with 3 element array",Arrays.toString(SortComparison.insertionSort(threeElementArray)), "[1.0, 2.0, 3.0]");
    }
    
    @Test
    public void testSelectionSort() {
    	double[] oneElementArray = new double[] {1};
    	double[] twoElementArray = new double[] {2,1};
    	double[] threeElementArray = new double[] {2,3,1};
    	assertEquals("Checking selection sort with 1 element array",Arrays.toString(SortComparison.selectionSort(oneElementArray)), "[1.0]");
    	assertEquals("Checking selection sort with 2 element array",Arrays.toString(SortComparison.selectionSort(twoElementArray)), "[1.0, 2.0]");
    	assertEquals("Checking selection sort with 3 element array",Arrays.toString(SortComparison.selectionSort(threeElementArray)), "[1.0, 2.0, 3.0]");
    }
    
    @Test
    public void testQuickSort() {
    	double[] oneElementArray = new double[] {1};
    	double[] twoElementArray = new double[] {2,1};
    	double[] threeElementArray = new double[] {2,3,1};
    	assertEquals("Checking quick sort with 1 element array",Arrays.toString(SortComparison.quickSort(oneElementArray)), "[1.0]");
    	assertEquals("Checking quick sort with 2 element array",Arrays.toString(SortComparison.quickSort(twoElementArray)), "[1.0, 2.0]");
    	assertEquals("Checking quick sort with 3 element array",Arrays.toString(SortComparison.quickSort(threeElementArray)), "[1.0, 2.0, 3.0]");
    	
    }
    
    @Test
    public void testMergeSortRecursive() {
    	
    	double[] oneElementArray = new double[] {1};
    	double[] twoElementArray = new double[] {2,1};
    	double[] threeElementArray = new double[] {2,3,1};
    	assertEquals("Checking recursive merge sort with 1 element array",Arrays.toString(SortComparison.mergeSortRecursive(oneElementArray)), "[1.0]");
    	assertEquals("Checking recursive merge sort with 2 element array",Arrays.toString(SortComparison.mergeSortRecursive(twoElementArray)), "[1.0, 2.0]");
    	assertEquals("Checking recursive merge sort with 3 element array",Arrays.toString(SortComparison.mergeSortRecursive(threeElementArray)), "[1.0, 2.0, 3.0]");
    }
    
    @Test
    public void testMergeSortIterative() {
    	double[] oneElementArray = new double[] {1};
    	double[] twoElementArray = new double[] {2,1};
    	double[] threeElementArray = new double[] {2,3,1};
    	assertEquals("Checking iterative merge sort with 1 element array",Arrays.toString(SortComparison.mergeSortIterative(oneElementArray)), "[1.0]");
    	assertEquals("Checking iterative merge sort with 2 element array",Arrays.toString(SortComparison.mergeSortIterative(twoElementArray)), "[1.0, 2.0]");
    	assertEquals("Checking iterative merge sort with 3 element array",Arrays.toString(SortComparison.mergeSortIterative(threeElementArray)), "[1.0, 2.0, 3.0]");
    }
    
    @Test
    public void testTenArray() {
    	double[] tenArray = new double[] {2377.88, 2910.66, 8458.14, 1522.08, 5855.37, 1934.75,8106.23, 1735.31, 4849.83, 1518.63};
    	double[] copyArray = new double[] {2377.88, 2910.66, 8458.14, 1522.08, 5855.37, 1934.75,8106.23, 1735.31, 4849.83, 1518.63};
        double[] tenArrayExpected = new double[] {1518.63, 1522.08, 1735.31, 1934.75, 2377.88, 2910.66, 4849.83, 5855.37, 8106.23, 8458.14};
        
        assertEquals("Checking insertion sort against 10 element array.", Arrays.toString(SortComparison.insertionSort(copyArray)), Arrays.toString(tenArrayExpected));
        copyArray = Arrays.copyOf(tenArray, tenArray.length);
        assertEquals("Checking selection sort against 10 element array.", Arrays.toString(SortComparison.selectionSort(copyArray)), Arrays.toString(tenArrayExpected));
        copyArray = Arrays.copyOf(tenArray, tenArray.length);
        assertEquals("Checking quick sort against 10 element array.", Arrays.toString(SortComparison.quickSort(copyArray)), Arrays.toString(tenArrayExpected));
        copyArray = Arrays.copyOf(tenArray, tenArray.length);
        assertEquals("Checking merge sort recursive against 10 element array.",Arrays.toString(SortComparison.mergeSortRecursive(copyArray)), Arrays.toString(tenArrayExpected));
        copyArray = Arrays.copyOf(tenArray, tenArray.length);
        assertEquals("Checking merge sort iterative against 10 element array.",Arrays.toString(SortComparison.mergeSortIterative(copyArray)), Arrays.toString(tenArrayExpected));

    }
    
    @Test
    public void testMain() throws Exception {
    	SortComparison.main(null);
    }


    // TODO: add more tests here. Each line of code and each decision in Collinear.java should
    // be executed at least once from at least one test.

    // ----------------------------------------------------------
    /**
     *  Main Method.
     *  Use this main method to create the experiments needed to answer the experimental performance questions of this assignment.
     * @throws Exception 
     *
     */
    
    public static void main(String[] args) throws Exception
    {
    	String[] testFiles = new String[] {"numbers10","numbers100","numbers1000","numbers1000Duplicates",
    										"numbersNearlyOrdered1000","numbersReverse1000","numbersSorted1000"};
    	
    	for (String filename: testFiles) {
    		
    		// For each test file we will read it in and convert it to an arraylist of doubles
    		
    		File testFile = new File("");
    	    String currentPath = testFile.getAbsolutePath();
    	    System.out.println("current path is: " + currentPath);
    	    
    	    String finalFilename = currentPath.toString() + "\\" + filename.toString();
    	    System.out.println("Attempting to read from file in: "+testFile.getCanonicalPath());
    		
    		Scanner scanner = new Scanner(new File(finalFilename));
    		ArrayList<Double> temp = new ArrayList<Double>();
    		
    		while (scanner.hasNextLine()) {
    			try {
    				temp.add(scanner.nextDouble());
    			}
    			catch (Exception e) {
    				break;
    			}
    		}
    		
    		double[] arr = new double[temp.size()];
    		double[] copyArr = new double[temp.size()];
    		for (int i = 0; i < temp.size(); i++) {
    			arr[i] = temp.get(i);
    		}
    		
    		System.out.println(filename+": ");
    		
    		// Here is where we test each of the algorithms 
    		long start_time = System.nanoTime();
    		long end_time = System.nanoTime();
    		double difference = (end_time - start_time) / 1e6;
    		
    		
    		copyArr = Arrays.copyOf(arr, arr.length);
    		start_time = System.nanoTime();
    		SortComparison.insertionSort(copyArr);
    		end_time = System.nanoTime();
    		System.out.println("Insertion Sort time: "+((end_time - start_time) / 1e6)+" ms");
    		
    		copyArr = Arrays.copyOf(arr, arr.length);
    		start_time = System.nanoTime();
    		SortComparison.selectionSort(copyArr);
    		end_time = System.nanoTime();
    		System.out.println("Selection Sort time: "+((end_time - start_time) / 1e6)+" ms");
    		
    		copyArr = Arrays.copyOf(arr, arr.length);
    		start_time = System.nanoTime();
    		SortComparison.quickSort(copyArr);
    		end_time = System.nanoTime();
    		System.out.println("    Quick Sort time: "+((end_time - start_time) / 1e6)+" ms");
    		
    		copyArr = Arrays.copyOf(arr, arr.length);
    		start_time = System.nanoTime();
    		SortComparison.mergeSortRecursive(copyArr);
    		end_time = System.nanoTime();
    		System.out.println("MergeSort Recursive: "+((end_time - start_time) / 1e6)+" ms");
    		
    		copyArr = Arrays.copyOf(arr, arr.length);
    		start_time = System.nanoTime();
    		SortComparison.mergeSortIterative(copyArr);
    		end_time = System.nanoTime();
    		System.out.println("MergeSort Iterative: "+((end_time - start_time) / 1e6)+" ms");
    		
    		System.out.println("");
    	}
    	
		
    }

}