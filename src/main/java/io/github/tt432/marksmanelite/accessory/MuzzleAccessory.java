package io.github.tt432.marksmanelite.accessory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tt432.marksmanelite.MarksmanElite;
import net.minecraft.resources.ResourceLocation;
import org.joml.Math;

import java.util.Optional;

public class MuzzleAccessory extends BasicAccessory<MuzzleAccessory> {
    public static final MapCodec<MuzzleAccessory> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ValueProvider.CODEC.fieldOf("values").forGetter(BasicAccessory::getValueProvider),
            ResourceLocation.CODEC.fieldOf("model").forGetter(BasicAccessory::getModelLocation),
            Codec.FLOAT.fieldOf("damping").forGetter(MuzzleAccessory::getDamping)
    ).apply(i, MuzzleAccessory::new));

    public final float damping;

    public MuzzleAccessory(ValueProvider valueProvider, ResourceLocation modelLocation, float damping) {
        super(valueProvider, modelLocation);
        this.damping = Math.clamp(0,1,damping);
    }

    public MuzzleAccessory(ValueProvider valueProvider, ResourceLocation modelLocation) {
        super(valueProvider, modelLocation);
        this.damping = 1;
    }


    public float getDamping() {
        return damping;
    }

    @Override
    public MapCodec<MuzzleAccessory> getType() {
        return MarksmanElite.MUZZLE_ACCESSORY.get();
    }
}
