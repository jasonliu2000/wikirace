import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AppTest 
{
    @Test
    @DisplayName("Get links in wikipedia article 'Wikiracing'")
    public void getWikiLinks()
    {
        WikiPage wikiPage = new WikiPage();
        assertTrue( wikiPage.getLinks("Wikiracing").size() > 0 );
        assertTrue( wikiPage.getLinks("Wikiracing").contains("Wikipedia") );
    }
}
