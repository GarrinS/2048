import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
	@Getter
	private static final int SIZE = 4;
	private Block[][] blocks = new Block[SIZE][SIZE];
	private Random rand = new Random();
	@Getter
	private int score = 0;

	public Board() {
		for (int i = 0; i < blocks[0].length; i++) {
			for (int j = 0; j < blocks.length; j++) {
				blocks[i][j] = new Block();
			}
		}
	}

	private boolean hasEmptyBlock() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (blocks[i][j].getScore() == 0) {
					return true;
				}
			}
		}
		return false;
	}

	private int getNewBlockScore() {
		int rng = this.rand.nextInt(2) + 1;
		return (rng * 2);
	}

	public boolean generateNewBlock() {
		if (!(hasEmptyBlock())) {
			return false;
		}
		while (true) {
			int x = this.rand.nextInt(SIZE);
			int y = this.rand.nextInt(SIZE);
			if (blocks[x][y].getScore() == 0) {
				blocks[x][y].setScore(getNewBlockScore());
				return true;
			}
		}
	}

	public void move(Direction direction) {
		for (int i = 0; i < SIZE; i++) {
			List<Block> blockSet = new ArrayList<>();
			for (int j = 0; j < SIZE; j++) {
				switch (direction) {
					case LEFT:
						blockSet.add(blocks[i][j]);
						break;
					case RIGHT:
						blockSet.add(blocks[i][SIZE - j - 1]);
						break;
					case UP:
						blockSet.add(blocks[j][i]);
						break;
					case DOWN:
						blockSet.add(blocks[SIZE - j - 1][i]);
						break;
					default:
						break;
				}
			}
			if (!(isEmptyBlock(blockSet))) {
				slide(blockSet);
			}
		}
	}

	private void slide(List<Block> blockSet) {
		slideToEdge(blockSet);
		mergeBlock(blockSet);
	}

	private void slideTo(List<Block> blockSet, int index) {
		for (int j = index; j < blockSet.size() - 1; j++) {
			blockSet.get(j).setScore(blockSet.get(j + 1).getScore());
		}
		blockSet.get(blockSet.size() - 1).clear();
	}

	private void slideToEdge(List<Block> blockSet) {
		for (int i = 0; i < blockSet.size(); i++) {
			if (remainingIsZero(blockSet, i)) {
				return;
			}
			while (blockSet.get(i).getScore() == 0) {
				slideTo(blockSet, i);
			}
		}
	}

	private void mergeBlock(List<Block> blockSet) {
		for (int i = 0; i < blockSet.size() - 1; i++) {
			if (blockSet.get(i).equilibrium(blockSet.get(i + 1))) {
				blockSet.get(i).merge(blockSet.get(i + 1));
				blockSet.get(i + 1).clear();
				slideTo(blockSet, i + 1);
				score += blockSet.get(i).getScore();
			}
		}
	}

	private boolean remainingIsZero(List<Block> blockSet, int i) {
		List<Block> remainingBlock = new ArrayList<>();
		for (int j = i; j < blockSet.size(); j++) {
			remainingBlock.add(blockSet.get(j));
		}
		return (isEmptyBlock(remainingBlock));
	}

	public boolean noPossibleMove() {
		if (hasEmptyBlock()) {
			return false;
		}
		return !(hasEqualNeighbour());
	}

	private boolean hasEqualNeighbour() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (j < SIZE - 1 && blocks[i][j].equilibrium(blocks[i][j + 1])) {
					return true;
				}
				if (i < SIZE - 1 && blocks[i][j].equilibrium(blocks[i + 1][j])) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isEmptyBlock(List<Block> blockSet) {
		for (Block block : blockSet) {
			if (block.getScore() != 0) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Block[] blockRow : blocks) {
			for (Block block : blockRow) {
				sb.append(block.getScore());
				switch (scoreMaxLength() - String.valueOf(block.getScore()).length() + 1) {
					case 1:
						sb.append(" ");
						break;
					case 2:
						sb.append("  ");
						break;
					case 3:
						sb.append("   ");
						break;
					case 4:
						sb.append("    ");
						break;
					default:
						return "";
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	private int scoreMaxLength() {
		int max = 0;
		for (Block[] blockRow : blocks) {
			for (Block block : blockRow) {
				if (block.getScore() > max) {
					max = block.getScore();
				}
			}
		}
		return String.valueOf(max).length();
	}

	public Direction convertKeyToDirection(String k) {
		switch (k) {
			case "W":
			case "w":
				return Direction.UP;
			case "A":
			case "a":
				return Direction.LEFT;
			case "S":
			case "s":
				return Direction.DOWN;
			case "D":
			case "d":
				return Direction.RIGHT;
			default:
				return Direction.INVALID;
		}
	}

	public enum Direction {
		UP, DOWN, LEFT, RIGHT, INVALID
	}
}