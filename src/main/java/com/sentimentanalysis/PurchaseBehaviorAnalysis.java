package com.sentimentanalysis;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class PurchaseBehaviorAnalysis {

    // Mapper Class
    public static class PurchaseBehaviorMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text outKey = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split(",");
            if (fields.length < 7) {
                System.err.println("Skipping malformed input line: " + value.toString());
                return;
            }

            String timestamp = fields[6];
            String productCategory = fields[2];

            try {
                String hour = getHourFromTimestamp(timestamp);
                if (hour != null) {
                    outKey.set(productCategory + "-" + hour);
                    context.write(outKey, one);
                }
            } catch (ParseException e) {
                System.err.println("Skipping malformed timestamp: " + timestamp);
                e.printStackTrace();
            }
        }

        private String getHourFromTimestamp(String timestamp) throws ParseException {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = inputFormat.parse(timestamp);
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
            return hourFormat.format(date);
        }
    }

    // Reducer Class
    public static class PurchaseBehaviorReducer extends Reducer<Text, IntWritable, Text, Text> {
        private Map<String, String> peakHourMap = new HashMap<>();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            String[] keyParts = key.toString().split("-");
            String category = keyParts[0];
            String hour = keyParts[1];

            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }

            String currentPeak = peakHourMap.getOrDefault(category, "00-0");
            int currentPeakHourCount = Integer.parseInt(currentPeak.split("-")[1]);

            if (sum > currentPeakHourCount) {
                peakHourMap.put(category, hour + "-" + sum);
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            for (Map.Entry<String, String> entry : peakHourMap.entrySet()) {
                String category = entry.getKey();
                String[] hourAndCount = entry.getValue().split("-");
                String peakHour = hourAndCount[0];
                String peakCount = hourAndCount[1];
                context.write(new Text(category), new Text("Peak Hour: " + peakHour + ", Purchases: " + peakCount));
            }
        }
    }

    // Driver Method
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length != 2) {
            System.err.println("Usage: PurchaseBehaviorAnalysis <input path> <output path>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf, "Purchase Behavior Analysis by Peak Hour");
        job.setJarByClass(PurchaseBehaviorAnalysis.class);
        job.setMapperClass(PurchaseBehaviorMapper.class);
        job.setReducerClass(PurchaseBehaviorReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
