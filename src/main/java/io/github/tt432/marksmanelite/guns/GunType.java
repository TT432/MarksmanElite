package io.github.tt432.marksmanelite.guns;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;

public record GunType(Holder<Item> gunItem, AmmoBehavior ammoBehavior) {


    public enum AmmoBehavior {
        NO_GRAVITY((player, stack) -> {
            Level level = player.level();
            Vec3 lookVec = player.getLookAngle();
            double speed = 20.0; // 子弹速度

            // 创建子弹实体
            BulletEntity bullet = new BulletEntity(level, player, lookVec.scale(speed),player.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
            Vec3 eyePos = player.getEyePosition(0);//TODO 应为模型位置
            bullet.setPos(eyePos.x, eyePos.y, eyePos.z);

            level.addFreshEntity(bullet);
        });
//        GRAVITY,
//        DIRECTLY;

        public final BiConsumer<Player, ItemStack> behavior;

        AmmoBehavior(BiConsumer<Player, ItemStack> behavior) {
            this.behavior = behavior;
        }
    }
}
