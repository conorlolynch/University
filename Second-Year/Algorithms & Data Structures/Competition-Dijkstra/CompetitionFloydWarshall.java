import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;


public class CompetitionFloydWarshall {
	
	/* Code from Algorithms, 4th Edition 
	 * @author Robert Sedgewick
	 * @author Kevin Wayne*/
	public class Bag<Item> implements Iterable<Item> {
	    private Node<Item> first;    // beginning of bag
	    private int n;               // number of elements in bag

	    // helper linked list class
	    private class Node<Item> {
	        private Item item;
	        private Node<Item> next;
	    }

	    /**
	     * Initializes an empty bag.
	     */
	    public Bag() {
	        first = null;
	        n = 0;
	    }

	    /**
	     * Returns true if this bag is empty.
	     *
	     * @return {@code true} if this bag is empty;
	     *         {@code false} otherwise
	     */
	    public boolean isEmpty() {
	        return first == null;
	    }

	    /**
	     * Returns the number of items in this bag.
	     *
	     * @return the number of items in this bag
	     */
	    public int size() {
	        return n;
	    }

	    /**
	     * Adds the item to this bag.
	     *
	     * @param  item the item to add to this bag
	     */
	    public void add(Item item) {
	        Node<Item> oldfirst = first;
	        first = new Node<Item>();
	        first.item = item;
	        first.next = oldfirst;
	        n++;
	    }


	    /**
	     * Returns an iterator that iterates over the items in this bag in arbitrary order.
	     *
	     * @return an iterator that iterates over the items in this bag in arbitrary order
	     */
	    public Iterator<Item> iterator()  {
	        return new LinkedIterator(first);  
	    }

	    // an iterator, doesn't implement remove() since it's optional
	    private class LinkedIterator implements Iterator<Item> {
	        private Node<Item> current;

	        public LinkedIterator(Node<Item> first) {
	            current = first;
	        }

	        public boolean hasNext()  { return current != null;                     }
	        public void remove()      { throw new UnsupportedOperationException();  }

	        public Item next() {
	            if (!hasNext()) throw new NoSuchElementException();
	            Item item = current.item;
	            current = current.next; 
	            return item;
	        }
	    }
	}
	
	/* Code from Algorithms, 4th Edition 
	 * @author Robert Sedgewick
	 * @author Kevin Wayne*/
	public class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
	    private int maxN;        // maximum number of elements on PQ
	    private int n;           // number of elements on PQ
	    private int[] pq;        // binary heap using 1-based indexing
	    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
	    private Key[] keys;      // keys[i] = priority of i

	    /**
	     * Initializes an empty indexed priority queue with indices between {@code 0}
	     * and {@code maxN - 1}.
	     * @param  maxN the keys on this priority queue are index from {@code 0}
	     *         {@code maxN - 1}
	     * @throws IllegalArgumentException if {@code maxN < 0}
	     */
	    public IndexMinPQ(int maxN) {
	        if (maxN < 0) throw new IllegalArgumentException();
	        this.maxN = maxN;
	        n = 0;
	        keys = (Key[]) new Comparable[maxN + 1];    // make this of length maxN??
	        pq   = new int[maxN + 1];
	        qp   = new int[maxN + 1];                   // make this of length maxN??
	        for (int i = 0; i <= maxN; i++)
	            qp[i] = -1;
	    }

	    /**
	     * Returns true if this priority queue is empty.
	     *
	     * @return {@code true} if this priority queue is empty;
	     *         {@code false} otherwise
	     */
	    public boolean isEmpty() {
	        return n == 0;
	    }

	    /**
	     * Is {@code i} an index on this priority queue?
	     *
	     * @param  i an index
	     * @return {@code true} if {@code i} is an index on this priority queue;
	     *         {@code false} otherwise
	     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
	     */
	    public boolean contains(int i) {
	        validateIndex(i);
	        return qp[i] != -1;
	    }

	    /**
	     * Returns the number of keys on this priority queue.
	     *
	     * @return the number of keys on this priority queue
	     */
	    public int size() {
	        return n;
	    }

