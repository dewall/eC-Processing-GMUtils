package org.envirocar.processing.ec4geomesa.ingestor.input;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 *
 * @author dewall
 */
public class ECRemoteRecordReader extends RecordReader<LongWritable, Text> {

    private static final Logger LOG = Logger.getLogger(
            ECRemoteRecordReader.class.getName());

    private final Text currentValue = new Text();
    private final OkHttpClient client = new OkHttpClient();

    private boolean processed = false;
    private TextInputSplit textInput;
    private Configuration config;

    @Override
    public void initialize(InputSplit is, TaskAttemptContext tac) throws IOException, InterruptedException {
        this.textInput = (TextInputSplit) is;
        this.config = tac.getConfiguration();
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (!processed) {
            String enviroCarURL = textInput.getText();

            Request request = new Request.Builder()
                    .url(enviroCarURL)
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();

            this.currentValue.clear();
            this.currentValue.set(body.toString());

            processed = true;
            return true;
        }
        return false;
    }

    @Override
    public LongWritable getCurrentKey() throws IOException, InterruptedException {
        return new LongWritable();
    }

    @Override
    public Text getCurrentValue() throws IOException, InterruptedException {
        return currentValue;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return processed ? 1.0f : 0.0f;
    }

    @Override
    public void close() throws IOException {
        // not required.
    }

}
