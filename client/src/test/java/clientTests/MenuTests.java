package clientTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.Menu;

public class MenuTests {
	@Test
	@DisplayName("Test menu")
	public void runMenu() {
		(new Menu()).run();
	}
}
