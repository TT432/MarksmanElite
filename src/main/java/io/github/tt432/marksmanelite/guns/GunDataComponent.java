package io.github.tt432.marksmanelite.guns;

import io.github.tt432.marksmanelite.accessory.LowerHangingAccessory;
import io.github.tt432.marksmanelite.accessory.MagazineAccessory;
import io.github.tt432.marksmanelite.accessory.MuzzleAccessory;
import io.github.tt432.marksmanelite.accessory.ScopeAccessory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public class GunDataComponent {
    private ScopeAccessory scope;//瞄具
    private MuzzleAccessory muzzle;//枪口
    private LowerHangingAccessory lowerHanging;//下挂
    private MagazineAccessory magazine;//弹匣

    private int ammoCount;


//    public void refresh(ItemStack stack) {
//        stack.set(DataComponents.ATTRIBUTE_MODIFIERS)
//    }
}
