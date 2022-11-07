package com.shivam.avikishayri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.shivam.avikishayri.Fragments.Home;
import com.shivam.avikishayri.Fragments.Zubair_fragment;
import com.shivam.avikishayri.Fragments.ali_fragment;
import com.shivam.avikishayri.Fragments.attitude_fragment;
import com.shivam.avikishayri.Fragments.broken_fragment;
import com.shivam.avikishayri.Fragments.hafi_fragment;
import com.shivam.avikishayri.Fragments.love_fragment;
import com.shivam.avikishayri.Fragments.varun_fragment;
import com.shivam.avikishayri.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;


import org.jetbrains.annotations.NotNull;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        Home.Fragmenthomelistener, hafi_fragment.Fragmenthafilistener, Zubair_fragment.FragmentZubairlistener
    , love_fragment.FragmentLovelistener, attitude_fragment.Fragmentattitudelistener,ali_fragment.Fragmentalilistener,
        broken_fragment.Fragmentbrokenlistener,varun_fragment.FragmentVarunlistener
{
    ActivityMainBinding binding;
    String shayritoshare="shairy";
    private int j=0;
    private MediaPlayer mediaPlayer;

    @Override
    public void onBackPressed() {
        if(binding.drawerlayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerlayout.closeDrawer(GravityCompat.START);
        }
        else
        super.onBackPressed();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mediaPlayer=new MediaPlayer();
        Toolbar toolbar=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,binding.drawerlayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        binding.drawerlayout
                .addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        assert binding.nav != null;
        binding.nav.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container,new Home()).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        if(mediaPlayer.isPlaying())mediaPlayer.stop();
        if(item.getItemId()==R.id.tabish){

           getSupportFragmentManager().beginTransaction().
                   replace(R.id.fragment_container,new Zubair_fragment()).commit();
        } if(item.getItemId()==R.id.homes){
           getSupportFragmentManager().beginTransaction().
                   replace(R.id.fragment_container,new Home()).commit();
        } if(item.getItemId()==R.id.hafi){
           getSupportFragmentManager().beginTransaction().
                   replace(R.id.fragment_container,new hafi_fragment()).commit();
        }if(item.getItemId()==R.id.varun){
           getSupportFragmentManager().beginTransaction().
                   replace(R.id.fragment_container,new varun_fragment()).commit();
        }if(item.getItemId()==R.id.love){
           getSupportFragmentManager().beginTransaction().
                   replace(R.id.fragment_container,new love_fragment()).commit();
        }if(item.getItemId()==R.id.broken){
           getSupportFragmentManager().beginTransaction().
                   replace(R.id.fragment_container,new broken_fragment()).commit();
        }if(item.getItemId()==R.id.attitude){
           getSupportFragmentManager().beginTransaction().
                   replace(R.id.fragment_container,new attitude_fragment()).commit();
        }if(item.getItemId()==R.id.ali){
           getSupportFragmentManager().beginTransaction().
                   replace(R.id.fragment_container,new ali_fragment()).commit();
        }
        binding.drawerlayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void Oninputsent(String s) {
        shayritoshare=s;
    }

    @Override
    public void onplayerinstancesent(MediaPlayer mediaPlayer) {
        this.mediaPlayer=mediaPlayer;

    }


    public void share(View view) {
        if(mediaPlayer.isPlaying())
        mediaPlayer.pause();

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        String shareBody = shayritoshare;
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, getString(R.string.share_using)));

    }
}