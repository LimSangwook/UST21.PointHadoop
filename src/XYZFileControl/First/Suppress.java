package XYZFileControl.First;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class Suppress {
	public static void  main(String[] args) {
		float rangeMeter = 2.0f;
		String inputFile = "/home/iswook/PointList/강화도인근.xyz";
		String outputFile = "/home/iswook/PointList/강화도인근"+"_"+rangeMeter+"m.xyz";
		
		HashMap<Long, Point3D> map = DoSuppress(inputFile, outputFile, rangeMeter);
		WriteFile(outputFile, map);
		System.out.println("cnt : " + map.size());
	}

	private static void WriteFile(String outputFile, HashMap<Long, Point3D> map) {
		FileWriter out = null;
		BufferedWriter writer = null;
		try {
			out = new FileWriter(outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer = new BufferedWriter(out);
		Set<Long> keySet = map.keySet();
		for (Long key : keySet) {
			try {
				writer.write(map.get(key).toString());
				writer.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		try {
			writer.flush();
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private static HashMap<Long, Point3D> DoSuppress(String inputFile, String outputFile, float rangeMeter) {

		HashMap<Long, Point3D> point3DMap = new HashMap<Long, Point3D>(); 
		FileReader in = null;
		BufferedReader reader = null;
		int cntLine = 0;
		double minX = 999999.0;
		double minY = 9999999.0;
		double maxX = 0;
		double maxY = 0;

		try {
			in = new FileReader(inputFile);
			reader = new BufferedReader(in);
			String str;
			Point3D pt3d = null;
			while (true) {
				str = reader.readLine();
				if (cntLine % 1000000 == 0) {
					System.out.print("cnt : " + (cntLine));
					System.out.println("\t mapCnt : " + point3DMap.size());
					System.gc();
				}
				
				if (str == null) 
					break;
				cntLine ++;
				pt3d = Point3D.Create(str);
				if (pt3d != null) {
					minX = Math.min(minX, pt3d.GetX());
					minY = Math.min(minY, pt3d.GetY());
					maxX = Math.max(maxX, pt3d.GetX());
					maxY = Math.max(maxY, pt3d.GetY());
					Long key = GetKey(pt3d, rangeMeter);
					if (point3DMap.containsKey(key) == true) {
						if (point3DMap.get(key).GetZ() >= pt3d.GetZ()) {
							point3DMap.put(key, pt3d);
						}
					} else {
						point3DMap.put(key, pt3d);
					}
				} else {
					System.out.println("null Point Str:"+str );
				}
			}
			System.out.println("minX : " + minX);
			System.out.println("minY : " + minY);
			System.out.println("maxX : " + maxX);
			System.out.println("maxY : " + maxY);
			System.out.println("Lines : " + cntLine);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return point3DMap;
	}

	private static Long GetKey(Point3D pt3d, float rangeMeter) {
		int x = (int)pt3d.GetX();
		int y = (int)pt3d.GetY();
		Long key =(int)(x/rangeMeter) * 1000000000L + (int)(y/rangeMeter);
		return key;
	}
}
