package USTPointMRService;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import Common.PointData;

public class SuppressCombiner extends Reducer<LongWritable, PointData, LongWritable, PointData> {

	public void reduce(LongWritable key, Iterable<PointData> values, Context context)
			throws IOException, InterruptedException {
		PointData minPT = new PointData();
		boolean first = true;
		for (PointData pt : values) {
			if (first) {
				minPT.Set(pt.GetX(), pt.GetY(), pt.GetZ());
				first = false;
				continue;
			}
			if (minPT.GetZ() > pt.GetZ()) {
				minPT.Set(pt.GetX(), pt.GetY(), pt.GetZ());
			}
		}
		context.write(key, minPT);
	}
}
