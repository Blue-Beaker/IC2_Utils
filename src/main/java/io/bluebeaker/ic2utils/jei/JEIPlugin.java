package io.bluebeaker.ic2utils.jei;

import io.bluebeaker.ic2utils.recipe.IC2UtilsRecipes;
import io.bluebeaker.ic2utils.recipe.WrenchRecipe;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
    private IJeiRuntime jeiRuntime;
    @Override
    public void register(IModRegistry registry) {
        List<WrenchRecipeWrapper> recipes = new ArrayList<>();
        for (WrenchRecipe wrenchRecipe : IC2UtilsRecipes.getWrenchRecipes()) {
            recipes.add(new WrenchRecipeWrapper(wrenchRecipe));
        }
        registry.addRecipes(recipes, VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        this.jeiRuntime=jeiRuntime;
    }
}