	    /**
	     * Associates key with index {@code i}.
	     *
	     * @param  i an index
	     * @param  key the key to associate with index {@code i}
	     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
	     * @throws IllegalArgumentException if there already is an item associated
	     *         with index {@code i}
	     */
	    public void insert(int i, Key key) {
	        validateIndex(i);
	        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
	        n++;
	        qp[i] = n;
	        pq[n] = i;
	        keys[i] = key;
	        swim(n);
	    }

	    /**
	     * Returns an index associated with a minimum key.
	     *
	     * @return an index associated with a minimum key
	     * @throws NoSuchElementException if this priority queue is empty
	     */
	    public int minIndex() {
	        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
	        return pq[1];
	    }

	    /**
	     * Returns a minimum key.
	     *
	     * @return a minimum key
	     * @throws NoSuchElementException if this priority queue is empty
	     */
	    public Key minKey() {
	        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
	        return keys[pq[1]];
	    }

	    /**
	     * Removes a minimum key and returns its associated index.
	     * @return an index associated with a minimum key
	     * @throws NoSuchElementException if this priority queue is empty
	     */
	    public int delMin() {
	        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
	        int min = pq[1];
	        exch(1, n--);
	        sink(1);
	        assert min == pq[n+1];
	        qp[min] = -1;        // delete
	        keys[min] = null;    // to help with garbage collection
	        pq[n+1] = -1;        // not needed
	        return min;
	    }

	    /**
	     * Returns the key associated with index {@code i}.
	     *
	     * @param  i the index of the key to return
	     * @return the key associated with index {@code i}
	     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
	     * @throws NoSuchElementException no key is associated with index {@code i}
	     */
	    public Key keyOf(int i) {
	        validateIndex(i);
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        else return keys[i];
	    }

	    /**
	     * Change the key associated with index {@code i} to the specified value.
	     *
	     * @param  i the index of the key to change
	     * @param  key change the key associated with index {@code i} to this key
	     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
	     * @throws NoSuchElementException no key is associated with index {@code i}
	     */
	    public void changeKey(int i, Key key) {
	        validateIndex(i);
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        keys[i] = key;
	        swim(qp[i]);
	        sink(qp[i]);
	    }

	    /**
	     * Change the key associated with index {@code i} to the specified value.
	     *
	     * @param  i the index of the key to change
	     * @param  key change the key associated with index {@code i} to this key
	     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
	     * @deprecated Replaced by {@code changeKey(int, Key)}.
	     */
	    @Deprecated
	    public void change(int i, Key key) {
	        changeKey(i, key);
	    }

	    /**
	     * Decrease the key associated with index {@code i} to the specified value.
	     *
	     * @param  i the index of the key to decrease
	     * @param  key decrease the key associated with index {@code i} to this key
	     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
	     * @throws IllegalArgumentException if {@code key >= keyOf(i)}
	     * @throws NoSuchElementException no key is associated with index {@code i}
	     */
	    public void decreaseKey(int i, Key key) {
	        validateIndex(i);
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        if (keys[i].compareTo(key) == 0)
	            throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
	        if (keys[i].compareTo(key) < 0)
	            throw new IllegalArgumentException("Calling decreaseKey() with a key strictly greater than the key in the priority queue");
	        keys[i] = key;
	        swim(qp[i]);
	    }

	    /**
	     * Increase the key associated with index {@code i} to the specified value.
	     *
	     * @param  i the index of the key to increase
	     * @param  key increase the key associated with index {@code i} to this key
	     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
	     * @throws IllegalArgumentException if {@code key <= keyOf(i)}
	     * @throws NoSuchElementException no key is associated with index {@code i}
	     */
	    public void increaseKey(int i, Key key) {
	        validateIndex(i);
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        if (keys[i].compareTo(key) == 0)
	            throw new IllegalArgumentException("Calling increaseKey() with a key equal to the key in the priority queue");
	        if (keys[i].compareTo(key) > 0)
	            throw new IllegalArgumentException("Calling increaseKey() with a key strictly less than the key in the priority queue");
	        keys[i] = key;
	        sink(qp[i]);
	    }

