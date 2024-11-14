
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MatrixMultiplication {

    public static class MatrixMapper
            extends Mapper<Object, Text, Text, Text> {

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] values = value.toString().split(",");
            if (values.length == 4) {
                String matrixName = values[0];
                int row = Integer.parseInt(values[1]);
                int col = Integer.parseInt(values[2]);
                double val = Double.parseDouble(values[3]);

                if (matrixName.equals("A")) {
                    for (int k = 0; k < context.getConfiguration().getInt("colsB", 0); k++) {
                        Text outputKey = new Text(row + "," + k);
                        Text outputValue = new Text("A," + col + "," + val);
                        context.write(outputKey, outputValue);
                    }
                } else if (matrixName.equals("B")) {
                    for (int i = 0; i < context.getConfiguration().getInt("rowsA", 0); i++) {
                        Text outputKey = new Text(i + "," + col);
                        Text outputValue = new Text("B," + row + "," + val);
                        context.write(outputKey, outputValue);
                    }
                }
            }
        }
    }

    public static class MatrixReducer
            extends Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            
            Map<Integer, Double> mapA = new HashMap<Integer, Double>();
            Map<Integer, Double> mapB = new HashMap<Integer, Double>();

            for (Text val : values) {
                String[] valStr = val.toString().split(",");
                if (valStr[0].equals("A")) {
                    mapA.put(Integer.parseInt(valStr[1]), Double.parseDouble(valStr[2]));
                } else {
                    mapB.put(Integer.parseInt(valStr[1]), Double.parseDouble(valStr[2]));
                }
            }

            double result = 0.0;
            for (Map.Entry<Integer, Double> entry : mapA.entrySet()) {
                Integer k = entry.getKey();
                if (mapB.containsKey(k)) {
                    result += entry.getValue() * mapB.get(k);
                }
            }

            if (result != 0.0) {
                context.write(key, new Text(String.valueOf(result)));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        if (args.length != 5) {
            System.err.println("Usage: MatrixMultiplication <input path> <output path> <rows A> <cols A/rows B> <cols B>");
            System.exit(2);
        }

        conf.setInt("rowsA", Integer.parseInt(args[2]));
        conf.setInt("colsA", Integer.parseInt(args[3]));
        conf.setInt("colsB", Integer.parseInt(args[4]));

        // Delete output directory if it exists
        FileSystem fs = FileSystem.get(conf);
        Path outputPath = new Path(args[1]);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }

        Job job = Job.getInstance(conf, "matrix multiplication");
        job.setJarByClass(MatrixMultiplication.class);
        job.setMapperClass(MatrixMapper.class);
        job.setReducerClass(MatrixReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}