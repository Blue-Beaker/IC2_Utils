package io.bluebeaker.ic2utils.recipe;

import ic2.api.item.IElectricItem;
import ic2.core.item.armor.jetpack.JetpackAttachmentRecipe;
import ic2.core.item.armor.jetpack.JetpackHandler;
import ic2.core.ref.ItemName;
import io.bluebeaker.ic2utils.NBTUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class JetpackRemovalRecipe implements IRecipe {
    public final ItemStack input;
    public ResourceLocation id;

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        MatchedIngredients matched = getMatched(inv);
        ItemStack original = inv.getStackInSlot(matched.slotPlate);
        NBTTagCompound originalTag = original.getTagCompound();
        // Process jetpack
        NonNullList<ItemStack> remainingItems = IRecipe.super.getRemainingItems(inv);
        ItemStack jetpack = ItemName.jetpack_electric.getItemStack();
        // Move jetpack data
        NBTTagCompound nbtPack = new NBTTagCompound();
        NBTUtils.moveTag("toggleTimer",originalTag,nbtPack);
        NBTUtils.moveTag("hoverMode",originalTag,nbtPack);

        if(!(original.getItem() instanceof IElectricItem)) {
            NBTUtils.moveTag("charge",originalTag,nbtPack);
        }

        jetpack.setTagCompound(nbtPack);
        remainingItems.set(matched.slotPlate, jetpack);

        // Process tool
        if(matched.slotTool>=0){
            ItemStack tool = inv.getStackInSlot(matched.slotTool).copy();

            if(tool.isItemStackDamageable()){
                tool.attemptDamageItem(1,new Random(),null);
                remainingItems.set(matched.slotTool,tool);
            }
        }

        return remainingItems;
    }

    public JetpackRemovalRecipe(ItemStack input, ResourceLocation id) {
        this.input = input;
        this.id = id;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return isValid(getMatched(inv));
    }

    private MatchedIngredients getMatched(InventoryCrafting inv) {
        MatchedIngredients matched = new MatchedIngredients();
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            // Get tool
            if (!input.isEmpty() && input.isItemEqualIgnoreDurability(stack) && matched.slotTool ==-1) {
                matched.slotTool =i;
            // Get armor
            }else if(JetpackHandler.hasJetpack(stack) && !JetpackAttachmentRecipe.blacklistedItems.contains(stack.getItem()) && matched.slotPlate ==-1){
                matched.slotPlate =i;
            }else return new MatchedIngredients();
        }
        return matched;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        MatchedIngredients matched = getMatched(inv);
        ItemStack copiedChestplate = inv.getStackInSlot(matched.slotPlate).copy();
        JetpackHandler.setJetpackAttached(copiedChestplate,false);
        NBTTagCompound nbt = copiedChestplate.getTagCompound();
        if(nbt!=null){
            if(!(copiedChestplate.getItem() instanceof IElectricItem)) nbt.removeTag("charge");
            nbt.removeTag("toggleTimer");
            nbt.removeTag("hoverMode");
            if(nbt.isEmpty()){
                copiedChestplate.setTagCompound(null);
            }
        }
        return copiedChestplate;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= 1 && height >= 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
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

    public boolean isValid(MatchedIngredients ingredients){
        return ingredients.slotPlate >=0 && (input.isEmpty() || ingredients.slotTool >=0);
    }

    public static class MatchedIngredients{
        int slotPlate = -1;
        int slotTool = -1;
    }
}
