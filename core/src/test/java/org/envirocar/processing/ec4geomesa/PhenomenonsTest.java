package org.envirocar.processing.ec4geomesa;

import org.envirocar.processing.ec4geomesa.core.model.Phenomenon;
import org.junit.Test;

/**
 *
 * @author dewall
 */
public class PhenomenonsTest {

    
    @Test
    public void testPhenomenons(){
        for(Phenomenon p : Phenomenon.values()){
            System.out.println(p.toString());
            System.out.println(p.name());
            System.out.println(p.ordinal());
            System.out.println(p.getReadableName());
        }
    }
}
