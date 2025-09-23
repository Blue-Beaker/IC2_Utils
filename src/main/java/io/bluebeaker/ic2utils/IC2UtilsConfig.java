package io.bluebeaker.ic2utils;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Type;

@Config(modid = IC2UtilsMod.MODID,type = Type.INSTANCE,category = "general")
public class IC2UtilsConfig {
    @Comment("Disable to remove default recipes of Electric Wrench (New).")
    @LangKey("config.ic2utils.wrenchRecipes.name")
    @Config.RequiresMcRestart
    public static boolean wrenchRecipes = true;

    @Comment({"Tool needed to split Jetpack from chestplates.",
        "format: modid:id[:metadata]",
        "Write minecraft:air for no tool needed.",
        "Leave this list empty to disable the features."})
    @LangKey("config.ic2utils.jetpackRemovalTool.name")
    @Config.RequiresMcRestart
    public static String[] jetpackRemovalTool = {"minecraft:air"};
}