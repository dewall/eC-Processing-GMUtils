package org.envirocar.processing.ec4geomesa.ingestor.utils;

import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
 *
 * @author dewall
 */
public class MRJsonInputFormat extends FileInputFormat<LongWritable, Text> {

    @Override
    public org.apache.hadoop.mapreduce.RecordReader<LongWritable, Text> createRecordReader(
            org.apache.hadoop.mapreduce.InputSplit is, TaskAttemptContext tac)
            throws IOException,
            InterruptedException {
        return new MRJsonRecordReader();
    }

    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        CompressionCodec codec = new CompressionCodecFactory(
                context.getConfiguration()).getCodec(filename);
        return codec == null;
    }

}
