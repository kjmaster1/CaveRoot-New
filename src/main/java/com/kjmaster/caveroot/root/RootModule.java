package com.kjmaster.caveroot.root;

import com.kjmaster.kjlib.blocks.BaseBlock;
import com.kjmaster.kjlib.datagen.DataGen;
import com.kjmaster.kjlib.datagen.Dob;
import com.kjmaster.kjlib.modules.IModule;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.compress.compressors.lz77support.LZ77Compressor;

import static com.kjmaster.caveroot.CaveRoot.tab;
import static com.kjmaster.caveroot.setup.Registration.BLOCKS;
import static com.kjmaster.caveroot.setup.Registration.ITEMS;

public class RootModule implements IModule {

    public static final RegistryObject<Block> CAVEROOT = BLOCKS.register("cave_root", CaveRootBlock::new);

    public static final RegistryObject<Item> CAVEROOT_ITEM = ITEMS.register("cave_root", tab(CaveRootItem::new));

    public static final RegistryObject<Block> DRIEDROOT = BLOCKS.register("dried_root", DriedRootBlock::new);

    @Override
    public void init(FMLCommonSetupEvent fmlCommonSetupEvent) {
    }

    @Override
    public void initClient(FMLClientSetupEvent fmlClientSetupEvent) {
        ItemBlockRenderTypes.setRenderLayer(CAVEROOT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(DRIEDROOT.get(), RenderType.cutout());
    }

    @Override
    public void initDatagen(DataGen dataGen) {
        dataGen.add(
                Dob.itemBuilder(CAVEROOT_ITEM)
                        .generatedItem("caveroot:items/cave_root")
                        .name("Cave Root")
        );
    }
}
