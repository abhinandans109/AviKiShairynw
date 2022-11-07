package com.shivam.avikishayri.Fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shivam.avikishayri.Models.ShairyData;
import com.shivam.avikishayri.R;
import com.shivam.avikishayri.databinding.BrokenFragmentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class broken_fragment extends Fragment  {
    private MediaPlayer player;
    BrokenFragmentBinding binding;
    ProgressBar progressBar;
    Boolean f;
    int i=0;
    private Fragmentbrokenlistener Fragmentbrokenlistener;
    private List<ShairyData> list=new ArrayList<>();
    private String mp3url;
    public interface Fragmentbrokenlistener{
        void Oninputsent(String s);
        void onplayerinstancesent(MediaPlayer mediaPlayer);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding=BrokenFragmentBinding.inflate(inflater,container,false);
        progressBar=binding.progress;
        player=new MediaPlayer();

        f=true;
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                Toast.makeText(getActivity(), "Loading Please Wait..", Toast.LENGTH_SHORT).show();
        Uri uri=null;
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

        FirebaseDatabase.getInstance().getReference("Broken").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                boolean a=false;
                for(DataSnapshot d:snapshot.getChildren()){
                    ShairyData data= d.getValue(ShairyData.class);
                    list.add(data);
                    if(!a) {
                        setData();
//                       progressBar.setVisibility(View.GONE);
                        a=true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }

    private void setData() {
        ShairyData shairyData=list.get(0);
//        Toast.makeText(this, shairyData.getShair(), Toast.LENGTH_SHORT).show();
        binding.shair.setText(shairyData.getShair());
        binding.shairy1.setText(shairyData.getShairy());
        String s=shairyData.getShairy();
        Fragmentbrokenlistener.Oninputsent(s);
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
        Fragmentbrokenlistener.Oninputsent(shairyData.getShairy());


    }

    public void playbutton() {
        Fragmentbrokenlistener.onplayerinstancesent(player);
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
        Fragmentbrokenlistener.Oninputsent(shairyData.getShairy());


    }
    public void getshayritext(){
        Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        Fragmentbrokenlistener=(Fragmentbrokenlistener) context;
    }
}
