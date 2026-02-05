package com.explosion_wands.customFunctions.falling_block;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

//Currently unused, but might be used in the future if some more finetuning is needed
public class CustomFallingBlockEntity extends FallingBlockEntity {
    public CustomFallingBlockEntity(EntityType<? extends FallingBlockEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
    }

    BlockState defaultBlockState = Blocks.SAND.defaultBlockState();

    protected void modifyBlockState() {
        Block block = this.defaultBlockState.getBlock();
    }


    public BlockState getDefaultBlockState() {
        return defaultBlockState;
    }

    public void setDefaultBlockState(BlockState blockState) {
        this.defaultBlockState = blockState;
    }
}