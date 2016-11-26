package com.example.jiarou.sharelove;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;




/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment implements View.OnClickListener{
    private OpenGame mListener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button generate_btn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_game, container, false);


        generate_btn= (Button)  view.findViewById(R.id.general_btn);
        generate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OpenGame();



/**

                final ChanglleFragment changlleFragment =
                        ChanglleFragment.newInstance();
                        getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.challenge_root, changlleFragment, "changenge")
                        .addToBackStack(null)
                        .commit();

                ChanglleFragment changlleFragment= new ChanglleFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.challenge_root, changlleFragment, "changenge");
                transaction.addToBackStack(null);
                transaction.commit();
 **/
            }
        });


        /**
        generate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /**  getActivity().getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.c_root, new  ChanglleFragment())
                        .addToBackStack(null).commitAllowingStateLoss();
              /**  FragmentManager fragmentMgr = getFragmentManager();
                FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
                ChanglleFragment changlleFragment=  new ChanglleFragment();
                fragmentTrans.replace(Rid.c_root, changlleFragment, "changenge");
                fragmentTrans.commit();*/
/**
                switch (v.getId()) {
                    case R.id.general_btn:
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.c_root, new ChanglleFragment(), "changllenge");
                        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                }


            }
        });
        **/





        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OpenGame){
            mListener = (OpenGame)context;
        }else{
            throw new ClassCastException(context.toString() + "must implement OpenGame");
        }
    }

    public interface OpenGame{
        void OpenGame();
    }




    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
