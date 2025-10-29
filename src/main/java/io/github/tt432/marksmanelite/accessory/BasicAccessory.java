package io.github.tt432.marksmanelite.accessory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.tt432.marksmanelite.NewRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class BasicAccessory<T extends BasicAccessory<T>> {
    public static final Codec<? extends BasicAccessory<?>> CODEC =
            NewRegistry.ACCESSORY_TYPE.byNameCodec().dispatchStable(BasicAccessory::getType, Function.identity());


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
