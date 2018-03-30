
import java.io.IOException;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.task.TaskAttemptContextImpl;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.hadoop.util.ReflectionUtils;
import org.envirocar.processing.ec4geomesa.ingestor.input.DownloadTracksInputFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dewall
 */
public class DownloadTracksMRInputTest {

    private final int limit = 50;
    private final int chunk = 10;
    private Configuration conf;
    private TaskAttemptContext context;

    // record reader parameter

    @Before
    public void before() {
        // Setting configuration parameters
        this.conf = new Configuration(false);
        this.conf.set("fs.default.name", "file:///");
        this.conf.set("limit", "" + limit);
        this.conf.set("chunksize", "" + chunk);

        // Creating the task context.
        this.context = new TaskAttemptContextImpl(conf, new TaskAttemptID());
    }

    @Test
    public void testInputFormat() throws IOException, InterruptedException {
        System.out.println("---- testInputFormat(): Testing DownloadTracksInputFormat");

        // Create a new download tracks input format instance and get the inputsplits to test
        DownloadTracksInputFormat inputFormat = ReflectionUtils.newInstance(DownloadTracksInputFormat.class, conf);
        List<InputSplit> splits = inputFormat.getSplits(context);

        // testing stuff.
        Assert.assertTrue(splits.size() > 0);
        Assert.assertTrue(splits.size() == limit / chunk);

        System.out.println("---- testInputFormat(): Testing record reader");
        RecordReader<LongWritable, Text> recordReader = inputFormat.createRecordReader(splits.get(0), this.context);
        recordReader.initialize(splits.get(0), context);

        recordReader.nextKeyValue();
        Text currentValue = recordReader.getCurrentValue();

        Assert.assertNotNull(currentValue.toString() != null);
    }

}
