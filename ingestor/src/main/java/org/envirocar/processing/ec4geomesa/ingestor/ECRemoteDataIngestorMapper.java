package org.envirocar.processing.ec4geomesa.ingestor;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class ECRemoteDataIngestorMapper extends Mapper<LongWritable, Text, Text, SimpleFeature>{

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        super.map(key, value, context); //To change body of generated methods, choose Tools | Templates.
    }

}
