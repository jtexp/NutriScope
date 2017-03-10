package edu.utdallas.csdesign.spring17.nutriscope;

/**
 * Created by john on 2/23/17.
 */

public enum FoodNutrients {
    FAT(204),
    PROTEIN(203),
    CARBOHYDRATE(205),
    CALORIE(208);/*,
    FIBER,
    SUGARS,
    CALCIUM,
    IRON,
    MAGNESIUM,
    PHOSPHORUS,
    POTASSIUM,
    SODIUM,
    ZINC,
    SELENIUM,
    VITAMIN_A,
    VITAMIN_D,
    VITAMIN_C,
    THIAMIN,
    RIBOFLAVIN,
    NIACIN,
    VITAMIN_B6,
    VITAMIN_B12,
    VITAMIN_K,
    FOLIC_ACID,
    CHOLESTEROL;*/

    int nutrientId;

    FoodNutrients(int id) {
        this.nutrientId = id;
    }

    public int getNutrientId() {
        return this.nutrientId;
    }




}
