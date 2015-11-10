package USTPointMRService;

import java.awt.geom.Path2D;
import java.util.HashMap;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Partitioner;

import Common.PointData;
import XYZFileControl.First.SurveyAreaManager;

public class SplitPointPartitioner extends Partitioner<LongWritable, PointData>{

	@Override
	public int getPartition(LongWritable key, PointData val, int numPartitions) {
		HashMap<Integer, Path2D> rectList = null;
		int year = 2012;
		SurveyAreaManager areaMge = new SurveyAreaManager();
		rectList = areaMge.GetRectList(year);
		int partition = 0;
		for (Integer rectKey : rectList.keySet()) {
			if (rectKey.intValue() == key.get()) {
				return partition;
			}
			partition++;
		}
		return partition;
	}
}
