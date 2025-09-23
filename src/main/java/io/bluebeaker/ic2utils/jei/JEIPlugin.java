package io.bluebeaker.ic2utils.jei;

import io.bluebeaker.ic2utils.recipe.IC2UtilsRecipes;
import io.bluebeaker.ic2utils.recipe.JetpackRemovalRecipe;
import io.bluebeaker.ic2utils.recipe.WrenchRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        List<IRecipeWrapper> recipes = new ArrayList<>();
        for (WrenchRecipe wrenchRecipe : IC2UtilsRecipes.getWrenchRecipes()) {
            recipes.add(new WrenchRecipeWrapper(wrenchRecipe));
        }

        for (JetpackRemovalRecipe recipe : IC2UtilsRecipes.getJetpackRemovalRecipes()){
            for (ItemStack applicableItem : JetpackRemovalRecipeWrapper.getApplicableItems()) {
                recipes.add(new JetpackRemovalRecipeWrapper(recipe,applicableItem));
            }

        }

        registry.addRecipes(recipes, VanillaRecipeCategoryUid.CRAFTING);
    }
}
