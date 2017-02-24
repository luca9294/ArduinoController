package arduino.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommandsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommandsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommandsFragment extends Fragment {

    private EditText edTemp;
    private EditText etBright;
    private EditText etHum;
    private EditText etUp;
    private EditText etDown;
    private EditText etLeft;
    private EditText etRight;

    public CommandsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommandsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommandsFragment newInstance(String param1, String param2) {
        CommandsFragment fragment = new CommandsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_commands, container, false);
         edTemp   = (EditText) v.findViewById(R.id.etTemp);
         etBright = (EditText) v.findViewById(R.id.etBright);
         etHum    = (EditText) v.findViewById(R.id.etHum);
         etUp     = (EditText) v.findViewById(R.id.etUp);
         etDown   = (EditText) v.findViewById(R.id.etDown);
         etLeft   = (EditText) v.findViewById(R.id.etLeft);
         etRight  = (EditText) v.findViewById(R.id.etRight);

        SharedPreferences prefs = getActivity().getSharedPreferences("Commands", 0);
        if (prefs == null)
          initializeDefaultValues(v);
        else
            initializeSavedValues(v);

        Button saveBtn = (Button) v.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateValues(v);
            }
        });

        // Inflate the layout for this fragment
        return v;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //Sets the values in the SharedPreferences
    private void initializeDefaultValues(View v){
        edTemp.setText("t");
        etBright.setText("b");
        etHum.setText("h");
        etUp.setText("u");
        etDown.setText("d");
        etLeft.setText("l");
        etRight.setText("r");

        SharedPreferences prefs = getActivity().getSharedPreferences("Commands", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("edTemp",   "t");
        editor.putString("etBright", "b");
        editor.putString("etHum",    "h");
        editor.putString("etUp",     "u");
        editor.putString("etDown",   "d");
        editor.putString("etLeft",   "l");
        editor.putString("etRight",  "r");
        editor.commit();
    }

    //Sets the values saved in the SharedPreferences
    private void initializeSavedValues(View v){
        SharedPreferences prefs = getActivity().getSharedPreferences("Commands", 0);
        edTemp .setText(prefs.getString("edTemp","empty"));
        etBright.setText(prefs.getString("etBright","empty"));
        etHum.setText(prefs.getString("etHum","empty"));
        etUp.setText(prefs.getString("etUp","empty"));
        etDown.setText(prefs.getString("etDown","empty"));
        etLeft.setText(prefs.getString("etLeft","empty"));
        etRight.setText(prefs.getString("etRight","empty"));
    }


    //Saves the values in the SharedPreferences
    private void updateValues(View v){
        SharedPreferences prefs = getActivity().getSharedPreferences("Commands", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("edTemp", edTemp.getText().toString());
        editor.putString("etBright", etBright.getText().toString());
        editor.putString("etHum", etHum.getText().toString());
        editor.putString("etUp", etUp.getText().toString());
        editor.putString("etDown", etDown.getText().toString());
        editor.putString("etLeft", etLeft.getText().toString());
        editor.putString("etRight", etRight.getText().toString());
        editor.commit();
    }
}
