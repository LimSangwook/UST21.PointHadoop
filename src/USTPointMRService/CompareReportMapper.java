package USTPointMRService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;

import Common.PointData;
import XYZFileControl.First.Point3D;

public class CompareReportMapper extends Mapper<LongWritable, Text, Text, Text> {
	private PointData ptData1 = new PointData(); 
	private PointData ptData2 = new PointData();
	private STRtree mainPTIndex = null;
	private double RANGE = 10000000.0;

	protected void setup(Context context) throws IOException, InterruptedException {
		URI[] localPaths = context.getCacheFiles();
		if (localPaths.length != 1) {
			System.out.println("CompareReportMapper ERROR : localPaths.length is Not 1!");
			return;
		}
		mainPTIndex = GetMainPTIndex(localPaths[0].toString());
		RANGE = context.getConfiguration().getDouble("RANGE", 10000000.0);
		System.out.println("index Size : " + mainPTIndex.size());
	}
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//		Point3D pt3d = Point3D.Create(value.toString());
//		if (pt3d == null)
//			return;
//		
//		Envelope e = new Envelope(pt3d.GetX() - RANGE, pt3d.GetX() + RANGE, pt3d.GetY() - RANGE, pt3d.GetY() + RANGE);
//		@SuppressWarnings("unchecked")
//		List<Point3D> list = mainPTIndex.query(e);
//		Iterator<Point3D> ptIter = list.iterator();
//		while (ptIter.hasNext()) {
//			Point3D pt2 = ptIter.next();
//			double distance = pt3d.GetPoint2D().distance(pt2.GetPoint2D());
//			if (distance <= RANGE) {
//				ptData1.Set(pt3d.GetX(), pt3d.GetY(), pt3d.GetZ());
//				ptData2.Set(pt2.GetX(), pt2.GetY(), pt2.GetZ());
//				context.write(ptData1, ptData2);
//				break;
//			}
//		}
	}

	private STRtree GetMainPTIndex(String mainPointFile) {
		STRtree pointList = new STRtree();
		FileReader in = null;
		BufferedReader reader = null;

		try {
			in = new FileReader(mainPointFile);
			reader = new BufferedReader(in);
			String str;
			Point3D pt3d = null;
			while (true) {
				str = reader.readLine();
				if (str == null)	break;
				
				pt3d = Point3D.Create(str);
				if (pt3d != null) {
					Coordinate pt = new Coordinate(pt3d.GetX(), pt3d.GetY());
					pointList.insert(new Envelope(pt), pt3d);
				} else {
					System.out.println("Err : " + str);
				}
			}
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
