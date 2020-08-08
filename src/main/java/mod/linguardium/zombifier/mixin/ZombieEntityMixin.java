package mod.linguardium.zombifier.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin extends HostileEntity {
    // useless constructor that gets ignored but is here to shut the ide up about it

    protected ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method="onKilledOther", at=@At(value="INVOKE",target="net/minecraft/world/World.getDifficulty()Lnet/minecraft/world/Difficulty;",ordinal=1))
    private Difficulty getHard(World world) {
        return Difficulty.HARD;
    }
    @Redirect(method="onKilledOther", at=@At(value="INVOKE",target="net/minecraft/world/World.getDifficulty()Lnet/minecraft/world/Difficulty;",ordinal=2))
    private Difficulty getHarder(World world) {
        return Difficulty.HARD;
    }
    @Inject(method="onKilledOther",at=@At(value="HEAD"), cancellable = true)
    private void pigsToZombifiedPiglin(LivingEntity e, CallbackInfo i) {
        if (e instanceof PigEntity) {
            ZombifiedPiglinEntity z = EntityType.ZOMBIFIED_PIGLIN.create(e.world);
            if (z != null) {
                z.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                z.refreshPositionAndAngles(e.getX(), e.getY(), e.getZ(), e.yaw, e.pitch);
                z.setAiDisabled(((PigEntity) e).isAiDisabled());
                z.setBaby(e.isBaby());
                if (e.hasCustomName()) {
                    z.setCustomName(e.getCustomName());
                    z.setCustomNameVisible(e.isCustomNameVisible());
                }
                z.setPersistent();
                e.world.spawnEntity(z);
                e.remove();
                i.cancel();
            }
        }
    }
    @Inject(method="initCustomGoals",at=@At("TAIL"))
    private void addAttackPigsGoal(CallbackInfo ci) {
        this.goalSelector.add(6,new FollowTargetGoal(this,PigEntity.class,true));
    }
}
