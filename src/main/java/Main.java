import java.util.Scanner;

public class Main {
	private static Board board = new Board();
	private static boolean endGame = false;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Board.Direction direction;
		System.out.println("Welcome to 2048! Rules are simple; use WASD to move the board around in order to combine like tiles together.");
		System.out.println("Refer to play2048.co for more information.");
		for (int i = 0; i < 2; i++) {
			board.generateNewBlock();
		}
		while (!endGame) {
			if (board.noPossibleMove()) {
				endGame = true;
			}
			System.out.print(board.toString());
			direction = board.convertKeyToDirection(scanner.next());
			if (direction == Board.Direction.INVALID) {
				System.out.println("This direction is invalid. Please try again");
			} else {
				board.move(direction);
				System.out.println(board.getScore());
				board.generateNewBlock();
			}
		}
		System.out.println("Game over!");
		scanner.close();
	}
}