package romb.pizza.slicer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	static ArrayList<Slice> slices = new ArrayList<>();
	static String strategicIngredient = MUSHROOM;

	public static void main(String[] args) {
		LoadData(args[0]);
		new_slicing();
		WriteData();

		WritePizza();
	}

	private static void WritePizza() {
		// The name of the file to open.
		String fileName = "pizzaFiled.txt";
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {

			for (int i = 0; i < R; i++) {
				for (int j = 0; j < C; j++) {
					bufferedWriter.write(String.format("%s", pizza[i][j]));
				}
				bufferedWriter.write(String.format("\n"));
			}

		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}

	}

	private static void WriteData() {
		// The name of the file to open.
		String fileName = "commands.txt";

		try {
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(fileName);

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Note that write() does not automatically
			// append a newline character.
			bufferedWriter.write(String.format("%d\n", slices.size()));
			System.out.println(slices.size());
			for (int i = 0; i < slices.size(); i++) {
				bufferedWriter.write(String.format("%s\n", slices.get(i).toString()));
				System.out.println(slices.get(i).toString());
			}

			// Always close files.
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}

	}

	private static void new_slicing() {
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

		inflateSlices();
	}

	private static void inflateSlices() {
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
		if (slice.C1 - 1 >= 0 && slice.getSize() + + slice.difR() <= H) {
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
		int maxSlice = -1;
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

	// private static void slicing() {
	// for (int i = 0; i < R; i++) {
	// for (int j = 0; j < C; j++) {
	// boolean haveM = false;
	// boolean haveT = false;
	// boolean isSliced = false;
	//
	// int limit = (i + H - 1) > R ? R : (i + H - 1);
	//
	// for (int ii = i; ii < limit; ++ii) {
	// if (MUSHROOM.equals(pizza[ii][j])) {
	// haveM = true;
	// }
	// if (TOMATO.equals(pizza[ii][j])) {
	// haveT = true;
	// }
	// if (SLICED.equals(pizza[ii][j])) {
	// isSliced = true;
	// }
	// }
	// if (!isSliced) {
	// if (haveT && haveM) {
	// for (int ii = i; ii < limit; ++ii) {
	// pizza[ii][j] = "#";
	// }
	// Slice s = new Slice(i, limit, j, j);
	// slices.add(s);
	// }
	// }
	// }
	// }
	// }

	static void LoadData(String fileName) {
		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

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

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}
}
