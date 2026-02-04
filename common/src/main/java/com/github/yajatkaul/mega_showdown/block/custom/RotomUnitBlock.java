package com.github.yajatkaul.mega_showdown.block.custom;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RotomUnitBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 15, 15, 15);
    private static final List<String> rotomAspects = List.of(
            "heat-appliance",
            "wash-appliance",
            "mow-appliance",
            "frost-appliance",
            "fan-appliance"
    );
    private final String form;

    public RotomUnitBlock(Properties arg, String form) {
        super(arg);
        this.form = form;
        registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        Direction direction = arg.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) {
            return;
        }

        if (entity instanceof PokemonEntity pokemonEntity && pokemonEntity.getPokemon().getSpecies().getName().equals("Rotom") && pokemonEntity.getAspects().stream().noneMatch(rotomAspects::contains)) {
            Effect.getEffect("mega_showdown:rotom_" + form + "_effect").applyEffects(pokemonEntity.getPokemon(), List.of(String.format("appliance=%s", form)), null);
            level.destroyBlock(pos, false);
            level.levelEvent(2001, pos, Block.getId(state));
        }
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0.1, 0.0, 0.1, 15.9, 16.0, 15.9); // Slightly smaller than a full cube
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState arg, BlockGetter arg2, BlockPos arg3, CollisionContext arg4) {
        return SHAPE;
    }
}
