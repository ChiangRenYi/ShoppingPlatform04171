package com.example.wmnl_yo.shoppingplatform.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmnl_yo.shoppingplatform.R;
import com.example.wmnl_yo.shoppingplatform.activity.MainActivity;
import com.example.wmnl_yo.shoppingplatform.database.DeleteCourse;
import com.example.wmnl_yo.shoppingplatform.database.GetParentChild;
import com.example.wmnl_yo.shoppingplatform.object.CourseRecordObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseRecordDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseRecordDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseRecordDetailFragment extends Fragment implements View.OnTouchListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static String CRDParentChild;

    public static CourseRecordObject.CourseRecordObjectItem courseRecordObject;

    public static String[] stringCRDParentChild;

    public static String deleteCheck;

    private ListView lvCourseDetail;

    private ArrayAdapter arrayAdapter;

    private String[] tmp;

    public CourseRecordDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseRecordDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseRecordDetailFragment newInstance(String param1, String param2) {
        CourseRecordDetailFragment fragment = new CourseRecordDetailFragment();
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

        courseRecordObject = (CourseRecordObject.CourseRecordObjectItem) getArguments().getSerializable("courseRecordDetail");

        tmp = new String[getContext().getResources().getStringArray(R.array.courseRecordDetail).length];

        tmp[0] = courseRecordObject.rNumber;
        tmp[1] = courseRecordObject.rCourseBuildingName;
        tmp[2] = courseRecordObject.rCourseName;
        tmp[3] = courseRecordObject.rCourseTeacher;
        tmp[4] = courseRecordObject.rCourseDate+courseRecordObject.rCourseWeek;
        tmp[5] = courseRecordObject.rCourseSTime+":"+courseRecordObject.rCourseSTime1+
                "~"+courseRecordObject.rCourseETime+":"+courseRecordObject.rCourseETime1;
        tmp[6] = courseRecordObject.rTeacherIntro;
        tmp[7] = courseRecordObject.rCourseContent;
        tmp[8] = "" + courseRecordObject.rTotal;
        tmp[9] = "" + courseRecordObject.rRemaining;
        tmp[10] = "" + courseRecordObject.rPrice;
        tmp[11] = "" + courseRecordObject.rPayment;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_course_record_detail, container, false);
        v.setOnTouchListener(this);

        lvCourseDetail = (ListView) v.findViewById(R.id.lCourse);

        arrayAdapter = new ArrayAdapter(getContext(), R.layout.custom_listview_order_detail) {
            @Override
            public int getCount() {
                return getContext().getResources().getStringArray(R.array.courseRecordDetail).length;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ViewHolder viewHolder = new ViewHolder();
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_listview_order_detail, null);
                    viewHolder.tvCourseDetail = (TextView) convertView.findViewById(R.id.textView);
                    viewHolder.tvCourseDetailContent = (TextView) convertView.findViewById(R.id.textView2);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.tvCourseDetail.setText(getContext().getResources().getStringArray(R.array.courseRecordDetail)[position]);
                if(position==6||position==7){
                    viewHolder.tvCourseDetailContent.setText(tmp[position]);
                    viewHolder.tvCourseDetailContent.setGravity(Gravity.LEFT);
                }
                else {
                    viewHolder.tvCourseDetailContent.setText(tmp[position]);
                }

                return convertView;
            }
        };
   //     Log.e("55125",courseObject.getmCourseTeacher());
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.custom_header_view, null);
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.custom_footer_view, null);
        final ImageView imgTeacherPhoto = (ImageView)headerView.findViewById(R.id.photo);

        new AsyncTask<String, Void, Bitmap>()
        {
            @Override
            protected Bitmap doInBackground(String... params)
            {
                String url = "http://163.13.128.77:8080/20171205-11v/ParentChildMuseum/CourseIntroductionRegistrations/CourseIntroductionRegistration/static/"+courseRecordObject.rPicture;
                return getBitmapFromURL(url);
            }

            @Override
            protected void onPostExecute(Bitmap result)
            {
                imgTeacherPhoto.setImageBitmap (result);
                super.onPostExecute(result);
            }
        }.execute("圖片連結網址路徑");
        Button btnCourseCancel = (Button)footerView.findViewById(R.id.btnOrderSearch);
        Log.e("55125",tmp[1]);
        btnCourseCancel.setText("取消上課");
        lvCourseDetail.setAdapter(arrayAdapter);
        lvCourseDetail.addHeaderView(headerView);
        lvCourseDetail.addFooterView(footerView);
        btnCourseCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetParentChild getParentChild = new GetParentChild();
                getParentChild.execute();
                Toast.makeText(getActivity(),"請稍後...", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                final Handler handler2 = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (stringCRDParentChild == null) {
                            Toast.makeText(getActivity(), "請以會員登入", Toast.LENGTH_SHORT).show();
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setItems(stringCRDParentChild, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            CRDParentChild = stringCRDParentChild[which];
                                            DeleteCourse deleteCourse = new DeleteCourse();
                                            deleteCourse.execute();

                                            handler2.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("55125-2",deleteCheck);

                                                    switch (deleteCheck)
                                                    {
                                                        case "delete" :
                                                            Toast.makeText(getActivity(),
                                                                    CRDParentChild + "已取消報名", Toast.LENGTH_SHORT).show();
                                                            ((MainActivity)getContext()).replaceFragment(CourseRecordFragment.class, null);
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                }
                                            },600);

                                        }
                                    }).show();
                        }
                    }
                }, 300);

            }
        });
        return v;
    }

    //photo
    private static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

    class ViewHolder {
        TextView tvCourseDetail, tvCourseDetailContent;
    }
}
