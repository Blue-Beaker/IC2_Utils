package io.bluebeaker.ic2utils.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WrenchRecipe implements IRecipe {
    public final ItemStack output;
    public final ItemStack input;
    public ResourceLocation id;

    public WrenchRecipe(ItemStack output, ItemStack input, ResourceLocation id) {
        this.output = output;
        this.input = input;
        this.id = id;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return !getMatched(inv).isEmpty();
    }

    private ItemStack getMatched(InventoryCrafting inv) {
        ItemStack matched = null;
        for (int y = 0; y < inv.getHeight(); y++) {
            for (int x = 0; x < inv.getWidth(); x++) {
                ItemStack stack = inv.getStackInRowAndColumn(y, x);
                if (!stack.isEmpty()) {
                    if (matched != null)
                        return ItemStack.EMPTY;
                    matched = stack;
                }
            }
        }
        if (matched != null && input.isItemEqualIgnoreDurability(matched)) {
            return matched;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack matched = getMatched(inv);
        ItemStack result = new ItemStack(output.getItem(), 1, matched.getMetadata());

        NBTTagCompound tagCompound = matched.getTagCompound();
        if (tagCompound == null) return result;

        result.setTagCompound(matched.getTagCompound());
        return result;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= 1 && height >= 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.from(Ingredient.fromStacks(this.input));
    }

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        this.id = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return id;
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return IRecipe.class;
    }
}