	    /**
	     * Remove the key associated with index {@code i}.
	     *
	     * @param  i the index of the key to remove
	     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
	     * @throws NoSuchElementException no key is associated with index {@code i}
	     */
	    public void delete(int i) {
	        validateIndex(i);
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        int index = qp[i];
	        exch(index, n--);
	        swim(index);
	        sink(index);
	        keys[i] = null;
	        qp[i] = -1;
	    }

	    // throw an IllegalArgumentException if i is an invalid index
	    private void validateIndex(int i) {
	        if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
	        if (i >= maxN) throw new IllegalArgumentException("index >= capacity: " + i);
	    }

	   /***************************************************************************
	    * General helper functions.
	    ***************************************************************************/
	    private boolean greater(int i, int j) {
	        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
	    }

	    private void exch(int i, int j) {
	        int swap = pq[i];
	        pq[i] = pq[j];
	        pq[j] = swap;
	        qp[pq[i]] = i;
	        qp[pq[j]] = j;
	    }


	   /***************************************************************************
	    * Heap helper functions.
	    ***************************************************************************/
	    private void swim(int k) {
	        while (k > 1 && greater(k/2, k)) {
	            exch(k, k/2);
	            k = k/2;
	        }
	    }

	    private void sink(int k) {
	        while (2*k <= n) {
	            int j = 2*k;
	            if (j < n && greater(j, j+1)) j++;
	            if (!greater(k, j)) break;
	            exch(k, j);
	            k = j;
	        }
	    }


	   /***************************************************************************
	    * Iterators.
	    ***************************************************************************/

	    /**
	     * Returns an iterator that iterates over the keys on the
	     * priority queue in ascending order.
	     * The iterator doesn't implement {@code remove()} since it's optional.
	     *
	     * @return an iterator that iterates over the keys in ascending order
	     */
	    public Iterator<Integer> iterator() { return new HeapIterator(); }

	    private class HeapIterator implements Iterator<Integer> {
	        // create a new pq
	        private IndexMinPQ<Key> copy;

	        // add all elements to copy of heap
	        // takes linear time since already in heap order so no keys move
	        public HeapIterator() {
	            copy = new IndexMinPQ<Key>(pq.length - 1);
	            for (int i = 1; i <= n; i++)
	                copy.insert(pq[i], keys[pq[i]]);
	        }

	        public boolean hasNext()  { return !copy.isEmpty();                     }
	        public void remove()      { throw new UnsupportedOperationException();  }

