package USTPointMRService;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

import Common.PointData;
import Common.XYSortPointKey;

public class SuppressMapper extends Mapper<LongWritable, Text, LongWritable, PointData> {
	public static PointData ptData = new PointData(); 
	public static LongWritable longwritable = new LongWritable();
	private double AREA = 10000000.0;

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		XYSortPointKey pt = XYSortPointKey.Create(value.toString());
		if (pt == null)
			return;
		
		// 현재 포인트의 Map키 만들기
		long XYKey = GetKey(pt.GetX(), pt.GetY(), AREA);
		longwritable.set(XYKey);
		ptData.Set(pt.GetX(), pt.GetY(), (float)(pt.GetZ()));

		context.write(longwritable, ptData);
	}

	protected void setup(Context context) throws IOException, InterruptedException {
		// 환경변수 가져오기
		AREA = context.getConfiguration().getDouble("AREA", 10000000.0);
	}
	
	private static long GetKey(double X, double Y, double rangeMeter) {
		int x = (int)X;
		int y = (int)Y;
		Long key =(int)(x/rangeMeter) * 10000000000L + (int)(y/rangeMeter);
		return key;
	}
}
