package USTPointMRService;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import Common.XYSortPointKey;

public class XYSortingMapper extends Mapper<LongWritable, Text, XYSortPointKey, DoubleWritable>{
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		XYSortPointKey ptKey = XYSortPointKey.Create(value.toString());
		if (ptKey == null)
			return;
		context.write(ptKey, new DoubleWritable(ptKey.GetZ()));
	}
}