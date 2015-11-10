package USTPointMRService;


import java.awt.geom.Point2D;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import Common.PointData;

public class CompareReportReducer extends Reducer<Text, Text, PointData, Text> {
	Text text = new Text();
	Point2D mainPT2d = new Point2D.Double(0.0, 0.0);
	Point2D pt2d = new Point2D.Double(0.0, 0.0);

	public void reduce(Text key, Iterable<Text> values, Context context) 
			throws IOException, InterruptedException {
//		mainPT2d.setLocation(key.GetX(), key.GetY());
		for (Text pt : values) {
			context.write(null, pt);
		}
	}
}
