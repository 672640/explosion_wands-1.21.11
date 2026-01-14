package com.fireball_stick.customFunctions;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;

public class CustomTnt extends PrimedTnt {
    public CustomTnt(EntityType<? extends CustomTnt> type, Level level) {
        super(type, level);
    }
//Ability to separate the values for the explosion power of TNTs for different classes

    float explosionPower = 5F; //Default: 4.0F
    double defaultGravity = 0.04; //Default: 0.04
    boolean explodeOnContact = false; //Custom-made, default: false

    @Override
    public void tick() {
        //Inherits logic from tick(), where we only override what's specified under. Otherwise, we have to put *all* the logic that tick() uses here
        super.tick();

        if((getFuse() <= 0 && !level().isClientSide()) || (this.onGround() && explodeOnContact)) {
            this.discard();
            primedTntExploded();
        }
    }

    @Override
    protected double getDefaultGravity() {
        return defaultGravity;
    }
    //Custom-made
    public void primedTntExploded() {
        level().explode(
                this,
                getX(),
                getY(),
                getZ(),
                explosionPower,
                Level.ExplosionInteraction.TNT
        );
    }

    //Getters and setters
    public void setExplosionPower(float power) {
        this.explosionPower = power;
    }
    //How strong the gravity of the primed TNT is
    public void setDefaultGravity(double gravity) {
        this.defaultGravity = gravity;
    }
    //Makes the TNT explode on contact, overrides the fuse timer
    public void setExplodeOnContact(boolean contactExplode) {
        this.explodeOnContact = contactExplode;
    }
}
//TODO:
//Add an option to adjust the pushback primed TNT get from exploding close to each other
