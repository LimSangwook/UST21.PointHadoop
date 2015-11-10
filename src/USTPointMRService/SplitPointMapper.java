package USTPointMRService;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Set;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import Common.PointData;
import XYZFileControl.First.Point3D;
import XYZFileControl.First.SurveyAreaManager;

public class SplitPointMapper extends Mapper<LongWritable, Text, LongWritable, PointData> {
	public LongWritable longwritable = new LongWritable();
	public PointData ptData = new PointData();
	
	HashMap<Integer, Path2D> rectList = null;
	Set<Integer> keySet = null;
		
	protected void setup(Context context) throws IOException, InterruptedException {
		int year = context.getConfiguration().getInt("YEAR", 0);
		SurveyAreaManager areaMge = new SurveyAreaManager();
		rectList = areaMge.GetRectList(year);
		keySet = rectList.keySet();
	}	
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		Point3D pt3d = Point3D.Create(value.toString());
		if (pt3d == null)
			return;
		
		for(int rectKey : keySet) {
			Path2D rect = rectList.get(rectKey); 
			if (rect.contains(pt3d.GetPoint2D()) == true) {
				ptData.Set(pt3d.GetX(), pt3d.GetY(), pt3d.GetZ());
				longwritable.set(rectKey);
				context.write(longwritable, ptData);
				return;
			}
		}
	}
}

