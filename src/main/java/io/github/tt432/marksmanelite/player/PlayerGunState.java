package io.github.tt432.marksmanelite.player;

import io.github.tt432.marksmanelite.guns.TestingGun;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerGunState {
    private boolean leftDown = false;
    private boolean rightDown = false;
    private int ticker = 0;
    private int deltaTicker = 0;

    public void handle(MouseClickPayload payload, Player player) {
        if (payload.isLeft()) {
            this.leftDown = payload.isPress();

            ItemStack st = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (st.getItem() instanceof TestingGun gun) {
                if (payload.isPress()) {
                    this.ticker = 0;
                    this.deltaTicker = 0;
                    gun.onLeftPress(player, st, this);
                } else {
                    gun.onLeftRelease(player, st, this);
                }
            }

        } else
            this.rightDown = payload.isPress();
    }

    public void tick(Player player) {
        if (this.leftDown) {
            this.ticker++;
            this.deltaTicker++;
            ItemStack st = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (st.getItem() instanceof TestingGun gun) {
                gun.onLeftPressTick(player, st, this, deltaTicker);
            }
        }
    }

    public void resetDeltaTicker() {
        this.deltaTicker = 0;
    }


    public boolean isRightDown() {
        return rightDown;
    }

    public boolean isLeftDown() {
        return leftDown;
    }

    public int getDeltaTicker() {
        return deltaTicker;
    }

    public int getTicker() {
        return ticker;
    }
}
