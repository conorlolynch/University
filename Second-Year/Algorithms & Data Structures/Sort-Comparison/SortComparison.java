import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;

// -------------------------------------------------------------------------

/**
 *  This class contains static methods that implementing sorting of an array of numbers
 *  using different sort algorithms.
 *
 *  @author
 *  @version HT 2020
 */

 class SortComparison {

    /**
     * Sorts an array of doubles using InsertionSort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     * @param a: An unsorted array of doubles.
     * @return array sorted in ascending order.
     *
     */
    static double [] insertionSort (double a[]){
    	int n = a.length;
    	
    	if (a.length > 1) {
    		for (int i = 1; i < n; i++) {
            	for (int j = i; j > 0; j--) {
            		if (a[j] < a[j-1]) {
            			// swap these items
            			double item = a[j];
            			a[j] = a[j-1];
            			a[j-1] = item;
            		}
            		else {
            			// we have found its position
            			break;
            		}
            	}
            }
    	}
        
        return a;
    }//end insertionsort
	
	    /**
     * Sorts an array of doubles using Selection Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     * @param a: An unsorted array of doubles.
     * @return array sorted in ascending order
     *
     */
    static double [] selectionSort (double a[]){
    	int n = a.length;
    	
    	if (n > 1) {
    		int min_item, i, j;
            for (i = 0; i < n; i++) {			// This is the sorted part
            	min_item = i;
            	for (j = i + 1; j < n; j++) {		// This is the current item 
            		if (a[j] < a[min_item]) {
            			min_item = j;
            		}
            	}
            	double item = a[i];
            	a[i] = a[min_item];
            	a[min_item] = item;
            }
    	}
    	return a;

    }//end selectionsort

    /**
     * Sorts an array of doubles using Quick Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     * @param a: An unsorted array of doubles.
     * @return array sorted in ascending order
     *
     */
    static double [] quickSort (double a[]){
    	if (a.length > 1) {
    		quickSort(a, 0, a.length-1);
    	}
    	return a;
    }//end quicksort
    
    static void quickSort (double a[], int low, int high) {
    	if (low < high) 
        { 
            int pivotIndex = partition(a, low, high); 
  
            // We sort the elements before and after the partition
            quickSort(a, low, pivotIndex-1); 
            quickSort(a, pivotIndex+1, high); 
        } 
    }
    
    static int partition(double a[], int low, int high) {
    	double pivotIndex = a[high];  
        int leftwall = low - 1;  
        for (int pointer = low; pointer < high; pointer++) { 
            if (a[pointer] < pivotIndex) { 					// If current element is smaller than the pivot, swap a[leftwall] and a[pointer] 
                double temp = a[++leftwall]; 
                a[leftwall] = a[pointer]; 
                a[pointer] = temp; 
            } 
        } 
  
        // swap a[leftwall+1] and a[high] (or pivot) 
        double temp = a[leftwall+1]; 
        a[leftwall+1] = a[high]; 
        a[high] = temp; 
  
        return leftwall+1; 

    }

    /**
     * Sorts an array of doubles using Merge Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     * @param a: An unsorted array of doubles.
     * @return array sorted in ascending order
     *
     */
    /**
     * Sorts an array of doubles using iterative implementation of Merge Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     *
     * @param a: An unsorted array of doubles.
     * @return after the method returns, the array must be in ascending sorted order.
     */

    static double[] mergeSortIterative (double a[]) {

    	
        int subArraySize;   
        int subArrayStartIndex;
        int arrLength = a.length;
         
        // subArraySize's = { 1, 2, 4, 8, 16, 32 ...} 
        for (subArraySize = 1; subArraySize <= arrLength-1; subArraySize *= 2) 
        { 
            for (subArrayStartIndex = 0; subArrayStartIndex < arrLength-1; subArrayStartIndex += subArraySize * 2 ) 
            { 
                int midpoint = Math.min(subArrayStartIndex + subArraySize - 1, arrLength-1); 
                int subArrayEndIndex = Math.min(subArrayStartIndex + 2 * subArraySize - 1, arrLength-1); 
          
                // merge sub-arrays a[subArrayStartIndex -> midpoint] and a[midpoint+1 -> subArrayEndIndex] 
                mergeSortIterative(a, subArrayStartIndex, midpoint, subArrayEndIndex); 
            } 
        } 
        return a;
	
    }//end mergesortIterative
    
    static void mergeSortIterative (double a[], int startIndex, int midpoint, int lastIndex) {
    	int leftPointer, rightPointer, mergePointer; 
        int leftSize = midpoint - startIndex + 1; 
        int rightSize = lastIndex - midpoint; 
        
        double[] leftArray = new double[leftSize];
        double[] rightArray = new double[rightSize];
        
        for (rightPointer = 0; rightPointer < rightSize; rightPointer++) {
        	rightArray[rightPointer] = a[rightPointer + midpoint + 1];
        }
        
        for (leftPointer = 0; leftPointer < leftSize; leftPointer++) {
        	leftArray[leftPointer] = a[leftPointer + startIndex];
        }
        
        mergePointer = startIndex; leftPointer = 0; rightPointer = 0;
        
        // Merge two sub arrays back into the source array
        while (leftPointer < leftSize && rightPointer < rightSize) 
        { 
            if (leftArray[leftPointer] <= rightArray[rightPointer]) { a[mergePointer] = leftArray[leftPointer++]; } 
            else { a[mergePointer] = rightArray[rightPointer++]; } 
            mergePointer++; 
        } 
        
        while (leftPointer < leftSize) { a[mergePointer++] = leftArray[leftPointer++]; } 
        while (rightPointer < rightSize){ a[mergePointer++] = rightArray[rightPointer++];} 

    }
    
    
    
    /**
     * Sorts an array of doubles using recursive implementation of Merge Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     *
     * @param a: An unsorted array of doubles.
     * @return after the method returns, the array must be in ascending sorted order.
     */
    static double[] mergeSortRecursive (double a[]) {
    	if (a.length > 1) {
    		
    		// Split passed array into equal partitions around the midpoint of the array
    		
    		int midpoint = a.length/2;
    		
    		double[] leftPartition = new double[midpoint];
    		double[] rightPartition = new double[a.length - midpoint];
    		
    		for (int i = 0; i < midpoint; i++) {
    			leftPartition[i] = a[i];
    		}
    		
    		for (int i = midpoint; i < a.length; i++) {
    			rightPartition[i - midpoint] = a[i];
    		}
    		
    		mergeSortRecursive(leftPartition);
    		mergeSortRecursive(rightPartition);
    		
    		// Merge the left and right array partitions
    		
    		int leftPointer = 0;
    		int rightPointer = 0;
    		int mergedPointer = 0;
    		
    		while (leftPointer < leftPartition.length && rightPointer < rightPartition.length) {
    			if (leftPartition[leftPointer] < rightPartition[rightPointer]) {
    				a[mergedPointer] = leftPartition[leftPointer++];
    			}
    			else {
    				a[mergedPointer] = rightPartition[rightPointer++];
    			}
    			
    			mergedPointer++;
    		}
    		
    		// Copy leftover elements into the merged array
    		
    		while(leftPointer < leftPartition.length) { a[mergedPointer++] = leftPartition[leftPointer++]; } 
            while(rightPointer < rightPartition.length) { a[mergedPointer++] = rightPartition[rightPointer++];} 
    	}
    	return a;
	
   }//end mergeSortRecursive


    public static void main(String[] args) throws Exception {
    	SortComparisonTest.main(null);
    }

 }//end class