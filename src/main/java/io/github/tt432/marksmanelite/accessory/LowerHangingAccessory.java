package io.github.tt432.marksmanelite.accessory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tt432.marksmanelite.MarksmanElite;
import net.minecraft.resources.ResourceLocation;
import org.joml.Math;

public class LowerHangingAccessory extends BasicAccessory<LowerHangingAccessory> {
    public static final MapCodec<LowerHangingAccessory> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ValueProvider.CODEC.fieldOf("values").forGetter(BasicAccessory::getValueProvider),
            ResourceLocation.CODEC.fieldOf("model").forGetter(BasicAccessory::getModelLocation)
    ).apply(i, LowerHangingAccessory::new));

    public LowerHangingAccessory(ValueProvider valueProvider, ResourceLocation modelLocation) {
        super(valueProvider, modelLocation);
    }

    @Override
    public MapCodec<LowerHangingAccessory> getType() {
        return MarksmanElite.LOWER_HANGING_ACCESSORY.get();
    }
}
