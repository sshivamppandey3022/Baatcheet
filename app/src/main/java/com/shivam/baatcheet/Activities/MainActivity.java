package com.shivam.baatcheet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.shivam.baatcheet.Adapters.FragmentAdapter;
import com.shivam.baatcheet.R;
import com.shivam.baatcheet.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        mainBinding.pager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        mainBinding.tabLayout.setupWithViewPager(mainBinding.pager);

        auth = FirebaseAuth.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.setting:
                Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent1);
                break;

                case R.id.groupChat:
                    Intent intent2 = new Intent(MainActivity.this, GroupChatActivity.class);
                    startActivity(intent2);
                break;

                case R.id.logOut:
                auth.signOut();
                Intent intent3 = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent3);
                break;
        }
        return true;
    }
}
