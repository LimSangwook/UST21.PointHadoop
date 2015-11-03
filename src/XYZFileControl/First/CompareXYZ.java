package XYZFileControl.First;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class CompareXYZ {
	public static void main(String[] args) {
		// PointFile을 작은 파일로 한다.
		String pointFile = "/home/iswook/PointList/25000납품_정리.xyz";
		String pointFile2 = "/home/iswook/PointList/3.5000_SUPPRESS_SO/ALL_LL.xyz";
		String NewFileName = "/home/iswook/PointList/5000-25000납품_비교.xyz";

		// 베이스 좌표를 가져온다.
		ArrayList<Point3D>  ptList = LoadPoint(pointFile);
		System.out.println("\n"+pointFile + "\t Load Completed");
		 
		ComparePoint(pointFile2, ptList, NewFileName);

		System.out.println("Points : " + ptList.size());
	}
	
	private static void ComparePoint(String pointFile, ArrayList<Point3D> ptList, String newFileName) {
		
		FileReader in = null;
		BufferedReader reader = null;
		int cntLine = 0;
		int errcnt = 0;

		ArrayList<Point3D> minPTList = new ArrayList<Point3D>();
		for (int i = 0 ; i < ptList.size() ; i++) minPTList.add(null);
		
		try {
			in = new FileReader(pointFile);
			reader = new BufferedReader(in);
			String str;
			Point3D nowPT3d = null;
			while (true) {
				str = reader.readLine();
				if (str == null) 
					break;
				cntLine ++;
				nowPT3d = Point3D.Create(str);
				if (cntLine % 1000 == 0) {
					System.out.println("cnt : " + (cntLine));
				}
				
				if (nowPT3d != null) {
					Iterator<Point3D> iter = ptList.iterator();
					int idx = 0;
					while (iter.hasNext()) {
						Point3D pt = iter.next();
						Point3D oldPT = minPTList.get(idx);
						if (oldPT == null) {
							minPTList.set(idx, nowPT3d);
						} else {
							Point3D minPT3d = minPTList.get(idx);
							double oldDistance = pt.GetPoint2D().distance(minPT3d.GetPoint2D());
							double nowDistance = pt.GetPoint2D().distance(nowPT3d.GetPoint2D());
							if (nowDistance < oldDistance) {
								minPTList.set(idx, nowPT3d);
							}
						}
						idx++;
					}
				} else {
					System.out.println("Err2 : " + str);
				}
			}
			System.out.println("Lines : " + cntLine);
			System.out.println("errcnt : " + errcnt);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter out = null;
		BufferedWriter writer = null;
		try {
			out = new FileWriter(newFileName);
			writer = new BufferedWriter(out);
			System.out.println("OutPutLines : " + ptList.size() + " : " + minPTList.size());
			for (int i = 0 ; i < ptList.size() ; i++) {
				Point3D pt3D = ptList.get(i);
				Point3D minPT3d = minPTList.get(i);
				double distance = pt3D.GetPoint2D().distance(minPT3d.GetPoint2D());
				writer.write(pt3D.GetSourceString() + "\t" + minPT3d.GetSourceString() + "\t" + String.format("%.2f",distance));
				writer.newLine();
			}
		} catch (IOException e) {
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
