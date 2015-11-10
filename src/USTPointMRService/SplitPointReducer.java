package USTPointMRService;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import Common.PointData;

public class SplitPointReducer extends Reducer<LongWritable, PointData, Text, NullWritable> {
	private NullWritable out = NullWritable.get();
	private Text text = new Text();
	public void reduce(LongWritable key, Iterable<PointData> values, Context context)
			throws IOException, InterruptedException {
		for (PointData pt : values) {
			if (pt.GetY() == 4153122.301) {
				System.out.println("Reducer key : " + key + " \t PT : " + pt.toString());
			}
			text.set(pt.toString());
			context.write(text, out);
		}
	}
}
