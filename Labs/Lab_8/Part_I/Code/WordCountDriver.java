package stubs;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
 * This driver class is called using the ToolRunner.run method
 * call in the main method (below). Extending the Configured class 
 * enables the driver class to access Hadoop configuration options.
 */
public class WordCountDriver extends Configured implements Tool {

  @Override
  public int run(String[] args) throws Exception {

    if (args.length != 2) {
      System.out.printf("Usage: WordCountDriver <input dir> <output dir>\n");
      return -1;
    }

    Job job = new Job(getConf());
    job.setJarByClass(WordCountDriver.class);
    job.setJobName("Word Count Driver");

    /*
     * TODO implement
     */
 // input + output paths
    org.apache.hadoop.mapreduce.lib.input.FileInputFormat.setInputPaths(
            job, new org.apache.hadoop.fs.Path(args[0]));
    org.apache.hadoop.mapreduce.lib.output.FileOutputFormat.setOutputPath(
            job, new org.apache.hadoop.fs.Path(args[1]));

    // mapper + reducer
    job.setMapperClass(WordMapper.class);
    job.setReducerClass(SumReducer.class);

    // combiner (same as reducer for WordCount)
    job.setCombinerClass(SumReducer.class);

    // map output types (what mapper emits)
    job.setMapOutputKeyClass(org.apache.hadoop.io.Text.class);
    job.setMapOutputValueClass(org.apache.hadoop.io.IntWritable.class);

    // final output types (what reducer emits)
    job.setOutputKeyClass(org.apache.hadoop.io.Text.class);
    job.setOutputValueClass(org.apache.hadoop.io.IntWritable.class);
    
    if (job.getCombinerClass() == null) {
      throw new Exception("Combiner not set");
    }

    boolean success = job.waitForCompletion(true);
    return success ? 0 : 1;
  }

  /*
   * The main method calls the ToolRunner.run method, which
   * calls an options parser that interprets Hadoop command-line
   * options and puts them into a Configuration object.
   */
  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(new Configuration(), new WordCountDriver(), args);
    System.exit(exitCode);
  }
}