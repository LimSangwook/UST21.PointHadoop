package Index.CreateIndex;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import Common.IndexData;
import Common.PointData;
import Common.Util;

public class CreateIndexReducer extends Reducer<IntWritable, IndexData, IntWritable, IndexData>{
	protected void setup(Context context) throws IOException, InterruptedException {
		Util.basicInfo = Util.GetBasicInfo(context.getConfiguration().get("PointName"));
		Util.basicInfo.depth = context.getConfiguration().getInt("Level", 0);
	}
	
	public void reduce(IntWritable key, Iterable<IndexData> values, Context context) throws IOException, InterruptedException {
		boolean first = true;
		IndexData idxData = new IndexData();
		idxData.SetMaxPT(new PointData(0.0,0.0,0.0f));
		idxData.SetMinPT(new PointData(0.0,0.0,1000.0f));
		
//		System.out.println("KEY : " + key.get());
		for (IndexData value : values) {
//			System.out.print("IdxData : " + idxData.toString());
//			System.out.print("\t value1 : " + value.toString());
			
			if (first == true) {
				idxData.SetMinPT(value.GetMinPT());
				idxData.SetMaxPT(value.GetMaxPT());
				idxData.avgZ = value.avgZ;
				idxData.offset = value.offset;
				idxData.maxOffset = value.maxOffset;
				idxData.cnt = value.cnt;
				first = false;
				continue;
			}
			if (idxData.GetMinPT().GetZ() > value.GetMinPT().GetZ()) {
				idxData.SetMinPT(value.GetMinPT());
			}
			if (idxData.GetMaxPT().GetZ() < value.GetMaxPT().GetZ()) {
				idxData.SetMaxPT(value.GetMaxPT());				
			}
			
			if (idxData.offset > value.offset) {
				idxData.offset = value.offset;
			}
			
			if (idxData.maxOffset < value.maxOffset) {
				idxData.maxOffset = value.maxOffset;
			}

			idxData.avgZ = (idxData.avgZ * (idxData.cnt) + value.avgZ*value.cnt) / (idxData.cnt+value.cnt);
			idxData.cnt += value.cnt;
//			System.out.println("\tIdxData : " + idxData.toString());
		}		
		context.write(key, idxData);			
	}
}
