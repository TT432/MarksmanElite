package io.github.tt432.marksmanelite.player;

import io.github.tt432.marksmanelite.guns.AbstractGun;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerGunState {
    public static final int AIMING_TIME = 5;//瞄准时间5tick
    public static final int PREPARE_TIME = 10;//准备时间10tick

    private boolean leftDown = false;
    private boolean rightDown = false;
    private int leftTicker = 0;
    private int rightTicker = 0;
    private ItemStack lastStack = ItemStack.EMPTY;
    private int keepStackTicker = 0;

    private boolean shooting = false;
    private boolean aiming = false;
    private boolean aimingFinished = false;
    private boolean reloading = false;

    private int shootingTicker = 0;
    private int aimingTicker = 0;
    private int stopAimingTicker = 0;

    public void handle(MouseClickPayload payload, Player player) {
        if (payload.isLeft()) {
            this.leftDown = payload.isPress();
            if (this.leftDown) {
                this.leftTicker = 0;
            }
        } else {
            this.rightDown = payload.isPress();
            if (this.rightDown) {
                this.rightTicker = 0;
            }
        }
    }

    public void tick(Player player) {
        if (this.leftDown) this.leftTicker++;
        if (this.rightDown) this.rightTicker++;


        ItemStack st = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (st != this.lastStack) {
            //处理枪械状态回收
            if (this.lastStack.getItem() instanceof AbstractGun gun) {
                if (shooting) {
                    gun.stopShooting(player, this.lastStack, this);
                    this.shooting = false;
                }
                if (aiming) {
                    gun.stopAiming(player, this.lastStack, this, this.aimingFinished);
                    this.aiming = false;
                    this.aimingFinished = false;
                }
            }

            this.lastStack = st;
            this.keepStackTicker = 0;
        }

        if (st.getItem() instanceof AbstractGun gun) {
            boolean shootable = isShootable();
            if (this.leftDown && shootable) {//如果可以射击且左键按下
                if (!this.shooting) {//初始化射击状态
                    this.shootingTicker = 0;
                    gun.startShooting(player, st, this);
                    this.shooting = true;
                }
                gun.onShootingTick(player, st, this, this.shootingTicker);
            } else if (shooting) {//如果不是射击状态但是标注正在射击
                gun.stopShooting(player, st, this);
                this.shooting = false;
            }

            if (this.rightDown && shootable) {//如果可以射击且右键按下
                if (!this.aiming) {
                    this.aimingTicker = 0;
                    gun.startAiming(player, st, this);
                    this.aiming = true;
                }
                if (isAimingFinish() && !aimingFinished) {//如果正好完成瞄准
                    gun.finishAiming(player, st, this);
                    this.aimingFinished = true;
                }
            } else if (aiming) {//如果处于不可射击状态或右键未按下
                this.stopAimingTicker = 0;
                gun.stopAiming(player, st, this, this.aimingFinished);
                this.aiming = false;
                this.aimingFinished = false;
            }

            //处理计数器
            if (shooting) {
                this.shootingTicker++;
            }
            if (aiming) {
                this.aimingTicker++;
            } else {
                this.stopAimingTicker++;
            }
        }


    }

    public void resetDeltaTicker() {
        this.shootingTicker = 0;
        this.aimingTicker = 0;
    }


    public boolean isRightDown() {
        return rightDown;
    }

    public boolean isLeftDown() {
        return leftDown;
    }

    public boolean isShootable() {
        return isReady() && !isReloading();
    }

    public boolean isReady() {
        return this.keepStackTicker >= PREPARE_TIME;
    }

    public boolean isAiming() {
        return aiming;
    }

    public boolean isAimingFinish() {
        return aimingTicker >= AIMING_TIME;
    }

    public boolean isReloading() {
        return reloading;
    }

    public int getShootingTicker() {
        return shootingTicker;
    }

    public int getLeftTicker() {
        return leftTicker;
    }

    public int getRightTicker() {
        return rightTicker;
    }

    public int getAimingTicker() {
        return aimingTicker;
    }

    public void startReloading() {
        this.reloading = true;
    }

    public void stopReloading() {
        this.reloading = false;
    }

    public float getScalingFactor() {
        return aiming ?
                (1 - 0.2F * Math.min(this.aimingTicker, AIMING_TIME) / AIMING_TIME) :
                0.8F + 0.2F * Math.min(this.stopAimingTicker, AIMING_TIME) / AIMING_TIME;
    }
}
