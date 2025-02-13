package taboo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LeadIn {
	static String csvFile = "D:/JAVA/学习/食堂/距离矩阵.csv";
	static BufferedReader br = null;
	static String line = "";
	static String cvsSplitBy = ",";
	static String[] a = {};
	static int index;
	static int[][] df = new int[17][17];
	public int[][] Lead() {
		try {
			br = new BufferedReader(new FileReader(csvFile));
			for (int i = 0; (line = br.readLine()) != null; i++) {
				a = line.split(cvsSplitBy);
				if (i != 0) {
					for (int j = 0; j < 17; j++) {
						index = Integer.valueOf(a[j + 1].toString());// string型转double型
						df[i - 1][j] = index;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return df;
	}
}
