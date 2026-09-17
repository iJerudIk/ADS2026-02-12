package by.it.group551002.shinkevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

	private Object[] elements;

	private int size;
	private static final int DEFAULT_CAPACITY = 10;

	public ListC() {
		this.elements = new Object[DEFAULT_CAPACITY];
		this.size = 0;
	}

	private void grow() {
		int newCapacity = elements.length == 0 ? DEFAULT_CAPACITY : elements.length * 2;
		Object[] newElements = new Object[newCapacity];

		for (int i = 0; i < size; i++) {
			newElements[i] = elements[i];
		}
		elements = newElements;
	}


	/// //////////////////////////////////////////////////////////////////////
	/// //////////////////////////////////////////////////////////////////////
	/// ///               Обязательные к реализации методы             ///////
	/// //////////////////////////////////////////////////////////////////////
	/// //////////////////////////////////////////////////////////////////////


	@Override
	public String toString() {
		if (size == 0) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < size; i++) {
			sb.append(elements[i]);
			if (i < size - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public boolean add(E e) {
		if (size == elements.length) {
			grow();
		}

		elements[size++] = e;
		return true;
	}

	@Override
	public E remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}

		E oldValue = (E) elements[index];

		for (int i = index; i < size - 1; i++) {
			elements[i] = elements[i + 1];
		}
		elements[--size] = null;

		return oldValue;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void add(int index, E element) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		if (size == elements.length) {
			grow();
		}

		for (int i = size; i > index; i--) {
			elements[i] = elements[i - 1];
		}
		elements[index] = element;
		size++;
	}

	@Override
	public boolean remove(Object o) {
		int index = indexOf(o);
		if (index >= 0) {
			remove(index);
			return true;
		}

		return false;
	}

	@Override
	public E set(int index, E element) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}

		E oldValue = (E) elements[index];

		elements[index] = element;

		return oldValue;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
	}

	@Override
	public int indexOf(Object o) {
		for (int i = 0; i < size; i++) {
			if (o == null) {
				if (elements[i] == null) return i;
			} else {
				if (o.equals(elements[i])) return i;
			}
		}

		return -1;
	}

	@Override
	public E get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}

		return (E) elements[index];
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		for (int i = size - 1; i >= 0; i--) {
			if (o == null) {
				if (elements[i] == null) return i;
			} else {
				if (o.equals(elements[i])) return i;
			}
		}

		return -1;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object e : c) {
			if (!contains(e)) return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean modified = false;
		for (E e : c) {
			if (add(e)) modified = true;
		}
		return modified;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}

		boolean modified = false;
		for (E e : c) {
			add(index++, e);
			modified = true;
		}
		return modified;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		for (int i = size - 1; i >= 0; i--) {
			if (c.contains(elements[i])) {
				remove(i);
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean modified = false;
		for (int i = size - 1; i >= 0; i--) {
			if (!c.contains(elements[i])) {
				remove(i);
				modified = true;
			}
		}
		return modified;
	}


	/// //////////////////////////////////////////////////////////////////////
	/// //////////////////////////////////////////////////////////////////////
	/// ///               Опциональные к реализации методы             ///////
	/// //////////////////////////////////////////////////////////////////////
	/// //////////////////////////////////////////////////////////////////////


	@Override
	public Object[] toArray() {
		Object[] result = new Object[size];
		for (int i = 0; i < size; i++) {
			result[i] = elements[i];
		}
		return result;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		if (a.length < size) {
			a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
		}

		for (int i = 0; i < size; i++) {
			a[i] = (T) elements[i];
		}

		if (a.length > size) {
			a[size] = null;
		}
		return a;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		ListC<E> subList = new ListC<>();
		for (int i = fromIndex; i < toIndex; i++) {
			subList.add((E) elements[i]);
		}
		return subList;
	}

	@Override
	public Iterator<E> iterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new ListIterator<E>() {
			private int cursor = index;
			private int lastRet = -1;

			@Override
			public boolean hasNext() {
				return cursor < size;
			}

			@Override
			public E next() {
				if (!hasNext()) throw new IndexOutOfBoundsException("No more elements");

				lastRet = cursor;

				return (E) elements[cursor++];
			}

			@Override
			public boolean hasPrevious() {
				return cursor > 0;
			}

			@Override
			public E previous() {
				if (!hasPrevious()) throw new IndexOutOfBoundsException("В начале списка");

				lastRet = --cursor;

				return (E) elements[cursor];
			}

			@Override
			public int nextIndex() {
				return cursor;
			}

			@Override
			public int previousIndex() {
				return cursor - 1;
			}

			@Override
			public void remove() {
				if (lastRet < 0) throw new IllegalStateException("First, call next() or previous().");

				ListC.this.remove(lastRet);

				cursor = lastRet;
				lastRet = -1;
			}

			@Override
			public void set(E e) {
				if (lastRet < 0) throw new IllegalStateException();
				ListC.this.set(lastRet, e);
			}

			@Override
			public void add(E e) {
				ListC.this.add(cursor++, e);
				lastRet = -1;
			}
		};
	}

}
