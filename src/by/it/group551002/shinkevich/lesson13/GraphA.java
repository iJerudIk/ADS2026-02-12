package by.it.group551002.shinkevich.lesson13;

import java.util.*;

public class GraphA {

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
		Map<String, Integer> inDegree = new HashMap<>();
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

		for (String v : allVertices) {
			inDegree.put(v, 0);
		}
		for (String u : graph.keySet()) {
			for (String v : graph.get(u)) {
				inDegree.put(v, inDegree.get(v) + 1);
			}
		}

		PriorityQueue<String> pq = new PriorityQueue<>();
		for (String v : allVertices) {
			if (inDegree.get(v) == 0) {
				pq.add(v);
			}
		}

		List<String> result = new ArrayList<>();
		while (!pq.isEmpty()) {
			String curr = pq.poll();
			result.add(curr);

			for (String neighbor : graph.get(curr)) {
				inDegree.put(neighbor, inDegree.get(neighbor) - 1);
				if (inDegree.get(neighbor) == 0) {
					pq.add(neighbor);
				}
			}
		}

		System.out.println(String.join(" ", result));
	}

}