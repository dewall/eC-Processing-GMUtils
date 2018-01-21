package org.envirocar.processing.ec4geomesa.core.feature.provider;

import com.google.inject.Inject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.envirocar.processing.ec4geomesa.core.GeoMesaDB;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class InitializeTableInterceptor implements MethodInterceptor {

    @Inject
    private GeoMesaDB geoMesaDB;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object proceed = invocation.proceed();
        if(proceed instanceof SimpleFeatureType){
            geoMesaDB.createTable((SimpleFeatureType) proceed);
        }
        return proceed;
    }
}
