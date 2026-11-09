package by.it.group551002.shinkevich.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

	private static final boolean RED = true;
	private static final boolean BLACK = false;

	private static class Node {
		Integer key;
		String value;
		Node left;
		Node right;
		boolean color;

		Node(Integer key, String value, boolean color) {
			this.key = key;
			this.value = value;
			this.color = color;
		}
	}

	private Node root;
	private int size = 0;

	private boolean isRed(Node node) {
		if (node == null) return false;
		return node.color == RED;
	}

	private Node rotateLeft(Node h) {
		Node x = h.right;
		h.right = x.left;
		x.left = h;

		x.color = h.color;
		h.color = RED;

		return x;
	}

	private Node rotateRight(Node h) {
		Node x = h.left;
		h.left = x.right;
		x.right = h;

		x.color = h.color;
		h.color = RED;

		return x;
	}

	private void flipColors(Node h) {
		h.color = !h.color;
		if (h.left != null) h.left.color = !h.left.color;
		if (h.right != null) h.right.color = !h.right.color;
	}

	private Node balance(Node h) {
		if (isRed(h.right) && !isRed(h.left)) {
			h = rotateLeft(h);
		}
		if (isRed(h.left) && isRed(h.left.left)) {
			h = rotateRight(h);
		}
		if (isRed(h.left) && isRed(h.right)) {
			flipColors(h);
		}
		return h;
	}


	private Node put(Node h, Integer key, String value, String[] oldValue) {
		if (h == null) {
			size++;
			return new Node(key, value, RED);
		}

		int cmp = key.compareTo(h.key);
		if (cmp < 0) {
			h.left = put(h.left, key, value, oldValue);
		} else if (cmp > 0) {
			h.right = put(h.right, key, value, oldValue);
		} else {
			oldValue[0] = h.value;
			h.value = value;
			return h;
		}

		return balance(h);
	}

	private Node moveRedLeft(Node h) {
		flipColors(h);
		if (h.right != null && isRed(h.right.left)) {
			h.right = rotateRight(h.right);
			h = rotateLeft(h);
			flipColors(h);
		}
		return h;
	}

	private Node moveRedRight(Node h) {
		flipColors(h);
		if (h.left != null && isRed(h.left.left)) {
			h = rotateRight(h);
			flipColors(h);
		}
		return h;
	}

	private Node min(Node h) {
		if (h.left == null) return h;
		return min(h.left);
	}

	private Node max(Node h) {
		if (h.right == null) return h;
		return max(h.right);
	}

	private Node deleteMin(Node h) {
		if (h.left == null) return null;

		if (!isRed(h.left) && !isRed(h.left.left)) {
			h = moveRedLeft(h);
		}

		h.left = deleteMin(h.left);
		return balance(h);
	}

	private Node remove(Node h, Integer key, String[] removed) {
		if (key.compareTo(h.key) < 0) {
			if (!isRed(h.left) && (h.left != null && !isRed(h.left.left))) {
				h = moveRedLeft(h);
			}
			h.left = remove(h.left, key, removed);
		} else {
			if (isRed(h.left)) {
				h = rotateRight(h);
			}
			if (key.compareTo(h.key) == 0 && h.right == null) {
				removed[0] = h.value;
				size--;
				return null;
			}
			if (!isRed(h.right) && (h.right != null && !isRed(h.right.left))) {
				h = moveRedRight(h);
			}
			if (key.compareTo(h.key) == 0) {
				removed[0] = h.value;
				size--;
				Node m = min(h.right);
				h.key = m.key;
				h.value = m.value;
				h.right = deleteMin(h.right);
			} else {
				h.right = remove(h.right, key, removed);
			}
		}
		return balance(h);
	}

	private void inOrderToString(Node node, StringBuilder sb) {
		if (node == null) return;
		inOrderToString(node.left, sb);
		if (sb.length() > 1) {
			sb.append(", ");
		}
		sb.append(node.key).append("=").append(node.value);
		inOrderToString(node.right, sb);
	}

	/// //////////////////////////////////////////////////////////////////////
	/// ///               Mandatory Methods (Level B)                  ///////
	/// //////////////////////////////////////////////////////////////////////

	@Override
	public String toString() {
		if (root == null) return "{}";

		StringBuilder sb = new StringBuilder("{");
		inOrderToString(root, sb);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void clear() {
		root = null;
		size = 0;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public String put(Integer key, String value) {
		String[] oldValue = new String[1];
		root = put(root, key, value, oldValue);
		root.color = BLACK;
		return oldValue[0];
	}

	@Override
	public String remove(Object key) {
		if (!(key instanceof Integer) || root == null || !containsKey(key)) {
			return null;
		}
		String[] removed = new String[1];
		if (!isRed(root.left) && !isRed(root.right)) {
			root.color = RED;
		}
		root = remove(root, (Integer) key, removed);
		if (root != null) {
			root.color = BLACK;
		}
		return removed[0];
	}

	@Override
	public String get(Object key) {
		if (!(key instanceof Integer)) return null;
		Node node = root;
		Integer target = (Integer) key;
		while (node != null) {
			int cmp = target.compareTo(node.key);
			if (cmp < 0) node = node.left;
			else if (cmp > 0) node = node.right;
			else return node.value;
		}
		return null;
	}

	@Override
	public boolean containsKey(Object key) {
		if (!(key instanceof Integer)) return false;
		Node node = root;
		Integer target = (Integer) key;
		while (node != null) {
			int cmp = target.compareTo(node.key);
			if (cmp < 0) node = node.left;
			else if (cmp > 0) node = node.right;
			else return true;
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return containsValue(root, value);
	}

	private boolean containsValue(Node node, Object value) {
		if (node == null) return false;
		if (value == null ? node.value == null : value.equals(node.value)) {
			return true;
		}
		return containsValue(node.left, value) || containsValue(node.right, value);
	}

	@Override
	public Integer firstKey() {
		if (root == null) throw new NoSuchElementException();
		return min(root).key;
	}

	@Override
	public Integer lastKey() {
		if (root == null) throw new NoSuchElementException();
		return max(root).key;
	}

	@Override
	public SortedMap<Integer, String> headMap(Integer toKey) {
		if (toKey == null) throw new NullPointerException();
		MyRbMap subMap = new MyRbMap();
		fillHeadMap(root, toKey, subMap);
		return subMap;
	}

	private void fillHeadMap(Node node, Integer toKey, MyRbMap subMap) {
		if (node == null) return;
		fillHeadMap(node.left, toKey, subMap);
		if (node.key.compareTo(toKey) < 0) {
			subMap.put(node.key, node.value);
			fillHeadMap(node.right, toKey, subMap);
		}
	}

	@Override
	public SortedMap<Integer, String> tailMap(Integer fromKey) {
		if (fromKey == null) throw new NullPointerException();
		MyRbMap subMap = new MyRbMap();
		fillTailMap(root, fromKey, subMap);
		return subMap;
	}

	private void fillTailMap(Node node, Integer fromKey, MyRbMap subMap) {
		if (node == null) return;
		if (node.key.compareTo(fromKey) >= 0) {
			fillTailMap(node.left, fromKey, subMap);
			subMap.put(node.key, node.value);
		}
		fillTailMap(node.right, fromKey, subMap);
	}

	@Override
	public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
		if (fromKey == null || toKey == null) throw new NullPointerException();
		MyRbMap subMap = new MyRbMap();
		fillSubMap(root, fromKey, toKey, subMap);
		return subMap;
	}

	private void fillSubMap(Node node, Integer fromKey, Integer toKey, MyRbMap subMap) {
		if (node == null) return;
		if (node.key.compareTo(fromKey) >= 0) {
			fillSubMap(node.left, fromKey, toKey, subMap);
		}
		if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
			subMap.put(node.key, node.value);
		}
		if (node.key.compareTo(toKey) < 0) {
			fillSubMap(node.right, fromKey, toKey, subMap);
		}
	}

	@Override
	public Comparator<? super Integer> comparator() {
		return null;
	}

	/// ////////////////////////////////////////////////////////
	/// ///               Unused Methods                 ///////
	/// ////////////////////////////////////////////////////////

	@Override
	public void putAll(Map<? extends Integer, ? extends String> m) {
	}

	@Override
	public Set<Integer> keySet() {
		return null;
	}

	@Override
	public Collection<String> values() {
		return null;
	}

	@Override
	public Set<Entry<Integer, String>> entrySet() {
		return null;
	}
}