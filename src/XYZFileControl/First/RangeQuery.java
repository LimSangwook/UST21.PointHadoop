package XYZFileControl.First;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RangeQuery {
	public static void main(String []args) {
		String pointFile = "/home/iswook/PointList/2014_Point.xyz";
		String rangeFile = "/home/iswook/PointList/2014_3725102.xyz";
		// 25000도엽 좌표를 가져온다.
		Path2D.Double rect25000 = Get25000Range(3725102);
		ArrayList<Point3D> ptList = WritePointList(pointFile, rect25000, rangeFile);
		System.out.println("Points : " + ptList.size());
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
						writer.write(str);
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

	private static Double Get25000Range(int id) {
		Path2D.Double path = new Path2D.Double();
		if (id == 3725102) {
			path.moveTo(178995.820, 4142642.540);
			path.lineTo(190068.640, 4142224.070);
			path.lineTo(190584.510, 4156098.550);
			path.lineTo(179530.180, 4156517.520);
			path.closePath();
		}
		return path;
	}
}
