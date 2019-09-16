package its_meow.skeppymod.entity;

import java.util.function.Supplier;

import its_meow.skeppymod.SkeppyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class EntityMrSqueegy extends EntityCreature {

    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;
    public static final DataParameter<Integer> ATTACK_TICK = EntityDataManager.<Integer>createKey(EntityMrSqueegy.class, DataSerializers.VARINT);
    private boolean attackStart = false;
    public long bolt;

    public EntityMrSqueegy(World worldIn) {
        super(worldIn);
        this.setSize(1, 0.5F);
        this.setCustomNameTag("Mr. Squeegy");
        this.setAlwaysRenderNameTag(true);
        this.rand.nextLong();
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ATTACK_TICK, Integer.valueOf(0));
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityMrSqueegy.AIMoveRandom(this));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getTrueSource() instanceof EntityLivingBase) {
            this.setAttackTarget((EntityLivingBase) source.getTrueSource());
            this.attackStart = true;
        }
        return super.attackEntityFrom(source, amount);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!this.world.isRemote) {
            this.motionX = (double) (this.randomMotionVecX);
            this.motionY = (double) (this.randomMotionVecY);
            this.motionZ = (double) (this.randomMotionVecZ);
        }
        this.renderYawOffset += (-((float) MathHelper.atan2(this.motionX, this.motionZ)) * (180F / (float) Math.PI) - this.renderYawOffset) * 0.1F;
        this.rotationYaw = this.renderYawOffset;
        if(this.world.isRemote && this.ticksExisted % 600 == 0) {
            Supplier<Supplier<EntityPlayer>> playerS = () -> () -> {
                return (EntityPlayer) Minecraft.getMinecraft().player;
            };
            EntityPlayer player = playerS.get().get();
            if(player.getDistance(this) <= 20) {
                if(SkeppyMod.isBBH(player)) {
                    player.sendMessage(new TextComponentString("[Mr. Squeegy -> You] :D best friends"));
                }
                if(SkeppyMod.isSkeppy(player)) {
                    player.sendMessage(new TextComponentString("[Mr. Squeegy -> You] you killed me :("));
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(this.getAttackTarget() != null && this.getAttackTarget().isDead) {
            this.setAttackTarget(null);
        }
        if(this.attackStart || (this.getAttackTarget() != null && this.dataManager.get(ATTACK_TICK) == 0)) {
            this.attackStart = false;
            this.dataManager.set(ATTACK_TICK, 30);
        }
        if(this.dataManager.get(ATTACK_TICK) > 0) {
            this.dataManager.set(ATTACK_TICK, this.dataManager.get(ATTACK_TICK) - 1);
            if(this.dataManager.get(ATTACK_TICK) == 0 && this.getAttackTarget() != null) {
                EntityLightningBolt l = new EntityLightningBolt(this.world, this.getAttackTarget().posX, this.getAttackTarget().posY, this.getAttackTarget().posZ, false);
                this.world.spawnEntity(l);
                this.getAttackTarget().sendMessage(new TextComponentString("uh oh spaghettio"));
            }
        }
        this.bolt = this.rand.nextLong();
    }

    public void travel(float strafe, float vertical, float forward) {
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
    }

    public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
        this.randomMotionVecX = randomMotionVecXIn;
        this.randomMotionVecY = randomMotionVecYIn;
        this.randomMotionVecZ = randomMotionVecZIn;
    }

    public boolean hasMovementVector() {
        return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        if(hand == EnumHand.MAIN_HAND) {
            if(player.getHeldItemMainhand().getItem() == Items.BUCKET) {
                player.getHeldItemMainhand().shrink(1);
                player.addItemStackToInventory(new ItemStack(SkeppyMod.SQUEEGY_BUCKET));
                this.setDead();
            }
        }
        return false;
    }

    static class AIMoveRandom extends EntityAIBase {
        private final EntityMrSqueegy sq;

        public AIMoveRandom(EntityMrSqueegy sq) {
            this.sq = sq;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask() {
            int i = this.sq.getIdleTime();

            if(i > 100) {
                this.sq.setMovementVector(0.0F, 0.0F, 0.0F);
            } else if(this.sq.getRNG().nextInt(50) == 0 || !this.sq.hasMovementVector()) {
                float f = this.sq.getRNG().nextFloat() * ((float) Math.PI * 2F);
                float f1 = MathHelper.cos(f) * 0.2F;
                float f2 = -0.1F + this.sq.getRNG().nextFloat() * 0.2F;
                float f3 = MathHelper.sin(f) * 0.2F;
                this.sq.setMovementVector(f1, f2, f3);
            }
        }
    }

}
