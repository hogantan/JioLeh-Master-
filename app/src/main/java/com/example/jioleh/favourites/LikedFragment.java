package com.example.jioleh.favourites;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jioleh.LinesOfChecks;
import com.example.jioleh.R;
import com.example.jioleh.listings.ActivityAdapter;
import com.example.jioleh.listings.JioActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

//Exactly the same as joined fragment just that collection directing different
//basically any "joined" is change into "liked"
public class LikedFragment extends Fragment {

    private View currentView;
    private TextView emptyText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FavouritesAdapter adapter;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> parent of 7c73d04... Added load more messages feature to chat feature to prevent retrieving all messages when opening chat
=======
>>>>>>> parent of 7c73d04... Added load more messages feature to chat feature to prevent retrieving all messages when opening chat
=======
>>>>>>> parent of 7c73d04... Added load more messages feature to chat feature to prevent retrieving all messages when opening chat
=======
>>>>>>> parent of 7c73d04... Added load more messages feature to chat feature to prevent retrieving all messages when opening chat
=======
>>>>>>> parent of 7c73d04... Added load more messages feature to chat feature to prevent retrieving all messages when opening chat
=======
    private LinesOfChecks linesOfChecks = new LinesOfChecks();

>>>>>>> parent of 1e5a48b... Revert "Added load more messages feature to chat feature to prevent retrieving all messages when opening chat"
    private FavouriteFragmentViewModel viewModel;
>>>>>>> parent of 7c73d04... Added load more messages feature to chat feature to prevent retrieving all messages when opening chat
=======
>>>>>>> parent of ca2abdd... 3/7
=======
    private FavouriteFragmentViewModel viewModel;
>>>>>>> parent of e40b192... Revert "Merge pull request #53 from hogantan/2/7"
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_liked, container, false);
        initialise();
        initialiseRecyclerView();

<<<<<<< HEAD
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.checkActivityExpiry();
                viewModel.checkActivityCancelledConfirmed();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

<<<<<<< HEAD
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.checkActivityExpiry();
                viewModel.checkActivityCancelledConfirmed();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.checkActivityExpiry();
                viewModel.checkActivityCancelledConfirmed();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.checkActivityExpiry();
                viewModel.checkActivityCancelledConfirmed();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.checkActivityExpiry();
                viewModel.checkActivityCancelledConfirmed();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.checkActivityExpiry();
                viewModel.checkActivityCancelledConfirmed();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.checkActivityExpiry();
                viewModel.checkActivityCancelledConfirmed();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

=======
>>>>>>> parent of ca2abdd... 3/7
=======
>>>>>>> parent of 1e5a48b... Revert "Added load more messages feature to chat feature to prevent retrieving all messages when opening chat"
        return currentView;
    }

    private void initialise() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        swipeRefreshLayout = currentView.findViewById(R.id.swipeContainer);
        emptyText = currentView.findViewById(R.id.tvFavouriteEmpty);
    }

    private void initialiseRecyclerView() {
        adapter = new FavouritesAdapter();
        recyclerView = currentView.findViewById(R.id.rvFavouriteLiked);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
    }

<<<<<<< HEAD
<<<<<<< HEAD
    public void checkActivityExpiry() {
        Date currentDateTime = Calendar.getInstance().getTime(); //this gets both date and time
        CollectionReference jioActivityColRef = FirebaseFirestore.getInstance().collection("activities");

        jioActivityColRef.whereLessThan("event_timestamp", currentDateTime)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list_of_documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot: list_of_documents) {
                            jioActivityColRef.document(documentSnapshot.getId())
                                    .update("expired", true);
                        }
                    }
                });
    }

    public void checkActivityCancelledConfirmed() {
        Date currentDateTime = Calendar.getInstance().getTime(); //this gets both date and time
        CollectionReference jioActivityColRef = FirebaseFirestore.getInstance().collection("activities");

        jioActivityColRef.whereLessThan("deadline_timestamp", currentDateTime)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list_of_documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot: list_of_documents) {
                            int minimum = Integer.parseInt(documentSnapshot.get("min_participants").toString());
                            int current = Integer.parseInt(documentSnapshot.get("current_participants").toString());
                            if (current < minimum) {
                                jioActivityColRef.document(documentSnapshot.getId())
                                        .update("cancelled", true);
                            } else {
                                jioActivityColRef.document(documentSnapshot.getId())
                                        .update("confirmed", true);
                            }
                        }
                    }
                });
<<<<<<< HEAD
=======
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new FavouriteFragmentViewModel(currentUser.getUid(), "liked");

=======
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new FavouriteFragmentViewModel(currentUser.getUid(), "liked");

>>>>>>> parent of e40b192... Revert "Merge pull request #53 from hogantan/2/7"
        //observe for changes in database
        viewModel.getListOfActivities().observe(getViewLifecycleOwner(), new Observer<List<JioActivity>>() {
            @Override
            public void onChanged(List<JioActivity> activities) {
                adapter.setData(activities, false, true);
                adapter.notifyDataSetChanged();

                //Display empty text message
                if(adapter.getItemCount() == 0) {
                    emptyText.setText("You have not like any activities!");
                } else {
                    emptyText.setText("");
                }
            }
        });
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> parent of 7c73d04... Added load more messages feature to chat feature to prevent retrieving all messages when opening chat
=======
>>>>>>> parent of 7c73d04... Added load more messages feature to chat feature to prevent retrieving all messages when opening chat
=======
>>>>>>> parent of 7c73d04... Added load more messages feature to chat feature to prevent retrieving all messages when opening chat
=======
>>>>>>> parent of 7c73d04... Added load more messages feature to chat feature to prevent retrieving all messages when opening chat
=======
>>>>>>> parent of 7c73d04... Added load more messages feature to chat feature to prevent retrieving all messages when opening chat
=======
>>>>>>> parent of 7c73d04... Added load more messages feature to chat feature to prevent retrieving all messages when opening chat
=======
>>>>>>> parent of ca2abdd... 3/7
=======
>>>>>>> parent of e40b192... Revert "Merge pull request #53 from hogantan/2/7"
=======

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                linesOfChecks.checkActivityExpiry();
                linesOfChecks.checkActivityCancelledConfirmed();

                viewModel.refreshActivities();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
>>>>>>> parent of 1e5a48b... Revert "Added load more messages feature to chat feature to prevent retrieving all messages when opening chat"
    }
}