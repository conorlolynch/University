import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

public class CompetitionDijkstra {
	
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
	public class DijkstraSP {
	    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
	    private DirectedEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
	    private IndexMinPQ<Double> pq;    // priority queue of vertices 
	    private int s;					  // start vertice for this shortest path 
	    private double[] longestDist;	  // stores the longest shortest path between s and v (distance and edge)

	    /**
	     * Computes a shortest-paths tree from the source vertex {@code s} to every other
	     * vertex in the edge-weighted digraph {@code G}.
	     *
	     * @param  G the edge-weighted digraph
	     * @param  s the source vertex
	     * @throws IllegalArgumentException if an edge weight is negative
	     * @throws IllegalArgumentException unless {@code 0 <= s < V}
	     */
	    public DijkstraSP(EdgeWeightedDigraph G, int s) {
	        for (DirectedEdge e : G.edges()) {
	            if (e.weight() < 0)
	                throw new IllegalArgumentException("edge " + e + " has negative weight");
	        }

	        this.s = s;
	        this.longestDist = new double[] {-1,-1};	// edge-to and distance
	        distTo = new double[G.V()];
	        edgeTo = new DirectedEdge[G.V()];

	        validateVertex(s);

	        for (int v = 0; v < G.V(); v++)
	            distTo[v] = Double.POSITIVE_INFINITY;
	        distTo[s] = 0.0;

	        // relax vertices in order of distance from s
	        pq = new IndexMinPQ<Double>(G.V());
	        pq.insert(s, distTo[s]);
	        while (!pq.isEmpty()) {
	            int v = pq.delMin();
	            for (DirectedEdge e : G.adj(v))
	                relax(e);
	        }

	        // check optimality conditions
	        assert check(G, s);
	    }

	    // relax edge e and update pq if changed
	    private void relax(DirectedEdge e) {
	        int v = e.from(), w = e.to();
	        if (distTo[w] > distTo[v] + e.weight()) {
	            distTo[w] = distTo[v] + e.weight();
	            edgeTo[w] = e;
	            
	            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
	            else                pq.insert(w, distTo[w]);
	        }
	        
	        if (distTo[w] > this.longestDist[1]) {
        		this.longestDist[1] = distTo[w];
        		this.longestDist[0] = w;
        	}
	        
	    }

