package by.it.group551002.shinkevich.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PointsA {

	private static class Point {
		int x, y, z;

		Point(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		double distanceTo(Point other) {
			long dx = this.x - other.x;
			long dy = this.y - other.y;
			long dz = this.z - other.z;
			return Math.sqrt(dx * dx + dy * dy + dz * dz);
		}
	}


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
		if (!scanner.hasNextDouble()) {
			return;
		}

		double distance = scanner.nextDouble();
		int count = scanner.nextInt();

		Point[] points = new Point[count];
		for (int i = 0; i < count; i++) {
			points[i] = new Point(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
		}

		DSU dsu = new DSU(count);

		for (int i = 0; i < count; i++) {
			for (int j = i + 1; j < count; j++) {
				if (points[i].distanceTo(points[j]) < distance) {
					dsu.union(i, j);
				}
			}
		}

		List<Integer> clusterSizes = new ArrayList<>();
		for (int i = 0; i < count; i++) {
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