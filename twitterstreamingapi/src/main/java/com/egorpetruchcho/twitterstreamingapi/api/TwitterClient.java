package com.egorpetruchcho.twitterstreamingapi.api;

import android.support.annotation.Nullable;

import com.egorpetruchcho.twitterstreamingapi.model.Tweet;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthException;

/**
 * The Class TwitterClient. The client to manage connections to twitter stream
 * API. It takes requests for new tasks only if there's no pending stream tasks.
 * If a task is downloading tweets in background, it will not take new requests.
 */
public class TwitterClient {

    /**
     * The Constant CONSUMER_KEY.
     */
    private static final String CONSUMER_KEY = "qgEXz3EfSmoOS2ncBZXcO4Av4";

    /**
     * The Constant CONSUMER_SECRET.
     */
    private static final String CONSUMER_SECRET = "yzQKTfBJRjqyZA2jcBFfPEKX3QRW6HNQjnGd277Dmr5Kst7eZO";

    /**
     * The Constant ACCESS_TOKEN.
     */
    private static final String ACCESS_TOKEN = "3074346977-ODVBIFTYTreSt993aOWAuffB5guY7ZRfLtCNDnK";

    /**
     * The Constant ACCESS_TOKEN_SECRET.
     */
    private static final String ACCESS_TOKEN_SECRET = "2k2iHhvui6lFSpTMF4uCydtUl4BUfHgkUKAe1syp5XHdM";

    /**
     * The Constant STREAM_END.
     */
    private static final String STREAM_ENDPOINT = "https://stream.twitter.com/1.1/statuses/sample.json";

    private static final String HTTP_METHOD = "POST";

    private static final int CONNECTION_TIMEOUT = 10000;


    /**
     * The authorize sign.
     */
    private OAuthConsumer authorizeSign;

    /**
     * The response.
     */
    private Response response;

    /**
     * The task.
     */
    private ConnectionTask task;

    public TwitterClient() {
        authorizeSign = new DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        authorizeSign.setTokenWithSecret(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
    }

    public Response getResponse(StreamingTweetsRequest request) throws IOException {
        StringBuilder url = new StringBuilder(STREAM_ENDPOINT);
        List<String> params = new ArrayList<>();

        if (request.track != null) {
            params.add("track=" + request.track);
        }
        if (request.filterLevel != null) {
            params.add("filter_level=" + request.filterLevel);
        }
        if (request.language != null) {
            params.add("language=" + request.language);
        }
        if (params.size() > 0) {
            url.append("?").append(params.get(0));
        }
        for (int i = 1; i < params.size(); i++) {
            url.append("&").append(params.get(i));
        }

        return createConnection(url.toString());
    }

    /**
     * Creates HTTP connection based on authorization. Uses an existing connection if
     * querying to the same stream.
     *
     * @param urlText the url endpoint of the stream API.
     * @return the response the wrapper class of the response containing connection object,
     * response stream, status.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private Response createConnection(String urlText) throws IOException {
        if (this.response == null || !this.response.isValid()) {
            try {
                Response clientResponse;
                URL url = new URL(urlText);
                HttpsURLConnection connection = (HttpsURLConnection) url
                        .openConnection();
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setRequestMethod(HTTP_METHOD);
                authorizeSign.sign(connection);
                clientResponse = new Response(connection);
                this.response = clientResponse;
            } catch (OAuthException e) {
                throw new IOException(e);
            }
        }
        return this.response;
    }

    public List<Tweet> downloadTweets(StreamingTweetsRequest request) {
        task = new ConnectionTask(this);
        return task.downloadTweets(request);
    }

    public static class StreamingTweetsRequest {
        @Nullable
        private final String language;
        @Nullable
        private final String track;
        @Nullable
        private final String filterLevel;

        private StreamingTweetsRequest(@Nullable String language, @Nullable String track, @Nullable String filterLevel) {
            this.language = language;
            this.track = track;
            this.filterLevel = filterLevel;
        }

        public static class Builder {

            private String language;
            private String track;
            private String filterLevel;

            public Builder() {
            }

            public Builder setTrack(String track) {
                this.track = track;
                return this;
            }

            public Builder setLanguage(String language) {
                this.language = language;
                return this;
            }

            public Builder setFilterLevel(String filterLevel) {
                this.filterLevel = filterLevel;
                return this;
            }

            public StreamingTweetsRequest build() {
                return new StreamingTweetsRequest(language, track, filterLevel);
            }
        }
    }
}
