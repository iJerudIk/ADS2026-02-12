package by.it.group551002.shinkevich.lesson13;

import java.util.*;

public class GraphB {

	private static final int WHITE = 0; // Unvisited
	private static final int GRAY = 1;  // Visiting
	private static final int BLACK = 2; // Visited

	private static boolean hasCycle(String u, Map<String, Set<String>> graph, Map<String, Integer> color) {
		color.put(u, GRAY);

		for (String neighbor : graph.getOrDefault(u, Collections.emptySet())) {
			int neighborColor = color.getOrDefault(neighbor, WHITE);
			if (neighborColor == GRAY) {
				return true;
			}
			if (neighborColor == WHITE) {
				if (hasCycle(neighbor, graph, color)) {
					return true;
				}
			}
		}

		color.put(u, BLACK);
		return false;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		if (!scanner.hasNextLine()) {
			System.out.println("no");
			return;
		}
		String input = scanner.nextLine().trim();
		if (input.isEmpty()) {
			System.out.println("no");
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

		Map<String, Integer> color = new HashMap<>();
		for (String v : allVertices) {
			color.put(v, WHITE);
		}

		boolean cycleFound = false;
		for (String v : allVertices) {
			if (color.get(v) == WHITE) {
				if (hasCycle(v, graph, color)) {
					cycleFound = true;
					break;
				}
			}
		}

		System.out.println(cycleFound ? "yes" : "no");
	}
}