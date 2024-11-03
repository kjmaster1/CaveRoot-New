package com.kjmaster.caveroot.root;

import com.kjmaster.kjlib.items.BaseItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class CaveRootItem extends BaseItem {

    public CaveRootItem() {
        super(new Properties()
                .food(
                        (new FoodProperties.Builder()).nutrition(3).saturationMod(0.6F).effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 1), 0.75F).build()
                )
        );
    }
}
