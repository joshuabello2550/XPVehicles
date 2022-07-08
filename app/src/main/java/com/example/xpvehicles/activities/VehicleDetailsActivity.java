package com.example.xpvehicles.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.xpvehicles.Miscellaneous.RentingStatus;
import com.example.xpvehicles.R;
import com.example.xpvehicles.adapters.VehicleImagesAdapter;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class VehicleDetailsActivity extends AppCompatActivity {

    public static final String TAG = "VehicleDetailsActivity";
    private Vehicle vehicle;
    private MaterialToolbar topAppBar;
    private EditText edtPickUpDate;
    private EditText edtReturnDate;
    private TextView tvDetailsVehicleName;
    private TextView tvDetailsVehicleDescription;
    private TextView tvDetailsDailyPrice;
    private TextView tvDetailsDistanceFromUser;
    private TextView tvOrderSummaryDailyPrice;
    private TextView tvOrderSummaryNumberOfDays;
    private TextView tvOrderSummaryOrderTotal;
    private Button btnReserveNow;
    private TextInputLayout detailsPickupDateOTF;
    private TextInputLayout detailsReturnDateOTF;
    private Date pickupDate;
    private Date returnDate;
    private int distanceFromUser;
    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;
    private PaymentSheet.CustomerConfiguration customerConfig;
    private Number orderTotal;

    private void setTopAppBarOnClickListener() {
        topAppBar.setNavigationOnClickListener(v -> {
            this.finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        vehicle = (Vehicle) getIntent().getParcelableExtra("vehicle");
        distanceFromUser = getIntent().getIntExtra("distanceFromUser", 0);
        setContentView(R.layout.activity_vehicle_details);
        bindVehicleImagesAdapter();
        bind();
        setValues();
        setTopAppBarOnClickListener();
        setPickupDateOnClickListener();
        setReturnDateOnClickListener();
        setReserveNowOnClickListener();
    }

    private void bindVehicleImagesAdapter() {
        List<ParseFile> images = vehicle.getVehicleImages();
        ViewPager2 viewPager = findViewById(R.id.viewPagerDetailsVehicleImages);
        VehicleImagesAdapter vehicleImagesAdapter = new VehicleImagesAdapter(this, images);
        viewPager.setAdapter(vehicleImagesAdapter);
        setVehicleSwipeListener(viewPager, images.size());
    }

    private void setVehicleSwipeListener(ViewPager2 viewPager, int totalNumberOfImages) {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                TextView tvVehicleImagePosition = findViewById(R.id.tvVehicleImagePosition);
                int currentPosition = position + 1;
                tvVehicleImagePosition.setText(currentPosition + " / " + totalNumberOfImages);
            }
        });
    }

    private void bind() {
        btnReserveNow = findViewById(R.id.btnReserveNow);
        edtPickUpDate = findViewById(R.id.edtPickUpDate);
        edtReturnDate = findViewById(R.id.edtReturnDate);
        topAppBar = findViewById(R.id.filterTopAppBar);
        tvDetailsVehicleName = findViewById(R.id.tvDetailsVehicleName);
        tvDetailsVehicleDescription = findViewById(R.id.tvDetailsVehicleDescription);
        tvDetailsDailyPrice = findViewById(R.id.tvDetailsDailyPrice);
        tvDetailsDistanceFromUser = findViewById(R.id.tvDetailsDistanceFromUser);
        tvOrderSummaryDailyPrice = findViewById(R.id.tvOrderSummaryDailyPrice);
        tvOrderSummaryNumberOfDays = findViewById(R.id.tvOrderSummaryNumberOfDays);
        tvOrderSummaryOrderTotal = findViewById(R.id.tvOrderSummaryOrderTotal);
        detailsPickupDateOTF = findViewById(R.id.detailsPickupDateOTF);
        detailsReturnDateOTF = findViewById(R.id.detailsReturnDateOTF);
    }

    private void setValues() {
        tvDetailsDistanceFromUser.setText(distanceFromUser + " mi.");
        tvDetailsVehicleName.setText(vehicle.getVehicleName());
        tvDetailsVehicleDescription.setText(vehicle.getDescription());
        tvDetailsDailyPrice.setText("$" + vehicle.getDailyPrice() + "/day");
        tvOrderSummaryDailyPrice.setText("$" + vehicle.getDailyPrice());
        tvOrderSummaryNumberOfDays.setText("0");
        tvOrderSummaryOrderTotal.setText("$0");
    }

    private void resetPickUpDateError() {
        detailsPickupDateOTF.setError(null);
    }

    private void resetReturnDateError() {
        detailsReturnDateOTF.setError(null);
    }

    private void setPickupDateOnClickListener() {
        //prevents keyboard from popping up
        edtPickUpDate.setInputType(InputType.TYPE_NULL);
        edtPickUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPickUpDateCalender();
            }
        });
        edtPickUpDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    displayPickUpDateCalender();
                }
            }
        });
    }

    private void displayPickUpDateCalender() {
        CalendarConstraints.Builder calendarConstraintBuilder = new CalendarConstraints.Builder();
        // prevent users form selecting any previous dates from current day
        calendarConstraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("PICKUP DATE");
        materialDateBuilder.setCalendarConstraints(calendarConstraintBuilder.build());
        MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

        // positive button == ok button
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                calendar.setTimeInMillis(selection);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String formattedDate = sdf.format(calendar.getTime());
                edtPickUpDate.setText(formattedDate);
                try {
                    // Converts the string date back into a date Object
                    pickupDate = sdf.parse(formattedDate);
                } catch (ParseException e) {
                    Log.e(TAG, "error converting the pickup date into a date object", e);
                }
                if (checkValidPickUpDate()) {
                    resetPickUpDateError();
                    calculateNumberOfDays();
                }
            }
        });
    }


    private void setReturnDateOnClickListener() {
        //prevents keyboard from popping up
        edtReturnDate.setInputType(InputType.TYPE_NULL);
        edtReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayReturnDateCalender();
            }
        });
        edtReturnDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    displayReturnDateCalender();
                }
            }
        });
    }

    private void displayReturnDateCalender() {
        CalendarConstraints.Builder calendarConstraintBuilder = new CalendarConstraints.Builder();
        // prevent users form selecting any previous dates from current day
        calendarConstraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("RETURN DATE");
        materialDateBuilder.setCalendarConstraints(calendarConstraintBuilder.build());
        MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

        // positive button == ok button
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                calendar.setTimeInMillis(selection);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String formattedDate = sdf.format(calendar.getTime());
                edtReturnDate.setText(formattedDate);
                try {
                    // Converts the string date back into a date Object
                    returnDate = sdf.parse(formattedDate);
                } catch (ParseException e) {
                    Log.e(TAG, "error converting the return date into a date object", e);
                }
                if (checkValidReturnDate()) {
                    resetReturnDateError();
                    calculateNumberOfDays();
                }
            }
        });
    }

    private Boolean checkValidPickUpDate() {
        Boolean validReturnDate = true;
        if (returnDate != null && returnDate.getTime() - pickupDate.getTime() < 0) {
            detailsPickupDateOTF.setError("Enter a valid date");
            validReturnDate =  false;
        }
        return validReturnDate;
    }

    private Boolean checkValidReturnDate() {
        Boolean validReturnDate = true;
        if (pickupDate != null && returnDate.getTime() - pickupDate.getTime() < 0) {
            detailsReturnDateOTF.setError("Enter a valid date");
            validReturnDate =  false;
        }
        return validReturnDate;
    }

    private void calculateNumberOfDays() {
        if (pickupDate != null && returnDate != null) {
            resetReturnDateError();
            resetPickUpDateError();
            int conversionDifference = 1;
            int conversionFactor = (1000 * 60 * 60 * 24);
            int numberOfRentDays = (int) ((returnDate.getTime() - pickupDate.getTime()) / (conversionFactor)) + conversionDifference;
            tvOrderSummaryNumberOfDays.setText(String.valueOf(numberOfRentDays));
            calculateOrderTotal(numberOfRentDays);
        }
    }

    private void calculateOrderTotal(int numberOfRentDays) {
        orderTotal = numberOfRentDays * (int) vehicle.getDailyPrice();
        Log.i(TAG, String.valueOf(orderTotal));
        tvOrderSummaryOrderTotal.setText("$" + orderTotal);
    }

    private Boolean checkValidDates() {
        Boolean validDates = true;
        if (pickupDate == null) {
            detailsPickupDateOTF.setError("Enter a valid date");
            validDates =  false;
        }
        if (returnDate == null) {
            detailsReturnDateOTF.setError("Enter a valid date");
            validDates =  false;
        }
        if (pickupDate != null && returnDate != null && returnDate.getTime() - pickupDate.getTime() < 0) {
            Toast.makeText(VehicleDetailsActivity.this, "Invalid Pickup or Return date", Toast.LENGTH_SHORT);
            validDates =  false;
        }
        return validDates;
    }

    private void setReserveNowOnClickListener() {
        btnReserveNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean validDates = checkValidDates();
                if (validDates) {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("amount", (Integer) orderTotal * 100);
                    params.put("currency", "usd");
                    ParseCloud.callFunctionInBackground("paymentSheet", params, new FunctionCallback<HashMap<String, String>>() {
                        @Override
                        public void done(HashMap<String, String> result, com.parse.ParseException e) {
                            PaymentConfiguration.init(getApplicationContext(), result.get("publishableKey"));
                            customerConfig = new PaymentSheet.CustomerConfiguration(
                                    result.get("customer"),
                                    result.get("ephemeralKey"));
                            paymentIntentClientSecret = result.get("paymentIntent");
                            presentPaymentSheet();
                        }
                    });
                }
            }
        });
    }
    
    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "Canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e(TAG, "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Log.d(TAG, "Completed");
            _User currentUser = (_User) ParseUser.getCurrentUser();
            RentVehicle rentVehicle = createRentVehicle(currentUser);
            requestVehicle(currentUser, rentVehicle);
            finish();
        }
    }

    private void presentPaymentSheet() {
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("XP Vehicles")
                .customer(customerConfig)
                .allowsDelayedPaymentMethods(false).build();
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration);
    }

    private RentVehicle createRentVehicle(_User currentUser) {
        RentVehicle rentVehicle = new RentVehicle();
        rentVehicle.setVehicle(vehicle);
        rentVehicle.setRentee(currentUser);
        rentVehicle.setPickUpDate(pickupDate);
        rentVehicle.setReturnDate(returnDate);
        rentVehicle.setStatus(RentingStatus.PENDING_APPROVAL.name());
        rentVehicle.saveInBackground();
        return rentVehicle;
    }

    private void requestVehicle(_User currentUser, RentVehicle rentVehicle) {
        List<RentVehicle> rentedVehicles = currentUser.getRentedVehicles();
        rentedVehicles.add(rentVehicle);
        currentUser.setRentedVehicles(rentedVehicles);
        currentUser.saveInBackground();
    }
}