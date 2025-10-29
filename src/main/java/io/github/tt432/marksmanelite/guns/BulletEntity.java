package io.github.tt432.marksmanelite.guns;

import io.github.tt432.marksmanelite.MarksmanElite;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BulletEntity extends AbstractHurtingProjectile{
    private int life;
    private final float damage = 5.0f; // 子弹伤害

    public BulletEntity(EntityType<BulletEntity> type, Level level) {
        super(type, level);
    }

    public BulletEntity(Level level, LivingEntity shooter, Vec3 movement) {
        super(MarksmanElite.BULLET_ENTITY.get(), shooter, movement, level);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide) {
            // 击中效果
            if (result.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityResult = (EntityHitResult) result;
                Entity target = entityResult.getEntity();

                if (target instanceof LivingEntity livingTarget) {
                    livingTarget.hurt(this.damageSources().indirectMagic(this, this.getOwner()), damage);
                }
            }

            // 产生爆炸效果
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0f, false, Level.ExplosionInteraction.NONE);

            this.discard(); // 移除实体
        }
    }

    @Override
    public void tick() {
        super.tick();

        // 子弹生命周期管理
        if (++this.life > 100) { // 100 tick 后消失
            this.discard();
            return;
        }

        // 粒子效果
        if (this.level().isClientSide) {
            for (int i = 0; i < 2; i++) {
                this.level().addParticle(ParticleTypes.SMOKE,
                        this.getX() + (this.random.nextDouble() - 0.5) * 0.5,
                        this.getY() + 0.5,
                        this.getZ() + (this.random.nextDouble() - 0.5) * 0.5,
                        0, 0, 0);
            }
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false; // 子弹不燃烧
    }
}
