package XYZFileControl.First;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;

public class CompareXYZ_JTS {
	static public double CheckDistance = 3.0;
	public static void main(String[] args) {
		// PointFile을 작은 파일로 한다.
		String pointFile = "/home/iswook/PointList/3. 5000_SUPPRESS_SO/2013_UTM_2.5.xyz";
		String pointFile2 = "/home/iswook/PointList/7. 25000납품원도수심/2013_25000.xyz";
		String NewFileName = "/home/iswook/PointList/2013_5000-25000납품_비교_2.5.xyz";

		// 베이스 좌표를 가져온다.
		
		System.out.println("Loading : " + pointFile);
		ArrayList<Point3D>  ptList = LoadPoint(pointFile);
		System.out.println(pointFile + "\t Load Completed");
		System.out.println("\n");

		System.out.println("Loading : " + pointFile2);
		STRtree spIndex = CreateSPIndex(pointFile2);
		System.out.println("\n"+pointFile + "\t Load Completed " + spIndex.size());
	
		ComparePoint(ptList, spIndex, NewFileName);

		System.out.println("Points : " + ptList.size());
	}
	
	private static void ComparePoint(ArrayList<Point3D> ptList, STRtree spIndex, String newFileName) {
		FileWriter out = null;
		BufferedWriter writer = null;
		int cntLine = 0;
		try {
			out = new FileWriter(newFileName);
			writer = new BufferedWriter(out);

			Iterator<Point3D> pt3dIter1 = ptList.iterator();
			while (pt3dIter1.hasNext()) {
				cntLine ++;
				Point3D pt1 = pt3dIter1.next();
				Envelope e = new Envelope(pt1.GetX() - CheckDistance, pt1.GetX() + CheckDistance,
						pt1.GetY() - CheckDistance, pt1.GetY() + CheckDistance);
				@SuppressWarnings("unchecked")
				List<Point3D> list = spIndex.query(e);
				Iterator<Point3D> ptIter = list.iterator();
				while (ptIter.hasNext()) {
					Point3D pt2 = ptIter.next();
					double distance = pt1.GetPoint2D().distance(pt2.GetPoint2D());
					if (distance <= CheckDistance) {
						System.out.println("CHeck cnt : " + (cntLine));
						writer.write(pt1.GetSourceString() + "\t" + pt2.GetSourceString() + "\t" + String.format("%.2f", distance));
						writer.newLine();
						break;
					}
				}

				if (cntLine % 1000000 == 0) {
					System.out.println("cnt : " + (cntLine));
				}
			}
			System.out.println("Lines : " + cntLine);
			System.out.println("Compare Completed!!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ;
	}

	
	private static STRtree CreateSPIndex(String pointFile) {
		STRtree spIndex = new STRtree();
		FileReader in = null;
		BufferedReader reader = null;
		int cntLine = 0;
		double minX = 999999.0;
		double minY = 9999999.0;
		double maxX = 0;
		double maxY = 0;
		int dupcnt = 0;
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
					System.out.println("cnt : " + (cntLine));// + " \t pt3D : " + utm.toDMSString());
				
				if (pt3d != null) {
					minX = Math.min(minX, pt3d.GetX());
					minY = Math.min(minY, pt3d.GetY());
					maxX = Math.max(maxX, pt3d.GetX());
					maxY = Math.max(maxY, pt3d.GetY());
					Coordinate pt = new Coordinate(pt3d.GetX(), pt3d.GetY());
					spIndex.insert(new Envelope(pt), pt3d);
				} else {
					System.out.println("Err : " + str);
				}
			}
			System.out.println("minX : " + minX);
			System.out.println("minY : " + minY);
			System.out.println("maxX : " + maxX);
			System.out.println("maxY : " + maxY);
			System.out.println("Lines : " + cntLine);
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
		
		return spIndex;
	}
	private static ArrayList<Point3D> LoadPoint(String pointFile) {
		ArrayList<Point3D> pointList = new ArrayList<Point3D>();
		FileReader in = null;
		BufferedReader reader = null;
		int cntLine = 0;
		double minX = 999999.0;
		double minY = 9999999.0;
		double maxX = 0;
		double maxY = 0;
		int dupcnt = 0;
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
					System.out.println("cnt : " + (cntLine));// + " \t pt3D : " + utm.toDMSString());
				
				if (pt3d != null) {
					minX = Math.min(minX, pt3d.GetX());
					minY = Math.min(minY, pt3d.GetY());
					maxX = Math.max(maxX, pt3d.GetX());
					maxY = Math.max(maxY, pt3d.GetY());
					pointList.add(pt3d);
				} else {
					System.out.println("Err : " + str);
				}
			}
			System.out.println("minX : " + minX);
			System.out.println("minY : " + minY);
			System.out.println("maxX : " + maxX);
			System.out.println("maxY : " + maxY);
			System.out.println("Lines : " + cntLine);
			System.out.println("List.size : " + pointList.size());
			
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
		
		return pointList;
	}
}
