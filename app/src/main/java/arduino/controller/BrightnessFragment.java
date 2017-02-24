package arduino.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TemperatureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemperatureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrightnessFragment extends Fragment {

    private TextView tvBr;
    private BtConnectionService localService;
    private boolean isBound = false;

    public BrightnessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TemperatureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrightnessFragment newInstance(String param1, String param2) {
        BrightnessFragment fragment = new BrightnessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localService =  ((DrawerActivity)this.getActivity()).getLocalService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_brightness, container, false);
        localService.setWStop(false);
        localService.writeString("b");
        String st =  localService.getString();
        st = st.replace("/","");
        tvBr = (TextView) v.findViewById(R.id.brightness);
        tvBr.setText(st + " lm");
        localService.setWStop(true);


        if (Double.parseDouble(st) < 20){
            new AlertDialog.Builder(this.getActivity())
                    .setTitle("Brightness Low")
                    .setMessage("The brightness is lower than 10 lm.\nDo you want to turn on the LED?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            localService.setWStop(false);
                            localService.writeString("x");
                            localService.setWStop(true);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        else{
            localService.setWStop(false);
            localService.writeString("y");
            localService.setWStop(true);
        }


        return v;
    }




}
