package qu_Item.user.service;



import com.qu.item.unity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import qu_Item.user.mapper.AdminMapper;

@Service
public class ControllerUserService {

    @Autowired
    private AdminMapper adminMapper;


    public String  login(Admin admin) {
        int i = adminMapper.selectCount(admin);
        if (i == 0){
            throw new quException(ExceptionEnum.ADMIN_CONTROLLER_NOT_FOUND);
        }
        return admin.getPassword();
    }
}
