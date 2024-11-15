
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import java.util.*;

public class HBaseSorting {
    private static final byte[] CF_SCORES = Bytes.toBytes("scores");
    private static final byte[] COL_MATH = Bytes.toBytes("math");
    private static final byte[] COL_ENGLISH = Bytes.toBytes("english");

    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);
        
        // Insert sample data
        insertSampleData(connection);
        
        // Retrieve and sort by math scores
        System.out.println("\nSorted by Math Scores:");
        sortBySubject(connection, "math");
        
        // Retrieve and sort by english scores
        System.out.println("\nSorted by English Scores:");
        sortBySubject(connection, "english");
        
        connection.close();
    }

    private static void insertSampleData(Connection connection) throws Exception {
        Table table = connection.getTable(TableName.valueOf("students"));
        List<Put> puts = new ArrayList<>();

        // Sample data: 10 students with different scores
        String[][] data = {
            {"1", "85", "78"}, {"2", "92", "88"}, {"3", "78", "92"},
            {"4", "95", "85"}, {"5", "88", "79"}, {"6", "73", "82"},
            {"7", "90", "88"}, {"8", "85", "95"}, {"9", "77", "75"},
            {"10", "89", "91"}
        };

        for (String[] student : data) {
            Put put = new Put(Bytes.toBytes("student" + student[0]));
            put.addColumn(CF_SCORES, COL_MATH, Bytes.toBytes(student[1]));
            put.addColumn(CF_SCORES, COL_ENGLISH, Bytes.toBytes(student[2]));
            puts.add(put);
        }

        table.put(puts);
        table.close();
        System.out.println("Sample data inserted successfully!");
    }

    private static void sortBySubject(Connection connection, String subject) throws Exception {
        Table table = connection.getTable(TableName.valueOf("students"));
        Scan scan = new Scan();
        scan.addFamily(CF_SCORES);
        
        ResultScanner scanner = table.getScanner(scan);
        List<Map.Entry<String, Integer>> scores = new ArrayList<>();
        
        for (Result result : scanner) {
            String studentId = Bytes.toString(result.getRow());
            byte[] scoreBytes = result.getValue(CF_SCORES, 
                subject.equals("math") ? COL_MATH : COL_ENGLISH);
            int score = Integer.parseInt(Bytes.toString(scoreBytes));
            scores.add(new AbstractMap.SimpleEntry<>(studentId, score));
        }
        
        // Sort by scores in descending order
        scores.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        // Print sorted results
        for (Map.Entry<String, Integer> entry : scores) {
            System.out.printf("%s: %d%n", entry.getKey(), entry.getValue());
        }
        
        scanner.close();
        table.close();
    }
}