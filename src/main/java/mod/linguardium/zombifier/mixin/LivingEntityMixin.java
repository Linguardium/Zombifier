package mod.linguardium.zombifier.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method="drop", at=@At("HEAD"), cancellable=true)
    private void pigsConvertNow(DamageSource source, CallbackInfo ci) {
        LivingEntity l = (LivingEntity)(Object)this;
        if (source.getAttacker() instanceof ZombieEntity && l instanceof PigEntity) {
            ci.cancel();
        }
    }
}
