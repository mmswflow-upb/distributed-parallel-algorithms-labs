package stubs;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Example input line:
 * 96.7.4.14 - - [24/Apr/2011:04:20:11 -0400] "GET /cat.jpg HTTP/1.1" 200 12433
 *
 */
public class LogFileMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

  private static final IntWritable ONE = new IntWritable(1);

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    String line = value.toString().trim();
    if (line.length() == 0) {
      return;
    }

    // IP address is the first token
    String[] parts = line.split("\\s+");
    if (parts.length == 0) {
      return;
    }

    String ip = parts[0];
    context.write(new Text(ip), ONE);
  }
}