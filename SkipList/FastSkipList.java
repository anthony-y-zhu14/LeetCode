package SkipList;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class FastSkipList {

    @SuppressWarnings("unchecked")
	protected static class Node<Integer> {
		Integer x;
		Node<Integer>[] next;
		int[] length;
		public Node(Integer ix, int h) {
			x = ix;
			next = (Node<Integer>[]) Array.newInstance(Node.class, h+1);
			length = new int[h+1];
		}
		public int height() {
			return next.length - 1;
		}
	}
	/**
	 * This node<Integer> sits on the left side of the skiplist
	 */
	protected Node<Integer> sentinel;
	/**
	 * The maximum height of any element
	 */
	int h;
	/**
	 * The number of elements stored in the skiplist
	 */
	int n;
	/**
	 * A source of random numbers
	 */
	Random rand;
	/**
	 * Used by add(x) method
	 */
	protected Node<Integer>[] stack;

	@SuppressWarnings("unchecked")
	public FastSkipList() {
		n = 0;
		sentinel = new Node<Integer>(null, 32);
		stack = (Node<Integer>[])Array.newInstance(Node.class, sentinel.next.length);
		h = 0;
		rand = new Random();
	}

	/**
	 * Find the node<Integer> u that precedes the value x in the skiplist.
	 *
	 * @param x - the value to search for
	 * @return a node<Integer> u that maximizes u.x subject to
	 * the constraint that u.x < x --- or sentinel if u.x >= x for
	 * all node<Integer>s x
	 */
	protected Node<Integer> findPredNode(Integer x) {
		Node<Integer> u = sentinel;
		int r = h;
		while (r >= 0) {
			while (u.next[r] != null && u.next[r].x < x)
				u = u.next[r];   // go right in list r
			r--;               // go down into list r-1
		}
		return u;
	}

	protected Node<Integer> findPredNodeAtGivenLevel(Integer x, int i) {
		Node<Integer> u = sentinel;
		int r = h;
		while (r >= i) {
			while (u.next[r] != null && u.next[r].x < x)
				u = u.next[r];   // go right in list r
			r--;               // go down into list r-1
		}
		return u;
	}

	protected Node<Integer> findPred(int i){
		Node<Integer> u = sentinel;
        int r = h;
        int j = -1;   // index of the sentinel node
        while (r >= 0) {
            while (u.next[r] != null && j + u.next[r].length[r] < i) {
                j += u.next[r].length[r];
                u = u.next[r];
            }
            r--;
        }
        return u;
	}

	public int find(int x) {
		Node<Integer> u = findPredNode(x);
		return u.next[0] == null ? null : u.next[0].x;
	}

	public int findGE(Integer x) {
		if (x == null) {   // return first node<Integer>
			return sentinel.next[0] == null ? null : sentinel.next[0].x;
		}
		return find(x);
	}

	public int findLT(Integer x) {
		if (x == null) {  // return last node<Integer>
			Node<Integer> u = sentinel;
			int r = h;
			while (r >= 0) {
				while (u.next[r] != null)
					u = u.next[r];
				r--;
			}
			return u.x;
		}
		return findPredNode(x).x;
	}

	public Integer get(int i) {
		// This is too slow and making it faster will take changes to this
		// structure			
		
		if (i < 0 || i > n-1) {
			throw new IndexOutOfBoundsException();
		}	
        return findPred(i).next[0].x;
	}

	public int rank(Integer x) {
		// This is too slow and making it faster will take changes to this
		// structure
		Node<Integer> u = sentinel;
		int r = h;
		int j = -1;		// index of the current node in list 0
		while (r >= 0) {
			while (u.next[r] != null && u.next[r].x < x){
				j += u.next[r].length[r];
				u = u.next[r];   // go right in list r
			}				
			r--;               // go down into list r-1
		}
		return j+1;
	}

	public boolean remove(Integer x) {
		boolean removed = false;
		Node<Integer> u = sentinel;
		int r = h;
		int comp = 0;
		while (r >= 0) {
			while (u.next[r] != null && (comp = u.next[r].x - x) < 0) {
				u = u.next[r];
			}
			if (u.next[r] != null && comp == 0) {	//this means the node has been found at level r
				removed = true;		
				if (u.next[r].next[r] != null){
					u.next[r].next[r].length[r] += u.next[r].length[r] - 1;
				}		
				u.next[r] = u.next[r].next[r];								
				if (u == sentinel && u.next[r] == null){
					h--;  // height has gone down
				}					
			}
			else if (u.next[r] != null){
				u.next[r].length[r] -= 1;
			}
			r--;
		}
		if (removed) n--;
		return removed;
	}


	/**
	 * Simulate repeatedly tossing a coin until it comes up tails.
	 * Note, this code will never generate a height greater than 32
	 * @return the number of coin tosses - 1
	 */
	protected int pickHeight() {
		int z = rand.nextInt();
		int k = 0;
		int m = 1;
		while ((z & m) != 0) {
			k++;
			m <<= 1;
		}
		return k;
	}

	public void clear() {
		n = 0;
		h = 0;
		Arrays.fill(sentinel.next, null);
	}

	public int size() {
		return n;
	}
	

	/**
	 * Create a new iterator in which the next value in the iteration is u.next.x
	 * TODO: Constant time removal requires the use of a skiplist finger (a stack)
	 * @param u
	 * @return
	 */
	protected Iterator<Integer> iterator(Node<Integer> u) {
		class SkiplistIterator implements Iterator<Integer> {
			Node<Integer> u, prev;
			public SkiplistIterator(Node<Integer> u) {
				this.u = u;
				prev = null;
			}
			public boolean hasNext() {
				return u.next[0] != null;
			}
			public Integer next() {
				prev = u;
				u = u.next[0];
				return u.x;
			}
			public void remove() {
				// Not constant time
				FastSkipList.this.remove(prev.x);
			}
		}
		return new SkiplistIterator(u);
	}

	public Iterator<Integer> iterator() {
		return iterator(sentinel);
	}

	public Iterator<Integer> iterator(int x) {
		return iterator(findPredNode(x));
	}

	public boolean add(Integer x) {	
		Node<Integer> u = sentinel;
		int r = h;
		int comp = 0;
		while (r >= 0) {
			while (u.next[r] != null
			       && (comp = (u.next[r].x -x)) < 0)
				u = u.next[r];
			if (u.next[r] != null && comp == 0) return false;
			stack[r--] = u;          // going down, store u
		}
		Node<Integer> w = new Node<Integer>(x, pickHeight());
		while (h < w.height())
			stack[++h] = sentinel;   // height increased
		for (int i = 0; i < h+1; i++) {	
			if (i < w.next.length){
				w.next[i] = stack[i].next[i];
				stack[i].next[i] = w;
				setLength(w, i);
				if (w.next[i] != null && i != 0){		
					/*
					Since if we insert a node before node.next, it will push node.next one index to the right
					If before insertion the distance from prev to node.next was 6, after insertion that distance would increase to 7.
					*/						
					w.next[i].length[i] -= (w.length[i]-1);		
				}
			}
			else if (stack[i].next[i] != null){
				/*
					inserting a node would increast the distance by one
				*/	
				stack[i].next[i].length[i] += 1;
			}		
			
								
		}

		n++;
		return true;
	}

	private void setLength(Node<Integer> w, int r){
		Node<Integer> u;		

		if (r == 0){
			w.length[r] = 1;	// accounts for new node in list 0
		}
		else{			
			u = findPredNodeAtGivenLevel(w.x, r);
			while (u.next[r-1] != null	&& u.next[r-1].x <= w.x ){
				w.length[r] += u.next[r-1].length[r-1];
				u = u.next[r-1];
			}
		}	

	}

				
}

