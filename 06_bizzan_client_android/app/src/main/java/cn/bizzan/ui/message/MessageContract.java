package cn.bizzan.ui.message;


import cn.bizzan.base.Contract;
import cn.bizzan.entity.Message;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface MessageContract {
    interface View extends Contract.BaseView<Presenter> {

        void messageSuccess(List<Message> obj);

        void messageFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void message(int pageNo, int pageSize);

    }
}
