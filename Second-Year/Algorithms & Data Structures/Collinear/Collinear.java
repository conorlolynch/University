import java.util.Arrays;

// -------------------------------------------------------------------------
/**
 *  This class contains only two static methods that search for points on the
 *  same line in three arrays of integers. 
 *
 *  @author  Conor Loftus
 *  @version 18/09/18 12:21:09
 */
class Collinear
{
	
	public static void main(String[] args) {
		int num = 50;
		int[] a = new int[num];
		int[] b = new int[num];
		int[] c = new int[num];
		
		a = fillArray(a);
		b = fillArray(b);
		c = fillArray(c);
		
		System.out.println("a: "+Arrays.toString(a));
		System.out.println("b: "+Arrays.toString(b));
		System.out.println("c: "+Arrays.toString(c));
		
		long lst = System.nanoTime();
		System.out.println(countCollinearFast(a,b,c));
		long let = System.nanoTime();
		
		System.out.println("--------------------------");
		
		long lStartTime = System.nanoTime();
		System.out.println(countCollinear(a,b,c));
		long lEndTime = System.nanoTime();
		
		//System.out.println(lEndTime-lStartTime);
		//System.out.println(let - lst);
		//System.out.println("Ratio: "+(lEndTime-lStartTime)/(let - lst));
		
		
	}
	
	static int[] fillArray(int[] arry) {
		for (int i = 0; i < arry.length; i++) {
			arry[i] = (int)(Math.random() * 1000);
		}
		return arry;
	}

   // ----------------------------------------------------------
    /**
     * Counts for the number of non-hoizontal lines that go through 3 points in arrays a1, a2, a3.
     * This method is static, thus it can be called as Collinear.countCollinear(a1,a2,a3)
     * @param a1: An UNSORTED array of integers. Each integer a1[i] represents the point (a1[i], 1) on the plain.
     * @param a2: An UNSORTED array of integers. Each integer a2[i] represents the point (a2[i], 2) on the plain.
     * @param a3: An UNSORTED array of integers. Each integer a3[i] represents the point (a3[i], 3) on the plain.
     * @return the number of points which are collinear and do not lie on a horizontal line.
     *
     * Array a1, a2 and a3 contain points on the horizontal line y=1, y=2 and y=3, respectively.
     * A non-horizontal line will have to cross all three of these lines. Thus
     * we are looking for 3 points, each in a1, a2, a3 which lie on the same
     * line.
     *
     * Three points (x1, y1), (x2, y2), (x3, y3) are collinear (i.e., they are on the same line) if
     * 
     * x1(y2−y3)+x2(y3−y1)+x3(y1−y2)=0 
     *
     * In our case y1=1, y2=2, y3=3.
     *
     * You should implement this using a BRUTE FORCE approach (check all possible combinations of numbers from a1, a2, a3)
     *
     * ----------------------------------------------------------
     *
     * 
     *  Order of Growth
     *  -------------------------
     *
     *  Calculate and write down the order of growth of your algorithm. You can use the asymptotic notation.
     *  You should adequately explain your answer. Answers without adequate explanation will not be counted.
     *
     *  Order of growth: N^3
     *
     *  Explanation: N^3 Order of Growth due to the nesting of three linear for loops.
     */
    static int countCollinear(int[] a1, int[] a2, int[] a3)
    {
      int count = 0; 
      
      for (int A1 = 0; A1 < a1.length; A1++) {
    	  for (int A2 = 0; A2 < a2.length; A2++ ) {
    		  for (int A3 = 0; A3 < a3.length; A3++) {
    			  if ((2*a2[A2] - a1[A1] - a3[A3]) == 0) {
    				  System.out.println(a1[A1]+"-"+a2[A2]+"-"+a3[A3]);
    				  count++;
    			  }
    		  }
    	  }
      }
      
      return count;
    }

