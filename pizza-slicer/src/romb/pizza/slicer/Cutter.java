package romb.pizza.slicer;

import java.io.*;
import java.util.ArrayList;

public class Cutter {
	static final String hash = "#";
	static final String point = ".";
	static final String PAINT_SQUARE =  "PAINT_SQUARE";
	static final String PAINT_LINE =  "PAINT_LINE";
	static final String ERASE_CELL =  "ERASE_CELL";
	static int N, M;
	static String[][] picture;
	static ArrayList<String> commands = new ArrayList<String>();
	
	public static void main(String[] args) {
		LoadData(args[0]);
		paint();
		WriteData();
	}

	private static void WriteData() {
		 // The name of the file to open.
        String fileName = "commands.txt";

        try {
            // Assume default encoding.
            FileWriter fileWriter =
                new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write(String.format("%d\n", commands.size()));
            for(int i=0; i<commands.size(); i++){
            	bufferedWriter.write(String.format("%s\n", commands.get(i)));
            }

            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
		
	}

	private static void paint() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if(picture[i][j].equals(hash)){
					commands.add(String.format("%s %d %d %d", "PAINT_SQUARE", i, j, 0));
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
				N = Integer.parseInt(rowCol[0]);
				M = Integer.parseInt(rowCol[1]);
			}
			picture = new String[N][M];

			for (int i = 0; i < N; i++) {
				if ((line = bufferedReader.readLine()) != null) {
					for (int j = 0; j < M; j++) {
						picture[i][j] = String.valueOf(line.charAt(j));
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
