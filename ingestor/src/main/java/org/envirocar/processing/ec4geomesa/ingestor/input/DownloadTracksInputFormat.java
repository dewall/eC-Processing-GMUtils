package org.envirocar.processing.ec4geomesa.ingestor.input;

import com.google.common.base.Joiner;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.decoding.EnvirocarJSONUtils;
import org.envirocar.processing.ec4geomesa.ingestor.MRWebBasedDataIngestor;
import org.json.simple.parser.ParseException;

/**
 *
 * @author dewall
 */
public class DownloadTracksInputFormat extends InputFormat<LongWritable, Text> {

    private static final Logger LOGGER = Logger.getLogger(DownloadTracksInputFormat.class);

    static {
        LOGGER.getParent().addAppender(new ConsoleAppender());
        LOGGER.setLevel(Level.INFO);
    }

    private static final String ENVIROCAR_TRACKS_URL = "http://envirocar.org/api/stable/tracks";

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<InputSplit> getSplits(JobContext jc) throws IOException, InterruptedException {
        int limit = jc.getConfiguration().getInt(MRWebBasedDataIngestor.OPTION_LIMIT,
                MRWebBasedDataIngestor.OPTION_LIMIT_DEFAULT);
        int chunksize = jc.getConfiguration().getInt(MRWebBasedDataIngestor.OPTION_CHUNKSIZE,
                MRWebBasedDataIngestor.OPTION_CHUNKSIZE_DEFAULT);
        
        LOGGER.info(String.format("Getting splits for the latest %s tracks.", "" + limit));

        // for the case that limit > 5000
        int numRequests = ((int) limit / 5000) + 1;

        try {
            List<String> trackIds = new ArrayList<>();

            if (limit < 5000) {
                trackIds.addAll(downloadTrackIds(limit, 1));
            } else {
                // only when limit > 5000
                for (int i = 1; i < numRequests; i++) {
                    trackIds.addAll(downloadTrackIds(limit, i));
                }

                int rest = limit % 5000;
                if (rest > 0) {
                    List<String> lastPage = downloadTrackIds(limit, numRequests);
                    int size = lastPage.size()-1;
                    trackIds.addAll(downloadTrackIds(limit, numRequests).subList(0, size < rest-1 ? size : rest-1));
                }
            }

            AtomicInteger counter = new AtomicInteger();
            List<InputSplit> collect = trackIds.stream()
                    .map(t -> ENVIROCAR_TRACKS_URL + "/" + t)
                    .collect(Collectors.groupingBy(x -> counter.getAndIncrement() / chunksize))
                    .values().stream()
                    .map(l -> Joiner.on(",").join(l))
                    .map(t -> new TextInputSplit(t))
                    .collect(Collectors.toList());

            LOGGER.info(String.format("Fetched %s trackIds to download.", counter.get()));
            return collect;
        } catch (ParseException ex) {
            LOGGER.error("Error while defining eC inputsplits", ex);
        }
        return null;
    }

    private List<String> downloadTrackIds(int limit, int page) throws IOException, ParseException {
        Request request = new Request.Builder()
                .url(ENVIROCAR_TRACKS_URL + "?limit=" + limit + "&page=" + page)
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();

        return EnvirocarJSONUtils.parseTrackIds(body.string());
    }

    @Override
    public RecordReader<LongWritable, Text> createRecordReader(InputSplit is, TaskAttemptContext tac) throws IOException,
            InterruptedException {
        return new DownloadTracksRecordReader();
    }

}
