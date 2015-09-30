package Index.CreateIndex;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import Common.IndexData;
import Common.PointData;
import Common.PointKey;
import Common.Util;

public class CreateIndexMapper extends Mapper<LongWritable, Text, IntWritable, IndexData>{
	public static IntWritable keyVal = new IntWritable();
	public static PointData ptData = new PointData();
	
	protected void setup(Context context) throws IOException, InterruptedException {
		Util.basicInfo = Util.GetBasicInfo(context.getConfiguration().get("PointName"));
		Util.basicInfo.depth = context.getConfiguration().getInt("Level", 10);
	}
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		String [] xyz = value.toString().split(" ");
		double X = Double.parseDouble(xyz[0]);
		double Y = Double.parseDouble(xyz[1]);
		double Z = Double.parseDouble(xyz[2]);
		
		int idxNumber = Util.GetIndexOfLevel(Util.basicInfo, new PointKey(X,Y), context.getConfiguration().getInt("Level", 0));
		keyVal.set(idxNumber);
		ptData.Set(X, Y, (float)Z);
		IndexData idxData = new IndexData();
		idxData.SetMaxPT(ptData);
		idxData.SetMinPT(ptData);
		idxData.avgZ = ptData.GetZ();
		idxData.offset = key.get();
		idxData.maxOffset = key.get();
		idxData.cnt = 1;
		context.write(keyVal, idxData);
	}
}