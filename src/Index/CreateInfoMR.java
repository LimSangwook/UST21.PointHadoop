package Index;
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

import Common.Util;
import Index.CreateInfo.CreateBasicInfoMapper;
import Index.CreateInfo.CreateBasicInfoReducer;

public class CreateInfoMR {

	public static void main(String [] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		if (args.length != 1) {
			System.err.println("Usage MinMaxCnt <input>");
			System.exit(2);
		}
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "MinMaxCnt");
		
//		conf.setInt("dfs.block.size", 32*1024);  // 안먹네... 시간날때 찾아보자

		job.setJarByClass(CreateInfoMR.class);
		job.setMapperClass(CreateBasicInfoMapper.class);
		job.setReducerClass(CreateBasicInfoReducer.class);
		job.setCombinerClass(CreateBasicInfoReducer.class);
		
		job.getConfiguration().setInt("dfs.block.size", 1*1024*1024);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);
		
		String outputFileName = Util.OutputPath + "/"+ Util.GetFileName(args[0]);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(outputFileName));
		
		job.waitForCompletion(true);
		 
		
		
	}
}
