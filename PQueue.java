package dateorganizer;

import java.util.*;

/**
 * This class describes a priority min-queue that uses an array-list-based min
 * binary heap that implements the PQueueAPI interface. The array holds objects
 * that implement the parameterized Comparable interface.
 * 
 * @author Duncan, Johnny Williams III
 * @param <E> the priority queue element type.
 * @since 09-20-2023
 */
public class PQueue<E extends Comparable<E>> implements PQueueAPI<E> {
	/**
	 * A complete tree stored in an array list representing the binary heap
	 */
	private ArrayList<E> tree;
	/**
	 * A comparator lambda function that compares two elements of this heap when
	 * rebuilding it; cmp.compare(x,y) gives 1. negative when x less than y 2.
	 * positive when x greater than y 3. 0 when x equal y
	 */
	private Comparator<? super E> cmp;

	/**
	 * Constructs an empty PQueue using the compareTo method of its data type as the
	 * comparator
	 */
	public PQueue() {
		tree = new ArrayList<>();
		cmp = (x, y) -> x.compareTo(y);
	}

	/**
	 * A parameterized constructor that uses an externally defined comparator
	 * 
	 * @param fn - a trichotomous integer value comparator function
	 */
	public PQueue(Comparator<? super E> fn) {
		tree = new ArrayList<>();
		cmp = fn;
	}

	public boolean isEmpty() {

		return tree.isEmpty();
	}

	public void insert(E obj) {
		tree.add(obj);
		int place = tree.size() - 1;
		int parent = (place - 1) / 2;
		do {
			if (cmp.compare(tree.get(place), tree.get(parent)) < 0) {
				swap(place, parent);
				place = parent;
				parent = (place - 1) / 2;
			}
		} while (place > 0 && cmp.compare(tree.get(place), tree.get(parent)) < 0);
	}

	public E remove() throws PQueueException {
		if (tree.isEmpty()) {
			throw new PQueueException("The Priority Queue is empty");
		} else {
			E minimum = tree.get(0);
			E bottomn = tree.remove(tree.size() - 1);
			tree.set(0, bottomn);
			rebuild(0);
			return minimum;
		}

	}

	public E peek() throws PQueueException {
		if (tree.isEmpty()) {
			throw new PQueueException("The Priority Queue is empty");
		} else {
			return tree.get(0);
		}
	}

	public int size() {
		return tree.size();
	}

	/**
	 * Swaps a parent and child elements of this heap at the specified indices
	 * 
	 * @param place  an index of the child element on this heap
	 * @param parent an index of the parent element on this heap
	 */
	private void swap(int place, int parent) {
		E temp = tree.get(parent);
		tree.set(parent, tree.get(place));
		tree.set(place, temp);
	}

	/**
	 * Rebuilds the heap to ensure that the heap property of the tree is preserved.
	 * 
	 * @param root the root index of the subtree to be rebuilt
	 */
	private void rebuild(int root) {
		int lnode = 2 * root + 1;
		int rnode = 2 * root + 2;
		int small = -1;

		if (lnode < tree.size()) {
			small = lnode;
			if (rnode < tree.size() && cmp.compare(tree.get(rnode), tree.get(lnode)) < 0) {
				small = rnode;
			}
			if (cmp.compare(tree.get(small), tree.get(root)) < 0) {
				swap(small, root);
				rebuild(small);
			}
		}
	}
}
