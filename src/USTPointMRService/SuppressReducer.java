package USTPointMRService;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import Common.PointData;

public class SuppressReducer extends Reducer<LongWritable, PointData, Text, NullWritable> {
	private Text text = new Text();
	private NullWritable out = NullWritable.get();
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
		text.set(minPT.toString());
		context.write(text, out);
	}
}
