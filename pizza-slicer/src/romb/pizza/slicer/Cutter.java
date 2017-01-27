package romb.pizza.slicer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Slice {
	int R1, R2, C1, C2;

	public Slice(int r1, int c1, int r2, int c2) {
		R1 = r1;
		R2 = r2;
		C1 = c1;
		C2 = c2;
	}

	public int getSize() {
		return difR() * difC();
	}

	public int difC() {
		return C2 - C1 + 1;
	}

	public int difR() {
		return R2 - R1 + 1;
	}

	@Override
	public String toString() {
		return R1 + " " + C1 + " " + R2 + " " + C2;
	}
}

public class Cutter {
	static final String MUSHROOM = "M";
	static final String TOMATO = "T";
	static final String SLICED = "#";
	static int R, C, L, H;
	static String[][] pizza;
	static List<String[][]> pizzas = new ArrayList<String[][]>();
	static Map<ArrayList<Slice>, Long> standings = new HashMap<>();
	static String strategicIngredient = MUSHROOM;

	public static void main(String[] args) {
		loadData(args[0]);
		createPizzas();
		for (String[][] pizza : pizzas) {
			ArrayList<Slice> slices = newSlicing(pizza);
			long score = calculateScore(pizza);
			standings.put(slices, new Long(score));
		}
		// for (String[][] pizza : pizzas) {
		// writePizza(pizza);
		// }
		writeData();
	}

	private static void createPizzas() {
		pizzas.add(pizza);
		String[][] oneHorizontalPizza = new String[R][C];
		for (int i = 0; i < R / 2 + 1; i++) {
			for (int j = 0; j < C; j++) {
				oneHorizontalPizza[R - (i + 1)][j] = pizza[i][j];
				oneHorizontalPizza[i][j] = pizza[R - (i + 1)][j];
			}
		}
		pizzas.add(oneHorizontalPizza);
		String[][] oneRotatedPizza = new String[C][R];
		for (int i = 0; i < R; ++i) {
			for (int j = 0; j < C; ++j) {
				oneRotatedPizza[j][R - 1 - i] = pizza[i][j];
			}
		}
		pizzas.add(oneRotatedPizza);
		String[][] twoHorizontalPizza = new String[C][R];
		for (int i = 0; i < C / 2 + 1; i++) {
			for (int j = 0; j < R; j++) {
				twoHorizontalPizza[C - (i + 1)][j] = oneRotatedPizza[i][j];
				twoHorizontalPizza[i][j] = oneRotatedPizza[C - (i + 1)][j];
			}
		}
		pizzas.add(twoHorizontalPizza);
		String[][] twoRotatedPizza = new String[R][C];
		for (int i = 0; i < C; ++i) {
			for (int j = 0; j < R; ++j) {
				twoRotatedPizza[j][C - 1 - i] = oneRotatedPizza[i][j];
			}
		}
		pizzas.add(twoRotatedPizza);
		String[][] threeHorizontalPizza = new String[R][C];
		for (int i = 0; i < R / 2 + 1; i++) {
			for (int j = 0; j < C; j++) {
				threeHorizontalPizza[R - (i + 1)][j] = twoRotatedPizza[i][j];
				threeHorizontalPizza[i][j] = twoRotatedPizza[R - (i + 1)][j];
			}
		}
		pizzas.add(threeHorizontalPizza);
		String[][] threeRotatedPizza = new String[C][R];
		for (int i = 0; i < R; ++i) {
			for (int j = 0; j < C; ++j) {
				threeRotatedPizza[j][R - 1 - i] = twoRotatedPizza[i][j];
			}
		}
		pizzas.add(threeRotatedPizza);
		String[][] fourHorizontalPizza = new String[C][R];
		for (int i = 0; i < C / 2 + 1; i++) {
			for (int j = 0; j < R; j++) {
				fourHorizontalPizza[C - (i + 1)][j] = threeRotatedPizza[i][j];
				fourHorizontalPizza[i][j] = threeRotatedPizza[C - (i + 1)][j];
			}
		}
		pizzas.add(fourHorizontalPizza);
	}

	private static long calculateScore(String[][] variantPizza) {
		long lengthRow = variantPizza.length;
		long lengthColumn = variantPizza[0].length;
		long maxScore = lengthColumn * lengthRow;
		long remainingElements = 0;
		for (int i = 0; i < lengthRow; ++i) {
			for (int j = 0; j < lengthColumn; ++j) {
				if (MUSHROOM.equals(variantPizza[i][j])) {
					remainingElements++;
				}
				if (TOMATO.equals(variantPizza[i][j])) {
					remainingElements++;
				}
			}
		}
		return maxScore - remainingElements;
	}

