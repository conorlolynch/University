/*************************************************************************
 *  Binary Search Tree class.
 *  Adapted from Sedgewick and Wayne.
 *
 *  @version 3.0 1/11/15 16:49:42
 *
 *  @author Conor Loftus
 *
 *************************************************************************/

import java.util.NoSuchElementException;


public class BST<Key extends Comparable<Key>, Value> {
    private Node root;             // root of BST

    /**
     * Private node class.
     */
    private class Node {
        private Key key;           // sorted by key
        private Value val;         // associated data
        private Node left, right;  // left and right subtrees
        private int N;             // number of nodes in subtree

        public Node(Key key, Value val, int N) {
            this.key = key;
            this.val = val;
            this.N = N;
        }
    }

    // is the symbol table empty?
    public boolean isEmpty() { return size() == 0; }

    // return number of key-value pairs in BST
    public int size() { return size(root); }

    // return number of key-value pairs in BST rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        else return x.N;
    }

    /**
     *  Search BST for given key.
     *  Does there exist a key-value pair with given key?
     *
     *  @param key the search key
     *  @return true if key is found and false otherwise
     */
    public boolean contains(Key key) {
        return get(key) != null;
    }

    /**
     *  Search BST for given key.
     *  What is the value associated with given key?
     *
     *  @param key the search key
     *  @return value associated with the given key if found, or null if no such key exists.
     */
    public Value get(Key key) { return get(root, key); }

    private Value get(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else              return x.val;
    }

    /**
     *  Insert key-value pair into BST.
     *  If key already exists, update with new value.
     *
     *  @param key the key to insert
     *  @param val the value associated with key
     */
    public void put(Key key, Value val) {
        if (val == null) { delete(key); return; }
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = put(x.left,  key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else              x.val   = val;
        x.N = 1 + size(x.left) + size(x.right);
        return x;
    }
    
    /**
     * Return the key which has a N keys less than it
     * @param n The number of keys less than the desired key
     * @return	The desired key
     */
    public Key select(int n) 
    {
    	if (n < 0 || n >= size()) return null;
    	Node x = select(root, n);
    	return x.key;
    }
    
    private Node select(Node x, int n) 
    {
    	if (x == null) return null;
    	int t = size(x.left);
    	if (t > n) return select(x.left, n);
    	else if (t < n) return select(x.right, n-t-1);
    	else return x;
    }


    /**
     * Tree height.
     *
     * Asymptotic worst-case running time using Theta notation: Theta(N)
     * Explanation:
     * 	The worst case scenario can occur if the binary search tree nodes are all linked linearly and occur one after another
     * 	This means we would have to do N iterations of the while loop to reach the deepest leaf in the tree. 
     *
     * @return the number of links from the root to the deepest leaf.
     *
     * Example 1: for an empty tree this should return -1.
     * Example 2: for a tree with only one node it should return 0.
     * Example 3: for the following tree it should return 2.
     *   B
     *  / \
     * A   C
     *      \
     *       D
     */
    public int height() {
    	if (root != null) {
    		int height = 0;
    		Node current = root;
    		while (current.N > 1) {
    			if (current.left == null) {
    				current = current.right;
    			}
    			else if (current.right == null) {
    				current = current.left;
    			}
    			else if ((current.left).N > (current.right).N) {	
    				current = current.left;
    			}
    			else {
    				current = current.right;
    			}
    			height++;
    		}
    		return height;
    	}
    	return -1;
    }
    

    /**
     * Median key.
     * If the tree has N keys k1 < k2 < k3 < ... < kN, then their median key 
     * is the element at position (N+1)/2 (where "/" here is integer division)
     * 
     * //The running time should be Theta(h), where h is the height of the tree.
     *
     * @return the median key, or null if the tree is empty.
     */
    public Key median() {
      if (isEmpty()) return null;
      return select((root.N-1)/2);
    }


    /**
     * Print all keys of the tree in a sequence, in-order.
     * That is, for each node, the keys in the left subtree should appear before the key in the node.
     * Also, for each node, the keys in the right subtree should appear before the key in the node.
     * For each subtree, its keys should appear within a parenthesis.
     *
     * Example 1: Empty tree -- output: "()"
     * Example 2: Tree containing only "A" -- output: "(()A())"
     * Example 3: Tree:
     *   B
     *  / \
     * A   C
     *      \
     *       D
     *
     * output: "((()A())B(()C(()D())))"
     *
     * output of example in the assignment: (((()A(()C()))E((()H(()M()))R()))S(()X()))
     *
     * @return a String with all keys in the tree, in order, parenthesized.
     */
    public String printKeysInOrder() {
      if (isEmpty()) return "()";
      StringBuilder sb = new StringBuilder();  
      printKeysInOrder(root, sb);
      return sb.toString();
    }
    
    /**
     * Add the keys of a binary search tree to a string builder along with parentheses surrounding its nodes
     * @param currentNode The node to begin the recursion from, usually start with the root node of BST
     * @param output We pass a reference to the string builder that we populate with the keys of the BST 
     */
    private void printKeysInOrder(Node currentNode, StringBuilder output) {
        output.append("(");
        if (currentNode != null) {
            printKeysInOrder(currentNode.left, output);
            output.append(currentNode.val);
            printKeysInOrder(currentNode.right, output);
        }
        output.append(")");
    }
    
    /**
     * Pretty Printing the tree. Each node is on one line -- see assignment for details.
     *
     * @return a multi-line string with the pretty ascii picture of the tree.
     */
    public String prettyPrintKeys() {
      String prefix = "";
      StringBuilder output = new StringBuilder();
      prettyPrintKeys(root, prefix, output);
      return output.toString();
    }
     
    private String prettyPrintKeys(Node node, String prefix, StringBuilder output) {
    	if (node == null) {
    		output.append(prefix + "-null\n");
    		if (prefix.length() > 0) {
    			int lastIndex = prefix.lastIndexOf('|', prefix.length()-1);
    			if (lastIndex > 0) {
    				prefix = prefix.substring(0, lastIndex);
        			prefix += " ";
    			}
    			
    		}
    		return prefix;
    	}
    	else {
    		output.append(prefix + "-"+node.key+"\n");
    		prefix += " |";
    	}
    	prefix = prettyPrintKeys(node.left, prefix, output);
    	prefix = prettyPrintKeys(node.right, prefix, output);
    	return prefix;
    }

    /**
     * Deteles a key from a tree (if the key is in the tree).
     * Note that this method works symmetrically from the Hibbard deletion:
     * If the node to be deleted has two child nodes, then it needs to be
     * replaced with its predecessor (not its successor) node.
     *
     * @param key the key to delete
     */
    public void delete(Key key) {
    	deleteNode(root, key);
    }
    
    private Node deleteNode(Node node, Key key) {
        if (node == null)  return null; 
  
        int cmp = key.compareTo(node.key);
        if (cmp < 0) 
            node.left = deleteNode(node.left, key); 	
        else if (cmp > 0) {
            node.right = deleteNode(node.right, key); 
        }    
        else
        { 
            if (node.left == null) {
            	if (node.right == null) {return null;}  // The node to delete has no children so set the parent nodes next to null
            	else {return node.right;}	// The node to delete only has a right child so we want to replace it with the child
            }
            else {
            	if (node.right == null) {return node.left;} // The node to delete only has a left child so we replace it with the left child;
            	else {
            		// There are two children so we find the largest key on the left hand side
            		Node temp = node;  // we copy the node we are about to delete
            		Node copyNode = findMaxNode(node.left);
            		deleteNode(node.left, copyNode.key);
            		node = copyNode;	// we say the node we are about to delete is equal to the next largest node on the left
            		node.left = temp.left;
            		node.right = temp.right;
            		return node;
            		
            	}
            }
        } 
  
        return node; 
    }
    
    private Node findMaxNode(Node node) {
    	if (node != null) {
    		if (node.right == null) {
    			return node;
    		}
    		else {
    			node = findMaxNode(node.right);
    		}
    	}
    	return node;
    }
    
    private int[] fillArray(int[] arry) {
		for (int i = 0; i < arry.length; i++) {
			arry[i] = (int)(Math.random() * 1000);
		}
		return arry;
	}

}