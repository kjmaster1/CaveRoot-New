package com.kjmaster.caveroot.torch;

import com.kjmaster.kjlib.items.BaseItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class TorchSporeItem extends ItemNameBlockItem implements IPlantable {

    public TorchSporeItem() {
        super(TorchModule.TORCHSPORE.get(), new Item.Properties());
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.CAVE;
    }

    @Override
    public BlockState getPlant(BlockGetter level, BlockPos pos) {
        return TorchModule.TORCHSPORE.get().defaultBlockState();
    }
}
