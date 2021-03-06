package com.example.wmnl_yo.shoppingplatform.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wmnl_yo.shoppingplatform.R;
import com.example.wmnl_yo.shoppingplatform.activity.MainActivity;
import com.example.wmnl_yo.shoppingplatform.database.GetCourseRecordFragmentResult;
import com.example.wmnl_yo.shoppingplatform.object.CourseRecordObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseRecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseRecordFragment extends Fragment implements View.OnTouchListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    public static MyAdapter rAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseRecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseRecordFragment newInstance(String param1, String param2) {
        CourseRecordFragment fragment = new CourseRecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        GetCourseRecordFragmentResult getCourseRecordFragmentResult = new GetCourseRecordFragmentResult();
        getCourseRecordFragmentResult.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_course_record, container, false);
        v.setOnTouchListener(this);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv);

        rAdapter = new MyAdapter(CourseRecordObject.ITEMS);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(rAdapter);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
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

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<CourseRecordObject.CourseRecordObjectItem> mCourseRecordList;
        public class ViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout ll;
            public TextView tvCourseName, tvCourseTeacher, tvCourseDate, tvCourseTime;
            public CourseRecordObject.CourseRecordObjectItem mItem;


            public ViewHolder(View v) {
                super(v);
                ll = (LinearLayout) v.findViewById(R.id.ll);
                tvCourseName = (TextView) v.findViewById(R.id.courseName);
                tvCourseTeacher = (TextView) v.findViewById(R.id.courseTeacher);
                tvCourseDate = (TextView) v.findViewById(R.id.courseDate);
                tvCourseTime = (TextView) v.findViewById(R.id.courseTime);
            }
        }

        public MyAdapter(List<CourseRecordObject.CourseRecordObjectItem> courseRecordList) {
            mCourseRecordList = courseRecordList;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_recyclerview_course_query_result, parent, false);
            ViewHolder vh = new ViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("courseRecordDetail",mCourseRecordList.get(position));
                    CourseRecordDetailFragment fragobj = new CourseRecordDetailFragment();
                    fragobj.setArguments(bundle);
                    ((MainActivity) getContext()).replaceFragment(CourseRecordDetailFragment.class, fragobj);
                }
            });
            holder.mItem = mCourseRecordList.get(position);
            holder.tvCourseName.setText(mCourseRecordList.get(position).rCourseName);
            holder.tvCourseTeacher.setText(mCourseRecordList.get(position).rCourseTeacher);
            holder.tvCourseDate.setText(mCourseRecordList.get(position).rCourseDate+mCourseRecordList.get(position).rCourseWeek);
            holder.tvCourseTime.setText(mCourseRecordList.get(position).rCourseSTime+":"+mCourseRecordList.get(position).rCourseSTime1+
                    "~"+mCourseRecordList.get(position).rCourseETime+":"+mCourseRecordList.get(position).rCourseETime1);
        }

        @Override
        public int getItemCount() {
            return mCourseRecordList.size();
        }
    }

}
