import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WikiRaceTests 
{
  @Test
  @DisplayName("Throw exception if number of arguments < 2")
  public void insufficientArgumentsProvided()
  {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> 
      WikiRace.main(new String[] {"OnlyOneArgument"}));
    assertTrue(exception.getMessage().contains("Invalid number of arguments"));
  }

  @Test
  @DisplayName("Throw exception if 1st wiki page is not found")
  public void firstPageNotFound()
  {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
      WikiRace.main(new String[] {"NonexistentPage", "Wikiracing"}));
    assertEquals("Please enter a valid starting wiki page.", exception.getMessage());
  }

  @Test
  @DisplayName("Throw exception if 2nd wiki page is not found")
  public void secondPageNotFound()
  {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
      WikiRace.main(new String[] {"Wikiracing", "PageNotFound"}));
    assertEquals("Please enter a valid destination wiki page.", exception.getMessage());
  }
}
