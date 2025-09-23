package io.bluebeaker.ic2utils.jei;

import ic2.core.item.armor.jetpack.JetpackAttachmentRecipe;
import ic2.core.item.armor.jetpack.JetpackHandler;
import ic2.core.util.ItemComparableItemStack;
import ic2.jeiIntegration.recipe.crafting.JetpackRecipeWrapper;
import io.bluebeaker.ic2utils.recipe.JetpackRemovalRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.lwjgl.util.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JetpackRemovalRecipeWrapper implements ICraftingRecipeWrapper {
    private static final List<ItemStack> applicableItems = new ArrayList<>();
    protected final JetpackRemovalRecipe recipe;
    public ItemStack input;
    public ItemStack output;
    public JetpackRemovalRecipeWrapper(JetpackRemovalRecipe recipe, ItemStack chestplate){
        this.recipe=recipe;
        this.input=chestplate.copy();
        this.output=chestplate;
        JetpackHandler.setJetpackAttached(this.input,true);
    }
    @Override
    public void getIngredients(IIngredients iIngredients) {
        ArrayList<ItemStack> inputs = new ArrayList<>();
        inputs.add(this.input);
        inputs.add(recipe.input);
        iIngredients.setInputs(VanillaTypes.ITEM, inputs);
        iIngredients.setOutput(VanillaTypes.ITEM, this.output);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        String text = I18n.format("recipe.ic2utils.jetpack_split.text");
        minecraft.fontRenderer.drawString(text,recipeWidth-minecraft.fontRenderer.getStringWidth(text),recipeHeight-10, 0xFF8B8B8B);
    }

    public static List<ItemStack> getApplicableItems(){
        if(!applicableItems.isEmpty()) return applicableItems;
        Set<ItemComparableItemStack> added = new HashSet<>();
        for (Item item : ForgeRegistries.ITEMS) {
            if (JetpackAttachmentRecipe.blacklistedItems.contains(item)) continue;
            NonNullList<ItemStack> stacks = NonNullList.create();
            item.getSubItems(CreativeTabs.SEARCH, stacks);
            for (ItemStack stack : stacks) {
                if (EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.CHEST) {
                    ItemComparableItemStack comparable = new ItemComparableItemStack(stack, false);
                    if (!added.contains(comparable)) {
                        applicableItems.add(stack);
                        added.add(comparable);
                    }
                }
            }
        }
        return applicableItems;
    }
}
