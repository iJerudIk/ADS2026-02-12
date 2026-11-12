package by.it.group551002.shinkevich.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

	private static int find(int i, int[] parent) {
		if (parent[i] == i) {
			return i;
		}
		return parent[i] = find(parent[i], parent);
	}

	private static void union(int i, int j, int[] parent, int[] size) {
		int rootI = find(i, parent);
		int rootJ = find(j, parent);
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

	private static void solveHanoi(
			int n, int from, int to, int aux,
			int[] heights,
			int[] parent, int[] size,
			int[] firstOfMaxHeight,
			int[] stepCounter) {

		if (n == 0) {
			return;
		}

		solveHanoi(n - 1, from, aux, to, heights, parent, size, firstOfMaxHeight, stepCounter);

		heights[from]--;
		heights[to]++;

		int currentStep = stepCounter[0]++;

		int maxH = heights[0];
		if (heights[1] > maxH) maxH = heights[1];
		if (heights[2] > maxH) maxH = heights[2];

		if (firstOfMaxHeight[maxH] == -1) {
			firstOfMaxHeight[maxH] = currentStep;
		} else {
			union(firstOfMaxHeight[maxH], currentStep, parent, size);
		}

		solveHanoi(n - 1, aux, to, from, heights, parent, size, firstOfMaxHeight, stepCounter);
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		if (!scanner.hasNextInt()) {
			return;
		}
		int n = scanner.nextInt();

		int totalSteps = (1 << n) - 1;

		int[] parent = new int[totalSteps];
		int[] size = new int[totalSteps];
		for (int i = 0; i < totalSteps; i++) {
			parent[i] = i;
			size[i] = 1;
		}

		int[] firstOfMaxHeight = new int[n + 1];
		for (int i = 0; i <= n; i++) {
			firstOfMaxHeight[i] = -1;
		}

		int[] heights = new int[3];
		heights[0] = n;
		heights[1] = 0;
		heights[2] = 0;

		int[] stepCounter = new int[]{0};

		solveHanoi(n, 0, 1, 2, heights, parent, size, firstOfMaxHeight, stepCounter);

		int rootCount = 0;
		for (int i = 0; i < totalSteps; i++) {
			if (parent[i] == i) {
				rootCount++;
			}
		}

		int[] result = new int[rootCount];
		int idx = 0;
		for (int i = 0; i < totalSteps; i++) {
			if (parent[i] == i) {
				result[idx++] = size[i];
			}
		}

		for (int i = 0; i < result.length - 1; i++) {
			for (int j = 0; j < result.length - 1 - i; j++) {
				if (result[j] > result[j + 1]) {
					int temp = result[j];
					result[j] = result[j + 1];
					result[j + 1] = temp;
				}
			}
		}


		for (int i = 0; i < result.length; i++) {
			System.out.print(result[i] + (i == result.length - 1 ? "" : " "));
		}
		System.out.println();
	}
}