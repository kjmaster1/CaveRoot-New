package com.kjmaster.caveroot.torch;

import com.kjmaster.kjlib.datagen.DataGen;
import com.kjmaster.kjlib.datagen.Dob;
import com.kjmaster.kjlib.modules.IModule;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import static com.kjmaster.caveroot.CaveRoot.tab;
import static com.kjmaster.caveroot.setup.Registration.BLOCKS;
import static com.kjmaster.caveroot.setup.Registration.ITEMS;

public class TorchModule implements IModule {

    public static final RegistryObject<MultifaceBlock> TORCHFUNGI = BLOCKS.register("torch_fungi", TorchFungiBlock::new);
    public static final RegistryObject<TorchSporeBlock> TORCHSPORE = BLOCKS.register("torch_spore", TorchSporeBlock::new);
    public static final RegistryObject<Item> TORCHSPORE_ITEM = ITEMS.register("torch_spore", tab(TorchSporeItem::new));

    @Override
    public void init(FMLCommonSetupEvent fmlCommonSetupEvent) {

    }

    @Override
    public void initClient(FMLClientSetupEvent fmlClientSetupEvent) {
        ItemBlockRenderTypes.setRenderLayer(TORCHFUNGI.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TORCHSPORE.get(), RenderType.cutout());
    }

    @Override
    public void initDatagen(DataGen dataGen) {
        dataGen.add(
                Dob.itemBuilder(TORCHSPORE_ITEM)
                        .generatedItem("caveroot:items/torch_spore")
                        .name("Torch Spore")
        );
    }
}
