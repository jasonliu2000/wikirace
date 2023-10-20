import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WikiPageTests
{   
  @Test
  @DisplayName("Return false if wiki page does not exist")
  public void returnFalseIfWikiPageExists()
  {
    assertFalse( WikiPage.exists("nonExistentPage") );
  }

  @Test
  @DisplayName("Return true if wiki page does not exist")
  public void returnTrueIfWikiPageExists()
  {
    assertTrue( WikiPage.exists("Wikipedia") );
  }

  @Test
  @DisplayName("Get links in wikipedia page 'Wikiracing'")
  public void getWikiLinks()
  {
    assertTrue( WikiPage.getLinks("Wikiracing").size() > 0 );
    assertTrue( WikiPage.getLinks("Wikiracing").contains("Wikipedia") );
  }
}
