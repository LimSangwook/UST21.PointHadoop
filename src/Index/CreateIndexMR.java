package Index;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import Common.IndexData;
import Common.Util;
import Index.CreateIndex.CreateIndexMapper;
import Index.CreateIndex.CreateIndexReducer;
public class CreateIndexMR extends Configured implements Tool {
	public static void main(String [] args) throws Exception {
		ToolRunner.run(new Configuration(), new CreateIndexMR(), args);
		
	}

	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
//		new GenericOptionsParser(conf, args).getRemainingArgs();
		if (args.length != 2) {
			System.err.println("Usage CreateIndexMR <pointName> <level>");
			System.exit(2);
		}
		String fileName = Util.GetFileName(args[0]);
		int level = Integer.parseInt(args[1]);
		
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "Indexing:" + fileName + " " + level);
		
		SetTextoutputformatSeparator(job, " " );
		
//		System.out.println("### BlockSize : " + (int)((((Math.pow(Math.pow(2.0, level - 1), 2))*100) / 1*1024*1024)+1) * 1*1024*1024);
		job.getConfiguration().set("PointName", Util.GetFileName(args[0]));
		job.getConfiguration().setInt("Level", Integer.parseInt(args[1]));
		job.getConfiguration().setInt("mapreduce.job.reduces", 1);
		job.getConfiguration().setInt("dfs.block.size",4*1024*1024);
		
		SetClass(job);

		FileInputFormat.addInputPath(job, new Path(Util.GetSoredDataPath(args[0])+"/part-r-00000"));
		FileOutputFormat.setOutputPath(job, new Path(Util.GetIndexPath(args[0]) + Integer.parseInt(args[1])));
		
		System.out.println("Input : " + Util.GetSoredDataPath(args[0])+"/part-r-00000");
		System.out.println("Output : " + Util.GetIndexPath(args[0]) + Integer.parseInt(args[1]));

		job.waitForCompletion(true);
		return 0;
	}
	
	public static void SetClass(Job job){
		
		job.setJarByClass(CreateIndexMR.class);
		job.setMapperClass(CreateIndexMapper.class);
		job.setReducerClass(CreateIndexReducer.class);
		job.setCombinerClass(CreateIndexReducer.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IndexData.class);

		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(IndexData.class);
		
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
