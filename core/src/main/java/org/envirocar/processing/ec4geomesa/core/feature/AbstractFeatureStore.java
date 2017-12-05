/*
 * Copyright (C) 2017 the enviroCar community
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.envirocar.processing.ec4geomesa.core.feature;

import com.google.common.base.Joiner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;

/**
 *
 * @author dewall
 */
public abstract class AbstractFeatureStore<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractFeatureStore.class);

    protected DataStore datastore;

    protected String tableName;
    protected String primaryKey;

    protected SimpleFeatureType featureType;
    protected SimpleFeatureBuilder featureBuilder;

    /**
     * Constructor.
     *
     * @param datastore
     * @param tableName
     * @param primaryKey
     * @param schema
     */
    public AbstractFeatureStore(DataStore datastore, String tableName, String primaryKey, List<String> schema) {
        this(datastore, tableName, primaryKey, null, schema);
    }

    public AbstractFeatureStore(DataStore datastore, String tableName, String primaryKey, String timeKey,
            List<String> schema) {
        this.datastore = datastore;
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.featureType = createSimpleFeatureType(schema, timeKey);
        this.featureBuilder = new SimpleFeatureBuilder(this.featureType);
    }

    public String getTableName() {
        return this.tableName;
    }

    public SimpleFeatureType getSimpleFeatureType() {
        return this.featureType;
    }

    public void createTable() {
        try {
            this.datastore.createSchema(this.featureType);
        } catch (Exception ex) {
            LOGGER.error(String.format("Unable to create schema for %s.", tableName));
            throw new RuntimeException(ex);
        }
    }

    public T getByFeatureID(String id) {
        try {
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            Filter filter = ff.id(ff.featureId(id));
            SimpleFeature feature = fetchSingle(filter);

            if (feature != null) {
                return createEntityFromFeature(feature);
            }
        } catch (IOException ex) {
            LOGGER.error(String.format("Error while fetching feature for featureId=%s", id), ex);
        }
        return null;
    }

    public T getByID(String id) {
        try {
            Filter filter = CQL.toFilter(String.format("%s = %s", this.primaryKey, id));
            SimpleFeature feature = fetchSingle(filter);

            if (feature != null) {
                return createEntityFromFeature(feature);
            }
        } catch (CQLException ex) {
            LOGGER.error("Error while creating Filter.", ex);
        } catch (IOException ex) {
            LOGGER.error(String.format("Error while fetching feature for %s=%s", primaryKey, id), ex);
        }
        return null;
    }

    protected SimpleFeature fetchSingle(Filter filter) throws IOException {
        SimpleFeatureCollection features = fetch(filter);
        return !features.isEmpty() ? features.features().next() : null;
    }

    protected SimpleFeatureCollection fetch(Filter filter) throws IOException {
        return getFeatureSource().getFeatures(filter);
    }

    protected SimpleFeatureSource getFeatureSource() throws IOException {
        return this.datastore.getFeatureSource(tableName);
    }

    protected SimpleFeatureType createSimpleFeatureType(List<String> schema, String timeKey) {
        try {
            SimpleFeatureType featureType = DataUtilities.createType(tableName, Joiner.on(",").join(schema));
            if (timeKey != null) {
                featureType.getUserData().put(
                        SimpleFeatureTypes.DEFAULT_DATE_KEY, timeKey);
            }
            return featureType;
        } catch (SchemaException ex) {
            LOGGER.error(String.format("Error while creating FeatureType for Table %s", tableName), ex);
        }
        return null;
    }

    protected List<T> createEntitiesFromFeatures(SimpleFeatureCollection sfc) {
        List<T> result = new ArrayList<>();

        sfc.sort(SortBy.NATURAL_ORDER);
        SimpleFeatureIterator features = sfc.features();

        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            T entity = createEntityFromFeature(feature);
            result.add(entity);
        }

        return result;
    }

    protected abstract T createEntityFromFeature(SimpleFeature sf);

    protected abstract SimpleFeature createFeatureFromEntity(T t);

}
