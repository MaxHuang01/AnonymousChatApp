package com.example.anonymouschat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anonymouschat.models.Conversation;
import com.example.anonymouschat.models.Message;
import com.example.anonymouschat.models.Topic;
import com.example.anonymouschat.models.User;
import com.example.anonymouschat.services.CurrentUserService;
import com.example.anonymouschat.services.JsonService;
import com.example.anonymouschat.services.MessageService;
import com.example.anonymouschat.services.ReportService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatWindowActivity extends AppCompatActivity {

    private EditText editText;
    private MessageAdapter messageAdapter;
    private ListView messagesView;

    private WebSocket webSocket;
    private MessageService _messageService;
    private ReportService _reportService;

    private User _currentUser;
    private User _otherUser;
    private Topic _selectedTopic;
    private Conversation _conversation;

    private TextView _userName;
    private TextView _topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        _messageService = new MessageService(this);
        _reportService = new ReportService(this);

        editText = (EditText) findViewById(R.id.editTextChat);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        _userName = findViewById(R.id.userName);
        _topicName = findViewById(R.id.activeChatTopic);

        Intent intent = getIntent();

        _currentUser = CurrentUserService.getCurrentUser();

        _otherUser = (User) intent.getSerializableExtra("USER");
        _selectedTopic = (Topic) intent.getSerializableExtra("TOPIC");
        if (_otherUser != null && _selectedTopic != null) {
            _currentUser.alias = RandomAliasGenerator.getRandomName();
            _setHeaderValues();
            initiateSocketConnection();
        }
        else {
            _conversation = (Conversation) intent.getSerializableExtra("CONVERSATION");
            _messageService.getMessagesForConversation(_conversation.id, (isSuccessful, jsonResponse) -> {
                if (isSuccessful) {
                    List<Message> messages = JsonService.parseMessagesJsonString(jsonResponse, _currentUser.id);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (Message message : messages) {
                                _addMessageToList(message);
                            }
                        }
                    });
                    _setHeaderValues();
                    initiateSocketConnection();
                }
            });
        }
      
        registerForContextMenu(_userName);
    }

    private void _setHeaderValues() {
        if (_conversation != null) {
            _userName.setText(_conversation.getOtherUserAlias(_currentUser.id));
            _topicName.setText(_conversation.topic.name);
        }
        else {
            _userName.setText(_otherUser.alias);
            _topicName.setText(_selectedTopic.name);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.voting_option, menu);
    }

    public void sendMessage(View view){
        String messageToSend = editText.getText().toString();
        if (messageToSend.length() > 0){
            final Message message = new Message(messageToSend, true);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    _addMessageToList(message);
                }
            });
            JSONObject jsonObject = new JSONObject();

            try {
                if (_conversation != null) {
                    jsonObject.put("conversationId", _conversation.id);
                    jsonObject.put("sender", _currentUser.id);
                    jsonObject.put("topic", _conversation.topic.id);
                }
                else {
                    jsonObject.put("sender", _currentUser.id);
                    jsonObject.put("senderAlias", _currentUser.alias);
                    jsonObject.put("receiver", _otherUser.id);
                    jsonObject.put("receiverAlias", _otherUser.alias);
                    jsonObject.put("topic", _selectedTopic.id);
                }
                jsonObject.put("message", messageToSend);
                webSocket.send(jsonObject.toString());
            }
            catch (JSONException error){
                error.printStackTrace();
            }

            editText.getText().clear();
        }
    }

    public void reportUser(View view){
        _reportService.reportUser(_currentUser.userName, _otherUser.userName, "User reported.", (isSuccessful, jsonResponse) -> {
            if (isSuccessful) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "User has been reported.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initiateSocketConnection(){
        OkHttpClient client = new OkHttpClient();
        String webSocketUrl = String.format(
                "%s%s",
                getString(R.string.webSocketProtocol),
                getString(R.string.apiBaseUrl)
        );
        Request req = new Request.Builder().url(webSocketUrl).build();
        webSocket = client.newWebSocket(req, new SocketListener());
        if (_conversation == null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("sender", _currentUser.id);
                jsonObject.put("senderAlias", _currentUser.alias);
                jsonObject.put("receiver", _otherUser.id);
                jsonObject.put("receiverAlias", _otherUser.alias);
                jsonObject.put("topic", _selectedTopic.id);
                jsonObject.put("message", "userSocketRegistration");
                webSocket.send(jsonObject.toString());
            }
            catch (JSONException error){
                error.printStackTrace();
            }
        }
    }

    private class SocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> {

                try {
                    JSONObject jsonObject = new JSONObject(text);
                    String messageContents = jsonObject.get("message").toString();

                    final Message message = new Message(messageContents, false);
                    _addMessageToList(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void _addMessageToList(Message message) {
        messageAdapter.add(message);
        messagesView.setSelection(messagesView.getCount() - 1);
    }
}
