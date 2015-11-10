package USTPointMRService;

import java.io.IOException;

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

public class SuppressMR {
	public static void main(String [] args) throws IOException, ClassNotFoundException, InterruptedException {
		// EX USTPointMRService.SuppressMR sourcePoint.xyz targetPiont.xyz 2
		Configuration conf = new Configuration();
		if (args.length != 3) {
			System.err.println("Usage SuppressMR <input> <output> <Area Range>");
			System.exit(2);
		}

		String fileName 	= Util.GetFileName(args[0]);
		Path inputPath 	= new Path(args[0]);
		Path outputPath 	= new Path(args[1]);
		double area		= Double.parseDouble(args[2]);

		Job job = Job.getInstance(conf, "SUPPRESS : " + fileName + "(Area : "+area+")");
		
		// 영역값 공유를 위한 변수 설정		
		job.getConfiguration().setDouble("AREA", Double.parseDouble(args[2]));

		// 기타 환경설정
		SetValues(job.getConfiguration(), args);
		SetTextoutputformatSeparator(job, " " );
		SetClass(job);

		FileInputFormat.addInputPath(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputPath);

		System.out.println("Input : " + inputPath.toString());
		System.out.println("Output : " + outputPath.toString());
		job.waitForCompletion(true);
	}
	
	private static void SetValues(Configuration conf, String[] args) {
		conf.setInt("mapreduce.tasktracker.map.tasks.maximum", 8);
		conf.setInt("mapreduce.job.reduces", 1);
		conf.setInt("mapreduce.job.maps", 6);
		conf.setInt("dfs.block.size", 64*1024*1024);
	}

	public static void SetClass(Job job){
		job.setJarByClass(SuppressMR.class);
		job.setMapperClass(SuppressMapper.class);
		job.setReducerClass(SuppressReducer.class);
		job.setCombinerClass(SuppressCombiner.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		// Set mapped Output Key Value 
		job.setMapOutputKeyClass(LongWritable.class);			
		job.setMapOutputValueClass(PointData.class);

		// Set reduced output Key Value 
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
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

