package XYZFileControl.First;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConvertDMS2D {
	public static void main(String[] args) {
		// 도-분-초 로되어있는것을 도+소숫점으로 표현한다.
		String pointFile = "/home/iswook/PointList/25000납품_정리.xyz";
		String saveFile = "/home/iswook/PointList/25000납품_정리"+"_LatLon.xyz";

		DoConvertDMS2D(pointFile, saveFile);
	}

	private static void DoConvertDMS2D(String pointFile, String saveFile) {
		FileReader in = null;
		BufferedReader reader = null;
		int cntLine = 0;
		
		FileWriter out = null;
		BufferedWriter writer = null;
		try {
			out = new FileWriter(saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer = new BufferedWriter(out);
		
		try {
			in = new FileReader(pointFile);
			reader = new BufferedReader(in);
			String str;
			Point3D pt3d = null;
			while (true) {
				str = reader.readLine();
				if (str == null) 
					break;
				cntLine ++;
				pt3d = Point3D.Create(str);
				if (cntLine % 1000000 == 0) {
					System.out.println("cnt : " + (cntLine));
					writer.flush();
				}
				
				if (pt3d != null) {
					writer.write(String.format("%.9f", pt3d.GetX()) + " " + String.format("%.9f", pt3d.GetY()) + " " + String.format("%.3f", pt3d.GetZ()));
					writer.newLine();

				} else {
					System.out.println("Err : " + str);
				}
			}
			System.out.println("Lines : " + cntLine);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				writer.flush();
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
