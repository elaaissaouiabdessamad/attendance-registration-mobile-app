package com.example.amapp;

import android.app.DownloadManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.example.amapp.beans.Student;
import com.example.amapp.beans.StudentDataSource;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 2;

    private boolean scanning = false;

    private StudentDataSource dataSource;

    private ArrayList<Student> students;
    private ListView studentListView;
    private StudentAdapter studentAdapter;
    private BluetoothAdapter bluetoothAdapter;

    private boolean isDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new StudentDataSource(this);
        dataSource.open();
        dataSource.clearTable();


        Student student1 = new Student("001", "ABDESSAMADELAAI", false, R.drawable.abdessamad_elaaissaoui);
        Student student2 = new Student("002", "Ilias ROUCHDI", false, R.drawable.ilias);
        Student student3 = new Student("003", "Outman OURICH", false, R.drawable.outman);
        Student student4 = new Student("004", "Khalid MOUSSAOUI", false, R.drawable.khalid);
        Student student5 = new Student("005", "Karim KARABI", false, R.drawable.karim);
        Student student6 = new Student("006", "Moussa HAJJAJI", false, R.drawable.moussa);
        Student student7 = new Student("007", "Brahim WAHMAN", false, R.drawable.brahim);
        Student student8 = new Student("008", "Mostafa SALIM", false, R.drawable.mostapha);


        dataSource.insertStudent(student1);
        dataSource.insertStudent(student2);
        dataSource.insertStudent(student3);
        dataSource.insertStudent(student4);
        dataSource.insertStudent(student5);
        dataSource.insertStudent(student6);
        dataSource.insertStudent(student7);
        dataSource.insertStudent(student8);


        students = dataSource.getAllStudents();



        // Add more students as needed...

        // Find the ListView in the layout
        studentListView = findViewById(R.id.studentListView);

        // Initialize Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check if Bluetooth is supported on the device
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }



        // Check if Bluetooth is enabled
        if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled, prompt the user to enable it
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            }
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
        }


        // Initialize ListView adapter for students
        studentAdapter = new StudentAdapter(this, students);
        ListView studentListView = findViewById(R.id.studentListView);

        ArrayList<Student> studentsFake = new ArrayList<>();

        StudentAdapter studentAdapterFake = new StudentAdapter(this, studentsFake);

        Button displayStudentsListButton = findViewById(R.id.displayStudentsListButton);
        displayStudentsListButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        displayStudentsListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isDisplayed)
                {
                    studentListView.setAdapter(studentAdapter);
                    displayStudentsListButton.setText("Disable Students List");
                    displayStudentsListButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    isDisplayed = true;
                }
                else
                {
                    studentListView.setAdapter(studentAdapterFake);
                    displayStudentsListButton.setText("Display Students List");
                    displayStudentsListButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

                    isDisplayed = false;
                }

            }
        });



        // Set up Bluetooth scan button click listener
        Button scanButton = findViewById(R.id.scanButton);
        scanButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startBluetoothScanning();
                startStopBluetoothScanning();
            }
        });
/*
        Button stopScanButton = findViewById(R.id.stopScanButton);
        stopScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBluetoothScanning();
            }
        });*/

        Button generateCsvButton = findViewById(R.id.generateCsvButton);

        // Set a click listener to the button
        generateCsvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateCSV();
            }
        });
    }

    private void startBluetoothScanning() {
        // Check if Bluetooth is enabled
        if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled, prompt the user to enable it
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            }
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
            return;
        }




        resetStudentAttendance();

        // Start scanning for nearby Bluetooth devices
        bluetoothAdapter.startDiscovery();
        scanning = true;
        //Toast.makeText(this, "Bluetooth scanning started ", Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Bluetooth scanning started ...", Snackbar.LENGTH_LONG);
        snackbar.show();


    }

    private void resetStudentAttendance() {
        for (Student student : students) {
            student.setPresent(false);
        }
        studentAdapter.notifyDataSetChanged();
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // A Bluetooth device has been found
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Check if the found device name matches any known student name
                for (Student student : students) {

                    if (student.getName().equals(device.getName())) {
                        // Found a matching student device, update presence status
                        student.setPresent(true);
                    }
                }
                // Update the ListView adapter to reflect changes
                studentAdapter.notifyDataSetChanged();
            }
        }
    };

    private void startStopBluetoothScanning() {
        if (!scanning) {
            startBluetoothScanning();
        } else {
            stopBluetoothScanning();
        }
        // Update the state of the Scan button
        updateScanButtonState();
    }

    private void updateScanButtonState() {
        Button scanButton = findViewById(R.id.scanButton);
        if (scanning) {
            // Change button text to "Stop" if scanning is in progress
            scanButton.setText("Stop Scanning");
            scanButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));

        } else {
            // Change button text to "Scan Bluetooth" if scanning is not in progress
            scanButton.setText("Start Scanning");
            scanButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the BroadcastReceiver to listen for Bluetooth device discovery events
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the BroadcastReceiver when the activity is paused
        unregisterReceiver(bluetoothReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure Bluetooth discovery is stopped when the activity is destroyed
        if (bluetoothAdapter != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

            }
            bluetoothAdapter.cancelDiscovery();
        }


            dataSource.close();

    }

    private void stopBluetoothScanning() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

        }

            bluetoothAdapter.cancelDiscovery();
            scanning = false;
            //Toast.makeText(this, "Bluetooth scanning stopped", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Bluetooth scanning stopped ...", Snackbar.LENGTH_LONG);
            snackbar.show();

    }

    private void showDetails(String title, String message) {
        // Create a dialog to display image details
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        builder.setMessage(message);

        // Add a button to close the dialog
        builder.setPositiveButton("OK", null);

        // Show the dialog
        builder.show();
    }

    private void generateCSV() {
        StringBuilder csvContent = new StringBuilder();

        // Add CSV header with date column
        csvContent.append("Student Number,Name,Attendance Status,Date\n");

        // Get current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        // Add student data to CSV with date
        for (Student student : students) {
            csvContent.append(student.getStudentNumber()).append(",");
            csvContent.append(student.getName()).append(",");
            csvContent.append(student.isPresent() ? "Present" : "Absent").append(",");
            csvContent.append(currentDate).append("\n");
        }

        // Write CSV content to a file
        try {
            File csvFile = new File(getExternalFilesDir(null), "attendance.csv");
            if (!csvFile.exists()) {
                csvFile.createNewFile();
            }

            try (FileWriter writer = new FileWriter(csvFile)) {
                writer.write(csvContent.toString());
            }

            // Show success message
            showDetails("The Attendance List", "The Path of The List : " + csvFile.getAbsolutePath());
            showDetails("The Attendance List Generation", "The Attendance List generated Successfully !");

          //  downloadFile(csvFile);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate CSV file", Toast.LENGTH_SHORT).show();
        }
    }


//    private void downloadFile(File file) {
//        Context context = getApplicationContext();
//        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri uri = Uri.fromFile(file);
//
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file.getName());
//
//        downloadManager.enqueue(request);
//    }






}