	@SuppressWarnings("unused")
	private static void writePizza(String[][] variantPizza) {
		// The name of the file to open.
		String fileName = "pizzaFiled.txt";
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
			int indexRow = variantPizza.length;
			int indexColumn = variantPizza[0].length;
			for (int i = 0; i < indexRow; i++) {
				for (int j = 0; j < indexColumn; j++) {
					bufferedWriter.write(String.format("%s", variantPizza[i][j]));
					System.out.print(variantPizza[i][j] + " ");
				}
				bufferedWriter.write(String.format("\n"));
				System.out.println();
			}
			System.out.println("-----------------------");
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}

	}

	private static void writeData() {
		// The name of the file to open.
		String fileName = "commands.txt";

		long maxScore = Long.MIN_VALUE;
		ArrayList<Slice> slices = null;
		for (ArrayList<Slice> slice : standings.keySet()) {
			if (standings.get(slice) >= maxScore) {
				maxScore = standings.get(slice);
				slices = slice;
			}
		}

		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {

			bufferedWriter.write(String.format("%d\n", slices.size()));

			System.out.println(slices.size());
			for (int i = 0; i < slices.size(); i++) {
				bufferedWriter.write(String.format("%s\n", slices.get(i).toString()));
				System.out.println(slices.get(i).toString());
			}
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}

	}

	private static ArrayList<Slice> newSlicing(String[][] variantPizza) {
		ArrayList<Slice> slices = new ArrayList<>();
		calculateStrategicIngredient();
		for (int i = 0; i < R; i++) {
			for (int j = 0; j < C; j++) {
				Slice nextSlice = getNextValidSlice(i, j);
				if (nextSlice != null) {
					slice(nextSlice);
					slices.add(nextSlice);
				}
			}
		}
		inflateSlices(slices);
		return slices;
	}

	private static void inflateSlices(ArrayList<Slice> slices) {
		slices.sort((s1, s2) -> -s1.getSize());
		for (Slice slice : slices) {
			inflate(slice);
		}
	}

	private static void inflate(Slice slice) {
		boolean inflated = false;
		do {
			inflated = false;
			if (slice.R2 - slice.R1 > slice.C1 - slice.C2) {
				if (canInflateR1(slice)) {
					inflateR1(slice);
					inflated = true;
				} else if (canInflateR2(slice)) {
					inflateR2(slice);
					inflated = true;
				} else if (canInflateC1(slice)) {
					inflateC1(slice);
					inflated = true;
				} else if (canInflateC2(slice)) {
					inflateC2(slice);
					inflated = true;
				}
			} else {
				if (canInflateC1(slice)) {
					inflateC1(slice);
					inflated = true;
				} else if (canInflateC2(slice)) {
					inflateC2(slice);
					inflated = true;
				} else if (canInflateR1(slice)) {
					inflateR1(slice);
					inflated = true;
				} else if (canInflateR1(slice)) {
					inflateR2(slice);
					inflated = true;
				}
			}

		} while (inflated);
	}

	private static void inflateC2(Slice slice) {
		slice.C2++;
		slice(slice);
	}

	private static boolean canInflateC2(Slice slice) {
		if (slice.C2 + 1 < C && slice.getSize() + slice.difR() <= H) {
			for (int i = slice.R1; i <= slice.R2; i++) {
				if (SLICED.equals(pizza[i][slice.C2 + 1])) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	private static void inflateC1(Slice slice) {
		slice.C1--;
		slice(slice);
	}

	private static boolean canInflateC1(Slice slice) {
		if (slice.C1 - 1 >= 0 && slice.getSize() + +slice.difR() <= H) {
			for (int i = slice.R1; i <= slice.R2; i++) {
				if (SLICED.equals(pizza[i][slice.C1 - 1])) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	private static void inflateR2(Slice slice) {
		slice.R2++;
		slice(slice);
	}

	private static boolean canInflateR2(Slice slice) {
		if (slice.R2 + 1 < R && slice.getSize() + slice.difC() <= H) {
			for (int j = slice.C1; j <= slice.C2; j++) {
				if (SLICED.equals(pizza[slice.R2 + 1][j])) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	private static void inflateR1(Slice slice) {
		slice.R1--;
		slice(slice);
	}

	private static boolean canInflateR1(Slice slice) {
		if (slice.R1 - 1 >= 0 && slice.getSize() + slice.difC() <= H) {
			for (int j = slice.C1; j <= slice.C2; j++) {
				if (SLICED.equals(pizza[slice.R1 - 1][j])) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	private static void calculateStrategicIngredient() {
		int countT = 0;
		int countM = 0;
		for (int i = 0; i < R; i++) {
			for (int j = 0; j < C; j++) {
				if (MUSHROOM.equals(pizza[i][j])) {
					countM++;
				}
				if (TOMATO.equals(pizza[i][j])) {
					countT++;
				}
			}
		}
		if (countT < countM) {
			strategicIngredient = TOMATO;
		}
	}

	private static void slice(Slice nextSlice) {
		for (int i = nextSlice.R1; i <= nextSlice.R2; i++)
			for (int j = nextSlice.C1; j <= nextSlice.C2; j++) {
				pizza[i][j] = SLICED;
			}
	}

	private static Slice getNextValidSlice(int row1, int column1) {
		List<Slice> slices = new ArrayList<Slice>();
		if (!SLICED.equals(pizza[row1][column1])) {
			int maxColumns = column1 + H < C ? H : C - column1 - 1;
			int maxRows = row1 + H < R ? H : R - row1 - 1;
			for (int column2 = maxColumns; column2 >= 0; column2--) {
				for (int row2 = maxRows; row2 >= 0; row2--) {
					Slice slice = new Slice(row1, column1, row1 + row2, column1 + column2);
					if (isValidSlice(slice)) {
						slices.add(slice);
					}
				}
			}
			return optimalSlice(slices);
		}
		return null;
	}

	private static Slice optimalSlice(List<Slice> validSlices) {
		if (validSlices.size() == 0)
			return null;
		List<Slice> slicesWithLeastStrategicIngredients = getSlicesWithLeastStrategicIngredients(validSlices);
		Slice bestSlice = smallestSlice(slicesWithLeastStrategicIngredients);
		return bestSlice;
	}

	private static Slice smallestSlice(List<Slice> slicesWithLeastStrategicIngredients) {
		Slice bestSlice = null;
		int minSlice = Integer.MAX_VALUE;
		for (Slice slice : slicesWithLeastStrategicIngredients) {
			if (slice.getSize() < minSlice) {
				bestSlice = slice;
				minSlice = slice.getSize();
			}
		}
		return bestSlice;
	}

	private static Slice bigestSlice(List<Slice> slicesWithLeastStrategicIngredients) {
		Slice bestSlice = null;
		int maxSlice = Integer.MAX_VALUE;
		for (Slice slice : slicesWithLeastStrategicIngredients) {
			if (slice.getSize() > maxSlice) {
				bestSlice = slice;
				maxSlice = slice.getSize();
			}
		}
		return bestSlice;
	}

	private static List<Slice> getSlicesWithLeastStrategicIngredients(List<Slice> validSlices) {
		List<Slice> strategicSlices = new ArrayList<Slice>();
		int minIngredients = Integer.MAX_VALUE;
		for (Slice slice : validSlices) {
			int strategicIngredientsCount = countIngredients(slice, strategicIngredient);
			if (strategicIngredientsCount < minIngredients) {
				strategicSlices.clear();
				minIngredients = strategicIngredientsCount;
			}
			if (strategicIngredientsCount == minIngredients) {
				strategicSlices.add(slice);
			}
		}
		return strategicSlices;
	}

	private static int countIngredients(Slice slice, String ingredient) {
		int countIngredient = 0;
		for (int i = slice.R1; i <= slice.R2; i++) {
			for (int j = slice.C1; j <= slice.C2; j++) {
				if (ingredient.equals(pizza[i][j])) {
					countIngredient++;
				}
			}
		}
		return countIngredient;
	}

	private static boolean isValidSlice(Slice slice) {
		if (slice.getSize() <= H && validIngredients(slice))
			return true;
		return false;
	}

	private static boolean validIngredients(Slice slice) {
		int countT = 0;
		int countM = 0;
		for (int i = slice.R1; i <= slice.R2; i++) {
			for (int j = slice.C1; j <= slice.C2; j++) {
				if (MUSHROOM.equals(pizza[i][j])) {
					countM++;
				}
				if (TOMATO.equals(pizza[i][j])) {
					countT++;
				}
				if (SLICED.equals(pizza[i][j])) {
					return false;
				}
			}
		}
		if (countT >= L && countM >= L) {
			return true;
		}
		return false;
	}

	static void loadData(String fileName) {
		// This will reference one line at a time
		String line = null;

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
			if ((line = bufferedReader.readLine()) != null) {
				String[] rowCol = line.split(" ");
				R = Integer.parseInt(rowCol[0]);
				C = Integer.parseInt(rowCol[1]);
				L = Integer.parseInt(rowCol[2]);
				H = Integer.parseInt(rowCol[3]);
			}
			pizza = new String[R][C];

			for (int i = 0; i < R; i++) {
				if ((line = bufferedReader.readLine()) != null) {
					for (int j = 0; j < C; j++) {
						pizza[i][j] = String.valueOf(line.charAt(j));
					}
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}
}
