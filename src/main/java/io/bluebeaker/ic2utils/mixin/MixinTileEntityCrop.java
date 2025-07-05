package io.bluebeaker.ic2utils.mixin;

import ic2.core.crop.TileEntityCrop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

import static io.bluebeaker.ic2utils.IC2UtilsConfig.noTrampleCrops;

@Mixin(value = TileEntityCrop.class,remap = false)
public class MixinTileEntityCrop {
    @Redirect(method = "onEntityCollision",at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
    public int preventTrample(Random instance, int i){
        if(noTrampleCrops) return 1;
        return instance.nextInt(i);
    }
}
