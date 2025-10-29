package io.github.tt432.marksmanelite.accessory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tt432.marksmanelite.MarksmanElite;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class ScopeAccessory extends BasicAccessory<ScopeAccessory> {
    public static final MapCodec<ScopeAccessory> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ValueProvider.CODEC.fieldOf("values").forGetter(BasicAccessory::getValueProvider),
            ResourceLocation.CODEC.fieldOf("model").forGetter(BasicAccessory::getModelLocation),
            Codec.FLOAT.fieldOf("magnification").forGetter(ScopeAccessory::getMagnification),
            ResourceLocation.CODEC.optionalFieldOf("overlay").forGetter(ScopeAccessory::getOverlayTexture)
    ).apply(i, ScopeAccessory::new));

    public final float magnification;
    public final Optional<ResourceLocation> overlayTexture;

    public ScopeAccessory(ValueProvider valueProvider, ResourceLocation modelLocation, float magnification, Optional<ResourceLocation> overlayTexture) {
        super(valueProvider, modelLocation);
        this.magnification = Math.max(1, magnification);
        this.overlayTexture = overlayTexture;
    }

    public ScopeAccessory(ValueProvider valueProvider, ResourceLocation modelLocation, float magnification) {
        super(valueProvider, modelLocation);
        this.magnification = magnification;
        this.overlayTexture = Optional.empty();
    }

    public ScopeAccessory(ValueProvider valueProvider, ResourceLocation modelLocation) {
        super(valueProvider, modelLocation);
        this.magnification = 1;
        this.overlayTexture = Optional.empty();
    }


    public float getMagnification() {
        return magnification;
    }

    public Optional<ResourceLocation> getOverlayTexture() {
        return overlayTexture;
    }

    @Override
    public MapCodec<ScopeAccessory> getType() {
        return MarksmanElite.SCOPE_ACCESSORY.get();
    }
}
