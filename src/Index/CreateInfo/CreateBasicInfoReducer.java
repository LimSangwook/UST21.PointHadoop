package Index.CreateInfo;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CreateBasicInfoReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable>{
	private DoubleWritable result = new DoubleWritable(0.0);
	
	public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
		double minVal = 10000000.0;
		double maxVal = 0.0;
		double dataCnt = 0.0;
		for (DoubleWritable val : values) {
			if (key.toString().compareTo("MinX") == 0 || key.toString().compareTo("MinY") == 0) {
				if (minVal > val.get()) minVal = val.get();
				result.set(minVal);
				
			} else if (key.toString().compareTo("MaxX") == 0 || key.toString().compareTo("MaxY") == 0) {
				if (maxVal < val.get()) maxVal = val.get();
				result.set(maxVal);
			} else if(key.toString().compareTo("DataCnt") == 0) {
				dataCnt += val.get();	
				result.set(dataCnt);
			}
		}
		context.write(key, result);
	}
}
