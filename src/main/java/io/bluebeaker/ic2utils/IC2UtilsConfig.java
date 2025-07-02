package io.bluebeaker.ic2utils;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Type;

@Config(modid = IC2Utils.MODID,type = Type.INSTANCE,category = "general")
public class IC2UtilsConfig {
    @Comment("Example")
    @LangKey("config.ic2utils.example.name")
    public static boolean example = true;
}