package io.github.tt432.marksmanelite.guns;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tt432.marksmanelite.MarksmanElite;
import io.github.tt432.marksmanelite.NewRegistry;
import io.github.tt432.marksmanelite.accessory.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class GunDataComponent {
    public static final ResourceLocation MODIFIER_ID = MarksmanElite.byPath("gun_item_attribute");
    public static final Codec<GunDataComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
            MarksmanElite.byNameCodec(NewRegistry.ACCESSORY_DATAPACK_REGKEY)
                    .flatXmap(a -> a instanceof ScopeAccessory b ? DataResult.success(b) : DataResult.error(() -> a + " is not a scope accessory."), DataResult::success)
                    .fieldOf("scope").forGetter(GunDataComponent::getScope),
            MarksmanElite.byNameCodec(NewRegistry.ACCESSORY_DATAPACK_REGKEY)
                    .flatXmap(a -> a instanceof MuzzleAccessory b ? DataResult.success(b) : DataResult.error(() -> a + " is not a muzzle accessory."), DataResult::success)
                    .fieldOf("muzzle").forGetter(GunDataComponent::getMuzzle),
            MarksmanElite.byNameCodec(NewRegistry.ACCESSORY_DATAPACK_REGKEY)
                    .flatXmap(a -> a instanceof LowerHangingAccessory b ? DataResult.success(b) : DataResult.error(() -> a + " is not a lower hanging accessory."), DataResult::success)
                    .fieldOf("lower_hanging").forGetter(GunDataComponent::getLowerHanging),
            MarksmanElite.byNameCodec(NewRegistry.ACCESSORY_DATAPACK_REGKEY)
                    .flatXmap(a -> a instanceof MagazineAccessory b ? DataResult.success(b) : DataResult.error(() -> a + " is not a magazine accessory."), DataResult::success)
                    .fieldOf("magazine").forGetter(GunDataComponent::getMagazine),
            Codec.INT.fieldOf("ammo_count").forGetter(GunDataComponent::getAmmoCount)
    ).apply(i, GunDataComponent::new));
    public static final StreamCodec<ByteBuf, GunDataComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    private ScopeAccessory scope;//瞄具
    private MuzzleAccessory muzzle;//枪口
    private LowerHangingAccessory lowerHanging;//下挂
    private MagazineAccessory magazine;//弹匣
    //特殊配件 侧挂装置 枪托

    private int ammoCount;


    public void refresh(ItemStack stack) {
        ValueProvider provider = ValueProvider.merge(scope.valueProvider, muzzle.valueProvider, lowerHanging.valueProvider, magazine.valueProvider);
        ItemAttributeModifiers modifiers = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(MODIFIER_ID, provider.damage(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(MODIFIER_ID, provider.speed(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(MarksmanElite.MANEUVERABILITY, new AttributeModifier(MODIFIER_ID, provider.maneuverability(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(MarksmanElite.STABILITY, new AttributeModifier(MODIFIER_ID, provider.stability(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(MarksmanElite.MAX_AMMO, new AttributeModifier(MODIFIER_ID, provider.maxAmmo(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();

        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);
    }

    public GunDataComponent(ScopeAccessory scope, MuzzleAccessory muzzle, LowerHangingAccessory lowerHanging, MagazineAccessory magazine, int ammoCount) {
        this.scope = scope;
        this.muzzle = muzzle;
        this.lowerHanging = lowerHanging;
        this.magazine = magazine;
        this.ammoCount = ammoCount;
    }

    public ScopeAccessory getScope() {
        return scope;
    }

    public MuzzleAccessory getMuzzle() {
        return muzzle;
    }

    public LowerHangingAccessory getLowerHanging() {
        return lowerHanging;
    }

    public MagazineAccessory getMagazine() {
        return magazine;
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public boolean isInfinityAmmo() {
        return ammoCount == -1;
    }
}
