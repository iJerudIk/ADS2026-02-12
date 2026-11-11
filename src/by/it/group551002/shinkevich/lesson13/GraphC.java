package by.it.group551002.shinkevich.lesson13;

import java.util.*;

public class GraphC {

	private static int timer = 0;
	private static final Map<String, Integer> tin = new HashMap<>();
	private static final Map<String, Integer> low = new HashMap<>();
	private static final Deque<String> stack = new ArrayDeque<>();
	private static final Set<String> onStack = new HashSet<>();
	private static final List<List<String>> sccList = new ArrayList<>();

	// Tarjan's
	private static void tarjanDFS(String u, Map<String, Set<String>> graph) {
		tin.put(u, ++timer);
		low.put(u, timer);
		stack.push(u);
		onStack.add(u);

		for (String v : graph.getOrDefault(u, Collections.emptySet())) {
			if (!tin.containsKey(v)) {
				tarjanDFS(v, graph);
				low.put(u, Math.min(low.get(u), low.get(v)));
			} else if (onStack.contains(v)) {
				low.put(u, Math.min(low.get(u), tin.get(v)));
			}
		}

		if (low.get(u).equals(tin.get(u))) {
			List<String> scc = new ArrayList<>();
			while (true) {
				String v = stack.pop();
				onStack.remove(v);
				scc.add(v);
				if (v.equals(u)) break;
			}
			sccList.add(scc);
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		if (!scanner.hasNextLine()) {
			return;
		}
		String input = scanner.nextLine().trim();
		if (input.isEmpty()) {
			return;
		}

		Map<String, Set<String>> graph = new HashMap<>();
		Set<String> allVertices = new HashSet<>();

		String[] edges = input.split(",\\s*");
		for (String edge : edges) {
			if (edge.isEmpty()) continue;
			String[] parts = edge.split("\\s*->\\s*");
			String from = parts[0].trim();
			String to = parts[1].trim();

			graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
			graph.putIfAbsent(to, new HashSet<>());

			allVertices.add(from);
			allVertices.add(to);
		}

		// Find SCCs
		List<String> sortedVertices = new ArrayList<>(allVertices);
		Collections.sort(sortedVertices);
		for (String v : sortedVertices) {
			if (!tin.containsKey(v)) {
				tarjanDFS(v, graph);
			}
		}

		// Format each component
		Map<String, String> vertexToComp = new HashMap<>();
		List<String> compNames = new ArrayList<>();

		for (List<String> scc : sccList) {
			scc.sort(String::compareTo);
			String compName = String.join("", scc);
			compNames.add(compName);
			for (String vertex : scc) {
				vertexToComp.put(vertex, compName);
			}
		}

		// Build DAG
		Map<String, Set<String>> condGraph = new HashMap<>();
		Map<String, Integer> inDegree = new HashMap<>();

		for (String comp : compNames) {
			condGraph.put(comp, new HashSet<>());
			inDegree.put(comp, 0);
		}

		for (String u : graph.keySet()) {
			for (String v : graph.get(u)) {
				String compU = vertexToComp.get(u);
				String compV = vertexToComp.get(v);
				if (!compU.equals(compV)) {
					if (condGraph.get(compU).add(compV)) {
						inDegree.put(compV, inDegree.get(compV) + 1);
					}
				}
			}
		}

		// Sort on DAG
		PriorityQueue<String> pq = new PriorityQueue<>();
		for (String comp : compNames) {
			if (inDegree.get(comp) == 0) {
				pq.add(comp);
			}
		}

		while (!pq.isEmpty()) {
			String curr = pq.poll();
			System.out.println(curr);

			for (String neighbor : condGraph.get(curr)) {
				inDegree.put(neighbor, inDegree.get(neighbor) - 1);
				if (inDegree.get(neighbor) == 0) {
					pq.add(neighbor);
				}
			}
		}
	}
}