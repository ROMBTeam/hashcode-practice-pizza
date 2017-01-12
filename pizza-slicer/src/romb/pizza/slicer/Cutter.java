package romb.pizza.slicer;

import java.io.*;
import java.util.ArrayList;

class Slice {
	int R1, R2, C1, C2;

	public Slice(int r1, int c1, int r2, int c2) {
		R1 = r1;
		R2 = r2;
		C1 = c1;
		C2 = c2;
	}

	public int getSize() {
		int result = 0;
		int difR = R2 - R1 + 1;
		int difC = C2 - C1 + 1;
		result = difR * difC;
		return result;
	}

	@Override
	public String toString() {
		return R1 + " " + C1 + " " + R2 + " " + C2;
	}
}

public class Cutter {
	static final String MUSHROOM = "M";
	static final String TOMATO = "T";
	static int R, C, L, H;
	static int noOfSlices = 0;
	static String[][] pizza;
	static ArrayList<Slice> slices = new ArrayList<>();

	public static void main(String[] args) {
		LoadData(args[0]);
		slicing();
		WriteData();
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
			bufferedWriter.write(String.format("%d\n", noOfSlices));
			for (int i = 0; i < slices.size(); i++) {
				bufferedWriter.write(String.format("%s\n", slices.get(i)));
			}

			// Always close files.
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}

	}

	private static void slicing() {
		for (int i = 0; i < R; i++) {
			for (int j = 0; j < C; j++) {
				if (pizza[i][j].equals(MUSHROOM)) {

				}
			}
		}
	}

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
