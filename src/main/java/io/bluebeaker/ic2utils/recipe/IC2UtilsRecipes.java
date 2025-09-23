package io.bluebeaker.ic2utils.recipe;

import ic2.api.item.IC2Items;
import ic2.core.IC2;
import ic2.core.ref.ItemName;
import io.bluebeaker.ic2utils.IC2UtilsConfig;
import io.bluebeaker.ic2utils.IC2UtilsItems;
import io.bluebeaker.ic2utils.IC2UtilsMod;
import net.minecraft.item.Item;
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

import static io.bluebeaker.ic2utils.IC2UtilsConfig.jetpackRemovalRecipe;
import static io.bluebeaker.ic2utils.IC2UtilsConfig.wrenchRecipes;

@Mod.EventBusSubscriber
public class IC2UtilsRecipes {
    public static List<WrenchRecipe> getWrenchRecipes() {
        return WRENCH_RECIPES;
    }

    static List<WrenchRecipe> WRENCH_RECIPES=new ArrayList<>();
    static List<JetpackRemovalRecipe> JETPACK_REMOVAL_RECIPE = new ArrayList<>();

    private static void addRecipes() {
        if(wrenchRecipes){
            WRENCH_RECIPES.add(new WrenchRecipe(new ItemStack(IC2UtilsItems.ITEM_ELECTRIC_WRENCH_NEW),ItemName.electric_wrench.getItemStack(),new ResourceLocation(IC2UtilsMod.MODID,"electric_wrench_to_new")));
            WRENCH_RECIPES.add(new WrenchRecipe(ItemName.electric_wrench.getItemStack(),new ItemStack(IC2UtilsItems.ITEM_ELECTRIC_WRENCH_NEW),new ResourceLocation(IC2UtilsMod.MODID,"electric_wrench_from_new")));
        }
        if(jetpackRemovalRecipe){
            for (String tool:IC2UtilsConfig.jetpackRemovalTool){
                ItemStack stack = getStackFromString(tool);
                if(stack==null) continue;
                JETPACK_REMOVAL_RECIPE.add(new JetpackRemovalRecipe(stack,new ResourceLocation(IC2UtilsMod.MODID,"jetpack_remove_"+(tool.replace(":","_")))));
            }
        }
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event){
        addRecipes();
        for (WrenchRecipe wrenchRecipe : WRENCH_RECIPES) {
            ForgeRegistries.RECIPES.register(wrenchRecipe);
        }
        for(JetpackRemovalRecipe recipe : JETPACK_REMOVAL_RECIPE){
            ForgeRegistries.RECIPES.register(recipe);
        }
    }

    public static ItemStack getStackFromString(String repr){
        String[] split = repr.split(":");
        ResourceLocation id;
        if(split.length==1){
            id=new ResourceLocation(split[0]);
        }else{
            id=new ResourceLocation(split[0],split[1]);
        }
        Item item = Item.REGISTRY.getObject(id);
        if(item!=null){
            ItemStack stack = new ItemStack(item);
            if(split.length>=3){
                try {
                    stack.setItemDamage(Integer.parseInt(split[2]));
                }catch (NumberFormatException e){
                    IC2UtilsMod.getLogger().error("Error parsing item {}:",repr,e);
                }
            }
            return stack;
        }
        return null;
    }
}
