import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.mapreduce.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.conf.Configuration;

public class HBaseAggregation {

    public static void main(String[] args) throws Exception {
        // Configuration
        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("students"));

        // Scan the table
        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes("scores"), Bytes.toBytes("math"));
        scan.addColumn(Bytes.toBytes("scores"), Bytes.toBytes("english"));

        // Aggregate data
        ResultScanner scanner = table.getScanner(scan);
        int totalMathScore = 0;
        int totalEnglishScore = 0;
        int totalStudents = 0;

        for (Result result : scanner) {
            totalMathScore += Integer.parseInt(Bytes.toString(result.getValue(Bytes.toBytes("scores"), Bytes.toBytes("math"))));
            totalEnglishScore += Integer.parseInt(Bytes.toString(result.getValue(Bytes.toBytes("scores"), Bytes.toBytes("english"))));
            totalStudents++;
        }

        // Print aggregated results
        System.out.println("Total Math Score: " + totalMathScore);
        System.out.println("Total English Score: " + totalEnglishScore);
        System.out.println("Average Math Score: " + totalMathScore / totalStudents);
        System.out.println("Average English Score: " + totalEnglishScore / totalStudents);

        scanner.close();
        table.close();
        connection.close();
    }
}
