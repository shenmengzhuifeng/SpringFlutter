package tw.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tw.bean.CommonResp;
import tw.utils.Constants;
import tw.utils.UnifyApiUri;

/**
 * 用户相关controller
 *
 * @author
 * @create 2019-10-20 7:50 PM
 **/
@RestController
@RequestMapping(UnifyApiUri.UserApi.API_CUSTOMER_BASE)
public class UserController {

    @RequestMapping(value = UnifyApiUri.UserApi.API_CUSTOMER_INFO,method = RequestMethod.GET)
    public CommonResp<String> getCustomerInfo() {
        return new CommonResp<>(Constants.RESULT_OK,"","","获取成功");
    }
}
