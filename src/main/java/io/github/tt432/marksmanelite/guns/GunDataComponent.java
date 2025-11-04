package io.github.tt432.marksmanelite.guns;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tt432.marksmanelite.MarksmanElite;
import io.github.tt432.marksmanelite.NewRegistry;
import io.github.tt432.marksmanelite.accessory.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
                    .fieldOf("lowerHanging").forGetter(GunDataComponent::getLowerHanging),
            MarksmanElite.byNameCodec(NewRegistry.ACCESSORY_DATAPACK_REGKEY)
                    .flatXmap(a -> a instanceof MagazineAccessory b ? DataResult.success(b) : DataResult.error(() -> a + " is not a magazine accessory."), DataResult::success)
                    .fieldOf("magazine").forGetter(GunDataComponent::getMagazine),
            Codec.INT.fieldOf("ammo_count").forGetter(GunDataComponent::getAmmoCount)
    ).apply(i, GunDataComponent::new));
    public static final StreamCodec<ByteBuf, GunDataComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
    public static final Logger LOGGER = LogManager.getLogger();

    public final AbstractGun.InitialAccessory initalAcc;
    private boolean inited;
    private ScopeAccessory scope;//瞄具
    private MuzzleAccessory muzzle;//枪口
    private LowerHangingAccessory lowerHanging;//下挂
    private MagazineAccessory magazine;//弹匣
    //特殊配件 侧挂装置 枪托

    private int ammoCount = 0;


    public void refresh(ItemStack stack) {
        initIfNot();
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

    public GunDataComponent(AbstractGun.InitialAccessory gunType) {
        this.initalAcc = gunType;
        this.inited = false;
    }

    public GunDataComponent(ScopeAccessory scope, MuzzleAccessory muzzle, LowerHangingAccessory lowerHanging, MagazineAccessory magazine, int ammoCount) {
        this.initalAcc = null;
        this.inited = true;
        this.scope = scope;
        this.muzzle = muzzle;
        this.lowerHanging = lowerHanging;
        this.magazine = magazine;
        this.ammoCount = ammoCount;
    }

    public ScopeAccessory getScope() {
        initIfNot();
        return scope;
    }

    public MuzzleAccessory getMuzzle() {
        initIfNot();
        return muzzle;
    }

    public LowerHangingAccessory getLowerHanging() {
        initIfNot();
        return lowerHanging;
    }

    public MagazineAccessory getMagazine() {
        initIfNot();
        return magazine;
    }

    public void setScope(ScopeAccessory scope) {
        this.scope = scope;
    }

    public void setMuzzle(MuzzleAccessory muzzle) {
        this.muzzle = muzzle;
    }

    public void setLowerHanging(LowerHangingAccessory lowerHanging) {
        this.lowerHanging = lowerHanging;
    }

    public void setMagazine(MagazineAccessory magazine) {
        this.magazine = magazine;
    }


    private void initIfNot() {
        if (!inited) {
            inited = true;
            Registry<BasicAccessory<?>> registry = MarksmanElite.getRegistry(NewRegistry.ACCESSORY_DATAPACK_REGKEY);
            BasicAccessory<?> acc;

            acc = registry.get(initalAcc.scope());
            if (acc == null)
                throw new NullPointerException("Unable to find accessory with id = " + initalAcc.scope());
            if (!(acc instanceof ScopeAccessory scopeAccessory))
                throw new IllegalArgumentException("Unable to cast " + acc + "to a scope accessory.");
            this.scope = scopeAccessory;

            acc = registry.get(initalAcc.muzzle());
            if (acc == null)
                throw new NullPointerException("Unable to find accessory with id = " + initalAcc.muzzle());
            if (!(acc instanceof MuzzleAccessory muzzleAccessory))
                throw new IllegalArgumentException("Unable to cast " + acc + "to a muzzle accessory.");
            this.muzzle = muzzleAccessory;

            acc = registry.get(initalAcc.lowerHanging());
            if (acc == null)
                throw new NullPointerException("Unable to find accessory with id = " + initalAcc.lowerHanging());
            if (!(acc instanceof LowerHangingAccessory lowerHangingAccessory))
                throw new IllegalArgumentException("Unable to cast " + acc + "to a lower hanging accessory.");
            this.lowerHanging = lowerHangingAccessory;

            acc = registry.get(initalAcc.magazine());
            if (acc == null)
                throw new NullPointerException("Unable to find accessory with id = " + initalAcc.magazine());
            if (!(acc instanceof MagazineAccessory magazineAccessory))
                throw new IllegalArgumentException("Unable to cast " + acc + "to a magazine accessory.");
            this.magazine = magazineAccessory;

        }
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public boolean isInfinityAmmo() {
        return ammoCount == -1;
    }
}
