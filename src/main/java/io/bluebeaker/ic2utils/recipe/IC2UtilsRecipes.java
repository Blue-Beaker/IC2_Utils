package io.bluebeaker.ic2utils.recipe;

import ic2.core.ref.ItemName;
import io.bluebeaker.ic2utils.IC2UtilsItems;
import io.bluebeaker.ic2utils.IC2UtilsMod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static io.bluebeaker.ic2utils.IC2UtilsConfig.wrenchRecipes;

@Mod.EventBusSubscriber
public class IC2UtilsRecipes {
    public static List<WrenchRecipe> getWrenchRecipes() {
        return WRENCH_RECIPES;
    }

    static List<WrenchRecipe> WRENCH_RECIPES=new ArrayList<>();

    private static void addRecipes() {
        if(wrenchRecipes){
            WRENCH_RECIPES.add(new WrenchRecipe(new ItemStack(IC2UtilsItems.ITEM_ELECTRIC_WRENCH_NEW),ItemName.electric_wrench.getItemStack(),new ResourceLocation(IC2UtilsMod.MODID,"electric_wrench_to_new")));
            WRENCH_RECIPES.add(new WrenchRecipe(ItemName.electric_wrench.getItemStack(),new ItemStack(IC2UtilsItems.ITEM_ELECTRIC_WRENCH_NEW),new ResourceLocation(IC2UtilsMod.MODID,"electric_wrench_from_new")));
        }
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event){
        addRecipes();
        for (WrenchRecipe wrenchRecipe : WRENCH_RECIPES) {
            ForgeRegistries.RECIPES.register(wrenchRecipe);
        }
    }
}
