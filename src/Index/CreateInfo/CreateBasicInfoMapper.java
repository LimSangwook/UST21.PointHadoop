package Index.CreateInfo;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CreateBasicInfoMapper extends Mapper<LongWritable, Text, Text, DoubleWritable>{
		double MinX = 1000000.0;
		double MinY = 10000000.0;
		double MaxX = 0.0;
		double MaxY = 0.0;
		double dataCnt = 0.0;
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
			String [] xyz = value.toString().split(" ");
			if (xyz.length != 3) {
				return;
			}
			double X = Double.parseDouble(xyz[0]);
			double Y = Double.parseDouble(xyz[1]);
			
			if (MinX > X) MinX = X;
			if (MinY > Y) MinY = Y;
			if (MaxX < X) MaxX = X;
			if (MaxY < Y) MaxY = Y;
			dataCnt += 1.0;
		}
		
		protected void cleanup(Context context) throws IOException, InterruptedException {
			context.write(new Text("MinX"), new DoubleWritable(MinX));
			context.write(new Text("MinY"), new DoubleWritable(MinY));
			context.write(new Text("MaxX"), new DoubleWritable(MaxX));
			context.write(new Text("MaxY"), new DoubleWritable(MaxY));
			context.write(new Text("DataCnt"), new DoubleWritable(dataCnt));
		}
	}