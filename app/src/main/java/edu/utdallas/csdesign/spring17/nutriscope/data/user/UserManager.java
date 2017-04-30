package edu.utdallas.csdesign.spring17.nutriscope.data.user;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by john on 4/29/17.
 */

public class UserManager {

    private static final String TAG = "UserManager";

    private User user;

    private final List<UserListener> userListeners = new LinkedList<>();

    private String last_uid = null;
    private String uid = null;
    private CountDownLatch didAuthListenerFire = new CountDownLatch(1);

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users");


    public UserManager() {
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Log.d(TAG, "User Logged Out " + uid);
                    last_uid = uid;
                    uid = null;
                    user = null;
                    userLoggedOut();
                }
                else {
                    uid = firebaseAuth.getCurrentUser().getUid();
                    if (last_uid == null) {
                        Log.d(TAG, "User Logged In " + uid);
                        userLoggedIn();
                    } else {
                        if (uid.equals(last_uid)) {
                            Log.d(TAG, "User Token refresh" + uid);
                        }
                        else if (!uid.equals(last_uid) && last_uid != null) {
                            Log.d(TAG, "User Switched " + uid + " last: " + last_uid);
                            userLoggedIn();
                        }
                        else {
                            Log.d(TAG, "Extraneous auth state call");
                        }
                    }

                }

                didAuthListenerFire.countDown();
            }
        });
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUid() {
        return uid;
    }

    public void addListener(UserListener userListener) {
        userListeners.add(userListener);
    }

    private void userLoggedIn() {
        getUserDataFromFirebase(new TaskStatus() {
            @Override
            public void success(UserStatus msg) {
                Log.d(TAG, "User Logged in, got user data");
                for (UserListener userListener: userListeners) {
                    userListener.userLoggedIn();
                }
            }

            @Override
            public void failure(UserStatus msg) {
                Log.d(TAG, "Failed to get user data");

            }
        });


    }

    private void userLoggedOut() {
        for (UserListener userListener: userListeners) {
            userListener.userLoggedOut();
        }
    }


    public void registerUserEmail(String email, String password, final TaskStatus taskStatus) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("AUTH", "createUserWithEmail:failed", task.getException());
                            taskStatus.failure(UserStatus.REGISTRATION_FAILED);
                        } else {
                            Log.d("AUTH", "createUserWithEmail:success:" + auth.getCurrentUser().getEmail());
                            taskStatus.success(UserStatus.REGISTRATION_SUCCESS);
                        }
                    }
                });

    }

    //public void registerUserFacebook()

    public void createUser() {
        try {
            this.user = new User(auth.getCurrentUser().getUid(), auth.getCurrentUser().getEmail());
        } catch (NullPointerException ex) {
            Log.w(TAG, "Could not get current user UID, could not create user");
        }

        db.child(user.getUid()).setValue(user);
    }

    public void loginUser(String email, String password, final TaskStatus taskStatus) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    taskStatus.success(UserStatus.USER_LOGGED_IN);
                } else {
                    taskStatus.failure(UserStatus.LOGIN_FAILED);
                }
            }
        });


    }

    public void  logoutUser() {

    }

    public void modifyUser() {

    }

    public void getUserStatus(final TaskStatus taskStatus) {
        Log.d(TAG, "getUserStatus called");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    didAuthListenerFire.await();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    taskStatus.failure(UserStatus.USER_LOGGED_OUT);
                }


                if (uid != null) {
                    taskStatus.success(UserStatus.USER_LOGGED_IN);
                }
                else {
                    taskStatus.failure(UserStatus.USER_LOGGED_OUT);
                }

            }
        }).start();

    }

    private void getUserDataFromFirebase(TaskStatus taskStatus) {
        db.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "retrieved user data");
                setUser(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendUserDataToFirebase() {
        Log.d(TAG, "sent user data to firebase");
        db.child(uid).setValue(getUser());
    }








}
