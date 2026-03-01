package stubs;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

public class TestWordCount {

  MapDriver<LongWritable, Text, Text, IntWritable> mapDriver;
  ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
  MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;

  @Before
  public void setUp() {

    WordMapper mapper = new WordMapper();
    mapDriver = new MapDriver<LongWritable, Text, Text, IntWritable>();
    mapDriver.setMapper(mapper);

    SumReducer reducer = new SumReducer();
    reduceDriver = new ReduceDriver<Text, IntWritable, Text, IntWritable>();
    reduceDriver.setReducer(reducer);

    mapReduceDriver = new MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable>();
    mapReduceDriver.setMapper(mapper);
    mapReduceDriver.setReducer(reducer);
  }

  @Test
  public void testMapper() throws Exception {

    // input: "1 cat cat dog"
    mapDriver.withInput(new LongWritable(1), new Text("cat cat dog"));

    // expected mapper outputs
    mapDriver.withOutput(new Text("cat"), new IntWritable(1));
    mapDriver.withOutput(new Text("cat"), new IntWritable(1));
    mapDriver.withOutput(new Text("dog"), new IntWritable(1));

    mapDriver.runTest();
  }

  @Test
  public void testReducer() throws Exception {

    // reducer input: key="cat", values=[1,1]
    reduceDriver.withInput(new Text("cat"),
        java.util.Arrays.asList(new IntWritable(1), new IntWritable(1)));

    // expected: "cat 2"
    reduceDriver.withOutput(new Text("cat"), new IntWritable(2));

    reduceDriver.runTest();
  }

  @Test
  public void testMapReduce() throws Exception {

    // mapreduce input
    mapReduceDriver.withInput(new LongWritable(1), new Text("cat cat dog"));

    // expected reducer outputs (order may matter depending on MRUnit version; this is typical)
    mapReduceDriver.withOutput(new Text("cat"), new IntWritable(2));
    mapReduceDriver.withOutput(new Text("dog"), new IntWritable(1));

    mapReduceDriver.runTest();
  }
}