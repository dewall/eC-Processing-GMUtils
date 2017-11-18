package org.envirocar.processing.ec4geomesa.core.feature;

import com.google.common.base.Joiner;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public abstract class AbstractFeatureProfile<T> {

    private static final Logger LOG = Logger.getLogger(
            AbstractFeatureProfile.class);

    protected String tableName;

    public AbstractFeatureProfile(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return this.tableName;
    }

    protected SimpleFeatureType createSimpleFeatureType(
            List<String> featureAttributes) throws SchemaException {
        String spec = Joiner.on(",").join(featureAttributes);
        SimpleFeatureType featureType = DataUtilities.createType(
                getTableName(), spec);
        return featureType;
    }

    public void createSchema(DataStore dataStore) throws IOException {
        LOG.info(String.format("Creating schema for feature table %s",
                getTableName()));
        SimpleFeatureType featureType = getSimpleFeatureType();
        dataStore.createSchema(featureType);
    }

    public abstract SimpleFeatureType getSimpleFeatureType();

    public abstract SimpleFeature createSimpleFeature(T t);
}
