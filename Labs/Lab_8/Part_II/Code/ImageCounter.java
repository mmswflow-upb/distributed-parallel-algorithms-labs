package stubs;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class ImageCounter extends Configured implements Tool {

  @Override
  public int run(String[] args) throws Exception {

    if (args.length != 2) {
      System.out.printf("Usage: ImageCounter <input dir> <output dir>\n");
      return -1;
    }

    Job job = new Job(getConf());
    job.setJarByClass(ImageCounter.class);
    job.setJobName("Image Counter");

    /*
     * TODO implement
     */
    	org.apache.hadoop.mapreduce.lib.input.FileInputFormat.setInputPaths(
    	    job, new org.apache.hadoop.fs.Path(args[0]));
    	org.apache.hadoop.mapreduce.lib.output.FileOutputFormat.setOutputPath(
    	    job, new org.apache.hadoop.fs.Path(args[1]));

    	// set mapper
    	job.setMapperClass(ImageCounterMapper.class);

    	// map-only job
    	job.setNumReduceTasks(0);

    	// output types (required even if mapper writes nothing)
    	job.setOutputKeyClass(org.apache.hadoop.io.Text.class);
    	job.setOutputValueClass(org.apache.hadoop.io.IntWritable.class);

    boolean success = job.waitForCompletion(true);
    if (success) {
      /*
       * Print out the counters that the mappers have been incrementing.
       * TODO implement
       */
    	long GIFs = job.getCounters().findCounter("ImageCounter", "gif").getValue();
    	long JPEGs = job.getCounters().findCounter("ImageCounter", "jpeg").getValue();
    	long OTHERs = job.getCounters().findCounter("ImageCounter", "other").getValue();

    	System.out.println("No GIFs = " + GIFs);
    	System.out.println("No JPEGs = " + JPEGs);
    	System.out.println("No OTHERs = " + OTHERs);
      return 0;
    } else
      return 1;

  }

  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(new Configuration(), new ImageCounter(), args);
    System.exit(exitCode);
  }
}