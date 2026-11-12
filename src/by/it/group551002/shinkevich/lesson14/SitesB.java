package by.it.group551002.shinkevich.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SitesB {

	private static class DSU {
		int[] parent;
		int[] size;

		DSU(int n) {
			parent = new int[n];
			size = new int[n];
			for (int i = 0; i < n; i++) {
				parent[i] = i;
				size[i] = 1;
			}
		}

		int find(int i) {
			if (parent[i] == i) {
				return i;
			}
			return parent[i] = find(parent[i]);
		}

		void union(int i, int j) {
			int rootI = find(i);
			int rootJ = find(j);
			if (rootI != rootJ) {
				if (size[rootI] < size[rootJ]) {
					parent[rootI] = rootJ;
					size[rootJ] += size[rootI];
				} else {
					parent[rootJ] = rootI;
					size[rootI] += size[rootJ];
				}
			}
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		List<String> links = new ArrayList<>();
		Map<String, Integer> siteToId = new HashMap<>();

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			if (line.equals("end")) {
				break;
			}
			if (!line.isEmpty()) {
				links.add(line);
				String[] parts = line.split("\\+");
				siteToId.putIfAbsent(parts[0], siteToId.size());
				siteToId.putIfAbsent(parts[1], siteToId.size());
			}
		}

		int n = siteToId.size();
		DSU dsu = new DSU(n);

		for (String link : links) {
			String[] parts = link.split("\\+");
			int id1 = siteToId.get(parts[0]);
			int id2 = siteToId.get(parts[1]);
			dsu.union(id1, id2);
		}

		List<Integer> clusterSizes = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			if (dsu.parent[i] == i) {
				clusterSizes.add(dsu.size[i]);
			}
		}

		clusterSizes.sort(Collections.reverseOrder());
		for (int i = 0; i < clusterSizes.size(); i++) {
			System.out.print(clusterSizes.get(i) + (i == clusterSizes.size() - 1 ? "" : " "));
		}
		System.out.println();
	}
}