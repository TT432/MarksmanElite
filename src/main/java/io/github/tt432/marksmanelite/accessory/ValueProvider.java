package io.github.tt432.marksmanelite.accessory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ValueProvider(float damage, float speed, float maneuverability, float stability, int maxAmmo) {
    public static final Codec<ValueProvider> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.FLOAT.fieldOf("damage").orElse(0f).forGetter(ValueProvider::damage),
            Codec.FLOAT.fieldOf("speed").orElse(0f).forGetter(ValueProvider::speed),
            Codec.FLOAT.fieldOf("maneuverability").orElse(0f).forGetter(ValueProvider::maneuverability),
            Codec.FLOAT.fieldOf("stability").orElse(0f).forGetter(ValueProvider::stability),
            Codec.INT.fieldOf("maxAmmo").orElse(1).forGetter(ValueProvider::maxAmmo)
    ).apply(i, ValueProvider::new));

    public static final ValueProvider EMPTY = new ValueProvider(0, 0, 0, 0, 1);

    public static ValueProvider merge(ValueProvider... providers) {
        if (providers.length == 0) return EMPTY;
        float damage = 0;
        float speed = 0;
        float maneuverability = 0;
        float stability = 0;
        int maxAmmo = 1;
        for (ValueProvider other : providers) {
            damage += other.damage;
            speed += other.speed;
            maneuverability += other.maneuverability;
            stability += other.stability;
            maxAmmo = Math.max(maxAmmo, other.maxAmmo);
        }
        return new ValueProvider(damage, speed, maneuverability, stability, Math.max(1, maxAmmo));
    }
}
