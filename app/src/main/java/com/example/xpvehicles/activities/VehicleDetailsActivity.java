package com.example.xpvehicles.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.xpvehicles.interfaces.OrderInformation;
import com.example.xpvehicles.interfaces.ParentActivity;
import com.example.xpvehicles.miscellaneous.RentingStatus;
import com.example.xpvehicles.R;
import com.example.xpvehicles.adapters.VehicleImagesAdapter;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.StorageCenter;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class VehicleDetailsActivity extends AppCompatActivity implements ParentActivity, OrderInformation {

    private static final String TAG = "VehicleDetailsActivity";
    public static final String STRIPE_API_PARAMETER_AMOUNT = "amount";
    public static final String STRIPE_API_PARAMETER_CURRENCY = "currency";
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
    private TextView tvVehicleImagePosition;
    private Button btnReserveNow;
    private TextInputLayout detailsPickupDateOTF;
    private TextInputLayout detailsReturnDateOTF;
    private Date pickupDate;
    private Date returnDate;
    private int distanceFromUser;
    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;
    private PaymentSheet.CustomerConfiguration customerConfig;
    private Number dailyPrice;
    private Double orderTotal;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        vehicle = (Vehicle) getIntent().getParcelableExtra("vehicle");
        distanceFromUser = getIntent().getIntExtra("distanceFromUser", 0);
        setContentView(R.layout.activity_vehicle_details);
        bind();
        bindVehicleImagesAdapter();
        setValues();
        setTopAppBarOnClickListener(topAppBar, this);
        setPickupDateOnClickListener();
        setReturnDateOnClickListener();
        setReserveNowOnClickListener();
    }

    private void bindVehicleImagesAdapter() {
        List<ParseFile> images = vehicle.getVehicleImages();
        VehicleImagesAdapter vehicleImagesAdapter = new VehicleImagesAdapter(this, images);
        viewPager.setAdapter(vehicleImagesAdapter);
        setVehicleSwipeListener(viewPager, images.size());
    }

    private void setVehicleSwipeListener(ViewPager2 viewPager, int totalNumberOfImages) {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
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
        viewPager = findViewById(R.id.viewPagerDetailsVehicleImages);
        tvVehicleImagePosition = findViewById(R.id.tvVehicleImagePosition);
    }

    private void setValues() {
        dailyPrice = vehicle.getDailyPrice();
        tvDetailsDistanceFromUser.setText(distanceFromUser + " mi.");
        tvDetailsVehicleName.setText(vehicle.getVehicleName());
        tvDetailsVehicleDescription.setText(vehicle.getDescription());
        tvDetailsDailyPrice.setText("$" + dailyPrice + "/day");
        tvOrderSummaryDailyPrice.setText("$" + dailyPrice);
        tvOrderSummaryNumberOfDays.setText("0");
        tvOrderSummaryOrderTotal.setText("$0");
    }

    private void resetPickUpDateError() {
        detailsPickupDateOTF.setError(null);
    }

    private void resetReturnDateError() {
        detailsReturnDateOTF.setError(null);
    }

    private void setMaterialDatePickerClearFocus (MaterialDatePicker<Long> materialDatePicker, EditText editTextDate) {
        materialDatePicker.addOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (editTextDate.getText().toString().isEmpty()) {
                    editTextDate.clearFocus();
                }
            }
        });
    }

    private MaterialDatePicker<Long> getCalender(String title) {
        CalendarConstraints.Builder calendarConstraintBuilder = new CalendarConstraints.Builder();
        // prevent users form selecting any previous dates from current day
        calendarConstraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText(title);
        materialDateBuilder.setCalendarConstraints(calendarConstraintBuilder.build());
        MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
        return materialDatePicker;
    }

    private void displayCalender(MaterialDatePicker<Long> materialDatePicker) {
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
    }

    private void setPickupDateOnClickListener() {
        String title =  "PICKUP DATE";
        MaterialDatePicker<Long> materialDatePicker =  getCalender(title);
        setPickupCalenderListener(materialDatePicker);
        setMaterialDatePickerClearFocus(materialDatePicker, edtPickUpDate);

        //prevents keyboard from popping up
        edtPickUpDate.setInputType(InputType.TYPE_NULL);
        edtPickUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCalender(materialDatePicker);
            }
        });
        edtPickUpDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    displayCalender(materialDatePicker);
                }
            }
        });
    }

    private void setPickupCalenderListener(MaterialDatePicker<Long> materialDatePicker) {
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
                    calculateOrderInformation();
                }
            }
        });
    }

    private void setReturnDateOnClickListener() {
        String title =  "RETURN DATE";
        MaterialDatePicker<Long> materialDatePicker =  getCalender(title);
        setReturnCalenderListener(materialDatePicker);
        setMaterialDatePickerClearFocus(materialDatePicker, edtReturnDate);

        //prevents keyboard from popping up
        edtReturnDate.setInputType(InputType.TYPE_NULL);
        edtReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCalender(materialDatePicker);
            }
        });
        edtReturnDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    displayCalender(materialDatePicker);
                }
            }
        });
    }

    private void setReturnCalenderListener(MaterialDatePicker<Long> materialDatePicker) {
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
                    calculateOrderInformation();
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

    private void calculateOrderInformation() {
        if (pickupDate != null && returnDate != null) {
            resetReturnDateError();
            resetPickUpDateError();
            int numberOfRentDays = getNumberOfDays(pickupDate, returnDate);
            tvOrderSummaryNumberOfDays.setText(String.valueOf(numberOfRentDays));
            Number dailyPrice =  vehicle.getDailyPrice();
            orderTotal =  getOrderTotal(numberOfRentDays, (int) dailyPrice);
            tvOrderSummaryOrderTotal.setText("$" + orderTotal);
        }
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
            validDates = false;
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
                    params.put(STRIPE_API_PARAMETER_AMOUNT, orderTotal * 100);
                    params.put(STRIPE_API_PARAMETER_CURRENCY, "usd");
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

    private void presentPaymentSheet() {
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("XP Vehicles")
                .customer(customerConfig)
                .allowsDelayedPaymentMethods(false).build();
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration);
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "Canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e(TAG, "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Log.d(TAG, "Completed");
            _User currentUser = (_User) ParseUser.getCurrentUser();
            RentVehicle rentVehicle = getRentVehicle(currentUser);
            String title = "Request submitted!";
            String message = "Thank you for your order.";

            saveRequestVehicle(currentUser, rentVehicle);
            alertDisplayer(title ,message);
            resetPickupAndReturnDateSelector();
            resetOrderSummary();
            setPushNotification();
        }
    }

    private RentVehicle getRentVehicle(_User currentUser) {
        RentVehicle rentVehicle = new RentVehicle();
        rentVehicle.setVehicle(vehicle);
        rentVehicle.setRentee(currentUser);
        rentVehicle.setPickUpDate(pickupDate);
        rentVehicle.setReturnDate(returnDate);
        rentVehicle.setStatus(RentingStatus.PENDING_APPROVAL.name());
        rentVehicle.saveInBackground();
        return rentVehicle;
    }

    private void saveRequestVehicle(_User currentUser, RentVehicle rentVehicle) {
        List<RentVehicle> rentedVehicles = currentUser.getRentedVehicles();
        rentedVehicles.add(rentVehicle);
        currentUser.setRentedVehicles(rentedVehicles);
        currentUser.saveInBackground();
    }

    private void setPushNotification() {
        _User vehicleOwner = vehicle.getOwner();
        String vehicleOwnerObjectId =  vehicleOwner.getObjectId();
        String title = "New Vehicle Rent Request!";
        String alert = "Request for " + vehicle.getVehicleName();

        sendPushNotification(vehicleOwnerObjectId, title, alert);
    }

    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void resetPickupAndReturnDateSelector() {
        edtPickUpDate.getText().clear();
        edtPickUpDate.clearFocus();
        edtReturnDate.getText().clear();
        edtReturnDate.clearFocus();
    }

    private void resetOrderSummary() {
        tvOrderSummaryNumberOfDays.setText("0");
        tvOrderSummaryOrderTotal.setText("$0");
    }
}