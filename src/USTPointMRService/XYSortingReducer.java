package USTPointMRService;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import Common.XYSortPointKey;

public class XYSortingReducer extends Reducer<XYSortPointKey, DoubleWritable, Text, DoubleWritable>{
	Text text = new Text();
	public void reduce(XYSortPointKey key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
		text.set(key.GetSourceString());
		
		for (DoubleWritable value : values) {
			context.write(text, value);
		}
	}
}
