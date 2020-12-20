package com.example.rezervacijakarata.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rezervacijakarata.App;
import com.example.rezervacijakarata.R;
import com.example.rezervacijakarata.datamodels.BookResponse;
import com.example.rezervacijakarata.datamodels.DetailedEvent;
import com.example.rezervacijakarata.datamodels.Event;
import com.example.rezervacijakarata.netutils.ApiClient;
import com.example.rezervacijakarata.netutils.ApiInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviewEventActivity extends AppCompatActivity {

    private static final String TAG = "rezKarataTag";
    GoogleMap mMap;
    ApiInterface apiInterface;
    TextView available_tickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_event);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        DetailedEvent detailedEvent = App.getDetailedEvent();

        TextView pair = findViewById(R.id.pair);
        TextView address = findViewById(R.id.address);
        TextView start = findViewById(R.id.start);
        TextView end = findViewById(R.id.end);
        TextView type = findViewById(R.id.type);
        TextView price = findViewById(R.id.price);
        TextView tickets = findViewById(R.id.tickets);
        available_tickets = findViewById(R.id.available_tickets);




        pair.setText( detailedEvent.getTeams().get(0).getName() + "   vs   " + detailedEvent.getTeams().get(1).getName());
        address.setText(detailedEvent.getAddress());
        start.setText(detailedEvent.getStart());
        end.setText(detailedEvent.getEnd());
        type.setText(detailedEvent.getSport());
        price.setText(""+detailedEvent.getPrice());
        tickets.setText(""+detailedEvent.getTickets());
        available_tickets.setText(""+detailedEvent.getAvailable_tickets());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;

            LatLng marker = new LatLng(detailedEvent.getLat(), detailedEvent.getWelcome10Long());
            mMap.addMarker(new MarkerOptions()
                    .position(marker));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
        });

        Button book = findViewById(R.id.book);

        book.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PreviewEventActivity.this);
            View view = LayoutInflater.from(PreviewEventActivity.this).inflate(R.layout.book_layout, null);

            EditText numberOfTicketsEt = view.findViewById(R.id.seats_number_et);
            TextView email = view.findViewById(R.id.mail_et);
            email.setText(MainActivity.getEmailName());

            builder.setView(view);
            builder.setPositiveButton("Book", (dialog, which) -> {

                Call<List<BookResponse>> bookResponseCall = apiInterface.bookTicket(App.getUserId(), detailedEvent.getId(), numberOfTicketsEt.getText().toString());
                bookResponseCall.enqueue(new Callback<List<BookResponse>>() {
                    @Override
                    public void onResponse(Call<List<BookResponse>> call, Response<List<BookResponse>> response) {
                        if (response.body() != null)

                                updateTickets(detailedEvent.getId());


                        else
                            Toast.makeText(PreviewEventActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<List<BookResponse>> call, Throwable t) {

                    }
                });

                dialog.dismiss();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        Button unbook = findViewById(R.id.unbook);

        unbook.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PreviewEventActivity.this);
            builder.setMessage("Are you sure you want to unbook tickets?");
            builder.setPositiveButton("Yes", (dialog, which) -> {

                Call<BookResponse> bookResponseCall = apiInterface.unbookTicket(App.getUserId(), detailedEvent.getId());
                bookResponseCall.enqueue(new Callback<BookResponse>() {
                    @Override
                    public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                        updateTickets(detailedEvent.getId());
                    }

                    @Override
                    public void onFailure(Call<BookResponse> call, Throwable t) {
                    }
                });

                dialog.dismiss();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });



    }

    private void updateTickets(int eventId) {
        Call<DetailedEvent> detailedEventCall = apiInterface.getEventDetails(App.getUserId(), eventId);
        detailedEventCall.enqueue(new Callback<DetailedEvent>() {
            @Override
            public void onResponse(Call<DetailedEvent> call, Response<DetailedEvent> response) {
                available_tickets.setText(""+response.body().getAvailable_tickets());
            }

            @Override
            public void onFailure(Call<DetailedEvent> call, Throwable t) {
            }
        });
    }

}