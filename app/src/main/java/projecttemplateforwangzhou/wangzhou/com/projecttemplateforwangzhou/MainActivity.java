package projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou;

import android.content.Context;
import android.os.Bundle;

import com.necer.ncalendar.calendar.MonthCalendar;

import butterknife.BindView;
import projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou.core.base.BKMVPActivity;
import uk.co.senab.photoview.PhotoView;

public class MainActivity extends BKMVPActivity<TestPerstener> {


    @BindView(R.id.iv)
    PhotoView imageView;

    @BindView(R.id.Month)
    MonthCalendar Month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public TestPerstener initPresenter(Context context) {
        return new TestPerstener(context);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
//        getPresenter().getCode("15827707520");
//        GlideUtil.DisplayImagesLoad(this, "http://58.250.30.13:8086/fileUpload/StolenWarning/201901/2019012411301257-905.jpg", imageView);



        
    }

}




