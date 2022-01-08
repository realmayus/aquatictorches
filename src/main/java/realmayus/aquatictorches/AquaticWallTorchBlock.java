package realmayus.aquatictorches;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

public class AquaticWallTorchBlock extends TorchBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty FLOWING_WATER = IntegerProperty.create("water_level", 1, 8);

    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(5.5D, 3.0D, 11.0D, 10.5D, 13.0D, 16.0D), Direction.SOUTH, Block.box(5.5D, 3.0D, 0.0D, 10.5D, 13.0D, 5.0D), Direction.WEST, Block.box(11.0D, 3.0D, 5.5D, 16.0D, 13.0D, 10.5D), Direction.EAST, Block.box(0.0D, 3.0D, 5.5D, 5.0D, 13.0D, 10.5D)));

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public AquaticWallTorchBlock(Properties p_58123_, ParticleOptions p_58124_) {
        super(p_58123_, p_58124_);
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false).setValue(FLOWING_WATER, 8).setValue(FACING, Direction.NORTH));
    }

    public String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }

    public VoxelShape getShape(BlockState p_58152_, BlockGetter p_58153_, BlockPos p_58154_, CollisionContext p_58155_) {
        return getShape(p_58152_);
    }

    public static VoxelShape getShape(BlockState p_58157_) {
        return AABBS.get(p_58157_.getValue(FACING));
    }

    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        Direction direction = blockState.getValue(FACING);
        BlockPos blockpos = blockPos.relative(direction.getOpposite());  // block pos of block our torch is connected to
        BlockState blockstate = levelReader.getBlockState(blockpos);
        return blockstate.isFaceSturdy(levelReader, blockpos, direction);
    }

    public BlockState rotate(BlockState p_58140_, Rotation p_58141_) {
        return p_58140_.setValue(FACING, p_58141_.rotate(p_58140_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_58137_, Mirror p_58138_) {
        return p_58137_.rotate(p_58138_.getRotation(p_58137_.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING).add(WATERLOGGED).add(FLOWING_WATER);
    }


    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        BlockState blockstate = this.defaultBlockState();
        LevelReader levelreader = placeContext.getLevel();
        BlockPos blockpos = placeContext.getClickedPos();
        Direction[] adirection = placeContext.getNearestLookingDirections();

        for(Direction direction : adirection) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction1 = direction.getOpposite();
                blockstate = blockstate.setValue(FACING, direction1);
                if (blockstate.canSurvive(levelreader, blockpos)) {
                    break;
                }
            }
        }

        FluidState fluidstate = placeContext.getLevel().getFluidState(placeContext.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER || fluidstate.getType() == Fluids.FLOWING_WATER;
        boolean is_flowing = fluidstate.getType() == Fluids.FLOWING_WATER;
        return blockstate.setValue(WATERLOGGED, flag).setValue(FLOWING_WATER, is_flowing ? fluidstate.getAmount() : 8);
    }

    public BlockState updateShape(BlockState thisState, Direction directionToNeighbor, BlockState neighborState, LevelAccessor levelAccessor, BlockPos thisPos, BlockPos neighborPos) {
        if (thisState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(thisPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        if (directionToNeighbor.getOpposite() == thisState.getValue(FACING) && !thisState.canSurvive(levelAccessor, thisPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return thisState;
        }
    }

    public FluidState getFluidState(BlockState blockState) {
        if (blockState.getValue(WATERLOGGED) && blockState.getValue(FLOWING_WATER) == 8) {
            return Fluids.WATER.getSource(false);
        } else if (blockState.getValue(WATERLOGGED) && blockState.getValue(FLOWING_WATER) != 8) {
            return Fluids.WATER.getFlowing(blockState.getValue(FLOWING_WATER), false);
        }
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
        Direction direction = blockState.getValue(FACING);
        double d0 = (double)blockPos.getX() + 0.5D;
        double d1 = (double)blockPos.getY() + 0.7D;
        double d2 = (double)blockPos.getZ() + 0.5D;

        Direction direction1 = direction.getOpposite();
        level.addParticle(ParticleTypes.UNDERWATER, d0 + 0.27D * (double)direction1.getStepX(), d1 + 0.22D, d2 + 0.27D * (double)direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.GLOW_SQUID_INK, d0 + 0.27D * (double)direction1.getStepX(), d1 + 0.22D, d2 + 0.27D * (double)direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
    }

}
