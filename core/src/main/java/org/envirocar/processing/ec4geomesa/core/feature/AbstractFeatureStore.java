package org.envirocar.processing.ec4geomesa.core.feature;

import com.google.common.base.Joiner;
import java.util.List;
import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public abstract class AbstractFeatureStore<T> {

    private static final Logger LOG = Logger.getLogger(AbstractFeatureStore.class);

    protected String tableName;

    protected SimpleFeatureType featureType;
    protected SimpleFeatureBuilder featureBuilder;

    /**
     * Constructor.
     *
     * @param tableName
     */
    public AbstractFeatureStore(String tableName) {
        this.tableName = tableName;
        this.featureType = this.createSimpleFeatureType();
        this.featureBuilder = new SimpleFeatureBuilder(this.featureType);
    }

    /**
     * Constructor.
     *
     * @param tableName
     * @param schema
     * @param dataStore
     */
    public AbstractFeatureStore(String tableName, List<String> schema, DataStore dataStore) {

    }

    protected SimpleFeatureType createSimpleFeatureType(
            List<String> featureAttributes) throws SchemaException {
        String spec = Joiner.on(",").join(featureAttributes);
        return DataUtilities.createType(getTableName(), spec);
    }

    protected abstract SimpleFeatureType createSimpleFeatureType();

    public abstract SimpleFeature createSimpleFeature(T t);

    public abstract T getById(DataStore ds, String id);

    public String getTableName() {
        return this.tableName;
    }

    public SimpleFeatureType getFeatureType() {
        return featureType;
    }
}
