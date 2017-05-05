package edu.utdallas.csdesign.spring17.nutriscope;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import edu.utdallas.csdesign.spring17.nutriscope.settings.TdeeCalculator;

/**
 * Created by john on 5/5/17.
 */

public class TdeeCalculatorTest {

    @Test
    public void getTdee_isCorrect() {
        double tdee = TdeeCalculator.getTdee("6", "0", "20", "180", "1.25", "Male");
        assertEquals(tdee, 2330.582, .01);
    }

}
