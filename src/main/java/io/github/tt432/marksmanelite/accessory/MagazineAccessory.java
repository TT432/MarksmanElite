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
            ResourceLocation.CODEC.fieldOf("ammo_type").forGetter(MagazineAccessory::getAmmoType)
    ).apply(i, MagazineAccessory::new));

    public final ResourceLocation ammoType;

    public MagazineAccessory(ValueProvider valueProvider, ResourceLocation modelLocation, ResourceLocation ammoType) {
        super(valueProvider, modelLocation);
        this.ammoType = ammoType;
    }


    public ResourceLocation getAmmoType() {
        return ammoType;
    }

    @Override
    public MapCodec<MagazineAccessory> getType() {
        return MarksmanElite.MAGAZINE_ACCESSORY.get();
    }
}
