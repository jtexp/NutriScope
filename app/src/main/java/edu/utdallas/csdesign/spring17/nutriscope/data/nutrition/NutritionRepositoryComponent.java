package edu.utdallas.csdesign.spring17.nutriscope.data.nutrition;

import dagger.Component;
import edu.utdallas.csdesign.spring17.nutriscope.ApplicationModule;

/**
 * Created by john on 4/17/17.
 */


@Component(modules = {NutritionRepositoryModule.class, ApplicationModule.class})
public interface NutritionRepositoryComponent {
    NutritionRepository getNutritionRepository();
}


