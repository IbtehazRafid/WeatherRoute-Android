package com.ibtehazrafid.weatherroute;

import java.util.*;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import android.widget.LinearLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.ibtehazrafid.weatherroute.network.RoutesRequest;
import com.ibtehazrafid.weatherroute.viewmodel.RouteViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;
    //Views
    private TextInputEditText originInput, destInput;
    private LinearLayout stopsContainer;
    private MaterialButton addStopButton, dateButton, timeButton, getRouteButton;
    private Chip chipDrive, chipWalk, chipCycle, chipTransit;
    //ViewModel
    private RouteViewModel routeViewModel;
    //State
    private String selectedTravelMode = "DRIVE";
    private long selectedDepartureTime = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        originInput = findViewById(R.id.originInput);
        destInput = findViewById(R.id.destInput);
        stopsContainer = findViewById(R.id.stopsContainer);
        addStopButton = findViewById(R.id.addStopButton);
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        getRouteButton = findViewById(R.id.getRouteButton);
        chipDrive = findViewById(R.id.chipDrive);
        chipWalk = findViewById(R.id.chipWalk);
        chipCycle = findViewById(R.id.chipCycle);
        chipTransit = findViewById(R.id.chipTransit);

        routeViewModel = new ViewModelProvider(this).get(RouteViewModel.class);

        setupTravelModeChips();
        setupClickListeners();
        observeViewModel();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void setupTravelModeChips() {
        Chip[] chips = {chipDrive, chipWalk, chipCycle, chipTransit};
        String[] modes = {"DRIVE", "WALK", "BICYCLE", "TRANSIT"};
        for (int i = 0; i < chips.length; i++) {
            final String mode = modes[i];
            chips[i].setOnClickListener(v -> {
                for (Chip chip : chips) {
                    chip.setChecked(false);
                }
                ((Chip) v).setChecked(true);
                selectedTravelMode = mode;
            });
        }
    }

    private void setupClickListeners() {
        addStopButton.setOnClickListener(v -> addStopField());
        dateButton.setOnClickListener(v -> showDatePicker());
        timeButton.setOnClickListener(v -> showTimePicker());
        getRouteButton.setOnClickListener(v -> computeRoute());
    }

    private void observeViewModel() {
        routeViewModel.getIsLoading().observe(this, isLoading -> {
            getRouteButton.setEnabled(!isLoading);
            getRouteButton.setText(isLoading ? "Loading..." : "Get WeatherRoute");
        });

        routeViewModel.getErrorMesg().observe(this, errorMsg -> {
            if (errorMsg != null) {
                com.google.android.material.snackbar.Snackbar.make(findViewById(android.R.id.content), errorMsg, com.google.android.material.snackbar.Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void addStopField() {
        if (stopsContainer.getChildCount() >= 25) {
            Snackbar.make(findViewById(android.R.id.content),
                    "Maximum 25 stops allowed", Snackbar.LENGTH_SHORT).show();
            return;
        }
        com.google.android.material.textfield.TextInputLayout stopLayout = new com.google.android.material.textfield.TextInputLayout(this);
        stopLayout.setHint("Stop" + (stopsContainer.getChildCount() + 1));
        stopLayout.setBoxBackgroundMode(com.google.android.material.textfield.TextInputLayout.BOX_BACKGROUND_OUTLINE);
        com.google.android.material.textfield.TextInputEditText stopInput = new com.google.android.material.textfield.TextInputEditText(stopLayout.getContext());
        stopInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        stopLayout.addView(stopInput);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,16);
        stopLayout.setLayoutParams(params);
        stopsContainer.addView(stopLayout);

    }

    private void showDatePicker() {
    com.google.android.material.datepicker.MaterialDatePicker<Long> datePicker =
        com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select departure date")
            .setSelection(selectedDepartureTime)
            .build();

    datePicker.addOnPositiveButtonClickListener(selection -> {
        selectedDepartureTime = selection;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        dateButton.setText(sdf.format(new Date(selection)));
    });

    datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
}

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDepartureTime);

        com.google.android.material.timepicker.MaterialTimePicker timePicker =
            new com.google.android.material.timepicker.MaterialTimePicker.Builder()
                .setTimeFormat(com.google.android.material.timepicker.TimeFormat.CLOCK_12H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTitleText("Select departure time")
                .build();

        timePicker.addOnPositiveButtonClickListener(v -> {
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
            selectedDepartureTime = calendar.getTimeInMillis();
            timeButton.setText(String.format(Locale.getDefault(),
                "%02d:%02d", timePicker.getHour(), timePicker.getMinute()));
        });

        timePicker.show(getSupportFragmentManager(), "TIME_PICKER");
    }

    private void computeRoute() {
        String origin = originInput.getText() != null ?
                originInput.getText().toString().trim() : "";
        String dest = destInput.getText() != null ?
                destInput.getText().toString().trim() : "";
        if (origin.isEmpty()) {
            originInput.setError("Please enter a starting point!");
            return;
        }
        if (dest.isEmpty()) {
            destInput.setError("Please enter a destination!");
            return;
        }
        RoutesRequest.Waypoint originWaypoint = new RoutesRequest.Waypoint(new RoutesRequest.Location(new RoutesRequest.LatLng(0, 0)));
        RoutesRequest.Waypoint destWaypoint = new RoutesRequest.Waypoint(new RoutesRequest.Location(new RoutesRequest.LatLng(0, 0)));

        List<RoutesRequest.Waypoint> intermidiates = new ArrayList<>();
        for (int i = 0; i < stopsContainer.getChildCount(); i++) {
            intermidiates.add(new RoutesRequest.Waypoint(
                    new RoutesRequest.Location(new RoutesRequest.LatLng(0, 0))));
        }

        java.time.Instant instant = java.time.Instant.ofEpochMilli(selectedDepartureTime);
        String departureTime = instant.toString();

        com.ibtehazrafid.weatherroute.network.RoutesRequest request = new com.ibtehazrafid.weatherroute.network.RoutesRequest(
                originWaypoint, destWaypoint, intermidiates, selectedTravelMode, departureTime);

        String fieldMask = "routes.duration,routes.staticDuration," +
                "routes.distanceMeters,routes.polyline.encodedPolyline," +
                "routes.legs.duration,routes.legs.staticDuration," +
                "routes.legs.distanceMeters,routes.legs.polyline.encodedPolyline," +
                "routes.legs.startLocation,routes.legs.endLocation";

        routeViewModel.computeRoute(request, fieldMask);

    }

}