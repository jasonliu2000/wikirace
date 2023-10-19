import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jsoup.HttpStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AppTest 
{   
    @Test
    @DisplayName("Return false if wiki article does not exist")
    public void returnFalseIfWikiArticleExists()
    {
        assertFalse( WikiPage.exists("nonExistentArticle") );
    }

    @Test
    @DisplayName("Return true if wiki article does not exist")
    public void returnTrueIfWikiArticleExists()
    {
        assertTrue( WikiPage.exists("Wikipedia") );
    }

    @Test
    @DisplayName("Get links in wikipedia article 'Wikiracing'")
    public void getWikiLinks()
    {
        assertTrue( WikiPage.getLinks("Wikiracing").size() > 0 );
        assertTrue( WikiPage.getLinks("Wikiracing").contains("Wikipedia") );
    }
}
