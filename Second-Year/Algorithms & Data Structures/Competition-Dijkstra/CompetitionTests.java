import static org.junit.Assert.assertEquals;

import org.junit.Test;
/*
 * @Author: Conor Loftus
 */

/**
 * 
 * Djkstra algorithm is used only when you have a single source and you want to know the smallest path from one node to another. 
 * It computes the shortest-path distances from a chosen source node s to every other node in the graph although the algorithm doesnt
 * handle negetavie weighted distances well without some compensations or optimizations. The time complexity for this algorithm is
 * O(E log V) but we have to run this algorithm for E edges so it becomes O(E^2 log V) for this assignment
 * 
 * 
 * Floyd-Warshall's algorithm is used when any of all the nodes can be a source, so you want the shortest distance to reach any 
 * destination node from any source node. This only fails when there are negative cycles. Floyd-Warshall's algorithm is better
 * suited to solving the competition problem for this assignment as we arent looking for the shortest path from a single edge but
 * the shortest path between multiple edges. The time complexity for this algorithm is O(V^3) but takes up v^2 space complexity 
 *
 */

public class CompetitionTests {
	public int sA;
	public int sB;
	public int sC;
	
//	@Test 
//	public void testBag() {
//		Bag<Integer> adj =  new Bag<Integer>();
//	}

	@Test
	public void testDijkstra() {
		
		try {
			CompetitionDijkstra dj;
			
			dj = new CompetitionDijkstra(null, 50, 50, 50);    	// Longest path = 1.86 from edge 1
			assertEquals(-1, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("", 50, 50, 50);    	// Longest path = 1.86 from edge 1
			assertEquals(-1, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("tinyEWD.txt", 50, 50, 50);    	// Longest path = 1.86 from edge 1
			assertEquals(38, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("tinyEWD.txt", 49, 50, 50);		// Path available but speeds incorrect
			assertEquals(-1, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("tinyEWD.txt", 150, 50, 50);		// Path available but speeds incorrect
			assertEquals(-1, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("input-A.txt", 50, 50, 50);		// No path available
			assertEquals(-1, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("input-B.txt", 50, 50, 50);		
			assertEquals(10000, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("input-C.txt", 50, 50, 50);		// No path available
			assertEquals(-1, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("input-D.txt", 50, 50, 50);		 
			assertEquals(38, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("input-E.txt", 50, 50, 50);		
			assertEquals(-1, dj.timeRequiredforCompetition());				// 28
			
			dj = new CompetitionDijkstra("input-F.txt", 50, 50, 50);		// No path available
			assertEquals(-1, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("input-G.txt", 50, 50, 50);		// No path available
			assertEquals(-1, dj.timeRequiredforCompetition());
			
			//dj = new CompetitionDijkstra("input-H.txt", 50, 50, 50);		// No path available
			//assertEquals(-1, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("input-I.txt", 72, 70, 65);		
			assertEquals(185, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("input-J.txt", 50, 50, 50);		// No path available
			assertEquals(-1, dj.timeRequiredforCompetition());
			
			dj = new CompetitionDijkstra("input-K.txt", 51, 70, 88);
			assertEquals(314, dj.timeRequiredforCompetition());
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		
		

	}

	@Test
	public void testFW() {
		
		try {
			CompetitionFloydWarshall fw;
			
			fw = new CompetitionFloydWarshall(null, 50, 50, 50);
			assertEquals(-1, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("", 50, 50, 50);
			assertEquals(-1, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("tinyEWD.txt", 50, 50, 50);
			assertEquals(38, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("input-A.txt", 50, 50, 50);		// No path available
			assertEquals(-1, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("input-B.txt", 50, 50, 50);		
			assertEquals(10000, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("input-B.txt", 39, 50, 50);		
			assertEquals(-1, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("input-B.txt", 50, 50, 101);		
			assertEquals(-1, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("input-C.txt", 50, 50, 50);		// No path available
			assertEquals(-1, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("input-D.txt", 50, 50, 50);		 
			assertEquals(38, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("input-E.txt", 50, 50, 50);		
			assertEquals(-1, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("input-F.txt", 50, 50, 50);		// No path available
			assertEquals(-1, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("input-G.txt", 50, 50, 50);		// No path available
			assertEquals(-1, fw.timeRequiredforCompetition());
			
			//fw = new CompetitionFloydWarshall("input-H.txt", 50, 50, 50);		// No path available
			//assertEquals(-1, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("input-I.txt", 72, 70, 65);		
			assertEquals(185, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("input-J.txt", 50, 50, 50);		// No path available
			assertEquals(-1, fw.timeRequiredforCompetition());
			
			fw = new CompetitionFloydWarshall("input-K.txt", 51, 70, 88);
			assertEquals(314, fw.timeRequiredforCompetition());
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		
	}
}