package by.it.group551002.shinkevich.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

	private static class Node {
		Integer key;
		String value;
		int height;
		Node left;
		Node right;

		Node(Integer key, String value) {
			this.key = key;
			this.value = value;
			this.height = 1;
		}
	}

	private Node root;
	private int size = 0;

	private int height(Node node) {
		return node == null ? 0 : node.height;
	}

	private int max(int a, int b) {
		return a > b ? a : b;
	}

	private void updateHeight(Node node) {
		node.height = 1 + max(height(node.left), height(node.right));
	}

	private int getBalance(Node node) {
		return node == null ? 0 : height(node.left) - height(node.right);
	}

	private Node rotateRight(Node y) {
		Node x = y.left;
		Node t2 = x.right;

		x.right = y;
		y.left = t2;

		updateHeight(y);
		updateHeight(x);

		return x;
	}

	private Node rotateLeft(Node x) {
		Node y = x.right;
		Node t2 = y.left;

		y.left = x;
		x.right = t2;

		updateHeight(x);
		updateHeight(y);

		return y;
	}

	private Node balance(Node node) {
		if (node == null) return null;

		updateHeight(node);
		int balance = getBalance(node);

		if (balance > 1) {
			if (getBalance(node.left) < 0) {
				node.left = rotateLeft(node.left);
			}
			return rotateRight(node);
		}

		if (balance < -1) {
			if (getBalance(node.right) > 0) {
				node.right = rotateRight(node.right);
			}
			return rotateLeft(node);
		}

		return node;
	}

	private Node put(Node node, Integer key, String value, String[] oldValueHolder) {
		if (node == null) {
			size++;
			return new Node(key, value);
		}

		int cmp = key.compareTo(node.key);
		if (cmp < 0) {
			node.left = put(node.left, key, value, oldValueHolder);
		} else if (cmp > 0) {
			node.right = put(node.right, key, value, oldValueHolder);
		} else {
			oldValueHolder[0] = node.value;
			node.value = value;
			return node;
		}

		return balance(node);
	}

	private Node findMin(Node node) {
		return node.left == null ? node : findMin(node.left);
	}

	private Node remove(Node node, Integer key, String[] removedValueHolder) {
		if (node == null) {
			return null;
		}

		int cmp = key.compareTo(node.key);
		if (cmp < 0) {
			node.left = remove(node.left, key, removedValueHolder);
		} else if (cmp > 0) {
			node.right = remove(node.right, key, removedValueHolder);
		} else {
			removedValueHolder[0] = node.value;
			size--;

			if (node.left == null) {
				return node.right;
			} else if (node.right == null) {
				return node.left;
			}

			Node successor = findMin(node.right);
			node.key = successor.key;
			node.value = successor.value;

			String[] dummy = new String[1];
			size++;
			node.right = remove(node.right, successor.key, dummy);
		}

		return balance(node);
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

	/// ////////////////////////////////////////////////////////////
	/// ///               Mandatory Methods                  ///////
	/// ////////////////////////////////////////////////////////////

	@Override
	public String toString() {
		if (root == null) {
			return "{}";
		}
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
		return oldValue[0];
	}

	@Override
	public String remove(Object key) {
		if (!(key instanceof Integer) || root == null) {
			return null;
		}

		String[] removedValue = new String[1];
		root = remove(root, (Integer) key, removedValue);
		return removedValue[0];
	}

	@Override
	public String get(Object key) {
		if (!(key instanceof Integer)) {
			return null;
		}

		Node current = root;
		Integer targetKey = (Integer) key;
		while (current != null) {
			int cmp = targetKey.compareTo(current.key);
			if (cmp < 0) {
				current = current.left;
			} else if (cmp > 0) {
				current = current.right;
			} else {
				return current.value;
			}
		}
		return null;
	}

	@Override
	public boolean containsKey(Object key) {
		return get(key) != null;
	}

	/// ////////////////////////////////////////////////////////
	/// ///               Unused Methods                 ///////
	/// ////////////////////////////////////////////////////////

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

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
