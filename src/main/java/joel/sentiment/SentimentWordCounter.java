package joel.sentiment;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import joel.sentiment.mapReduce.SentimentWordCounterMapper;
import joel.sentiment.mapReduce.SentimentWordCounterReducer;

public class SentimentWordCounter extends Configured implements Tool {
      
	public int run(String[] args) throws Exception {
            //creating a JobConf object and assigning a job name for identification purposes
            JobConf conf = new JobConf(getConf(), SentimentWordCounter.class);
            conf.setJobName("SentimentWordCounter");

            //Setting configuration object with the Data Type of output Key and Value
            conf.setOutputKeyClass(Text.class);
            conf.setOutputValueClass(IntWritable.class);

            //Providing the mapper and reducer class names
            conf.setMapperClass(SentimentWordCounterMapper.class);
            conf.setReducerClass(SentimentWordCounterReducer.class);

            //the hdfs input and output directory to be fetched from the command line
            FileInputFormat.addInputPath(conf, new Path(args[0]));
            FileOutputFormat.setOutputPath(conf, new Path(args[1]));

            JobClient.runJob(conf);
            return 0;
      }
     
      public static void main(String[] args) throws Exception {
            int res = ToolRunner.run(new Configuration(), new SentimentWordCounter(),args);
            System.exit(res);
      }
}