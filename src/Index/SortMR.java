package Index;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import Common.BasicInfo;
import Common.PointKey;
import Common.PointKeyComparator;
import Common.Util;
import Index.CreateSort.SortMapper;
import Index.CreateSort.SortReducer;

public class SortMR {
	public static void main(String [] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		if (args.length != 2) {
			System.err.println("Usage RawDataSort <input> <Level>");
			System.exit(2);
		}
		
		String fileName = Util.GetFileName(args[0]);
		int depth = Integer.parseInt(args[1]);
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "Sort:" + fileName + " " + depth);
		
		SetTextoutputformatSeparator(job, " " );
		SetValues(job.getConfiguration(), args);

		conf.setInt("mapreduce.tasktracker.map.tasks.maximum", 8);
		SetClass(job);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(Util.GetSoredDataPath(args[0])));

		Util.SetDepthToBasicInfo(fileName, depth );
		System.out.println("Input : " + args[0]);
		System.out.println("Output : " + Util.GetSoredDataPath(args[0]));
		System.out.println("Depth : " + depth);
		job.waitForCompletion(true);
	}
	
	private static void SetValues(Configuration conf, String[] args) {
		conf.set("PointName", Util.GetFileName(args[0]));
		conf.setInt("Level", Integer.parseInt(args[1]));
		
		conf.setInt("mapreduce.job.reduces", 1);
		conf.setInt("dfs.block.size", 64*1024*1024);
		
		BasicInfo bi;
		try {
			bi = Util.GetBasicInfo(Util.GetFileName(args[0]));
			bi.depth = Integer.parseInt(args[1]);
			conf.setDouble("MinX", bi.minX);
			conf.setDouble("MinY", bi.minY);
			conf.setDouble("MaxX", bi.maxX);
			conf.setDouble("MaxY", bi.maxY);
			conf.setDouble("DataCnt", bi.dataCnt);
			
			System.out.println("RRRRRR "+ bi.toString());
			
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
	}

	public static void SetClass(Job job){
		job.setJarByClass(SortMR.class);
		job.setMapperClass(SortMapper.class);
		job.setReducerClass(SortReducer.class);
//		job.setCombinerClass(RawDataSortReducer.class);
		job.setSortComparatorClass(PointKeyComparator.class);
//		job.setPartitionerClass(GroupKeyPartitioner.class);
//		job.setGroupingComparatorClass(GroupKeyComparator.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setMapOutputKeyClass(PointKey.class);
		job.setMapOutputValueClass(DoubleWritable.class);
		
		job.setOutputKeyClass(PointKey.class);
		job.setOutputValueClass(DoubleWritable.class);
	}
	
	private static void SetTextoutputformatSeparator(final Job job, final String separator){
         final Configuration conf = job.getConfiguration(); //ensure accurate config ref

         conf.set("mapred.textoutputformat.separator", separator); //Prior to Hadoop 2 (YARN)
         conf.set("mapreduce.textoutputformat.separator", separator);  //Hadoop v2+ (YARN)
         conf.set("mapreduce.output.textoutputformat.separator", separator);
         conf.set("mapreduce.output.key.field.separator", separator);
         conf.set("mapred.textoutputformat.separatorText", separator); // ?
	 }
}
