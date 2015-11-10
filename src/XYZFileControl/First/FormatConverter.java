package XYZFileControl.First;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FormatConverter {
	public static void main(String[] args) {
		String pointFile = "/home/iswook/PointList/2012_연평도부근_싱글빔_위경도.xyz";
		String outPutFile = "/home/iswook/PointList/2012_연평도부근_싱글빔_위경도_정리_UTM.xyz";
		DoFormatConverter(pointFile, outPutFile);
	}
	
	private static void DoFormatConverter(String pointFile, String outPutFile) {
		FileReader in = null;
		BufferedReader reader = null;
		FileWriter out = null;
		BufferedWriter writer = null;
		int cntLine = 0;
		try {
			out = new FileWriter(outPutFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer = new BufferedWriter(out);		
		
		try {
			in = new FileReader(pointFile);
			reader = new BufferedReader(in);
			String str;
			while (true) {
				str = reader.readLine();
				if (str == null) 
					break;
				str = str.trim();

				String[] token = str.split(" ");
				if (token.length == 9) {
					//37 29 59.1805 N 125 51 46.4266 E 4.63
					if (token[3].equals("N") && token[7].equals("E")) {
						str = token[0] + "-" + token[1] + "-" + token[2] + token[3] + " "
								+ token[4] + "-" + token[5] + "-" + token[6] + token[7] + " "
								+ token[8];
						
					}
				} else {
					str = str.replace("C:\\SJJEONG\\2", "");
					str = str.replace("C:\\2012\\B\\20", "");
					str = str.replace("BACK", "");
					str = str.replace("            ", " ");
					str = str.replace("     ", " ");
					str = str.replace("   ", " ");
				}
				cntLine++ ;

				Point3D pt3D = Point3D.Create(str);
				if (pt3D == null) {
					System.out.println("Convert ERR : " + (token.length) + " : "+str);
					continue;
				}
				
				if (cntLine % 1000000 == 0) {
					System.out.println("cnt : " + (cntLine));
				}
				str = pt3D.toString();
				writer.write(str);
				writer.newLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			writer.flush();
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("@@ Completed CntLine : " + cntLine);
	}
}
