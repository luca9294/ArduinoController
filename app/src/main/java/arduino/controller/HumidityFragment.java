package arduino.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HumidityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HumidityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HumidityFragment extends Fragment {

    private TextView tvHum;
    private BtConnectionService localService;
    private boolean isBound = false;

    public HumidityFragment() {
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
    public static HumidityFragment newInstance(String param1, String param2) {
        HumidityFragment fragment = new HumidityFragment();
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
        View v = inflater.inflate(R.layout.fragment_humidity, container, false);
        localService.setWStop(false);
        localService.writeString("h");
        String st =  localService.getString();
        st = st.replace("/","");
        tvHum = (TextView) v.findViewById(R.id.humidity);
        tvHum.setText(st + "%");
        localService.setWStop(true);
        return v;
    }






}
