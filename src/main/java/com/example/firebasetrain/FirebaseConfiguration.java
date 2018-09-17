package com.example.firebasetrain;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Configuration
public class FirebaseConfiguration {

    private static final String DATABASE_URL = "https://<YOUR-DATABASE>.firebaseio.com/";

    private static DatabaseReference database;
    
    private static class DbUser{
    	public String user;
    	public int age;
    }
    
	FirebaseConfiguration() throws IOException{
		FileInputStream serviceAccount = new FileInputStream(System.getenv("ACCOUNT_FILE_PATH").toString());

		FirebaseOptions options = new FirebaseOptions.Builder()
		    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		    .setDatabaseUrl("https://trainsyukai.firebaseio.com/")
		    .build();

		FirebaseApp.initializeApp(options);

        // Shared Database reference
        database = FirebaseDatabase.getInstance().getReference();

        // Start listening to the Database
        startListeners();
	}
	
	public void startListeners() {
        database.child("user").addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildName) {
                final String postId = dataSnapshot.getKey();
                final DbUser user = dataSnapshot.getValue(DbUser.class);
                
                System.out.println("ChildAdded:getkey[" + postId + "]");
                System.out.println("user:" + user.user + " ,age:"+user.age);

//                // Listen for changes in the number of stars and update starCount
//                addStarsChangedListener(user, postId);

//                // Listen for new stars on the post, notify users on changes
//                addNewStarsListener(dataSnapshot.getRef(), user);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildName) {
            	System.out.println("ChildChanged");
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
            	System.out.println("ChildRemoved");
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildName) {
            	System.out.println("ChildMoved");
            }

            public void onCancelled(DatabaseError databaseError) {
                System.out.println("startListeners: unable to attach listener to posts");
                System.out.println("startListeners: " + databaseError.getMessage());
            }
        });
		
	}

//    /**
//     * Listen for stars added or removed and update the starCount.
//     */
//    private static void addStarsChangedListener(DbUser user, String postId) {
//        // Get references to the post in both locations
//        final DatabaseReference postRef = database.child("user").child(postId);
//        final DatabaseReference userPostRef = database.child("user-posts").child(user.name).child(postId);
//
//        // When the post changes, update the star counts
//        // [START post_value_event_listener]
//        postRef.child("stars").addValueEventListener(new ValueEventListener() {
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                updateStarCount(postRef);
//                // [START_EXCLUDE]
//                updateStarCount(userPostRef);
//                // [END_EXCLUDE]
//            }
//
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("Unable to attach listener to stars for post: " + postRef.getKey());
//                System.out.println("Error: " + databaseError.getMessage());
//            }
//        });
//        // [END post_value_event_listener]
//    }
}
