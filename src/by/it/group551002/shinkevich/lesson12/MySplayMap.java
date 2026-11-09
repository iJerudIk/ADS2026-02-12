package by.it.group551002.shinkevich.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class MySplayMap implements NavigableMap<Integer, String> {

	private static class Node {
		Integer key;
		String value;
		Node left;
		Node right;
		Node parent;

		Node(Integer key, String value, Node parent) {
			this.key = key;
			this.value = value;
			this.parent = parent;
		}
	}

	private Node root;
	private int size = 0;

	private void rotateLeft(Node x) {
		Node y = x.right;
		if (y != null) {
			x.right = y.left;
			if (y.left != null) {
				y.left.parent = x;
			}

			y.parent = x.parent;
			if (x.parent == null) {
				root = y;
			} else if (x == x.parent.left) {
				x.parent.left = y;
			} else {
				x.parent.right = y;
			}

			y.left = x;
			x.parent = y;
		}
	}

	private void rotateRight(Node x) {
		Node y = x.left;
		if (y != null) {
			x.left = y.right;
			if (y.right != null) {
				y.right.parent = x;
			}

			y.parent = x.parent;
			if (x.parent == null) {
				root = y;
			} else if (x == x.parent.left) {
				x.parent.left = y;
			} else {
				x.parent.right = y;
			}

			y.right = x;
			x.parent = y;
		}
	}

	private void splay(Node x) {
		if (x == null) return;

		while (x.parent != null) {
			Node p = x.parent;
			Node g = p.parent;

			if (g == null) {
				// Zig step
				if (x == p.left) {
					rotateRight(p);
				} else {
					rotateLeft(p);
				}
			} else if ((x == p.left) == (p == g.left)) {
				// Zig-Zig step
				if (x == p.left) {
					rotateRight(g);
					rotateRight(p);
				} else {
					rotateLeft(g);
					rotateLeft(p);
				}
			} else {
				// Zig-Zag step
				if (x == p.left) {
					rotateRight(p);
					rotateLeft(g);
				} else {
					rotateLeft(p);
					rotateRight(g);
				}
			}
		}
		root = x;
	}

	private Node findNode(Integer key) {
		if (key == null || root == null) return null;

		Node curr = root;
		Node last = null;
		while (curr != null) {
			last = curr;
			int cmp = key.compareTo(curr.key);
			if (cmp < 0) {
				curr = curr.left;
			} else if (cmp > 0) {
				curr = curr.right;
			} else {
				splay(curr);
				return curr;
			}
		}

		if (last != null) {
			splay(last);
		}
		return null;
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
		if (key == null) throw new NullPointerException();

		if (root == null) {
			root = new Node(key, value, null);
			size++;
			return null;
		}

		Node curr = root;
		Node parent = null;
		int cmp = 0;

		while (curr != null) {
			parent = curr;
			cmp = key.compareTo(curr.key);
			if (cmp < 0) {
				curr = curr.left;
			} else if (cmp > 0) {
				curr = curr.right;
			} else {
				String oldValue = curr.value;
				curr.value = value;
				splay(curr);
				return oldValue;
			}
		}

		Node newNode = new Node(key, value, parent);
		if (cmp < 0) {
			parent.left = newNode;
		} else {
			parent.right = newNode;
		}

		splay(newNode);
		size++;
		return null;
	}

	@Override
	public String remove(Object key) {
		if (!(key instanceof Integer) || root == null) {
			return null;
		}

		Node node = findNode((Integer) key);
		if (node == null) {
			return null;
		}

		String oldValue = node.value;

		if (node.left == null) {
			root = node.right;
			if (root != null) root.parent = null;
		} else {
			Node rightSubtree = node.right;
			Node leftSubtree = node.left;
			leftSubtree.parent = null;

			Node maxLeft = leftSubtree;
			while (maxLeft.right != null) {
				maxLeft = maxLeft.right;
			}

			root = leftSubtree;
			splay(maxLeft);

			root.right = rightSubtree;
			if (rightSubtree != null) {
				rightSubtree.parent = root;
			}
		}

		size--;
		return oldValue;
	}

	@Override
	public String get(Object key) {
		if (!(key instanceof Integer)) return null;

		Node node = findNode((Integer) key);
		return node == null ? null : node.value;
	}

	@Override
	public boolean containsKey(Object key) {
		if (!(key instanceof Integer)) return false;
		return findNode((Integer) key) != null;
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
		Node curr = root;
		while (curr.left != null) {
			curr = curr.left;
		}
		splay(curr);
		return curr.key;
	}

	@Override
	public Integer lastKey() {
		if (root == null) throw new NoSuchElementException();
		Node curr = root;
		while (curr.right != null) {
			curr = curr.right;
		}
		splay(curr);
		return curr.key;
	}

	@Override
	public Integer lowerKey(Integer key) {
		if (key == null) throw new NullPointerException();
		Node curr = root;
		Node best = null;

		while (curr != null) {
			if (curr.key.compareTo(key) < 0) {
				best = curr;
				curr = curr.right;
			} else {
				curr = curr.left;
			}
		}

		if (best != null) splay(best);
		return best == null ? null : best.key;
	}

	@Override
	public Integer floorKey(Integer key) {
		if (key == null) throw new NullPointerException();
		Node curr = root;
		Node best = null;

		while (curr != null) {
			if (curr.key.compareTo(key) <= 0) {
				best = curr;
				curr = curr.right;
			} else {
				curr = curr.left;
			}
		}

		if (best != null) splay(best);
		return best == null ? null : best.key;
	}

	@Override
	public Integer ceilingKey(Integer key) {
		if (key == null) throw new NullPointerException();
		Node curr = root;
		Node best = null;

		while (curr != null) {
			if (curr.key.compareTo(key) >= 0) {
				best = curr;
				curr = curr.left;
			} else {
				curr = curr.right;
			}
		}

		if (best != null) splay(best);
		return best == null ? null : best.key;
	}

	@Override
	public Integer higherKey(Integer key) {
		if (key == null) throw new NullPointerException();
		Node curr = root;
		Node best = null;

		while (curr != null) {
			if (curr.key.compareTo(key) > 0) {
				best = curr;
				curr = curr.left;
			} else {
				curr = curr.right;
			}
		}

		if (best != null) splay(best);
		return best == null ? null : best.key;
	}

	@Override
	public NavigableMap<Integer, String> headMap(Integer toKey) {
		return headMap(toKey, false);
	}

	@Override
	public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
		if (toKey == null) throw new NullPointerException();
		MySplayMap subMap = new MySplayMap();
		fillHeadMap(root, toKey, inclusive, subMap);
		return subMap;
	}

	private void fillHeadMap(Node node, Integer toKey, boolean inclusive, MySplayMap subMap) {
		if (node == null) return;
		fillHeadMap(node.left, toKey, inclusive, subMap);
		int cmp = node.key.compareTo(toKey);
		if (cmp < 0 || (inclusive && cmp == 0)) {
			subMap.put(node.key, node.value);
			fillHeadMap(node.right, toKey, inclusive, subMap);
		}
	}

	@Override
	public NavigableMap<Integer, String> tailMap(Integer fromKey) {
		return tailMap(fromKey, true);
	}

	@Override
	public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
		if (fromKey == null) throw new NullPointerException();
		MySplayMap subMap = new MySplayMap();
		fillTailMap(root, fromKey, inclusive, subMap);
		return subMap;
	}

	private void fillTailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap subMap) {
		if (node == null) return;
		int cmp = node.key.compareTo(fromKey);
		if (cmp > 0 || (inclusive && cmp == 0)) {
			fillTailMap(node.left, fromKey, inclusive, subMap);
			subMap.put(node.key, node.value);
		}
		fillTailMap(node.right, fromKey, inclusive, subMap);
	}

	/// //////////////////////////////////////////////////////////
	/// ///               Unused  Methods                  ///////
	/// //////////////////////////////////////////////////////////

	@Override
	public Entry<Integer, String> lowerEntry(Integer key) {
		return null;
	}

	@Override
	public Entry<Integer, String> floorEntry(Integer key) {
		return null;
	}

	@Override
	public Entry<Integer, String> ceilingEntry(Integer key) {
		return null;
	}

	@Override
	public Entry<Integer, String> higherEntry(Integer key) {
		return null;
	}

	@Override
	public Entry<Integer, String> firstEntry() {
		return null;
	}

	@Override
	public Entry<Integer, String> lastEntry() {
		return null;
	}

	@Override
	public Entry<Integer, String> pollFirstEntry() {
		return null;
	}

	@Override
	public Entry<Integer, String> pollLastEntry() {
		return null;
	}

	@Override
	public NavigableMap<Integer, String> descendingMap() {
		return null;
	}

	@Override
	public NavigableSet<Integer> navigableKeySet() {
		return null;
	}

	@Override
	public NavigableSet<Integer> descendingKeySet() {
		return null;
	}

	@Override
	public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
		return null;
	}

	@Override
	public java.util.SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
		return null;
	}

	@Override
	public Comparator<? super Integer> comparator() {
		return null;
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

	@Override
	public void putAll(Map<? extends Integer, ? extends String> m) {
	}
}