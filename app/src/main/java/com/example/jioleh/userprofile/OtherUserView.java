package com.example.jioleh.userprofile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jioleh.R;
import com.example.jioleh.chat.MessagePage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class OtherUserView extends AppCompatActivity {

    private String profileUID;
    private String profileUsername;

    private String imageUrl;
    private userProfileViewModel viewModel;
    private TextView tv_username, tv_age, tv_gender;
    private ImageView iv_ProfilePic;

    private Button btn_message, btn_review;

    private Toolbar toolbar;

    private ProgressBar progressBar;

    private UserProfileViewPagerAdapter pagerAdapter;
    private ViewPager2 viewPager2;

    private boolean documentExist=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_view);
        initialise();

        final Intent intent = getIntent();
        //the intent that opens this must put extra as "user_id" the user's id
        //this is the current profile user id not the current user
        profileUID = intent.getStringExtra("user_id");
        profileUsername = intent.getStringExtra("username");
        chcekIfBlocked();

        if (documentExist) {
            LinearLayout blocked_user_view = findViewById(R.id.blocked_user_layout);
            blocked_user_view.setVisibility(View.VISIBLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(OtherUserView.this);
            builder.setTitle("Error fetching user details");
            builder.setMessage("The user cannot be found!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.show();

        } else {
            initialiseToolbar();

            viewModel = new ViewModelProvider(this).get(userProfileViewModel.class);


            viewModel = new ViewModelProvider(this).get(userProfileViewModel.class);
            viewModel.getUser(profileUID).observe(this, new Observer<UserProfile>() {
                @Override
                public void onChanged(UserProfile userProfile) {
                    fill(userProfile);
                }
            });

            initialiseViewPagerAndTab(profileUID);

            btn_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent nextActivity = new Intent(OtherUserView.this, MessagePage.class);
                    nextActivity.putExtra("username", tv_username.getText().toString());
                    nextActivity.putExtra("user_id", profileUID);
                    nextActivity.putExtra("image_url", imageUrl);
                    startActivity(nextActivity);
                }
            });

            btn_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent reviewPage = new Intent(OtherUserView.this, ReviewPage.class);
                    reviewPage.putExtra("username", profileUsername);
                    reviewPage.putExtra("user_id", profileUID);
                    startActivity(reviewPage);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.other_user_profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.report_user_option:
                Intent reportPage = new Intent(OtherUserView.this, ReportUserPage.class);
                reportPage.putExtra("username",profileUsername);
                reportPage.putExtra("user_id",profileUID);
                startActivity(reportPage);
                return true;
            case R.id.block_user_option:
                AlertDialog.Builder builder =  new AlertDialog.Builder(OtherUserView.this);
                builder.setMessage("Are you sure you want to block" + " "+ profileUsername + "?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        uploadBlockedUserDetails(profileUID,profileUsername);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fill(UserProfile userProfile) {
        tv_username.setText(userProfile.getUsername());
        tv_age.setText(userProfile.getAge());
        tv_gender.setText(userProfile.getGender());
        imageUrl = userProfile.getImageUrl();
        if (!userProfile.getImageUrl().equals("") && userProfile.getImageUrl() != null) {
            Picasso.get().load(imageUrl).into(iv_ProfilePic);
        }
    }

    public void initialise(){
        tv_username = findViewById(R.id.tv_profilePageUsername);
        tv_age = findViewById(R.id.tv_profilePageAge);
        tv_gender = findViewById(R.id.tv_profilePageGender);
        iv_ProfilePic = findViewById(R.id.iv_userProfilePageImage);
        btn_message = findViewById(R.id.message_other_user);
        btn_review = findViewById(R.id.write_review_other_user);
        progressBar = findViewById(R.id.pb);
    }

    private void initialiseToolbar() {
        toolbar = findViewById(R.id.include_top_app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(this.profileUsername);
        toolbar.setTitleTextColor(getResources().getColor(R.color.baseGreen));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initialiseViewPagerAndTab(String uid) {
        viewPager2 = findViewById(R.id.userProfile_viewPager);
        pagerAdapter = new UserProfileViewPagerAdapter(this,uid);
        viewPager2.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.userProfile_tabLayout);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        tab.setText("Listings");
                        break;
                    }
                    case 1: {
                        tab.setText("Reviews");
                        break;
                    }
                    case 2: {
                        tab.setText("About Me");
                        break;
                    }
                }
            }
        });

        tabLayoutMediator.attach();

    }


    private void uploadBlockedUserDetails(String uid, String username) {
        String currentViewerUid = FirebaseAuth.getInstance().getUid();
        HashMap<String, Object> blockedUser = new HashMap<>();
        blockedUser.put("username",username);

        CollectionReference colRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentViewerUid)
                .collection("blocked users");
        colRef.document(uid)
                .set(blockedUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OtherUserView.this,
                                    "User have been successful blocked",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            finish();
                        } else {
                            Toast.makeText(OtherUserView.this,
                                    "There is an error, please try again",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void chcekIfBlocked() {

        String viewerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("users")
                .document(profileUID)
                .collection("blocked users")
                .document(viewerUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (!documentSnapshot.exists()) {
                                isBlocked(documentSnapshot.exists());
                            }
                        }
                    }
                });
    }

    private void isBlocked(boolean result) {
        this.documentExist = result;
    }
    
}
