package dev.emmaguy.fruitninja.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import dev.emmaguy.fruitninja.R;

public class ResultsFragment extends Fragment implements View.OnClickListener {

    private int score;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View v = inflater.inflate(R.layout.fragment_results, null);
	v.findViewById(R.id.button_home).setOnClickListener(this);
	return v;
    }

    @Override
    public void onClick(View view) {
	if (view.getId() == R.id.button_home) {
	    getActivity().onBackPressed();
	}
    }
    
    @Override 
    public void onResume(){
	super.onResume();
	
	TextView view = (TextView) getView().findViewById(R.id.textview_results);
	view.setText("Score: " + score);
    }

    public void setScore(int score) {
	this.score = score;
    }
}