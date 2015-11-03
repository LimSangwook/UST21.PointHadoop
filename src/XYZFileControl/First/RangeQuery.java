package XYZFileControl.First;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import gov.nasa.worldwind.geom.coords.UTMCoord;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
public class RangeQuery {
	public static void main(String []args) {
//		// 연도별파일을 25000 도엽으로 나누기;
//		GetYear25000Data(2013);
		
		// 영역 추출
		GetRangeData();
	}
	
	private static void GetRangeData() {
		String pointFile = "/home/iswook/PointList/1. 연도별 통합/2013_Point.xyz";
		String rangeFile = "/home/iswook/PointList/강화도인근.xyz";
		Path2D.Double rect25000 = Get25000Range("강화도 인근");
		ArrayList<Point3D> ptList = WritePointList(pointFile, rect25000, rangeFile);
		System.out.println("Points : " + ptList.size());
	}
	
	private static void GetYear25000Data(int YEAR) {
		HashMap<Integer, Path2D> rectList = null;
		String pointFile = "/home/iswook/PointList/1. 연도별 통합/2013_Point.xyz";
		String rangeFilePath = "/home/iswook/PointList/2. 25000도엽원본/" + Integer.toString(YEAR)+"_";
		
		SurveyAreaManager areaMge = new SurveyAreaManager();
		
		rectList = areaMge.GetRectList(YEAR);
		
		WritePointLists(pointFile, rectList, rangeFilePath);
	}
	
	private static void WritePointLists(String pointFile, HashMap<Integer, Path2D> rectList, String rangeFilePath) {
		FileReader in = null;
		BufferedReader reader = null;
		
		if (rectList == null) {
			System.out.println("rectList is NULL!!");
		}
		try {
			in = new FileReader(pointFile);
			reader = new BufferedReader(in);
			String str;
			Point3D pt3d = null;
			int cntLine = 0;
			long cntContained = 0;
			while (true) {
				cntLine ++;
				str = reader.readLine();
				if (str == null)	break;
				
				pt3d = Point3D.Create(str);
				if (pt3d == null) {
					System.out.println("ERR pt3d is null : " + str);
					continue;
				}
				
				Set<Integer> keySet = rectList.keySet();
				for(int key : keySet) {
					Path2D rect = rectList.get(key); 
					if (rect.contains(pt3d.GetPoint2D()) == true) {
						String filename = rangeFilePath + Integer.toString(key) + ".xyz";
						WritePointData(filename, pt3d);
						cntContained++;
					}
				}
				if (cntLine % 100000 == 0) {
					System.out.println("cnt : " + (cntLine) + "\t, cnt contained : " + cntContained);
				}
			}
			System.out.println("Lines : " + cntLine);
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

	private static void WritePointData(String filename, Point3D pt3d) {
		FileWriter out = null;
		BufferedWriter writer = null;
		try {
			out = new FileWriter(filename, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer = new BufferedWriter(out);
		try {
			writer.write(String.format("%.3f", pt3d.GetX()) + " " + String.format("%.3f", pt3d.GetY()) + " " + String.format("%.3f", pt3d.GetZ()));
			writer.newLine();
			writer.flush();
			out.flush();
			writer.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static ArrayList<Point3D> WritePointList(String pointFile, Path2D.Double rect, String rangeFile) {
		ArrayList<Point3D> pointList = new ArrayList<Point3D>();
		FileReader in = null;
		BufferedReader reader = null;
		int cntLine = 0;
		double minX = 999999.0;
		double minY = 9999999.0;
		double maxX = 0;
		double maxY = 0;

		FileWriter out = null;
		BufferedWriter writer = null;
		try {
			out = new FileWriter(rangeFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer = new BufferedWriter(out);
		
		
		try {
			in = new FileReader(pointFile);
			reader = new BufferedReader(in);
			String str;
			Point3D pt3d = null;
			long cntContained = 0;
			while (true) {
				str = reader.readLine();
				if (cntLine % 1000000 == 0) {
					System.out.println("cnt : " + (cntLine) + "\t, cnt contained : " + cntContained);
					writer.flush();
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
					if (rect.contains(pt3d.GetPoint2D()) == true) {
						writer.write(String.format("%.3f", pt3d.GetX()) + " " + String.format("%.3f", pt3d.GetY()) + " " + String.format("%.3f", pt3d.GetZ()));
						writer.newLine();
						cntContained++;
					}
				} else {
					System.out.println("Err : " + str);
				}
			}
			System.out.println("minX : " + minX);
			System.out.println("minY : " + minY);
			System.out.println("maxX : " + maxX);
			System.out.println("maxY : " + maxY);
			System.out.println("Lines : " + cntLine);
			System.out.println("cntContained : " + cntContained);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				writer.flush();
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		return pointList;
	}

	@SuppressWarnings("unused")
	private static Double Get25000Range(String id) {
		Path2D.Double path = new Path2D.Double();
		if (id.equals("3724034") == true) {
			path.moveTo(643158.49, 4179320.64);
			path.lineTo(654171.51, 4179519.28);
			path.lineTo(653911.7, 4193389.47);
			path.lineTo(642917.25, 4193190.61);
			path.closePath();
		}
		if (id.equals("372403098") == true) {
			path.moveTo(649766.28, 4179438.06);
			path.lineTo(651968.89, 4179478.38);
			path.lineTo(651917.73, 4182252.38);
			path.lineTo(649715.86, 4182212.06);
			path.closePath();
		}		
		if (id.equals("강화도 인근") == true) {
			path.moveTo(257553.41072342035, 4176089.8466957053);
			path.lineTo(268575.7170212956, 4175773.4474794846);
			path.lineTo(268809.2718620563, 4184096.691344014);
			path.lineTo(257798.11039035654, 4184413.305691137);
			path.closePath();
		}		
		return path;
	}
}
