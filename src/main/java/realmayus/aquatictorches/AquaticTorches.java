package realmayus.aquatictorches;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod("aquatictorches")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AquaticTorches {

    private static final Logger LOGGER = LogManager.getLogger();

    public AquaticTorches() {
        LOGGER.info("Aquatic Torches loaded.");
    }

    @ObjectHolder("aquatictorches:aquatic_torch")
    public static AquaticTorchBlock AQUATIC_TORCH;

    @ObjectHolder("aquatictorches:aquatic_wall_torch")
    public static AquaticWallTorchBlock AQUATIC_WALL_TORCH;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new AquaticTorchBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((p_50886_) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME).setRegistryName("aquatictorches:aquatic_torch"));
        event.getRegistry().register(new AquaticWallTorchBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((p_152607_) -> 15).sound(SoundType.WOOD).lootFrom(() -> AQUATIC_TORCH), ParticleTypes.FLAME).setRegistryName("aquatictorches:aquatic_wall_torch"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new StandingAndWallBlockItem(AQUATIC_TORCH, AQUATIC_WALL_TORCH, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)).setRegistryName("aquatictorches:aquatic_torch"));
    }
}
