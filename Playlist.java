/* THIS CODE WAS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
CODE WRITTEN BY OTHER STUDENTS OR COPIED FROM ONLINE RESOURCES. 
_Alejandro_Pacheco_  */

/*  This class represents a Playlist of podcast episodes, where each
/*  episode is implemented as an object of type Episode. A user navigating
/*  a Playlist should be able to move between songs using next or previous references.
/*
/*  To enable flexible navigation, the Playlist is implemented as
/*  a Doubly Linked List where each episode has a link to both
/*  the next and the prev episodes in the list.
*/
import java.util.*;

public class Playlist {
	private Episode head;
	private int size;

	public Playlist() {
		head = null;
		size = 0;
	}

	public boolean isEmpty() {
		return head == null;
	}

	// Ensure that "size" is updated properly in other methods, to always
	// reflect the correct number of episodes in the current Playlist
	public int getSize() {
		return size;
	}

	// Our implementation of toString() displays the Playlist forward,
	// starting at the first episode (i.e. head) and ending at the last episode,
	// while utilizing the "next" reference in each episode
	@Override
	public String toString() {
		String output = "[HEAD] ";
		Episode current = head;
		if (!isEmpty()) {
			while (current.next != null) {
				output += current + " -> ";
				current = current.next;
			}
			output += current + " [END]\n";
		} else {
			output += " [END]\n";
		}
		return output;
	}

	// This method displays the Playlist backward, starting at
	// the last episode and ending at the first episode (i.e. head),
	// while utilizing the "prev" reference in each episode
	public String toReverseString() {
		String output = "[END] ";
		Episode current = head;
		if (!isEmpty()) {
			while (current.next != null)
				current = current.next;
			// current is now pointing to last node
			while (current.prev != null) {
				output += current + " -> ";
				current = current.prev;
			}
			output += current + " [HEAD]\n";
		} else {
			output += " [HEAD]\n";
		}
		return output;
	}

	/**************************************************************/
	// A4 Part 1 Methods (Add/Delete Operations)
	/*
	 * 1. Create a new Episode object with the passed title and duration
	 * 2. If the Playlist is empty, set the new Episode as the head
	 * 3. If the Playlist is not empty, set the new Episode as the head
	 * and set the current head's prev reference to the new Episode
	 * 4. Increment the size of the Playlist
	 * if nothing just set it as the head
	 * if there is a head, set the new episode as the head
	 * do this by doing it
	 * 
	 * do this for addLast as well w/ differewnt steps
	 */

	public void addFirst(String title, double duration) {
		Episode addFirst = new Episode(title, duration, head, null); // null at prev since it is the head
		if (isEmpty()) { // use implemented method
			head = addFirst;
			size++;
		} else { // if not empty, set the new episode as the head by making the head's prev
					// reference the new episode
			head.prev = addFirst;
			head = addFirst;
			size++;
		}
	}

	public void addLast(String title, double duration) {
		Episode addLast = new Episode(title, duration, null, head); // null at next since it is the last
		if (isEmpty()) { // use implemented method
			head = addLast;
			size++;
		} else {
			Episode current = head;
			while (current.next != null) { // while there is a next, keep going
				current = current.next; // current is now pointing to last node
			}
			current.next = addLast; // last node is now addLast
			addLast.prev = current;
			size++;
		}
	}

	public Episode deleteFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException(); // throw exception if empty
		} else {
			Episode temp = head;
			head = head.next;
			head.prev = null;
			size--;
			return temp;
		}
	}

	public Episode deleteLast() {
		if (isEmpty()) {
			throw new NoSuchElementException(); // throw exception if empty
		}
		Episode current = head;
		while (current.next != null) {
			current = current.next;
		}
		Episode temp = current;
		current.prev.next = null; // current.prev.next = null; ?
		size--;
		return temp;
	}

	public Episode deleteEpisode(String title) {
		if (isEmpty()) {
			throw new NoSuchElementException(); // throw exception if empty
		}
		Episode current = head;
		while (current != null) {
			if (current.title.equals(title)) {
				if (current == head) {
					head = head.next; // head is now pointing to the next node
					if (head != null) {
						head.prev = null; // head's prev is now pointing to null
					}
				} else if (current.next == null) {
					current.prev.next = null;
				} else {
					current.prev.next = current.next;
					current.next.prev = current.prev;
				}
				size--; // decrement size // not sure if needed
				return current; // we want to return the episode that was deleted
			}
			current = current.next;
		}
		throw new NoSuchElementException(); // throw exception if nothing passes
	}

	/***************************************************************/
	// A4 Part 2 Methods (Sorting the Playlist using MergeSort)

	public Episode merge(Episode a, Episode b) {
		Episode result = null; // merged list
		if (a == null)
			return b;
		if (b == null)
			return a;
		if (a.compareTo(b) < 0) { // if (a.title.compareTo(b.title) < 0) { //
			result = a;
			result.next = merge(a.next, b);
			result.next.prev = result; //
			result.prev = null;
		} else { // covers == 0 and > 0, so no need to do each individually
			result = b;
			result.next = merge(a, b.next);
			result.next.prev = result;
			result.prev = null;
		}
		return result; // not actually sorting in this step just mergin, sorting happens in mergeSort
	}

	// Finds the middle episode of the list that begins at the passed node reference
	private Episode getMiddleEpisode(Episode node) {
		if (node == null)
			return node;
		Episode slow = node;
		Episode fast = node;
		while (fast.next != null && fast.next.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		return slow;
	}

	// MergeSort starting point
	public void mergeSort() {
		if (isEmpty())
			throw new RuntimeException("Cannot sort empty list.");
		head = sort(head);
	}

	// Recursively splits the list starting at a given node reference
	public Episode sort(Episode node) {
		if (node == null || node.next == null)
			return node;
		Episode middle = getMiddleEpisode(node); // get the middle of the list
		Episode left_head = node;
		Episode right_head = middle.next;

		// split the list into two halves:
		if (right_head != null)
			right_head.prev = null;
		middle.next = null;

		Episode left = sort(left_head);
		Episode right = sort(right_head);
		return merge(left, right);
	}
} // End of Playlist class    