package com.example.anonymouschat.services;

import com.example.anonymouschat.models.Conversation;
import com.example.anonymouschat.models.Message;
import com.example.anonymouschat.models.Topic;
import com.example.anonymouschat.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonService {
    public static List<Topic> parseTopicsJsonString(String jsonTopicsString) {
        try {
            JSONArray jsonTopics = new JSONArray(jsonTopicsString);
            return parseJsonTopics(jsonTopics);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<Topic> parseJsonTopics(JSONArray jsonTopics) {
        List<Topic> topics = new ArrayList<>();

        try {
            for (int j = 0; j < jsonTopics.length(); j++) {
                JSONObject jsonTopic = jsonTopics.getJSONObject(j);
                Topic topic = parseJsonTopic(jsonTopic);
                if (topic != null) {
                    topics.add(topic);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return topics;
    }

    public static Topic parseJsonTopic(JSONObject jsonTopic) {
        try {
            String topicId = jsonTopic.getString("_id");
            String topicName = jsonTopic.getString("name");
            return new Topic(topicId, topicName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<User> parseUsersJsonString(String jsonUsersString) {
        List<User> users = new ArrayList<>();

        try {
            JSONArray jsonUsers = new JSONArray(jsonUsersString);
            for (int i = 0; i < jsonUsers.length(); i++) {
                JSONObject jsonUser = jsonUsers.getJSONObject(i);
                User user = parseJsonUser(jsonUser);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static User parseUserJsonString(String jsonUserString) {
        try {
            JSONObject jsonUser = new JSONObject(jsonUserString);
            User user = parseJsonUser(jsonUser);
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static User parseFirstUserInArrayJsonString(String jsonUsersString) {
        try {
            JSONArray jsonUsers = new JSONArray(jsonUsersString);
            for (int i = 0; i < jsonUsers.length(); i++) {
                JSONObject jsonUser = jsonUsers.getJSONObject(i);
                User user = parseJsonUser(jsonUser);
                return user;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User parseJsonUser(JSONObject jsonUser) {
        try {
            String id = jsonUser.getString("_id");
            String userName = jsonUser.getString("userName");
            Integer age = null;
            if (!jsonUser.isNull("age")) {
                age = jsonUser.getInt("age");
            }
            String gender = jsonUser.getString("gender");
            String sexualOrientation = jsonUser.getString("sexualOrientation");

            JSONArray jsonTopics = jsonUser.getJSONArray("selectedTopics");
            List<Topic> topics = parseJsonTopics(jsonTopics);

            JSONArray jsonBlockedUsers = jsonUser.getJSONArray("blockedUsers");
            List<User> blockedUsers = new ArrayList<User>();
            for (int j = 0; j < jsonBlockedUsers.length(); j++) {
                JSONObject blockedUserJson = jsonBlockedUsers.getJSONObject(j);

                User blockedUser = parseJsonUser(blockedUserJson);
                blockedUsers.add(blockedUser);
            }

            return new User(id, userName, age, gender, sexualOrientation, topics, blockedUsers);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getLoginRequestJsonString(String idToken) {
        JSONObject jsonLoginRequest = new JSONObject();

        try {
            jsonLoginRequest.put("idToken", idToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonLoginRequest.toString();
    }

    public static String getReportJsonString(String reporteeUser, String reportedUser, String reportMessage) {
        JSONObject jsonReportRequest = new JSONObject();

        try {
            jsonReportRequest.put("reporteeUser", reporteeUser);
            jsonReportRequest.put("reportedUser", reportedUser);
            jsonReportRequest.put("message", reportMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonReportRequest.toString();
    }

    public static List<Conversation> parseConversationsJsonString(String conversationsJsonString) {
        List<Conversation> conversations = new ArrayList<>();

        try {
            JSONArray jsonConversations = new JSONArray(conversationsJsonString);
            for (int i = 0; i < jsonConversations.length(); i++) {
                JSONObject jsonConversation = jsonConversations.getJSONObject(i);
                String id = jsonConversation.getString("_id");
                String startedByUserId = jsonConversation.getString("startedByUserId");
                String startedByUserAlias = jsonConversation.getString("startedByUserAlias");
                String otherUserId = jsonConversation.getString("otherUserId");
                String otherUserAlias = jsonConversation.getString("otherUserAlias");
                JSONObject jsonTopic = jsonConversation.getJSONObject("topic");
                String topicId = jsonTopic.getString("_id");
                String topicName = jsonTopic.getString("name");

                conversations.add(new Conversation(
                        id,
                        startedByUserId,
                        startedByUserAlias,
                        otherUserId,
                        otherUserAlias,
                        new Topic(topicId, topicName)
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return conversations;
    }

    public static List<Message> parseMessagesJsonString(String messagesJsonString, String currentUserId) {
        List<Message> messages = new ArrayList<>();

        try {
            JSONArray jsonMessages = new JSONArray(messagesJsonString);
            for (int i = 0; i < jsonMessages.length(); i++) {
                JSONObject jsonMessage = jsonMessages.getJSONObject(i);
                String text = jsonMessage.getString("message");
                String userId = jsonMessage.getString("userId");
                messages.add(new Message(text, userId.equals(currentUserId)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messages;
    }
}
