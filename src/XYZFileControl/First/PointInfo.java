package XYZFileControl.First;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PointInfo {
	public static void main(String[] args) {
		String pointFile = "/home/iswook/PointList/3.5000_SUPPRESS_SO/ALL_LL.xyz";
		ShowPointInfo(pointFile);
	}
	
	private static void ShowPointInfo(String pointFile) {
		FileReader in = null;
		BufferedReader reader = null;
		int cntLine = 0;
		double minX = 999999.0;
		double minY = 9999999.0;
		double maxX = 0;
		double maxY = 0;
		int dupcnt = 0;
		int errcnt = 0;
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
				if (cntLine % 1000000 == 0 ) 
					System.out.println("cnt : " + (cntLine));
				
				if (pt3d != null) {
					minX = Math.min(minX, pt3d.GetX());
					minY = Math.min(minY, pt3d.GetY());
					maxX = Math.max(maxX, pt3d.GetX());
					maxY = Math.max(maxY, pt3d.GetY());
				} else {
					System.out.println("ERR LoadPoint : " + str);
					errcnt++;
				}
			}
			System.out.println("minX : " + minX);
			System.out.println("minY : " + minY);
			System.out.println("maxX : " + maxX);
			System.out.println("maxY : " + maxY);
			System.out.println("Lines : " + cntLine);
			System.out.println("errcnt : " + errcnt);
			System.out.println("dupcnt : " + dupcnt);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
