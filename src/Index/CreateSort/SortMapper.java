package Index.CreateSort;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import Common.PointKey;
import Common.Util;

public class SortMapper extends Mapper<LongWritable, Text, PointKey, DoubleWritable>{
	protected void setup(Context context) throws IOException, InterruptedException {
		Util.basicInfo = Util.GetBasicInfo(context.getConfiguration().get("PointName"));
		Util.basicInfo.depth = context.getConfiguration().getInt("Level", 0);
	}
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		String [] xyz = value.toString().split(" ");
		if (xyz.length != 3) return;
		double X = Double.parseDouble(xyz[0]);
		double Y = Double.parseDouble(xyz[1]);
		double Z = Double.parseDouble(xyz[2]);
		PointKey ptKey = new PointKey(X,Y);
		context.write(ptKey, new DoubleWritable(Z));
	}
}