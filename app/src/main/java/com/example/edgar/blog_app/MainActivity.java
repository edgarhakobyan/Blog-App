package com.example.edgar.blog_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.edgar.blog_app.activities.AccountActivity;
import com.example.edgar.blog_app.activities.LoginActivity;
import com.example.edgar.blog_app.activities.NotificationActivity;
import com.example.edgar.blog_app.activities.PostActivity;
import com.example.edgar.blog_app.activities.SetupActivity;
import com.example.edgar.blog_app.adapters.PostAdapter;
import com.example.edgar.blog_app.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //RecyclerItemTouchHelper.RecyclerItemTouchHelperListener

    FloatingActionButton addPostBtn;

    private RecyclerView mPostListView;

    private static ArrayList<Post> mPosts = new ArrayList<>();
    private PostAdapter mPostAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private String currentUserId;

    private DocumentSnapshot lastVisible;

    private Boolean isFirstPageFirstLoad = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        addPostBtn = (FloatingActionButton) findViewById(R.id.fab);
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, PostActivity.class);
               startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (mAuth.getCurrentUser() != null) {
            initRecyclerView();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        } else {
            currentUserId = mAuth.getCurrentUser().getUid();
            firebaseFirestore.collection(Constants.USERS).document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {
                            Intent intent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:

                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_notification:
                sendToNotification();
                break;
            case R.id.nav_account:
                sendToAccount();
                break;
            case R.id.nav_account_settings:
                sendToSetup();
                break;
            case R.id.nav_logout:
                logOut();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initRecyclerView() {
        mPosts = new ArrayList<>();

        mPostAdapter = new PostAdapter(mPosts);

        mPostListView = findViewById(R.id.post_list_view);
        mPostListView.setLayoutManager(new LinearLayoutManager(this));
        mPostListView.setAdapter(mPostAdapter);

        if (mAuth.getCurrentUser() != null) {

            mPostListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !mPostListView.canScrollVertically(1);
                    if (reachedBottom) {
                        String desc = lastVisible.getString(Constants.DESCRIPTION);
                        Toast.makeText(MainActivity.this, "Reached: " + desc, Toast.LENGTH_LONG).show();
                        loadMorePosts();
                    }
                }
            });

            // Get posts by order timestamp
            Query firstQuery = firebaseFirestore.collection(Constants.POSTS)
                    .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)
                    .limit(3);
            firstQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                    if (isFirstPageFirstLoad && queryDocumentSnapshots.size() > 0) {
                        lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    }

                    for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String postId = doc.getDocument().getId();
                            Post post = doc.getDocument().toObject(Post.class).withId(postId);

                            if (isFirstPageFirstLoad) {
                                mPosts.add(post);
                            } else {
                                mPosts.add(0, post);
                            }
                            mPostAdapter.notifyDataSetChanged();

                        }
                    }
                    isFirstPageFirstLoad = false;
                }
            });
        }
    }

    private void loadMorePosts() {
        Query nextQuery = firebaseFirestore.collection(Constants.POSTS)
                .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(3);

        nextQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String postId = doc.getDocument().getId();
                            Post post = doc.getDocument().toObject(Post.class).withId(postId);

                            mPosts.add(post);
                            mPostAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }
        });
    }

    private void logOut() {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendToSetup() {
        Intent intent = new Intent(MainActivity.this, SetupActivity.class);
        startActivity(intent);
        //finish();
    }

    private void sendToNotification() {
        Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
        startActivity(intent);
        //finish();
    }

    private void sendToAccount() {
        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
        startActivity(intent);
        //finish();
    }

}
