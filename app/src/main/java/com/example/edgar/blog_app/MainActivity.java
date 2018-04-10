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
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        } else {
            currentUserId = mAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {
                            Intent intent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            initRecyclerView();
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

        mPostListView = (RecyclerView) findViewById(R.id.post_list_view);
        mPostListView.setLayoutManager(new LinearLayoutManager(this));
        mPostListView.setAdapter(mPostAdapter);

        //Get posts
        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        Post post = doc.getDocument().toObject(Post.class);
                        mPosts.add(post);

                        mPostAdapter.notifyDataSetChanged();

                    }
                }

            }
        });

    }

//    private void initRecyclerView() {
//        Comment mComment1 = new Comment("Edgar", "this is a comment 1");
//        Comment mComment2 = new Comment("Edgar", "this is a comment 2");
//
//        Post post1 = new Post("First Post", "This is first cardjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", R.drawable.food);
//        post1.setComments(mComment1);
//        post1.setComments(mComment2);
//        Post post2 = new Post("Second Post", "This is second card", R.drawable.food);
//        post2.setComments(mComment1);
//        post2.setComments(mComment2);
//        Post post3 = new Post("Third Post", "This is third card", R.drawable.food);
//        post3.setComments(mComment1);
//        post3.setComments(mComment2);
//        Post post4 = new Post("Fourth Post", "This is fourth card", R.drawable.food);
//        post4.setComments(mComment1);
//        post4.setComments(mComment2);
//        Post post5 = new Post("Fifth Post", "This is fifth card", R.drawable.food);
//        post5.setComments(mComment1);
//        post5.setComments(mComment2);
//
//
//        mPosts.add(post1);
//        mPosts.add(post2);
//        mPosts.add(post3);
//        mPosts.add(post4);
//        mPosts.add(post5);
//        mPosts.add(post5);
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.itemsRecyclerView);
//        mPostAdapter = new PostAdapter(MainActivity.this, mPosts);
//        mRecyclerView.setAdapter(mPostAdapter);
//
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//
//        //ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
//        //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
//
//    }

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
        finish();
    }

    private void sendToNotification() {
        Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendToAccount() {
        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

}