	        public Integer next() {
	            if (!hasNext()) throw new NoSuchElementException();
	            return copy.delMin();
	        }
	    }
	}
	
	/* Code from Algorithms, 4th Edition 
	 * @author Robert Sedgewick
	 * @author Kevin Wayne*/
	public class DirectedEdge { 
	    private final int v;
	    private final int w;
	    private final double weight;

	    /**
	     * Initializes a directed edge from vertex {@code v} to vertex {@code w} with
	     * the given {@code weight}.
	     * @param v the tail vertex
	     * @param w the head vertex
	     * @param weight the weight of the directed edge
	     * @throws IllegalArgumentException if either {@code v} or {@code w}
	     *    is a negative integer
	     * @throws IllegalArgumentException if {@code weight} is {@code NaN}
	     */
	    public DirectedEdge(int v, int w, double weight) {
	        if (v < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
	        if (w < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
	        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
	        this.v = v;
	        this.w = w;
	        this.weight = weight;
	    }

	    /**
	     * Returns the tail vertex of the directed edge.
	     * @return the tail vertex of the directed edge
	     */
	    public int from() {
	        return v;
	    }

	    /**
	     * Returns the head vertex of the directed edge.
	     * @return the head vertex of the directed edge
	     */
	    public int to() {
	        return w;
	    }

	    /**
	     * Returns the weight of the directed edge.
	     * @return the weight of the directed edge
	     */
	    public double weight() {
	        return weight;
	    }

	    /**
	     * Returns a string representation of the directed edge.
	     * @return a string representation of the directed edge
	     */
	    public String toString() {
	        return v + "->" + w + " " + String.format("%5.2f", weight);
	    }
	}
	
	/* Code from Algorithms, 4th Edition 
	 * @author Robert Sedgewick
	 * @author Kevin Wayne*/
	public class EdgeWeightedDigraph {
	    private final String NEWLINE = System.getProperty("line.separator");

	    private final int V;                // number of vertices in this digraph
	    private int E;                      // number of edges in this digraph
	    private Bag<DirectedEdge>[] adj;    // adj[v] = adjacency list for vertex v
	    private int[] indegree;             // indegree[v] = indegree of vertex v
	    
	    /**
	     * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
	     *
	     * @param  V the number of vertices
	     * @throws IllegalArgumentException if {@code V < 0}
	     */
	    public EdgeWeightedDigraph(int V) {
	        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
	        this.V = V;
	        this.E = 0;
	        this.indegree = new int[V];
	        adj = (Bag<DirectedEdge>[]) new Bag[V];
	        for (int v = 0; v < V; v++)
	            adj[v] = new Bag<DirectedEdge>();
	    }
	    
	    /**
	     * Initializes a new edge-weighted digraph that is a deep copy of {@code G}.
	     *
	     * @param  G the edge-weighted digraph to copy
	     */
	    public EdgeWeightedDigraph(EdgeWeightedDigraph G) {
	        this(G.V());
	        this.E = G.E();
	        for (int v = 0; v < G.V(); v++)
	            this.indegree[v] = G.indegree(v);
	        for (int v = 0; v < G.V(); v++) {
	            // reverse so that adjacency list is in same order as original
	            Stack<DirectedEdge> reverse = new Stack<DirectedEdge>();
	            for (DirectedEdge e : G.adj[v]) {
	                reverse.push(e);
	            }
	            for (DirectedEdge e : reverse) {
	                adj[v].add(e);
	            }
	        }
	    }

	    /**
	     * Returns the number of vertices in this edge-weighted digraph.
	     *
	     * @return the number of vertices in this edge-weighted digraph
	     */
	    public int V() {
	        return V;
	    }

	    /**
	     * Returns the number of edges in this edge-weighted digraph.
	     *
	     * @return the number of edges in this edge-weighted digraph
	     */
	    public int E() {
	        return E;
	    }

	    // throw an IllegalArgumentException unless {@code 0 <= v < V}
	    private void validateVertex(int v) {
	        if (v < 0 || v >= V)
	            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	    }

	    /**
	     * Adds the directed edge {@code e} to this edge-weighted digraph.
	     *
	     * @param  e the edge
	     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
	     *         and {@code V-1}
	     */
	    public void addEdge(DirectedEdge e) {
	        int v = e.from();
	        int w = e.to();
	        validateVertex(v);
	        validateVertex(w);
	        adj[v].add(e);
	        indegree[w]++;
	        E++;
	    }


	    /**
	     * Returns the directed edges incident from vertex {@code v}.
	     *
	     * @param  v the vertex
	     * @return the directed edges incident from vertex {@code v} as an Iterable
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public Iterable<DirectedEdge> adj(int v) {
	        validateVertex(v);
	        return adj[v];
	    }

	    /**
	     * Returns the number of directed edges incident from vertex {@code v}.
	     * This is known as the <em>outdegree</em> of vertex {@code v}.
	     *
	     * @param  v the vertex
	     * @return the outdegree of vertex {@code v}
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public int outdegree(int v) {
	        validateVertex(v);
	        return adj[v].size();
	    }

	    /**
	     * Returns the number of directed edges incident to vertex {@code v}.
	     * This is known as the <em>indegree</em> of vertex {@code v}.
	     *
	     * @param  v the vertex
	     * @return the indegree of vertex {@code v}
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public int indegree(int v) {
	        validateVertex(v);
	        return indegree[v];
	    }

	    /**
	     * Returns all directed edges in this edge-weighted digraph.
	     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
	     * {@code for (DirectedEdge e : G.edges())}.
	     *
	     * @return all edges in this edge-weighted digraph, as an iterable
	     */
	    public Iterable<DirectedEdge> edges() {
	        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
	        for (int v = 0; v < V; v++) {
	            for (DirectedEdge e : adj(v)) {
	                list.add(e);
	            }
	        }
	        return list;
	    } 

	    /**
	     * Returns a string representation of this edge-weighted digraph.
	     *
	     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
	     *         followed by the <em>V</em> adjacency lists of edges
	     */
	    public String toString() {
	        StringBuilder s = new StringBuilder();
	        s.append(V + " " + E + NEWLINE);
	        for (int v = 0; v < V; v++) {
	            s.append(v + ": ");
	            for (DirectedEdge e : adj[v]) {
	                s.append(e + "  ");
	            }
	            s.append(NEWLINE);
	        }
	        return s.toString();
	    }

	}
	
	/* Code from Algorithms, 4th Edition 
	 * @author Robert Sedgewick
	 * @author Kevin Wayne*/
	public class AdjMatrixEdgeWeightedDigraph {
		private final String NEWLINE = System.getProperty("line.separator");

		private final int V;
		private int E;
		private DirectedEdge[][] adj;
    
		/**
		 * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
		 * @param V the number of vertices
		 * @throws IllegalArgumentException if {@code V < 0}
		 */
		public AdjMatrixEdgeWeightedDigraph(int V) {
			if (V < 0) throw new IllegalArgumentException("number of vertices must be nonnegative");
			this.V = V;
			this.E = 0;
			this.adj = new DirectedEdge[V][V];
		}

		/**
		 * Returns the number of vertices in the edge-weighted digraph.
		 * @return the number of vertices in the edge-weighted digraph
		 */
		public int V() {
			return V;
		}

		/**
		 * Returns the number of edges in the edge-weighted digraph.
		 * @return the number of edges in the edge-weighted digraph
		 */
		public int E() {
			return E;
		}

		/**
		 * Adds the directed edge {@code e} to the edge-weighted digraph (if there
		 * is not already an edge with the same endpoints).
		 * @param e the edge
		 */
		public void addEdge(DirectedEdge e) {
			int v = e.from();
			int w = e.to();
			validateVertex(v);
			validateVertex(w);
			if (adj[v][w] == null) {
				E++;
				adj[v][w] = e;
			}
		}

		/**
		 * Returns the directed edges incident from vertex {@code v}.
		 * @param v the vertex
		 * @return the directed edges incident from vertex {@code v} as an Iterable
		 * @throws IllegalArgumentException unless {@code 0 <= v < V}
		 */
		public Iterable<DirectedEdge> adj(int v) {
			validateVertex(v);
			return new AdjIterator(v);
		}

		// support iteration over graph vertices
		private class AdjIterator implements Iterator<DirectedEdge>, Iterable<DirectedEdge> {
			private int v;
			private int w = 0;

			public AdjIterator(int v) {
				this.v = v;
			}

			public Iterator<DirectedEdge> iterator() {
				return this;
			}

			public boolean hasNext() {
				while (w < V) {
					if (adj[v][w] != null) return true;
					w++;
				}
				return false;
			}

			public DirectedEdge next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return adj[v][w++];
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
    }

    /**
     * Returns a string representation of the edge-weighted digraph. This method takes
     * time proportional to <em>V</em><sup>2</sup>.
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *   followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (DirectedEdge e : adj(v)) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
}
	
	/* Code from Algorithms, 4th Edition 
	 * @author Robert Sedgewick
	 * @author Kevin Wayne*/
	public class FloydWarshall {
	    private boolean hasNegativeCycle;  // is there a negative cycle?
	    private double[][] distTo;         // distTo[v][w] = length of shortest v->w path
	    private DirectedEdge[][] edgeTo;   // edgeTo[v][w] = last edge on shortest v->w path

	    /**
	     * Computes a shortest paths tree from each vertex to to every other vertex in
	     * the edge-weighted digraph {@code G}. If no such shortest path exists for
	     * some pair of vertices, it computes a negative cycle.
	     * @param G the edge-weighted digraph
	     */
	    public FloydWarshall(AdjMatrixEdgeWeightedDigraph G) {
	        int V = G.V();
	        distTo = new double[V][V];
	        edgeTo = new DirectedEdge[V][V];

	        // initialize distances to infinity
	        for (int v = 0; v < V; v++) {
	            for (int w = 0; w < V; w++) {
	                distTo[v][w] = Double.POSITIVE_INFINITY;
	            }
	        }

	        // initialize distances using edge-weighted digraph's
	        for (int v = 0; v < G.V(); v++) {
	            for (DirectedEdge e : G.adj(v)) {
	                distTo[e.from()][e.to()] = e.weight();
	                edgeTo[e.from()][e.to()] = e;
	            }
	            // in case of self-loops
	            if (distTo[v][v] >= 0.0) {
	                distTo[v][v] = 0.0;
	                edgeTo[v][v] = null;
	            }
	        }

	        // Floyd-Warshall updates
	        for (int i = 0; i < V; i++) {
	            // compute shortest paths using only 0, 1, ..., i as intermediate vertices
	            for (int v = 0; v < V; v++) {
	                if (edgeTo[v][i] == null) continue;  // optimization
	                for (int w = 0; w < V; w++) {
	                    if (distTo[v][w] > distTo[v][i] + distTo[i][w]) {	
	                        distTo[v][w] = distTo[v][i] + distTo[i][w];
	                        edgeTo[v][w] = edgeTo[i][w];
	                       
	                    }
	                }
	                // check for negative cycle
	                if (distTo[v][v] < 0.0) {
	                    hasNegativeCycle = true;
	                    return;
	                }
	            }
	            
	        }
	        //assert check(G);
	    }

	    /**
	     * Is there a negative cycle?
	     * @return {@code true} if there is a negative cycle, and {@code false} otherwise
	     */
	    public boolean hasNegativeCycle() {
	        return hasNegativeCycle;
	    }

	    /**
	     * Is there a path from the vertex {@code s} to vertex {@code t}?
	     * @param  s the source vertex
	     * @param  t the destination vertex
	     * @return {@code true} if there is a path from vertex {@code s}
	     *         to vertex {@code t}, and {@code false} otherwise
	     * @throws IllegalArgumentException unless {@code 0 <= s < V}
	     * @throws IllegalArgumentException unless {@code 0 <= t < V}
	     */
	    public boolean hasPath(int s, int t) {
	        validateVertex(s);
	        validateVertex(t);
	        return distTo[s][t] < Double.POSITIVE_INFINITY;
	    }

	    /**
	     * Returns the length of a shortest path from vertex {@code s} to vertex {@code t}.
	     * @param  s the source vertex
	     * @param  t the destination vertex
	     * @return the length of a shortest path from vertex {@code s} to vertex {@code t};
	     *         {@code Double.POSITIVE_INFINITY} if no such path
	     * @throws UnsupportedOperationException if there is a negative cost cycle
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public double dist(int s, int t) {
	        validateVertex(s);
	        validateVertex(t);
	        if (hasNegativeCycle())
	            throw new UnsupportedOperationException("Negative cost cycle exists");
	        return distTo[s][t];
	    }

	    /**
	     * Returns a shortest path from vertex {@code s} to vertex {@code t}.
	     * @param  s the source vertex
	     * @param  t the destination vertex
	     * @return a shortest path from vertex {@code s} to vertex {@code t}
	     *         as an iterable of edges, and {@code null} if no such path
	     * @throws UnsupportedOperationException if there is a negative cost cycle
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public Iterable<DirectedEdge> path(int s, int t) {
	        validateVertex(s);
	        validateVertex(t);
	        if (hasNegativeCycle())
	            throw new UnsupportedOperationException("Negative cost cycle exists");
	        if (!hasPath(s, t)) return null;
	        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
	        for (DirectedEdge e = edgeTo[s][t]; e != null; e = edgeTo[s][e.from()]) {
	            path.push(e);
	        }
	        return path;
	    }

	    // check optimality conditions
	    private boolean check(AdjMatrixEdgeWeightedDigraph G) {

	        // no negative cycle
	        if (!hasNegativeCycle()) {
	            for (int v = 0; v < G.V(); v++) {
	                for (DirectedEdge e : G.adj(v)) {
	                    int w = e.to();
	                    for (int i = 0; i < G.V(); i++) {
	                        if (distTo[i][w] > distTo[i][v] + e.weight()) {
	                            System.err.println("edge " + e + " is eligible");
	                            return false;
	                        }
	                    }
	                }
	            }
	        }
	        return true;
	    }

	    // throw an IllegalArgumentException unless {@code 0 <= v < V}
	    private void validateVertex(int v) {
	        int V = distTo.length;
	        if (v < 0 || v >= V)
	            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	    }
	}
	
	/*  
	 * @author Conor Loftus */
	public AdjMatrixEdgeWeightedDigraph createAdjMatrixEdgeWeightedDiagraph(String filename){
		AdjMatrixEdgeWeightedDigraph diagram = null;
		try {
			FileReader fr=new FileReader(filename);    
	        BufferedReader br=new BufferedReader(fr);    
	        String thisLine;
	        int count = 0;
	        
	        while ((thisLine = br.readLine()) != null) {
	        	if (count > 1) {
	        		String[] splitted = thisLine.trim().split("\\s+");
	        		diagram.addEdge(new DirectedEdge(Integer.valueOf(splitted[0]), Integer.valueOf(splitted[1]), Double.valueOf(splitted[2])));
	        	}
	        	else if (count == 0) {
	        		diagram = new AdjMatrixEdgeWeightedDigraph(Integer.valueOf(thisLine));
	        	}
	        	
	        	count++;
	        	
	        }    
	        br.close();    
	        fr.close(); 
		}
		catch (Exception e) {
			System.out.println(e);
			return null;
		}
		
		return diagram;
	}
	
	/* 
	 * @author Conor Loftus */
	public double findLongestDist() {
		double dist = -1;
		for (int v = 0; v < G.V(); v++) {
            for (int w = 0; w < G.V(); w++) {
            	if (!FW.hasPath(v, w)) {
                    // We need to check w to v to see if there is an opposite path, if not then no solution to this competiton
            		if (w >= v) {
            			if (!FW.hasPath(v, w)) {
            				// there isnt a path to the converse so there is no solution to this input
            				return -1;
            			}
            		}
                    
                }
            	else {
            		if (FW.dist(v, w) < Double.POSITIVE_INFINITY && dist < FW.dist(v, w)) {
                		dist = FW.dist(v, w);
                	}
            	}           	           	
            }
        }  
		
		return dist;
	}
	
	int sA;
	int sB;
	int sC;
	double[] longestFound;
	AdjMatrixEdgeWeightedDigraph G;
	FloydWarshall FW;
	
    CompetitionFloydWarshall (String filename, int sA, int sB, int sC) {
    	
    	this.sA = sA;
    	this.sB = sB;
    	this.sC = sC;
    	
    	longestFound = new double[] {-1, -1};
    	
    	if (filename == null || filename.equalsIgnoreCase("")) {
    		longestFound[1] = -1;
    	}
    	else {
    		
    		try {
    			// Create a matrix edge weighted diagraph and pass this graph into the floyd warshall constructor
            	G = createAdjMatrixEdgeWeightedDiagraph(filename);
            	FW = new FloydWarshall(G); 
            	
            	longestFound[1] = findLongestDist();
    		}
    		catch (Exception e) {
    			System.out.println(e);
    		}
    	}
    }


    /**
     * @return int: minimum minutes that will pass before the three contestants can meet
     */
    public int timeRequiredforCompetition(){
    	if (G == null) {
    		return -1;
    	}
    	// There has to be at least 3 vertices for the contestants to spawn at
    	else if (G.V() < 3) {
    		return -1;
    	}
    	// All the speeds of the contetsants must be between 50 and 100 so we check for that
    	else if ((this.sA < 50 || this.sA > 100) || (this.sB < 50 || this.sB > 100) || (this.sC < 50 || this.sC > 100)) {
    		return -1;
    	}
    	else if (longestFound[1] < 0) {
    		return -1;
    	}
    	else {
    		int slowestSpeed = Math.min(this.sA, Math.min(this.sB, this.sC));	// Speed in km per minute
    		return (int)Math.ceil((this.longestFound[1] * 1000 / slowestSpeed));
    	}
    }

}