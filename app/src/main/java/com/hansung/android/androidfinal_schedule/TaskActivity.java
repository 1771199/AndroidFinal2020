package com.hansung.android.androidfinal_schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DBHelper mDbHelper;
    GoogleMap googleMap;
    LatLng glocation;
    Button deletebtn;
    Button savebtn;
    EditText _id;
    EditText title;
    TextView date;
    EditText starth;
    EditText endh;
    EditText startm;
    EditText endm;
    EditText place;
    Button search;
    EditText memo;
    Button recbtn;
    Button recstopbtn;
    Button playrecbtn;
    Button camerabtn;
    Button videobtn;
    ImageView imageView;
    VideoView videoView;
    MediaRecorder mMediaRecorder;
    private String mPhotoFileName = null;
    private File mPhotoFile = null;
    private File destination = null;
    private String mVideoFileName = null;
    private String mRecordFileName = null;
    private Uri videoUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;
    public static boolean isNew;
    public static SingleTask task;
    MediaPlayer mediaPlayer;
    MediaController mc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        mDbHelper = new DBHelper(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUI();

        Intent intent = getIntent();
        isNew = intent.getExtras().getBoolean("isNew");
        if(isNew == false){
            task = (SingleTask) intent.getSerializableExtra("SingleTask");
            setTaskDetail();
        }
        btnTextSet();

        killMediaPlayer();

        savebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isNew){
                    insertRecord();
                }
                else updateRecord();
                finish();

            }
        });
        deletebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                deleteRecord();
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAndSetLocation(place.getText().toString());
            }
        });

        recbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudioRec();
            }
        });

        recstopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudioRec();
            }
        });

        playrecbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    playAudio();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        camerabtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dispatchTakePictureIntent();
            }
        });


        videobtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dispatchTakeVideoIntent();
            }
        });



    }

    private void killMediaPlayer() {
        if(mediaPlayer!=null){
            try{
                mediaPlayer.release();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected  void onStop() {
        super.onStop();
        killMediaPlayer();

    }

    /*
    ---------- 초기 설정 ----------
    */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if(isNew == false){
            getAndSetLocation(task.place);
        }
        else initMap();

    }

    public void setUI(){
        deletebtn = findViewById(R.id.delete);
        savebtn = findViewById(R.id.save);
        title = findViewById(R.id.task_name);
        date = findViewById(R.id.task_date);
        starth = findViewById(R.id.start_h);
        startm = findViewById(R.id.start_m);
        endh = findViewById(R.id.end_h);
        endm = findViewById(R.id.end_m);
        place = findViewById(R.id.place);
        search = findViewById(R.id.search);
        memo = findViewById(R.id.memo);
        recbtn = findViewById(R.id.start_rec);
        recstopbtn = findViewById(R.id.stop_rec);
        playrecbtn = findViewById(R.id.play_rec);
        camerabtn = findViewById(R.id.camerabtn);
        imageView = findViewById(R.id.image);
        videobtn = findViewById(R.id.videobtn);
        videoView = findViewById(R.id.video);
    }

    private void initUI(){
        title.setText("");
        date.setText("");
        starth.setText("");
        startm.setText("");
        endh.setText("");
        endm.setText("");
        place.setText("");
        memo.setText("");
        mPhotoFileName = "";
        mVideoFileName = "";
    }

    public void btnTextSet(){
        if(isNew) deletebtn.setActivated(false);
        else savebtn.setText("Revise");
    }



    /*
    ---------- DB 관리 ----------
    */

    private void deleteRecord() {

        long nOfRows = mDbHelper.deleteUserByMethod(String.valueOf(task._id));
        if (nOfRows >0){
            initUI();
            Toast.makeText(this,"일정 삭제 완료!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"삭제된 일정 없음", Toast.LENGTH_SHORT).show();
    }

    private void insertRecord() {

        long nOfRows = mDbHelper.insertTaskByMethod(title.getText().toString(), date.getText().toString(), starth.getText()+":"+startm.getText(),
        endh.getText()+":"+endm.getText(), place.getText().toString(),memo.getText().toString(), mPhotoFileName, mVideoFileName, mRecordFileName);

        if (nOfRows >0){
            //initUI();
            Toast.makeText(this,"일정 추가 완료!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"추가된 일정 없음", Toast.LENGTH_SHORT).show();
    }

    private void updateRecord() {

        long nOfRows = mDbHelper.updateTaskByMethod(String.valueOf(task._id), title.getText().toString(), date.getText().toString(), starth.getText()+":"+startm.getText(),
                endh.getText()+":"+endm.getText(), place.getText().toString(),memo.getText().toString(), mPhotoFileName, mVideoFileName, mRecordFileName);

        if (nOfRows >0)
            Toast.makeText(this,"일정 수정 완료!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"수정된 일정 없음", Toast.LENGTH_SHORT).show();
    }

    /*
    ---------- 멀티미디어 ----------
    */

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            mPhotoFileName = "IMG" + currentDateFormat() + ".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);
            if (mPhotoFile != null) {
                Uri imgUri = FileProvider.getUriForFile(this, "com.hansung.android.androidfinal_schedule.fileprovider", mPhotoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            mVideoFileName = "VIDEO" + currentDateFormat() + ".mp4";
            destination = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), mVideoFileName);

            if (destination != null) {
                videoUri = FileProvider.getUriForFile(this, "com.hansung.android.androidfinal_schedule.fileprovider", destination);

                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(mPhotoFile != null){
                mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        mPhotoFileName);

                imageView.setImageURI(Uri.fromFile(mPhotoFile));
            }
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            if(mVideoFileName != null){
                mc = new MediaController(this);
                destination = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES),
                        mVideoFileName);
                Log.e("VideoFileName: ",destination.getName());
                videoView.setVideoURI(Uri.fromFile(destination));
                videoView.setMediaController(mc);


                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer player) {
                        videoView.seekTo(0);
                        videoView.start();
                    }
                });

            }

        }
    }

    private void startAudioRec()  {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        mRecordFileName = "VOICE" + currentDateFormat() + ".mp4";

        // 출력 파일의 위치를 앱 전용 외부저장소의 /Music/ 위치로 설정
        mMediaRecorder.setOutputFile(getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath()+"/"  + mRecordFileName);


        try {
            mMediaRecorder.prepare();
            Toast.makeText(getApplicationContext(), "녹음을 시작하세요.", Toast.LENGTH_SHORT).show();
            mMediaRecorder.start();
        } catch (Exception ex) {
            Log.e("SampleAudioRecorder", "Exception : ", ex);
        }
    }

    private void stopAudioRec(){
        if(mMediaRecorder!=null){
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            Toast.makeText(getApplicationContext(), "녹음을 종료합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void playAudio() throws IOException {
        String uri = "file://" +
                getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath()+ "/" + mRecordFileName;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(uri);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                Toast.makeText(getApplicationContext(), "녹음을 재생합니다.", Toast.LENGTH_SHORT).show();
            }
        });
        mediaPlayer.prepareAsync();
    }



    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    /*
    ---------- 지도 ----------
    */

    void initMap(){
        LatLng hansung = new LatLng(37.5817891, 127.008175);
        // move the camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(hansung));
    }

    private void getAndSetLocation(String locationName){
        if(!locationName.equals("")){
            try {
                Geocoder geocoder = new Geocoder(this, Locale.KOREA);
                List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
                Address bestResult = (Address) addresses.get(0);

                glocation = new LatLng(bestResult.getLatitude(), bestResult.getLongitude());
                googleMap.addMarker(
                        new MarkerOptions().position(glocation).title(locationName).alpha(0.8f).icon(BitmapDescriptorFactory.defaultMarker())
                );
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(glocation, 15));
            }
            catch (IOException e){
                Toast.makeText(getApplicationContext(), "Please enter a different name for the location.", Toast.LENGTH_SHORT).show();
            }
        }
        else initMap();

    }

    /*
    ---------- UI 세팅 ----------
    */

    public void setTaskDetail(){
        title.setText(task.taskName);
        date.setText(task.date);
        if(!task.startTime.equals(":")){
            starth.setText(task.startTime.split(":")[0]);
            startm.setText(task.startTime.split(":")[1]);
        }
        if(!task.endTime.equals(":")){
            endh.setText(task.endTime.split(":")[0]);
            endm.setText(task.endTime.split(":")[1]);
        }
        if(!task.place.equals("")){
            place.setText(task.place);
        }
        if(!task.textMemo.equals(""))        memo.setText(task.textMemo);

        if(task.image != null){
            mPhotoFileName = task.image;
            Log.d("ImageFileName: ", task.image);
            setImageView(task.image);
        }
        if(task.video != null){
            mVideoFileName = task.video;
            setVideoView(task.video);
        }
        if(task.audio != null){
            mRecordFileName = task.audio;
        }
    }


    public void setImageView(String imageFileName){
        if(imageFileName != ""){
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName);
            imageView.setImageURI(Uri.fromFile(mPhotoFile));
        }
    }

    public void setVideoView(String vedName){
        if(vedName !=""){
            mc = new MediaController(this);
            destination = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES),
                    vedName);
            videoView.setMediaController(mc);
            videoView.setVideoURI(Uri.fromFile(destination));


            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer player) {
                    videoView.seekTo(0);
                    videoView.start();
                }
            });
        }

    }

}

