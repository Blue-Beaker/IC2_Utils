package io.bluebeaker.ic2utils;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.item.ElectricItemManager;
import ic2.core.ref.ItemName;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import javax.annotation.Nullable;

import static io.bluebeaker.ic2utils.IC2UtilsConfig.wrenchRecipes;

@Mod.EventBusSubscriber
public class IC2UtilsRecipes {
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent<IRecipe> event){
        if(wrenchRecipes){
            ForgeRegistries.RECIPES.register(new WrenchRecipe(new ItemStack(IC2UtilsItems.ITEM_ELECTRIC_WRENCH_NEW),ItemName.electric_wrench.getItemStack(),new ResourceLocation(IC2UtilsMod.MODID,"electric_wrench_to_new")));
            ForgeRegistries.RECIPES.register(new WrenchRecipe(ItemName.electric_wrench.getItemStack(),new ItemStack(IC2UtilsItems.ITEM_ELECTRIC_WRENCH_NEW),new ResourceLocation(IC2UtilsMod.MODID,"electric_wrench_from_new")));
        }
    }
    public static class WrenchRecipe implements IRecipe{
        public final ItemStack output;
        public final ItemStack input;
        public ResourceLocation id;

        public WrenchRecipe(ItemStack output, ItemStack input, ResourceLocation id) {
            this.output = output;
            this.input = input;
            this.id=id;
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
                    if(!stack.isEmpty()){
                        if(matched !=null)
                            return ItemStack.EMPTY;
                        matched =stack;
                    }
                }
            }
            if(matched!=null && input.isItemEqualIgnoreDurability(matched)){
                return matched;
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {
            ItemStack matched = getMatched(inv);
            ItemStack result = new ItemStack(output.getItem(),1,matched.getMetadata());

            NBTTagCompound tagCompound = matched.getTagCompound();
            if(tagCompound==null) return result;

            result.setTagCompound(matched.getTagCompound());
            return result;
        }

        @Override
        public boolean canFit(int width, int height) {
            return width>=1 && height>=1;
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
            this.id=name;
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
}
