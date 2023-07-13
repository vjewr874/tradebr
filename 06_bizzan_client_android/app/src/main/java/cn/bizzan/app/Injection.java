package cn.bizzan.app;

import android.content.Context;

import cn.bizzan.data.DataRepository;
import cn.bizzan.data.LocalDataSource;
import cn.bizzan.data.RemoteDataSource;


/**
 * Created by Administrator on 2017/9/25.
 */

public class Injection {
    public static DataRepository provideTasksRepository(Context context) {
        return DataRepository.getInstance(RemoteDataSource.getInstance(), LocalDataSource.getInstance(context));
    }
}
