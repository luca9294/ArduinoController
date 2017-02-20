package arduino.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TemperatureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemperatureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControllerFragment extends Fragment {

    private TextView tx;
    private BtConnectionService localService;

    public ControllerFragment() {
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
    public static ControllerFragment newInstance(String param1, String param2) {
        ControllerFragment fragment = new ControllerFragment();
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
        View v = inflater.inflate(R.layout.fragment_controller, container, false);
        ImageView upBtn = (ImageView)  v.findViewById(R.id.upBtn);
        upBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    localService.setWStop(false);
                    localService.writeString("u");
                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    localService.writeString("s");
                    localService.setWStop(true);
                    return true;
            }
                return true;
        }});

        ImageView downBtn = (ImageView)  v.findViewById(R.id.downBtn);
        downBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    localService.setWStop(false);
                    localService.writeString("d");
                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    localService.writeString("s");
                    localService.setWStop(true);
                    return true;
                }
                return true;
            }});

        ImageView rightBtn = (ImageView)  v.findViewById(R.id.rightBtn);
        rightBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    localService.setWStop(false);
                    localService.writeString("r");
                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    localService.writeString("s");
                    localService.setWStop(true);
                    return true;
                }
                return true;
            }});

        ImageView leftBtn = (ImageView)  v.findViewById(R.id.leftBtn);
        leftBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    localService.setWStop(false);
                    localService.writeString("l");
                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    localService.writeString("s");
                    localService.setWStop(true);
                    return true;
                }
                return true;
            }});








        return v;
    }





}
