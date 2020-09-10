import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CollinearTest {
	
	
	int[] ar1 = new int[100];
	int[] ar2 = new int[100];
	int[] ar3 = new int[100];
	
	int[] fillArray(int[] arry) {
		for (int i = 0; i < arry.length; i++) {
			arry[i] = (int)(Math.random() * 1000);
		}
		return arry;
	}

	@Test
	void testCountCollinear() {
//		int[] a1 = new int[] {2,4,6,7,8};
//		int[] a2 = new int[] {12,15,16,18};
//		int[] a3 = new int[] {22,25,26,29};
		
		for (int i = 0; i < 10; i++) {
			fillArray(ar1);
			fillArray(ar2);
			fillArray(ar3);
			
			assertEquals(Collinear.countCollinear(ar1, ar2, ar3), Collinear.countCollinearFast(ar1, ar2, ar3));
		}
		
	}

	@Test
	void testCountCollinearFast() {
		int[] a1 = new int[] {2,4,6,7,8};
		int[] a2 = new int[] {12,15,16,18};
		int[] a3 = new int[] {22,25,26,29};
		
		assertEquals(Collinear.countCollinear(a1, a2, a3), 6);
	}

	@Test
	void testSort() {
		fail("Not yet implemented");
	}

	@Test
	void testBinarySearch() {
		fail("Not yet implemented");
	}

}