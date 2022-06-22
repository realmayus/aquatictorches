package realmayus.aquatictorches;


import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid="aquatictorches", bus=Mod.EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class ClientRegistry {

    @SubscribeEvent
    public static void registerItems(FMLClientSetupEvent event)
    {
        System.out.println("REGISTER ITEMS CLIENT");
        ItemBlockRenderTypes.setRenderLayer(AquaticTorches.AQUATIC_TORCH.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(AquaticTorches.AQUATIC_WALL_TORCH.get(), RenderType.cutout());
    }
}
