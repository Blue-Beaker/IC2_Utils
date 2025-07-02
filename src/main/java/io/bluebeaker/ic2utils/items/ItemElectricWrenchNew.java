package io.bluebeaker.ic2utils.items;

import ic2.api.item.*;
import ic2.core.IHitSoundOverride;
import ic2.core.item.IPseudoDamageItem;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ItemToolWrenchElectric;
import ic2.core.item.tool.ItemToolWrenchNew;
import ic2.core.item.tool.ToolClass;
import ic2.core.ref.ItemName;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class ItemElectricWrenchNew extends ItemTool implements IPseudoDamageItem, IElectricItem, IItemHudInfo,IEnhancedOverlayProvider, IHitSoundOverride , IBoxable {

    ItemToolWrenchNew newWrench;
    ItemToolWrenchElectric electricWrench;

    public final double energyPerUse = 100;

    public ItemElectricWrenchNew() {
        super(ToolMaterial.IRON,new HashSet<>());
        this.efficiency=8.0F;
        Item wrench = ItemName.wrench_new.getItemStack().getItem();

        setHarvestLevel(ToolClass.Wrench.getName(), HarvestLevel.Iron.level);

        if(wrench instanceof ItemToolWrenchNew){
            newWrench = (ItemToolWrenchNew) wrench;
        }
        Item wrenchElectric = ItemName.electric_wrench.getItemStack().getItem();
        if(wrenchElectric instanceof ItemToolWrenchElectric){
            electricWrench = (ItemToolWrenchElectric) wrenchElectric;
        }
    }
    @Override
    public boolean canProvideEnergy(ItemStack paramItemStack) {
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack paramItemStack) {
        return 12000D;
    }

    @Override
    public int getTier(ItemStack paramItemStack) {
        return 1;
    }

    @Override
    public double getTransferLimit(ItemStack paramItemStack) {
        return 250D;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, IBlockState state) {
        return canHarvestBlock(state, itemStack) ? this.efficiency : 1.0F;
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack itemStack) {
        if(newWrench==null || !ElectricItem.manager.canUse(itemStack,energyPerUse)) return false;
        return newWrench.canHarvestBlock(state,itemStack);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(newWrench==null || !ElectricItem.manager.canUse(stack,energyPerUse)) return EnumActionResult.FAIL;
        return newWrench.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
        return false;
    }
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> info, ITooltipFlag flagIn) {
        if(newWrench==null) return;
        newWrench.addInformation(stack, worldIn, info, flagIn);
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        ElectricItem.manager.use(stack, this.energyPerUse, attacker);
        return true;
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if (!worldIn.isRemote && (double)state.getBlockHardness(worldIn, pos) != 0.0D)
        {
            if(!ElectricItem.manager.canUse(stack,energyPerUse)){
                return false;
            }
            ElectricItem.manager.use(stack, energyPerUse, entityLiving);
        }
        return true;
    }

    @Override
    public boolean providesEnhancedOverlay(World paramWorld, BlockPos paramBlockPos, EnumFacing paramEnumFacing, EntityPlayer paramEntityPlayer, ItemStack paramItemStack) {
        if(newWrench ==null) return false;
        return newWrench.providesEnhancedOverlay(paramWorld, paramBlockPos, paramEnumFacing, paramEntityPlayer, paramItemStack);
    }

    @Override
    public String getHitSoundForBlock(EntityPlayerSP paramEntityPlayerSP, World paramWorld, BlockPos paramBlockPos, ItemStack paramItemStack) {
        if(newWrench ==null) return "";
        return newWrench.getHitSoundForBlock(paramEntityPlayerSP, paramWorld, paramBlockPos, paramItemStack);
    }

    @Override
    public String getBreakSoundForBlock(EntityPlayerSP paramEntityPlayerSP, World paramWorld, BlockPos paramBlockPos, ItemStack paramItemStack) {
        if(newWrench ==null) return "";
        return newWrench.getBreakSoundForBlock(paramEntityPlayerSP, paramWorld, paramBlockPos, paramItemStack);
    }

    @Override
    public List<String> getHudInfo(ItemStack paramItemStack, boolean paramBoolean) {
        List<String> info = new LinkedList<>();
        info.add(ElectricItem.manager.getToolTip(paramItemStack));
        return info;
    }

    @Override
    public void setStackDamage(ItemStack paramItemStack, int paramInt) {
        super.setDamage(paramItemStack,paramInt);
    }

    @Override
    public boolean canBeStoredInToolbox(ItemStack itemStack) {
      return true;
    }
}
