package io.github.tt432.marksmanelite;

import com.mojang.serialization.MapCodec;
import io.github.tt432.marksmanelite.accessory.BasicAccessory;
import io.github.tt432.marksmanelite.accessory.ScopeAccessory;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(MarksmanElite.MODID)
public class MarksmanElite {
    public static final String MODID = "marksman_elite";

    public MarksmanElite(IEventBus eventBus) {
        ACCESSORY_REG.register(eventBus);
    }

    public static ResourceLocation byPath(String path) {
        return ResourceLocation.fromNamespaceAndPath(MarksmanElite.MODID, path);
    }

    private static final DeferredRegister<MapCodec<? extends BasicAccessory<?>>> ACCESSORY_REG =
            DeferredRegister.create(NewRegistry.ACCESSORY_TYPE, MarksmanElite.MODID);

    public static final DeferredHolder<MapCodec<? extends BasicAccessory<?>>, MapCodec<ScopeAccessory>> SCOPE_ACCESSORY =
            ACCESSORY_REG.register("scope", () -> ScopeAccessory.CODEC);
}
