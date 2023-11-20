package com.kjmaster.caveroot.torch;

import com.kjmaster.kjlib.blocks.BaseBlock;
import com.kjmaster.kjlib.blocks.RotationType;
import com.kjmaster.kjlib.builder.BlockBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;


public class TorchFungiBlock extends MultifaceBlock {

    protected static final VoxelShape SHAPE = Block.box(2.4D, 0.0D, 2.4D, 12.8D, 9.92D, 12.8D);

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public TorchFungiBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY).lightLevel((pLightEmission) -> 7));
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public Item asItem() {
        return TorchModule.TORCHSPORE_ITEM.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        if (!hasAnyFace(pState)) {
            if (pLevel.getBlockState(pPos.below()).is(Blocks.FARMLAND)) {
                return pState;
            }
            return Blocks.AIR.defaultBlockState();
        } else {
            return hasFace(pState, pDirection) && !canAttachTo(pLevel, pDirection, pNeighborPos, pNeighborState) ? removeFace(pState, getFaceProperty(pDirection)) : pState;
        }
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return spreader;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return pState.getFluidState().isEmpty();
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return pType == PathComputationType.AIR && !this.hasCollision ? true : super.isPathfindable(pState, pLevel, pPos, pType);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        boolean flag = false;

        for(Direction direction : DIRECTIONS) {
            if (hasFace(pState, direction)) {
                BlockPos blockpos = pPos.relative(direction);
                if (!canAttachTo(pLevel, direction, blockpos, pLevel.getBlockState(blockpos))) {
                    return false;
                }

                flag = true;
            }
        }

        return flag;
    }

    private static BlockState removeFace(BlockState pState, BooleanProperty pFaceProp) {
        BlockState blockstate = pState.setValue(pFaceProp, Boolean.valueOf(false));
        return hasAnyFace(blockstate) ? blockstate : Blocks.AIR.defaultBlockState();
    }

    public static boolean canAttachTo(BlockGetter pLevel, Direction pDirection, BlockPos pPos, BlockState pState) {
        if (pState.is(Blocks.FARMLAND)) {
            return true;
        }
        return Block.isFaceFull(pState.getBlockSupportShape(pLevel, pPos), pDirection.getOpposite()) || Block.isFaceFull(pState.getCollisionShape(pLevel, pPos), pDirection.getOpposite());
    }
}