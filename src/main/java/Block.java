import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Block {
	@Getter
	@Setter
	private int score;

	public boolean equilibrium(Block block) {
		return block.getScore() == this.getScore();
	}

	public void merge(Block block) {
		this.setScore(score + block.getScore());
	}

	public void clear() {
		this.setScore(0);
	}
}