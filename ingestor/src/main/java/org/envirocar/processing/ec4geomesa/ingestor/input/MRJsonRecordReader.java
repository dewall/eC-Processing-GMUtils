package org.envirocar.processing.ec4geomesa.ingestor.input;

import java.io.IOException;
import java.util.logging.Logger;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author dewall
 */
public class MRJsonRecordReader extends RecordReader<LongWritable, Text> {

    private static final Logger LOG = Logger.getLogger(
            MRJsonInputFormat.class.getName());

    private final LineRecordReader reader = new LineRecordReader();
    private final Text currentLine = new Text();
    private final Text currentValue = new Text();
    private final JSONParser jsonParser = new JSONParser();

    @Override
    public void initialize(InputSplit is, TaskAttemptContext tac) throws
            IOException, InterruptedException {
        reader.initialize(is, tac);
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        while (reader.nextKeyValue()) {
            currentValue.clear();
            currentValue.set(currentLine.toString());
            return true;
        }
        return false;
    }

    @Override
    public LongWritable getCurrentKey() throws IOException, InterruptedException {
        return reader.getCurrentKey();
    }

    @Override
    public Text getCurrentValue() throws IOException,
            InterruptedException {
        return currentValue;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return reader.getProgress();
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }
}
