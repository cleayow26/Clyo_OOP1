
package com.clyo.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;
import java.util.List;

public class GameAPI {

    private static final String BASE_URL =
            "http://localhost:8080/api/scores";

    public interface ScoreCallback {
        void onSuccess();
        void onError(String message);
    }

    public interface LeaderboardCallback {
        void onSuccess(List<ScoreEntry> scores);
        void onError(String message);
    }

    public void submitScore(
            String playerName,
            int point,
            ScoreCallback callback
    ) {
        JsonValue data = new JsonValue(JsonValue.ValueType.object);
        data.addChild("playerName", new JsonValue(playerName));
        data.addChild("point", new JsonValue(point));

        Net.HttpRequest request =
                new Net.HttpRequest(Net.HttpMethods.POST);

        request.setUrl(BASE_URL);
        request.setHeader("Content-Type", "application/json");
        request.setContent(data.toJson(JsonWriter.OutputType.json));

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {

            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                int statusCode = response.getStatus().getStatusCode();

                if (statusCode >= 200 && statusCode < 300) {
                    callback.onSuccess();
                } else {
                    callback.onError("HTTP Error: " + statusCode);
                }
            }

            @Override
            public void failed(Throwable t) {
                callback.onError(t.getMessage());
            }

            @Override
            public void cancelled() {
                callback.onError("Request cancelled");
            }
        });
    }

    public void getLeaderboard(LeaderboardCallback callback) {

        Net.HttpRequest request =
                new Net.HttpRequest(Net.HttpMethods.GET);

        request.setUrl(BASE_URL + "/leaderboard?limit=10");

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {

            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                int statusCode = response.getStatus().getStatusCode();

                if (statusCode >= 200 && statusCode < 300) {
                    try {
                        String result = response.getResultAsString();
                        JsonValue json = new JsonReader().parse(result);

                        List<ScoreEntry> scores = new ArrayList<>();

                        for (JsonValue item : json) {
                            ScoreEntry entry = new ScoreEntry();

                            entry.setScoreId(item.getString("scoreId", ""));
                            entry.setPlayerName(item.getString("playerName", ""));
                            entry.setPoint(item.getInt("point", 0));
                            entry.setCreatedAt(item.getString("createdAt", ""));

                            scores.add(entry);
                        }

                        callback.onSuccess(scores);

                    } catch (Exception e) {
                        callback.onError(
                                "Failed to parse leaderboard: " + e.getMessage()
                        );
                    }
                } else {
                    callback.onError("HTTP Error: " + statusCode);
                }
            }

            @Override
            public void failed(Throwable t) {
                callback.onError(t.getMessage());
            }

            @Override
            public void cancelled() {
                callback.onError("Request cancelled");
            }
        });
    }
}
