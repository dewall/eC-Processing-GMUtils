package org.envirocar.processing.ec4geomesa.ingestor.input;

import com.google.common.collect.Lists;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author dewall
 */
public class DownloadTracksRecordReader extends RecordReader<LongWritable, Text> {

    private static final Logger LOGGER = Logger.getLogger(DownloadTracksRecordReader.class.getName());

    static {
        LOGGER.getParent().addAppender(new ConsoleAppender());
        LOGGER.setLevel(Level.INFO);
    }

    private final Text currentValue = new Text();
    private final OkHttpClient client = new OkHttpClient();

    private Configuration config;
    private boolean downloaded = false;
    private String[] downloadUrls;
    private AtomicInteger counter = new AtomicInteger();

    @Override
    public void initialize(InputSplit is, TaskAttemptContext tac) throws IOException, InterruptedException {
        this.config = tac.getConfiguration();
        this.downloadUrls = ((TextInputSplit) is).getText().split(",");
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (!downloaded) {
            String enviroCarURL = downloadUrls[counter.getAndIncrement()];

            Request request = new Request.Builder()
                    .url(enviroCarURL)
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();

            this.currentValue.clear();
            this.currentValue.set(body.string());

            if (downloadUrls.length == counter.get()) {
                LOGGER.info("Tracks successfully downloaded");
                downloaded = true;
            }
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
        return downloaded ? 1.0f : 0.0f;
    }

    @Override
    public void close() throws IOException {
        // not required.
    }

}
