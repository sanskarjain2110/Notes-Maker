package com.stranger.notepad.contentPage;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stranger.notepad.R;

import java.util.HashMap;
import java.util.Map;

public class ContentActivity extends AppCompatActivity {
    FirebaseFirestore database;
    DocumentReference reference;

    TextView topicTextView, contentTextView;
    String topicId;
    String topic, content;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        currentUserId = "test";

        topicId = getIntent().getStringExtra("topicId");

        database = FirebaseFirestore.getInstance();
        reference = database.collection(currentUserId).document(topicId);

        topicTextView = findViewById(R.id.topic);
        contentTextView = findViewById(R.id.content);


        reference.addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                return;
            }
            if (snapshot != null) {
                topic = (String) snapshot.get("topic");
                content = (String) snapshot.get("content");

                topicTextView.setText(topic);
                contentTextView.setText(content);
            }
        });

        //  back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // saveButton
        findViewById(R.id.saveButton).setOnClickListener(v -> {
            topic = topicTextView.getText().toString().trim();
            content = contentTextView.getText().toString().trim();

            Map<String, Object> data = new HashMap<>();
            data.put("topic", topic);
            data.put("content", content);
            // time stamp

            reference.update(data).addOnSuccessListener(unused -> finish());
        });


    }
}