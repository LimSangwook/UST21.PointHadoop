package Common;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class GroupKeyPartitioner extends Partitioner<PointKey, DoubleWritable>{

	@Override
	public int getPartition(PointKey key, DoubleWritable val, int numPartitions) {
		int hash = Util.GetIndexOfLevel(Util.basicInfo, key, Util.basicInfo.depth);
		int partition = (hash-1) % numPartitions;
		return partition;
	}
}