    // ----------------------------------------------------------
    /**
     * Counts for the number of non-hoizontal lines that go through 3 points in arrays a1, a2, a3.
     * This method is static, thus it can be called as Collinear.countCollinearFast(a1,a2,a3)
     * @param a1: An UNSORTED array of integers. Each integer a1[i] represents the point (a1[i], 1) on the plain.
     * @param a2: An UNSORTED array of integers. Each integer a2[i] represents the point (a2[i], 2) on the plain.
     * @param a3: An UNSORTED array of integers. Each integer a3[i] represents the point (a3[i], 3) on the plain.
     * @return the number of points which are collinear and do not lie on a horizontal line.
     *
     * In this implementation you should make non-trivial use of InsertionSort and Binary Search.
     * The performance of this method should be much better than that of the above method.
     *
     *
     *  Order of Growth
     *  -------------------------
     *
     *  Calculate and write down the order of growth of your algorithm. You can use the asymptotic notation.
     *  You should adequately explain your answer. Answers without adequate explanation will not be counted.
     *
     *  Order of Growth: ~ N^2 **** Need to check this ***
     *
     *  Explanation: Order of growth derived from the two nested linear for loops 
     *
     *
     */
    static int countCollinearFast(int[] a1, int[] a2, int[] a3)
    {
    	// First we sort the two other arrays by insertion sort since we will be looking up values in them by binary search
    	Collinear.sort(a2);
    	Collinear.sort(a3);
    	
    	int count = 0;
 
    	for (int i = 0 ; i < a1.length; i++) {
    		for (int j = 0; j < a2.length; j++ ) {
    			if (Collinear.binarySearch(a3, -a1[i] + 2*a2[j])) {
    				System.out.println(a1[i] + "-"+a2[j]+"-"+(-a1[i] + 2*a2[j]));
    				count++;
    			}
    		}
    	}
    	
    	return count;
    }

    // ----------------------------------------------------------
    /**
     * Sorts an array of integers according to InsertionSort.
     * This method is static, thus it can be called as Collinear.sort(a)
     * @param a: An UNSORTED array of integers. 
     * @return after the method returns, the array must be in ascending sorted order.
     *
     * ----------------------------------------------------------
     *
     *  Order of Growth
     *  -------------------------
     *
     *  Caclulate and write down the order of growth of your algorithm. You can use the asymptotic notation.
     *  You should adequately explain your answer. Answers without adequate explanation will not be counted.
     *
     *  Order of Growth: N^2
     *
     *  Explanation: Two linear for-loops.
     *
     */
    static void sort(int[] a)
    {
      for ( int j = 1; j<a.length; j++)
      {
        int i = j - 1;
        while(i>=0 && a[i]>a[i+1])
        {
          int temp = a[i];
          a[i] = a[i+1];
          a[i+1] = temp;
          i--;
        }
      }
    }

    // ----------------------------------------------------------
    /**
     * Searches for an integer inside an array of integers.
     * This method is static, thus it can be called as Collinear.binarySearch(a,x)
     * @param a: A array of integers SORTED in ascending order.
     * @param x: An integer.
     * @return true if 'x' is contained in 'a'; false otherwise.
     *
     * ----------------------------------------------------------
     *
     *  Order of Growth
     *  -------------------------
     *
     *  Caclulate and write down the order of growth of your algorithm. You can use the asymptotic notation.
     *  You should adequately explain your answer. Answers without adequate explanation will not be counted.
     *
     *  Order of Growth: log(N)
     *
     *  Explanation: Binary Search will only check check half of an array for a certain input value to see if it exists,
     *  			 Similar to a Binary Tree, we want a mathematical model that tells us how many times it takes to divide a number to reach 1.
     *  			 log(N) is that mathematical function.
     *
     */
    static boolean binarySearch(int[] a, int x)
    {
    	 int leftSide = 0;
    	 int rightSide = a.length - 1; 
         while (leftSide <= rightSide) { 
             int currentIndex = leftSide + (rightSide - leftSide) / 2; 
   
             // Check if x is present at mid between current left boundary and current right boundary of array. 
             if (a[currentIndex] == x) 
                 return true; 
   
             // If x greater, set the left boundary to this index, ignoring the left half of the array.
             if (a[currentIndex] < x) 
                 leftSide = currentIndex + 1; 
   
             // If x is smaller, set the right boundary to this index, ignoring the right half of the array. 
             else
                 rightSide = currentIndex - 1; 
         } 
   
         // Element was not found in the array, return false. 
         return false; 
    }

}