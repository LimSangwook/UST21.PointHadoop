package USTPointMRService;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
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

public class CompareReportMR {
	public static void main(String [] args) throws IOException, ClassNotFoundException, InterruptedException {
		// Arg1을 기준으로 Arg2에서 제일 가까운점을 찾고 거리를 계산한 Report(Arg30)를 생성한다 
		// EX) USTPointMRService.CompareReportMR /25000.xyz /5000.xyz /25000.Report 
		// - outPut Format : x y z x y z d
		// - 두파일 모두 HDFS에 들어있어야 한다.
		
		Configuration conf = new Configuration();
		if (args.length != 4) {
			System.err.println("Usage CompareReportMR <MainPoint> <ComparePoint> <OutputDIR> <Range>");
			System.exit(2);
		}
		
		String mainFileName 		= Util.GetFileName(args[0]);
		String compareFileName 	= Util.GetFileName(args[1]);
		double range				= Double.parseDouble(args[3]);

		Job job = Job.getInstance(conf, "CompareReport : " + mainFileName + "("+compareFileName+":" + range+")");
		
		// 영역값 공유를 위한 변수 설정		
		job.getConfiguration().setDouble("RANGE", range);
		
		// CompareFile을 분산캐시 한다.
		job.addCacheFile(new Path(args[0]).toUri());

		// 환경 설정
		SetTextoutputformatSeparator(job, " " );
		SetValues(job.getConfiguration(), args);
		SetClass(job);
		
		// 입출력 패스 설정
		FileInputFormat.addInputPath(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		System.out.println("MainPoint : " + args[0]);
		System.out.println("ComparePoint : " + args[1]);
		System.out.println("Output : " + args[2]);
		job.waitForCompletion(true);
	}
	
	private static void SetValues(Configuration conf, String[] args) {
		conf.setInt("mapreduce.tasktracker.map.tasks.maximum", 8);
		conf.setInt("mapreduce.job.reduces", 1);
		conf.setInt("mapreduce.job.maps", 6);
		conf.setInt("dfs.block.size", 64*1024*1024);
	}

	public static void SetClass(Job job){
		job.setJarByClass(CompareReportMR.class);
		job.setMapperClass(CompareReportMapper.class);
		job.setReducerClass(CompareReportReducer.class);
//		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setMapOutputKeyClass(Text.class);			
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(PointData.class);
		job.setOutputValueClass(Text.class);

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