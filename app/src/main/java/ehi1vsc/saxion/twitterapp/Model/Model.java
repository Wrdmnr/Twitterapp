package ehi1vsc.saxion.twitterapp.Model;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ehi1vsc.saxion.twitterapp.WebAsynctasks.TwitterAPI;

/**
 * Created by edwin_000 on 25/04/2016.
 */
public class Model {

    private OAuth10aService twitterService = new ServiceBuilder().apiKey(Ref.API_KEY)
            .apiSecret(Ref.API_SECRET)
            .callback(Ref.OAUTH_CALLBACK_URL)
            .build(new TwitterAPI());

    private ArrayList<Tweet> tweets = new ArrayList();
    private Map<String, User> users = new HashMap<>();

    private static Model ourInstance;

    public static Model getInstance() {
        if (ourInstance == null) {
            ourInstance = new Model();
        }
        return ourInstance;
    }

    public OAuth10aService getTwitterService() {
        return twitterService;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public User addUser(User user){
        if (!users.containsKey(user.getId_str())) {
            users.put(user.getId_str(), user);
            return user;
        }
        return user;
    }

}
