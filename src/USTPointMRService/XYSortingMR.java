package USTPointMRService;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import Common.PointKeyComparator;
import Common.Util;
import Common.XYSortPointKey;

public class XYSortingMR {
	public static void main(String [] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		if (args.length != 2) {
			System.err.println("Usage RawDataSort <input> <Level>");
			System.exit(2);
		}
		
		String fileName = Util.GetFileName(args[0]);
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "XY Sort:" + fileName);
		
		SetTextoutputformatSeparator(job, " " );
		SetValues(job.getConfiguration(), args);

		SetClass(job);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		System.out.println("Input : " + args[0]);
		System.out.println("Output : " + args[1]);
		job.waitForCompletion(true);
	}
	
	private static void SetValues(Configuration conf, String[] args) {
		conf.setInt("mapreduce.tasktracker.map.tasks.maximum", 8);
		
		conf.setInt("mapreduce.job.reduces", 1);
		conf.setInt("dfs.block.size", 64*1024*1024);
	}

	public static void SetClass(Job job){
		job.setJarByClass(XYSortingMR.class);
		job.setMapperClass(XYSortingMapper.class);
		job.setReducerClass(XYSortingReducer.class);
		job.setSortComparatorClass(PointKeyComparator.class);
//		job.setPartitionerClass(GroupKeyPartitioner.class);
//		job.setGroupingComparatorClass(GroupKeyComparator.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setMapOutputKeyClass(XYSortPointKey.class);
		job.setMapOutputValueClass(DoubleWritable.class);
		
		job.setOutputKeyClass(Text.class);
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
