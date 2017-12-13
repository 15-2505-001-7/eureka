package com.example.v001ff.footmark;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Realm mRealm;

    public PostListFragment() {

    }

    public static PostListFragment newInstance(){
        PostListFragment fragment = new PostListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle args) {
        Bundle bundle = getArguments();                         //ShowSpotActivityでsetしたPlaceIdを取り出す.ShowSpotの62行目参照
        int pid = bundle.getInt("PIDkey");                      //pidにPlaceIdを格納
        View v = inflater.inflate(R.layout.fragment_post_list, container, false);       //vにfragment_post_listを代入.ここではrecyclerビューを張り付けてるだけ
        RecyclerView recyclerView = /*(RecyclerView)*/ v.findViewById(R.id.recycler);       //xmlに張り付けてるrecyclerビューを代入

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        RealmResults<FootmarkDataTable> query = mRealm.where(FootmarkDataTable.class).equalTo("PlaceId",pid).findAll();
        PostRealmAdapter adapter = new PostRealmAdapter(getActivity(), query, true);

        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onAddDiarySelected();
    }
}
