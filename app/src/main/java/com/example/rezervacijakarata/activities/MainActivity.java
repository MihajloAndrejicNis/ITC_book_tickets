package com.example.rezervacijakarata.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rezervacijakarata.App;
import com.example.rezervacijakarata.R;
import com.example.rezervacijakarata.adapters.EventAdapter;
import com.example.rezervacijakarata.datamodels.DetailedEvent;
import com.example.rezervacijakarata.datamodels.Event;
import com.example.rezervacijakarata.datamodels.User;
import com.example.rezervacijakarata.listeners.OnEventClickListener;
import com.example.rezervacijakarata.netutils.ApiClient;
import com.example.rezervacijakarata.netutils.ApiInterface;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "rezKarataTag";

    SignInButton signInButton;
    GoogleSignInAccount account;
    Button signOutBtn;
    ApiInterface apiInterface;
    RecyclerView eventsRv;
    public static String emailName;

    public static String getEmailName() {
        return emailName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        account = GoogleSignIn.getLastSignedInAccount(this);
        signOutBtn = findViewById(R.id.signOutBtn);
        eventsRv = findViewById(R.id.events_rv);

        signInButton = findViewById(R.id.sign_in_button);

        signOutBtn.setOnClickListener(v -> {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(task -> {
                        signOutBtn.setVisibility(View.GONE);
                        signInButton.setVisibility(View.VISIBLE);
                        eventsRv.setVisibility(View.GONE);
                    });
        });

        signInButton.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        if (account != null) {
            //prijavljen
            signInButton.setVisibility(View.GONE);
            signOutBtn.setVisibility(View.VISIBLE);

            String personEmail = account.getEmail();

            postUser(personEmail);

        } else {
            //nije prijavljen
            signOutBtn.setVisibility(View.GONE);
            signInButton.setSize(SignInButton.SIZE_STANDARD);
        }

    }

    private void postUser(String email) {

        Call<User> userPostCall = apiInterface.postUser(email);
        userPostCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User user = new User(response.body().getId(), response.body().getEmail());

                emailName = response.body().getEmail();

                App.setUserId(user.getId());

                initEventsRv(user.getId());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {



            }
        });
    }

    private void initEventsRv(int userId) {
        Call<List<Event>> eventsGetCall = apiInterface.getEvents(userId);
        eventsGetCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

                EventAdapter eventAdapter = new EventAdapter(response.body(), MainActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                eventsRv.setHasFixedSize(true);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL);
                eventsRv.addItemDecoration(dividerItemDecoration);
                eventsRv.setLayoutManager(linearLayoutManager);
                eventsRv.setAdapter(eventAdapter);

                eventAdapter.setOnEventClickListener(new OnEventClickListener() {
                    @Override
                    public void onClick(int eventId) {
                        Call<DetailedEvent> detailedEventCall = apiInterface.getEventDetails(userId, eventId);
                        detailedEventCall.enqueue(new Callback<DetailedEvent>() {
                            @Override
                            public void onResponse(Call<DetailedEvent> call, Response<DetailedEvent> response) {
                                App.setDetailedEvent(response.body());
                                startActivity(new Intent(MainActivity.this, PreviewEventActivity.class));
                            }

                            @Override
                            public void onFailure(Call<DetailedEvent> call, Throwable t) {

                            }
                        });
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            GoogleSignInAccount account = null;
            try {

                account = task.getResult(ApiException.class);
                if (account != null) {

                    signOutBtn.setVisibility(View.VISIBLE);
                    //ulogovao

                    signInButton.setVisibility(View.GONE);
                    eventsRv.setVisibility(View.VISIBLE);

                    String personEmail = account.getEmail();

                    postUser(personEmail);
                }

            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}