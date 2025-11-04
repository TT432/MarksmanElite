package io.github.tt432.marksmanelite.accessory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tt432.marksmanelite.MarksmanElite;
import net.minecraft.resources.ResourceLocation;
import org.joml.Math;

public class MagazineAccessory extends BasicAccessory<MagazineAccessory> {
    public static final MapCodec<MagazineAccessory> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ValueProvider.CODEC.fieldOf("values").forGetter(BasicAccessory::getValueProvider),
            ResourceLocation.CODEC.fieldOf("model").forGetter(BasicAccessory::getModelLocation),
            Codec.INT.fieldOf("max_ammo").forGetter(MagazineAccessory::getMaxAmmo)
    ).apply(i, MagazineAccessory::new));

    public final int maxAmmoCount;

    public MagazineAccessory(ValueProvider valueProvider, ResourceLocation modelLocation, int maxAmmoCount) {
        super(valueProvider, modelLocation);
        this.maxAmmoCount = Math.max(1, maxAmmoCount);
    }


    public int getMaxAmmo() {
        return maxAmmoCount;
    }

    @Override
    public MapCodec<MagazineAccessory> getType() {
        return MarksmanElite.MAGAZINE_ACCESSORY.get();
    }
}
