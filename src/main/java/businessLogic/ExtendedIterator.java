package businessLogic;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ExtendedIterator<E> implements Iterator<E> {
	
		int pointer;
		List<E> elems;
	
		/**
		 * Initializes the Extended Iterator
		 * The pointer will be before the first element
		 * @param list
		 */
		public ExtendedIterator(List<E> list) {
			elems = list;
			goFirst();
		}
		
		public E previous() throws NoSuchElementException {
			E item;
			try {
				item = elems.get(--pointer);
			} catch (IndexOutOfBoundsException e) {
				pointer++;
				throw new NoSuchElementException();
			}
			return item;
		}
		
		public boolean hasPrevious() {
			return pointer > 0;
		}
		
		public void goFirst() {
			pointer = -1;
		}
		
		public void goLast() {
			pointer = elems.size();
		}

		public boolean hasNext() {
			return pointer < elems.size() -1;
		}

		@Override
		public E next() throws NoSuchElementException {
			E item;
			try {
				item = elems.get(++pointer);
			} catch (IndexOutOfBoundsException e) {
				pointer--;
				throw new NoSuchElementException();
			}
			return item;
		}
}
