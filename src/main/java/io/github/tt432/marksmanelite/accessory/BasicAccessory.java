package io.github.tt432.marksmanelite.accessory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.tt432.marksmanelite.NewRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Function;

public abstract class BasicAccessory<T extends BasicAccessory<T>> {
    public static final Codec<BasicAccessory<?>> CODEC =
            NewRegistry.ACCESSORY_TYPE.byNameCodec().dispatchStable(BasicAccessory::getType, Function.identity());

    public static final Codec<Holder<BasicAccessory<?>>> REFERENCE_CODEC =
            RegistryFileCodec.create(NewRegistry.ACCESSORY_DATAPACK_REGKEY, CODEC);

    public static final Codec<HolderSet<BasicAccessory<?>>> LIST_CODEC =
            RegistryCodecs.homogeneousList(NewRegistry.ACCESSORY_DATAPACK_REGKEY, CODEC);


    public final ValueProvider valueProvider;
    public final ResourceLocation modelLocation;

    protected BasicAccessory(ValueProvider valueProvider, ResourceLocation modelLocation) {
        this.valueProvider = valueProvider;
        this.modelLocation = modelLocation;
    }

    public ResourceLocation getModelLocation() {
        return modelLocation;
    }

    public ValueProvider getValueProvider() {
        return valueProvider;
    }

    public abstract MapCodec<T> getType();

    public T cast() {
        return (T) this;
    }
}
