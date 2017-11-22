package org.envirocar.processing.ec4geomesa.ingestor.input;

import java.io.IOException;
import org.apache.hadoop.mapreduce.InputSplit;

/**
 *
 * @author dewall
 */
public class TextInputSplit extends InputSplit {

    private String text;

    /**
     * Constructor.
     *
     * @param url
     */
    public TextInputSplit(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public long getLength() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public String[] getLocations() throws IOException, InterruptedException {
        return null;
    }

}
