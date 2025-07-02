package io.bluebeaker.ic2utils;

import ic2.core.CreativeTabIC2;
import ic2.core.IC2;
import io.bluebeaker.ic2utils.items.ItemElectricWrenchNew;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class IC2UtilsItems {
    public static ItemElectricWrenchNew ITEM_ELECTRIC_WRENCH_NEW=
            (ItemElectricWrenchNew) addItem(new ItemElectricWrenchNew(),"electric_wrench_new");

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ITEM_ELECTRIC_WRENCH_NEW);
        ITEM_ELECTRIC_WRENCH_NEW.setCreativeTab(IC2.tabIC2);
        if(!IC2Utils.isServer()){
            ModelLoader.setCustomModelResourceLocation(ITEM_ELECTRIC_WRENCH_NEW,0,new ModelResourceLocation(ITEM_ELECTRIC_WRENCH_NEW.getRegistryName(),"inventory"));
        }
    }
    private static Item addItem(Item item, String id){
        item.setRegistryName(IC2Utils.MODID,id);
        item.setTranslationKey(IC2Utils.MODID+"."+id);
        return item;
    }
}
