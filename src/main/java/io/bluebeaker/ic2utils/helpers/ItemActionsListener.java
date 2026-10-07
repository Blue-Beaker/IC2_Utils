package io.bluebeaker.ic2utils.helpers;

import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.block.heatgenerator.tileentity.TileEntityElectricHeatGenerator;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.kineticgenerator.tileentity.TileEntityElectricKineticGenerator;
import ic2.core.block.machine.tileentity.TileEntityLiquidHeatExchanger;
import io.bluebeaker.ic2utils.IC2UtilsConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

public class ItemActionsListener {
    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickBlock event) {
        if(!IC2UtilsConfig.sneakAddCoils) return;
        if(!event.getEntityPlayer().isSneaking()) {
            return;
        }
        ItemStack stack = event.getItemStack();

        BlockPos pos = event.getPos();
        World world = event.getWorld();

        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity == null || event.getFace() == null) {
            return;
        }

        InvSlot upgradeSlot = getUpgradeSlot(tileEntity);

        if (upgradeSlot == null) return;

        boolean accepted = false;

        if(upgradeSlot.accepts(stack)) {
            accepted = installItemIntoSlot(upgradeSlot, stack, 0);
        }

        if(accepted) {
            event.setUseItem(Event.Result.ALLOW);
            event.setUseBlock(Event.Result.DENY);
            event.setResult(Event.Result.ALLOW);
            event.getEntityPlayer().swingArm(event.getHand());

            if (event.getSide()== Side.SERVER){
                IC2.audioManager.playOnce(event.getEntityPlayer(), PositionSpec.Hand, "Tools/wrench.ogg", true, IC2.audioManager.getDefaultVolume());
            }
        }
    }

    private static boolean installItemIntoSlot(InvSlot upgradeSlot, ItemStack stack, int maxCount) {
        int countRemaining = maxCount>0 ? maxCount: stack.getCount();
        boolean accepted = false;
        int limit = upgradeSlot.getStackSizeLimit();
        for (int i = 0; i < upgradeSlot.size(); i++) {
            if(countRemaining<=0) break;

            ItemStack s1 = upgradeSlot.get(i);

            if(!s1.isEmpty() && (s1.getCount()>=limit || !ItemStack.areItemStacksEqual(s1, stack) || !ItemStack.areItemStackTagsEqual(s1, stack))) continue;

            int deltaCount = limit-s1.getCount(); // Slot space

            ItemStack spl = stack.splitStack(Math.min(deltaCount, countRemaining));
            countRemaining -= spl.getCount();
            if (spl.isEmpty()) continue;
            if(s1.isEmpty()){
                upgradeSlot.put(i, spl);
            }else {
                s1.grow(spl.getCount());
                upgradeSlot.put(i, s1);
            }
            accepted = true;
        }
        return accepted;
    }

    public static @Nullable InvSlot getUpgradeSlot(TileEntity tile) {
        if (tile instanceof TileEntityLiquidHeatExchanger){
            return ((TileEntityLiquidHeatExchanger) tile).heatexchangerslots;
        } else if (tile instanceof TileEntityElectricHeatGenerator) {
            return ((TileEntityElectricHeatGenerator) tile).coilSlot;
        } else if (tile instanceof TileEntityElectricKineticGenerator) {
            return ((TileEntityElectricKineticGenerator) tile).slotMotor;
        }

        return null;
    }
}
