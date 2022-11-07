package com.shivam.avikishayri.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.shivam.avikishayri.MainActivity;
import com.shivam.avikishayri.Models.ShairyData;
import com.shivam.avikishayri.R;
import com.shivam.avikishayri.databinding.HomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Home extends Fragment  {
    private static final int RESULT_OK =23 ;
    private static final int RESULT_CANCELED = 32;
    private MediaPlayer player;
     HomeBinding binding;
     ProgressBar progressBar;
     Boolean f;
     int i=0;
    private AppUpdateManager appUpdateManager;

    private static final int MY_REQUEST_CODE = 17326;
     private Fragmenthomelistener fragmenthomelistener;
    private List<ShairyData> list;
    private String mp3url;
    public interface Fragmenthomelistener{
        void Oninputsent(String s);
        void onplayerinstancesent(MediaPlayer mediaPlayer);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding=HomeBinding.inflate(inflater,container,false);
        progressBar=binding.progress;
        checkUpdate();
        player=new MediaPlayer();
        list=new ArrayList<>();
        f=true;
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        i=0;
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextClicked();
            }
        });  binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousClicked();
            }
        });  binding.playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playbutton();
            }
        });

        FirebaseDatabase.getInstance().getReference("shairy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                boolean a=false;
                for(DataSnapshot d:snapshot.getChildren()){
                   ShairyData data= d.getValue(ShairyData.class);
                    list.add(data);

                }
                Collections.reverse(list);
                setData();
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }

    private void setData() {
        ShairyData shairyData=list.get(0);
        binding.shair.setText(shairyData.getShair());
        binding.shairy1.setText(shairyData.getShairy());
        String s=shairyData.getShairy();
        fragmenthomelistener.Oninputsent(s);
        mp3url=shairyData.getAudio();
        binding.progress.setVisibility(View.INVISIBLE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }


    public void nextClicked() {
        binding.playbutton.setImageResource(R.drawable.playbutton);

        player.stop();
        player=new MediaPlayer();
        f=true;
        i++;
        if(i==list.size()){
            i=0;
        }
        ShairyData shairyData=list.get(i);
        binding.shair.setText(shairyData.getShair());
        binding.shairy1.setText(shairyData.getShairy());
        mp3url=shairyData.getAudio();
        fragmenthomelistener.Oninputsent(shairyData.getShairy());


    }

    public void playbutton() {
        fragmenthomelistener.onplayerinstancesent(player);
//        Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
        if(player.isPlaying()) {
            player.pause();
            binding.playbutton.setImageResource(R.drawable.playbutton);
            Toast.makeText(getActivity(), "Paused", Toast.LENGTH_SHORT).show();
        }
        else {
            if(f) {
                try {
                    Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_SHORT).show();
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    player.setDataSource(mp3url);
                    binding.progress.setVisibility(View.VISIBLE);

                    player.prepareAsync();

//                 if(player.isPlaying()) player.stop();
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            player.start();
                            binding.playbutton.setImageResource(R.drawable.pause);
                            binding.progress.setVisibility(View.INVISIBLE);
                        }
                    });
                    f=false;

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }else{
                player.start();
                binding.playbutton.setImageResource(R.drawable.pause);

            }
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                binding.playbutton.setImageResource(R.drawable.playbutton);
            }
        });
    }

    public void previousClicked() {
        binding.playbutton.setImageResource(R.drawable.playbutton);

//        Toast.makeText(getActivity(), getshayritext(), Toast.LENGTH_SHORT).show();
        player.stop();
        player=new MediaPlayer();
        f=true;
        i--;
        if(i==-1){
            i=list.size()-1;
        }
        ShairyData shairyData=list.get(i);
        binding.shair.setText(shairyData.getShair());
        binding.shairy1.setText(shairyData.getShairy());
        mp3url=shairyData.getAudio();
        fragmenthomelistener.Oninputsent(shairyData.getShairy());


    }
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        fragmenthomelistener=(Fragmenthomelistener) context;
    }
    private void checkUpdate() {



        InstallStateUpdatedListener listener = new InstallStateUpdatedListener() {

            @Override

            public void onStateUpdate(InstallState installState) {

                if (installState.installStatus() == InstallStatus.DOWNLOADED) {

                    Log.d("InstallDownloded", "InstallStatus sucsses");

                    notifyUser();

                }

            }

        };



        appUpdateManager = AppUpdateManagerFactory.create(getActivity().getApplicationContext());

        appUpdateManager.registerListener(listener);



        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {

            @Override

            public void onSuccess(AppUpdateInfo appUpdateInfo) {

                String data = "packageName :" + appUpdateInfo.packageName() + ", " +

                        "availableVersionCode :" + appUpdateInfo.availableVersionCode() + ", " +

                        "updateAvailability :" + appUpdateInfo.updateAvailability() + ", " +

                        "installStatus :" + appUpdateInfo.installStatus()+ ", ";



                Log.e("appUpdateInfo :", ""+data);

                Toast.makeText(getActivity().getApplicationContext(), ""+data, Toast.LENGTH_LONG).show();



                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE

                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                    requestUpdate(appUpdateInfo);

                    Log.d("UpdateAvailable", "update is there ");

                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {

                    Log.d("Update", "3");

                    notifyUser();

                } else {

                    Toast.makeText(getActivity().getApplicationContext(), "No Update Available", Toast.LENGTH_SHORT).show();

                    Log.e("NoUpdateAvailable", "update is not there ");

                }

            }

        });

    }



    private void requestUpdate(AppUpdateInfo appUpdateInfo) {

        try {

            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, getActivity(), MY_REQUEST_CODE);

            resume();

        } catch (IntentSender.SendIntentException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == MY_REQUEST_CODE) {

            switch (resultCode) {

                case Activity.RESULT_OK:

                    if (resultCode != RESULT_OK) {

                        Toast.makeText(getActivity(), "RESULT_OK" + resultCode, Toast.LENGTH_LONG).show();

                        Log.d("RESULT_OK  :", "" + resultCode);

                    }

                    break;

                case Activity.RESULT_CANCELED:



                    if (resultCode != RESULT_CANCELED) {

                        Toast.makeText(getActivity(), "RESULT_CANCELED" + resultCode, Toast.LENGTH_LONG).show();

                        Log.d("RESULT_CANCELED  :", "" + resultCode);

                    }

                    break;

                case ActivityResult.RESULT_IN_APP_UPDATE_FAILED:



                    /*if (resultCode != RESULT_IN_APP_UPDATE_FAILED){



                        Toast.makeText(this,"RESULT_IN_APP_UPDATE_FAILED" +resultCode, Toast.LENGTH_LONG).show();

                        Log.d("RESULT_IN_APP_FAILED:",""+resultCode);

                    }*/

            }

        }

    }



    @Override

    public void onDestroy() {

        super.onDestroy();

        try {

            appUpdateManager.unregisterListener((InstallStateUpdatedListener) this);

        }catch (RuntimeException e){



        }



    }



    private void notifyUser() {



        Snackbar snackbar =

                Snackbar.make(binding.getRoot(),

                        "An update has just been downloaded.",

                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("RESTART", new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                appUpdateManager.completeUpdate();

            }

        });

        snackbar.setActionTextColor(

                getResources().getColor(R.color.black));

        snackbar.show();

    }



    private void resume() {

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {

            @Override

            public void onSuccess(AppUpdateInfo appUpdateInfo) {

                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {

                    notifyUser();



                }



            }

        });

    }
}
