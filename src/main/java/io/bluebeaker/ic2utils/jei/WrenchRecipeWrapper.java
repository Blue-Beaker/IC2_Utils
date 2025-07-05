package io.bluebeaker.ic2utils.jei;

import io.bluebeaker.ic2utils.recipe.WrenchRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;

public class WrenchRecipeWrapper implements ICraftingRecipeWrapper {
    protected final WrenchRecipe recipe;
    public WrenchRecipeWrapper(WrenchRecipe recipe){
        this.recipe=recipe;
    }
    @Override
    public void getIngredients(IIngredients iIngredients) {
        iIngredients.setInput(VanillaTypes.ITEM,recipe.input);
        iIngredients.setOutput(VanillaTypes.ITEM,recipe.output);
    }
}
