package dev.emmaguy.fruitninja.ui;

import dev.emmaguy.fruitninja.R;
import dev.emmaguy.fruitninja.ui.GameFragment.OnGameOver;
import dev.emmaguy.fruitninja.ui.MainMenuFragment.OnMainMenuButtonClicked;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity implements OnMainMenuButtonClicked, OnGameOver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_main);

	showMainMenu();
    }

    private void showMainMenu() {
	MainMenuFragment fragment = new MainMenuFragment();
	FragmentTransaction transaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
	transaction.replace(R.id.fragment_container, fragment, "MainMenu");
	transaction.addToBackStack("MainMenu");
	transaction.commit();
    }

    @Override
    public void onPlayButtonClicked() {
	GameFragment gameFragment = new GameFragment();

	FragmentTransaction transaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
	transaction.replace(R.id.fragment_container, gameFragment, "Game");
	transaction.addToBackStack("Game");
	transaction.commit();
    }

    @Override
    public void onGameOver(int score) {
	ResultsFragment resultsFragment = new ResultsFragment();
	resultsFragment.setScore(score);

	FragmentTransaction transaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
	transaction.replace(R.id.fragment_container, resultsFragment, "Results");
	transaction.commit();
    }
    
    @Override
    public void onBackPressed() {
	if (getSupportFragmentManager().findFragmentByTag("Results") != null) {
	    getSupportFragmentManager().popBackStack();
	    showMainMenu();
	} else {
	    super.onBackPressed();
	}
    }
}