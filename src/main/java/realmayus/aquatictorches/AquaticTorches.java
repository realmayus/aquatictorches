package realmayus.aquatictorches;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod("aquatictorches")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AquaticTorches {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "aquatictorches");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "aquatictorches");
    public static final RegistryObject<AquaticTorchBlock> AQUATIC_TORCH = BLOCKS.register("aquatic_torch", () -> new AquaticTorchBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((p_50886_) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<AquaticWallTorchBlock> AQUATIC_WALL_TORCH = BLOCKS.register("aquatic_wall_torch", () -> new AquaticWallTorchBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((p_152607_) -> 15).sound(SoundType.WOOD).lootFrom(AQUATIC_TORCH), ParticleTypes.FLAME));
    public static final RegistryObject<StandingAndWallBlockItem> AQUATIC_TORCH_ITEM = ITEMS.register("aquatic_torch", () -> new StandingAndWallBlockItem(AQUATIC_TORCH.get(), AQUATIC_WALL_TORCH.get(), new Item.Properties(), Direction.DOWN));

    public AquaticTorches() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        LOGGER.info("Aquatic Torches loaded.");
    }

    @SubscribeEvent
    public static void onCreativeModeTabBuildContents(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == CreativeModeTabs.FUNCTIONAL_BLOCKS)
            event.getEntries().putAfter(new ItemStack(Items.REDSTONE_TORCH), new ItemStack(AQUATIC_TORCH_ITEM.get()), TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}
