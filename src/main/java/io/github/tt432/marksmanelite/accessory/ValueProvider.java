package io.github.tt432.marksmanelite.accessory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ValueProvider(float damage, float speed, float controllability, float stability, int maxAmmo) {
    public static final Codec<ValueProvider> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.FLOAT.fieldOf("damage").orElse(0f).forGetter(ValueProvider::damage),
            Codec.FLOAT.fieldOf("speed").orElse(0f).forGetter(ValueProvider::speed),
            Codec.FLOAT.fieldOf("controllability").orElse(0f).forGetter(ValueProvider::controllability),
            Codec.FLOAT.fieldOf("stability").orElse(0f).forGetter(ValueProvider::stability),
            Codec.INT.fieldOf("maxAmmo").orElse(1).forGetter(ValueProvider::maxAmmo)
    ).apply(i, ValueProvider::new));

    public ValueProvider merge(ValueProvider... others) {
        if (others.length == 0) return this;
        float damage = this.damage;
        float speed = this.speed;
        float controllability = this.controllability;
        float stability = this.stability;
        int maxAmmo = this.maxAmmo;
        for (ValueProvider other : others) {
            damage += other.damage;
            speed += other.speed;
            controllability += other.controllability;
            stability += other.stability;
            maxAmmo = Math.max(maxAmmo, other.maxAmmo);
        }
        return new ValueProvider(damage, speed, controllability, stability, Math.max(1, maxAmmo));
    }
}
