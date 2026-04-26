package com.ibtehazrafid.weatherroute;

import java.util.*;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import android.widget.TextView;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.ibtehazrafid.weatherroute.network.RoutesRequest;
import com.ibtehazrafid.weatherroute.viewmodel.RouteViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;



public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;
    //Views
    private TextInputEditText originInput, destInput;
    private LinearLayout stopsContainer;
    private MaterialButton addStopButton, dateButton, timeButton, getRouteButton, toggleStopsButton;
    private Chip chipDrive, chipWalk, chipCycle, chipTransit;
    private TextView stopCountLabel;
    //ViewModel
    private RouteViewModel routeViewModel;
    //State
    private String selectedTravelMode = "DRIVE";
    private long selectedDepartureTime = System.currentTimeMillis();
    private boolean stopsExpanded = true;
    private PlacesClient placesClient;
    private com.google.android.gms.maps.model.LatLng originLatLng, destLatLng;
    private Map<Integer, com.google.android.gms.maps.model.LatLng> stopLatLngs = new HashMap<>();
    private final ActivityResultLauncher<Intent> startAutoComplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    handleAutocompleteResult(place);
                } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                    Intent data = result.getData();
                    com.google.android.gms.common.api.Status status = Autocomplete.getStatusFromIntent(data);
                    Snackbar.make(findViewById(android.R.id.content), "Error: " + status.getStatusMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
    );
    private int activeInputId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.util.Log.d("APIKEY", "Key: " + BuildConfig.MAPS_API_KEY);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }
        placesClient = Places.createClient(this);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        originInput = findViewById(R.id.originInput);
        destInput = findViewById(R.id.destInput);
        stopsContainer = findViewById(R.id.stopsContainer);
        addStopButton = findViewById(R.id.addStopButton);
        stopCountLabel = findViewById(R.id.stopCountLabel);
        toggleStopsButton = findViewById(R.id.toggleStopsButton);
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        getRouteButton = findViewById(R.id.getRouteButton);
        chipDrive = findViewById(R.id.chipDrive);
        chipWalk = findViewById(R.id.chipWalk);
        chipCycle = findViewById(R.id.chipCycle);
        chipTransit = findViewById(R.id.chipTransit);

        routeViewModel = new ViewModelProvider(this).get(RouteViewModel.class);

        setupAutocompleteInputs();
        setupTravelModeChips();
        setupClickListeners();
        observeViewModel();
    }

    private void setupAutocompleteInputs() {
        originInput.setFocusable(false);
        originInput.setOnClickListener(v -> launchAutocomplete(R.id.originInput));

        destInput.setFocusable(false);
        destInput.setOnClickListener(v -> launchAutocomplete(R.id.destInput));
    }

    private void launchAutocomplete(int inputId) {
        activeInputId = inputId;
        List<Place.Field> field = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, field).build(this);
        startAutoComplete.launch(intent);
    }

    private void handleAutocompleteResult(Place place) {
        if (activeInputId == R.id.originInput) {
            originInput.setText(place.getName());
            originLatLng = place.getLatLng();
        } else if (activeInputId == R.id.destInput) {
            destInput.setText(place.getName());
            destLatLng = place.getLatLng();
        } else if (activeInputId >= 1000) {
            int stopIndex = activeInputId - 1000;
            LinearLayout row = (LinearLayout) stopsContainer.getChildAt(stopIndex);
            com.google.android.material.textfield.TextInputLayout layout = (com.google.android.material.textfield.TextInputLayout) row.getChildAt(0);
            TextInputEditText input = (TextInputEditText) layout.getEditText();
            if (input != null) {
                input.setText(place.getName());
            }
            stopLatLngs.put(stopIndex, place.getLatLng());
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setPadding(0, 300, 0, 180);
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
        toggleStopsButton.setOnClickListener(v -> toggleStops());
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
        int index = stopsContainer.getChildCount();
        if (index >= 25) {
            Snackbar.make(findViewById(android.R.id.content),
                    "Maximum 25 stops allowed", Snackbar.LENGTH_SHORT).show();
            return;
        }

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // Stop input
        com.google.android.material.textfield.TextInputLayout stopLayout = new com.google.android.material.textfield.TextInputLayout(this);
        stopLayout.setHint("Stop " + (index + 1));
        stopLayout.setBoxBackgroundMode(com.google.android.material.textfield.TextInputLayout.BOX_BACKGROUND_OUTLINE);
        com.google.android.material.textfield.TextInputEditText stopInput = new com.google.android.material.textfield.TextInputEditText(stopLayout.getContext());
        stopInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        stopLayout.addView(stopInput);
        LinearLayout.LayoutParams stopParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        stopParams.setMargins(0,0,8,16);
        stopLayout.setLayoutParams(stopParams);

        MaterialButton deleteButton = new MaterialButton(new android.view.ContextThemeWrapper(this, com.google.android.material.R.style.Widget_Material3_Button), null, 0);
        deleteButton.setIconResource(android.R.drawable.ic_menu_close_clear_cancel);
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        stopInput.setFocusable(false);
        int finalIndex = index;
        stopInput.setOnClickListener(v -> launchAutocomplete(1000 + finalIndex));

        row.addView(stopLayout);
        row.addView(deleteButton);

        deleteButton.setOnClickListener(v -> {
            stopsContainer.removeView(row);
            reindexStops();
            updateStopCountLabel();
        });

        stopsContainer.addView(row);
        updateStopCountLabel();
        toggleStopsButton.setVisibility(android.view.View.VISIBLE);
    }

    private void reindexStops() {
        stopLatLngs.clear();
        for (int i = 0; i < stopsContainer.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) stopsContainer.getChildAt(i);
            com.google.android.material.textfield.TextInputLayout layout = (com.google.android.material.textfield.TextInputLayout) row.getChildAt(0);
            layout.setHint("Stop " + (i + 1));
            TextInputEditText input = (TextInputEditText) layout.getEditText();
            if (input != null) {
                int finalI = i;
                input.setOnClickListener(v -> launchAutocomplete(1000 + finalI));
            }
        }
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
        if (originLatLng == null) {
            originInput.setError("Please enter a starting point!");
            return;
        }
        if (destLatLng == null) {
            destInput.setError("Please enter a destination!");
            return;
        }
        RoutesRequest.Waypoint originWaypoint = new RoutesRequest.Waypoint(new RoutesRequest.Location(new RoutesRequest.LatLng(originLatLng.latitude, originLatLng.longitude)));
        RoutesRequest.Waypoint destWaypoint = new RoutesRequest.Waypoint(new RoutesRequest.Location(new RoutesRequest.LatLng(originLatLng.latitude, destLatLng.longitude)));

        List<RoutesRequest.Waypoint> intermediates = new ArrayList<>();
        for (int i = 0; i < stopsContainer.getChildCount(); i++) {
            com.google.android.gms.maps.model.LatLng latLng = stopLatLngs.get(i);
            if (latLng != null) {
                intermediates.add(new RoutesRequest.Waypoint(new RoutesRequest.Location(new RoutesRequest.LatLng(latLng.latitude, latLng.longitude))));
            }
        }

        java.time.Instant instant = java.time.Instant.ofEpochMilli(selectedDepartureTime);
        String departureTime = instant.toString();

        com.ibtehazrafid.weatherroute.network.RoutesRequest request = new com.ibtehazrafid.weatherroute.network.RoutesRequest(
                originWaypoint, destWaypoint, intermediates, selectedTravelMode, departureTime);

        String fieldMask = "routes.duration,routes.staticDuration," +
                "routes.distanceMeters,routes.polyline.encodedPolyline," +
                "routes.legs.duration,routes.legs.staticDuration," +
                "routes.legs.distanceMeters,routes.legs.polyline.encodedPolyline," +
                "routes.legs.startLocation,routes.legs.endLocation";

        routeViewModel.computeRoute(request, fieldMask);
        stopsContainer.setVisibility(android.view.View.GONE);
        addStopButton.setVisibility(android.view.View.GONE);
    }

    private void toggleStops() {
        stopsExpanded = !stopsExpanded;
        if (stopsExpanded) {
            stopsContainer.setVisibility(android.view.View.VISIBLE);
            toggleStopsButton.setText("▲ Hide");
        } else {
            stopsContainer.setVisibility(android.view.View.GONE);
            toggleStopsButton.setText("▼ Show");
        }
        updateStopCountLabel();
    }

    private void updateStopCountLabel() {
        int count = stopsContainer.getChildCount();
        String text = String.format(Locale.getDefault(), "%d %s planned",
                count, count == 1 ? "Stop" : "Stops");
        stopCountLabel.setText(text);
        if (!stopsExpanded && count > 0) {
            stopCountLabel.setVisibility(android.view.View.VISIBLE);
        } else {
            stopCountLabel.setVisibility(android.view.View.GONE);
        }
    }

}
