package io.bluebeaker.ic2utils;

import net.minecraft.nbt.NBTTagCompound;

public class NBTUtils {
    /** Move the nbt tag with specified key from nbt1 to nbt2.
     * @param key Key for the nbt to be moved.
     * @param nbt1 NBT1
     * @param nbt2 NBT2
     */
    public static void moveTag(String key, NBTTagCompound nbt1, NBTTagCompound nbt2){
        if(nbt1==null) return;
        if(nbt2==null) nbt2=new NBTTagCompound();
        if(nbt1.hasKey(key)){
            nbt2.setTag(key,nbt1.getTag(key));
        }
    }
}
