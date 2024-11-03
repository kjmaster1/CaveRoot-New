package com.kjmaster.caveroot;

import com.kjmaster.caveroot.root.RootModule;
import com.kjmaster.caveroot.setup.ModSetup;
import com.kjmaster.caveroot.setup.Registration;
import com.kjmaster.caveroot.torch.TorchModule;
import com.kjmaster.kjlib.datagen.DataGen;
import com.kjmaster.kjlib.datagen.Dob;
import com.kjmaster.kjlib.modules.Modules;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.obj.ObjLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CaveRoot.MODID)
public class CaveRoot
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "caveroot";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("PublicField")
    public static final ModSetup setup = new ModSetup();

    @SuppressWarnings("PublicField")
    public static CaveRoot instance;

    private final Modules modules = new Modules();

    public CaveRoot()
    {

        instance = this;
        setupModules();

        Registration.register();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(setup::init);
        bus.addListener(modules::init);
        bus.addListener(this::onDataGen);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(modules::initClient);
        });
    }

    private void onDataGen(GatherDataEvent event) {
        DataGen datagen = new DataGen(MODID, event);
        datagen.add(Dob.builder().keyedMessage("itemGroup.caveroot", "Cave Root"));
        modules.datagen(datagen);
        datagen.generate();
    }

    public static <T extends Item> Supplier<T> tab(Supplier<T> supplier) {
        return setup.tab(supplier);
    }

    private void setupModules() {
        modules.register(new RootModule());
        modules.register(new TorchModule());
    }
}
