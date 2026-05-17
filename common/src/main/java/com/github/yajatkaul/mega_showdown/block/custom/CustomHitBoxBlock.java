package com.github.yajatkaul.mega_showdown.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class CustomHitBoxBlock extends Block {
    public final VoxelShape SHAPE;

    public CustomHitBoxBlock(Properties properties, VoxelShape shape) {
        super(properties);
        this.SHAPE = shape;
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState arg, BlockGetter arg2, BlockPos arg3, CollisionContext arg4) {
        return SHAPE;
    }
}
