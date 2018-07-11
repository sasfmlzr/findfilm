import com.sasfmlzr.findfilm.model.RetrofitSingleton;
import com.sasfmlzr.findfilm.request.CurrentMovieRequest;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.request.FindFilmApi;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Response;

import static com.sasfmlzr.findfilm.model.SystemSettings.API_KEY;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

public class RetrofitTest {
    private FindFilmApi findFilmApi;

    @Before
    public void setUP() {
        findFilmApi = RetrofitSingleton.getFindFilmApi();
        if (findFilmApi == null) {
            fail("findFilmApi cannot be null");
        }
        if (API_KEY == null) {
            fail("Api key cannot be null");
        }
    }

    @Test
    public void connectSearchMovieTest() {
        assertNotNull(connectSearchMovie());
    }

    @Test
    public void jsonParseDiscoverMovieTest() {
        Response response = connectSearchMovie();
        assertNotNull(response);

        DiscoverMovieRequest discoverMovieRequest = (DiscoverMovieRequest) response.body();
        assertNotNull(discoverMovieRequest);
    }

    @Test
    public void connectCurrentMovieTest() {
        assertNotNull(connectCurrentMovie());
    }

    @Test
    public void jsonParseCurrentMovieTest() {
        Response response = connectCurrentMovie();
        assertNotNull(response);

        CurrentMovieRequest currentMovieRequest = (CurrentMovieRequest) response.body();
        assertNotNull(currentMovieRequest);
    }

    private Response connectSearchMovie() {
        String LANGUAGE = "en_US";
        String query = "deadpool";

        try {
            return findFilmApi.getSearchMovie(API_KEY, LANGUAGE, query, 1).execute();
        } catch (IOException e) {
            return null;
        }
    }

    private Response connectCurrentMovie() {
        String LANGUAGE = "en_US";

        try {
            return findFilmApi.getCurrentMovie(12, API_KEY, LANGUAGE).execute();
        } catch (IOException e) {
            return null;
        }
    }
}
