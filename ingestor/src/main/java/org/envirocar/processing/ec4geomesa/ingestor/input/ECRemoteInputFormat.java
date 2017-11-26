package org.envirocar.processing.ec4geomesa.ingestor.input;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.decoding.EnvirocarJSONUtils;
import org.envirocar.processing.ec4geomesa.ingestor.DownloadTracksDataIngestorMR;
import org.json.simple.parser.ParseException;

/**
 *
 * @author dewall
 */
public class ECRemoteInputFormat extends InputFormat<LongWritable, Text> {

    private static final Logger LOG = Logger.getLogger(ECRemoteInputFormat.class);

    private static final String ENVIROCAR_TRACKS_URL = "http://envirocar.org/api/stable/tracks";

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<InputSplit> getSplits(JobContext jc) throws IOException, InterruptedException {
        int limit = jc.getConfiguration().getInt(DownloadTracksDataIngestorMR.OPTION_LIMIT,
                DownloadTracksDataIngestorMR.OPTION_LIMIT_DEFAULT);

        Request request = new Request.Builder()
                .url(ENVIROCAR_TRACKS_URL + "?limit=" + limit)
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();

        try {
            List<String> trackIds = EnvirocarJSONUtils.parseTrackIds(body.string());

            List<InputSplit> collect = trackIds.stream()
                    .map(t -> new TextInputSplit(ENVIROCAR_TRACKS_URL + "/" + t))
                    .collect(Collectors.toList());

            System.out.println(collect.size());
            return collect;
        } catch (ParseException ex) {
            LOG.error("Error while defining eC inputsplits", ex);
        }
        return null;
    }

    @Override
    public RecordReader<LongWritable, Text> createRecordReader(InputSplit is, TaskAttemptContext tac) throws IOException,
            InterruptedException {
        return new ECRemoteRecordReader();
    }

}
