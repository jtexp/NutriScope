package edu.utdallas.csdesign.spring17.nutriscope.data.food;

import dagger.Module;
import dagger.Provides;
import edu.utdallas.csdesign.spring17.nutriscope.data.source.ndb.FoodReportClient;
import edu.utdallas.csdesign.spring17.nutriscope.data.source.ndb.FoodReportService;

/**
 * Created by john on 3/18/17.
 */

@Module
public class FoodRepositoryModule {

    private final FoodRepository foodRepository;

    public FoodRepositoryModule(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Provides
    FoodRepository providesFoodRepository() {
        return foodRepository;
    }
}
