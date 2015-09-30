package Index.CreateSort;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Reducer;

import Common.PointKey;
import Common.Util;


public class SortReducer extends Reducer<PointKey, DoubleWritable, PointKey, DoubleWritable>{
	protected void setup(Context context) throws IOException, InterruptedException {
		Util.basicInfo = Util.GetBasicInfo(context.getConfiguration().get("PointName"));
		Util.basicInfo.depth = context.getConfiguration().getInt("Level", 0);
//		System.out.println("RRRRRR "+ Util.basicInfo.toString());
	}
	public void reduce(PointKey key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
		for (DoubleWritable value : values) {
			context.write(key, value);
		}
	}
}
