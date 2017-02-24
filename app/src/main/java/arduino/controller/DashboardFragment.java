package arduino.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.ScrollView;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HumidityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HumidityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // All controls here
    private TextView mTxtReceive;
    private EditText mEditSend;
    private Button mBtnDisconnect;
    private Button mBtnSend;
    private Button mBtnClear;
    private Button mBtnClearInput;
    private ScrollView scrollView;
    private CheckBox chkScroll;
    private CheckBox chkReceiveText;
    private BtConnectionService localService;
    private boolean isBound = false;
    private   String strInput = "";

    public DashboardFragment() {
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
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mTxtReceive = (TextView) v.findViewById(R.id.txtReceive);
        mBtnClear = (Button) v.findViewById(R.id.btnClear);
        mEditSend = (EditText) v.findViewById(R.id.editSend);
        scrollView = (ScrollView) v.findViewById(R.id.viewScroll);
        mBtnClearInput = (Button) v.findViewById(R.id.btnClearInput);
        localService.setWStop(true);

        mBtnSend = (Button) v.findViewById(R.id.btnSend);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String input =mEditSend.getText().toString();
                    input = input.replace("x", "z");
                    input = input.replace("y", "k");
                    writeString(input);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        return v;

    }

    private void writeString(String s) throws InterruptedException {
        localService.setWStop(false);
        localService.writeString(s);
        Thread.sleep(500);
        strInput = localService.getString();
        if (!strInput.isEmpty()) {
            mTxtReceive.post(new Runnable() {
                @Override
                public void run() {
                    mTxtReceive.append("\n"+strInput);
                    scrollView.post(new Runnable() { // Snippet from http://stackoverflow.com/a/4612082/1287554
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }

            });

        }
        localService.setWStop(true);


    }

}
