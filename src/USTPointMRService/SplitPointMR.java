package USTPointMRService;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import Common.PointData;
import Common.Util;
import XYZFileControl.First.SurveyAreaManager;

public class SplitPointMR {
	public static int area = 10;
	public static void main(String [] args) throws IOException, ClassNotFoundException, InterruptedException {
		// EX USTPointMRService.SuppressMR sourcePoint.xyz 2012

		Configuration conf = new Configuration();
		if (args.length != 2) {
			System.err.println("Usage CompareReportMR <MainPoint> <Year> ");
			System.exit(2);
		}
		
		String mainPointFileName = Util.GetFileName(args[0]);

		Job job = Job.getInstance(conf, "SplitPoint : " + mainPointFileName + "("+args[1]+")");

		SetTextoutputformatSeparator(job, " " );
		SetValues(job.getConfiguration(), args);

		SetClass(job);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[0] + "_" + args[1]));

		System.out.println("MainPoint : " + args[0]);
		System.out.println("Year : " + args[1]);
		job.waitForCompletion(true);
	}
	
	private static void SetValues(Configuration conf, String[] args) {
		HashMap<Integer, Path2D> rectList = null;
		int year = Integer.parseInt(args[1]);
		SurveyAreaManager areaMge = new SurveyAreaManager();
		rectList = areaMge.GetRectList(year);
		System.out.println("##### rectList : " + rectList.keySet().size());
		
		conf.setInt("mapreduce.tasktracker.map.tasks.maximum", 8);
		conf.setInt("mapreduce.job.reduces", rectList.size());
		conf.setInt("mapreduce.job.maps", 6);
		conf.setInt("dfs.block.size", 64*1024*1024);
		conf.setInt("YEAR", Integer.parseInt(args[1]));
		conf.setInt("MAP25000COUNT", rectList.size());
	}

	public static void SetClass(Job job){
		job.setJarByClass(SplitPointMR.class);
		job.setMapperClass(SplitPointMapper.class);
		job.setReducerClass(SplitPointReducer.class);
		job.setPartitionerClass(SplitPointPartitioner.class);
//		job.setCombinerClass(CompareReportCombiner.class);
//		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		job.setMapOutputKeyClass(LongWritable.class);			
		job.setMapOutputValueClass(PointData.class);
	}
	
	private static void SetTextoutputformatSeparator(final Job job, final String separator){
         final Configuration conf = job.getConfiguration(); 					// ensure accurate config ref

         conf.set("mapred.textoutputformat.separator", separator); 			// Prior to Hadoop 2 (YARN)
         conf.set("mapreduce.textoutputformat.separator", separator);			// Hadoop v2+ (YARN)
         conf.set("mapreduce.output.textoutputformat.separator", separator);
         conf.set("mapreduce.output.key.field.separator", separator);
         conf.set("mapred.textoutputformat.separatorText", separator); 
	 }
}
