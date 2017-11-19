package org.envirocar.processing.ec4geomesa.ingestor;

import com.vividsolutions.jts.geom.GeometryFactory;
import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class FileDataIngestorMapper extends Mapper<LongWritable, Text, Text, SimpleFeature> {

    private GeometryFactory geometryFactory;
    
    @Override
    protected void setup(Context context) throws IOException,
            InterruptedException {
        super.setup(context); 
        this.geometryFactory = JTSFactoryFinder.getGeometryFactory();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        super.map(key, value, context); 
        // TODO
    }

}
