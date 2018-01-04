package org.envirocar.processing.ec4geomesa.ingestor.input;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

/**
 *
 * @author dewall
 */
public class TextInputSplit extends InputSplit implements Writable {

    private String text;

    /**
     * Constructor.
     */
    public TextInputSplit() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param text
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
        return new String[0];
    }

    @Override
    public void write(DataOutput d) throws IOException {
        d.writeUTF(text);
    }

    @Override
    public void readFields(DataInput di) throws IOException {
        this.text = di.readUTF();
    }

}