	    /**
	     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
	     * @param  v the destination vertex
	     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
	     *         {@code Double.POSITIVE_INFINITY} if no such path
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public double distTo(int v) {
	        validateVertex(v);
	        return distTo[v];
	    }

	    /**
	     * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
	     *
	     * @param  v the destination vertex
	     * @return {@code true} if there is a path from the source vertex
	     *         {@code s} to vertex {@code v}; {@code false} otherwise
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public boolean hasPathTo(int v) {
	        validateVertex(v);
	        return distTo[v] < Double.POSITIVE_INFINITY;
	    }

	    /**
	     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
	     *
	     * @param  v the destination vertex
	     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
	     *         as an iterable of edges, and {@code null} if no such path
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public Iterable<DirectedEdge> pathTo(int v) {
	        validateVertex(v);
	        if (!hasPathTo(v)) return null;
	        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
	        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
	            path.push(e);
	        }
	        return path;
	    }


	    // check optimality conditions:
	    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
	    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
	    private boolean check(EdgeWeightedDigraph G, int s) {

	        // check that edge weights are nonnegative
	        for (DirectedEdge e : G.edges()) {
	            if (e.weight() < 0) {
	                System.err.println("negative edge weight detected");
	                return false;
	            }
	        }

	        // check that distTo[v] and edgeTo[v] are consistent
	        if (distTo[s] != 0.0 || edgeTo[s] != null) {
	            System.err.println("distTo[s] and edgeTo[s] inconsistent");
	            return false;
	        }
	        for (int v = 0; v < G.V(); v++) {
	            if (v == s) continue;
	            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
	                System.err.println("distTo[] and edgeTo[] inconsistent");
	                return false;
	            }
	        }

	        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
	        for (int v = 0; v < G.V(); v++) {
	            for (DirectedEdge e : G.adj(v)) {
	                int w = e.to();
	                if (distTo[v] + e.weight() < distTo[w]) {
	                    System.err.println("edge " + e + " not relaxed");
	                    return false;
	                }
	            }
	        }

	        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
	        for (int w = 0; w < G.V(); w++) {
	            if (edgeTo[w] == null) continue;
	            DirectedEdge e = edgeTo[w];
	            int v = e.from();
	            if (w != e.to()) return false;
	            if (distTo[v] + e.weight() != distTo[w]) {
	                System.err.println("edge " + e + " on shortest path not tight");
	                return false;
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
	    
	    public void printShortestPaths() {
	    	for (int t = 0; t < G.V(); t++) {
	            if (SP.hasPathTo(t)) {
	                System.out.printf("%d to %d (%.2f)  ", this.s, t, SP.distTo(t));
	                for (DirectedEdge e : SP.pathTo(t)) {
	                	System.out.print(e + "   ");
	                }
	                System.out.println();
	            }
	            else {
	            	System.out.printf("%d to %d         no path\n", this.s, t);
	            }
	        }
	    }
	    
	    public double[] getLongestDist() {
	    	return this.longestDist;
	    }
	    
	    // Return an arraylist of all the vertices which have an infinite distance from the start vertice
	    public ArrayList<Integer> getInfinityIndexes(){
	    	ArrayList<Integer> arr = new ArrayList<Integer>();
	    	for (int i = 0; i < G.V(); i++) {
	    		if (distTo[i] == Double.POSITIVE_INFINITY) {
	    			arr.add(i);
	    		}
	    	}
	    	return arr;
	    }
	}

	public EdgeWeightedDigraph createDiagraph(String filename){
		EdgeWeightedDigraph diagram = null;	
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
	        		diagram = new EdgeWeightedDigraph(Integer.valueOf(thisLine));
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
	
	int sA;
	int sB;
	int sC;
	double[] longestDist;
	EdgeWeightedDigraph G;
	DijkstraSP SP;
	
	CompetitionDijkstra (String filename, int sA, int sB, int sC) {
		
		this.sA = sA;
		this.sB = sB;
		this.sC = sC;
		
		this.longestDist = new double[] {-1,-1};	// edge-to and distance
		
		// First read in the file and create a graph out of it
		if (filename == null || filename.equalsIgnoreCase("")) {
			longestDist[1] = -1;
			longestDist[0] = -1;
		}
		else {
			try {
				this.G = createDiagraph(filename);
		    	
		    	// Next we create shortest paths from each vertice and see which has the largest
		    	Map<Integer, Double> verts = new HashMap<Integer, Double>();		// vertice to , distance
		    	
		    	// We want to build the empty hashmap 
		    	for (int i = 0; i < G.V(); i++) {
		    		verts.put(i, -1.0);
		    	}
		    	
		    	for (int s = 0; s < G.V(); s++) {
		    		// Compute shortest paths
		    		SP = new DijkstraSP(G, s);
		    		
		    		// For every vertice in the graph we want to update the longest distance in the hashmap 
		    		for (int v = 0; v < G.V(); v++) {
		    			// if at any point the hashmap is empty then break out of this and set longestdist to -1 to ensure error
		    			if (verts.isEmpty()) {
		    				break;
		    			}
		    			// if the distance to this vertice is infinity then we want to remove this key and value from the hashmap
		    			else if (SP.distTo(v) == Double.POSITIVE_INFINITY) {
		    				verts.remove(v);
		    			}
		    			// if the distance to this vertice is larger than the current value stored in this key then update the value
		    			else {
		    				if (verts.get(v) != null) {
		    					if (SP.distTo(v) > verts.get(v)) {
		    						verts.put(v, SP.distTo(v));
		    					}
		    				}
		    			}
		    		}
		    		
		    		
		    		if (verts.isEmpty()) {
		    			this.longestDist[0] = -1;
		    			this.longestDist[1] = -1;
		    		}
		    		else {    			
		    			// In this part we want to choose the longest distance from the remaining vertices in the hashmap
		    			for (int vertice: verts.keySet()){
		                    double value = verts.get(vertice);
		                    //System.out.println(vertice + " " + value);  
		                    
		                    if (this.longestDist[1] < value) {
		                    	this.longestDist[1] = value;
		                    	this.longestDist[0] = vertice;
		                    }
		        		} 

		    		}
		    	}
			}
			catch (Exception e){
				System.out.println(e);
			}
		}
    	
	}
	
	/**
	    * @return int: minimum minutes that will pass before the three contestants can meet
	     */
	    public int timeRequiredforCompetition(){
	    	if (this.G == null) {
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
	    	else if (this.longestDist[1] < 0) {
	    		return -1;
	    	}
	    	else {
	    		int slowestSpeed = Math.min(this.sA, Math.min(this.sB, this.sC));	// Speed in km per minute
        		return (int)Math.ceil((this.longestDist[1] * 1000 / slowestSpeed));
	    	}
	    }
}