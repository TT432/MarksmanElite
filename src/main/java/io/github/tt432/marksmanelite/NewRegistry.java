package io.github.tt432.marksmanelite;

import com.mojang.serialization.MapCodec;
import io.github.tt432.marksmanelite.accessory.BasicAccessory;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = MarksmanElite.MODID)
public class NewRegistry {
    public static final  ResourceKey<Registry<MapCodec<? extends BasicAccessory<?>>>> ACCESSORY_TYPE_REGKEY =
            ResourceKey.createRegistryKey(MarksmanElite.byPath("accessory_type"));

    public static final Registry<MapCodec<? extends BasicAccessory<?>>> ACCESSORY_TYPE =
            new RegistryBuilder<>(ACCESSORY_TYPE_REGKEY).sync(true).create();

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        event.register(ACCESSORY_TYPE);
    }
}
