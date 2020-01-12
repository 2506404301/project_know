package qu_Item.user.web;

import com.qu.item.unity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qu_Item.user.service.ControllerUserService;

@RestController
@RequestMapping("controller")
public class ControllerUserController {

    @Autowired
    private ControllerUserService controllerUserService;

    /**
     * 管理员信息查询
     * @param admin
     * @return
     */
    @PostMapping("user")
    public ResponseEntity<String> controllerLogin(@RequestBody Admin admin){
        String login = controllerUserService.login(admin);
        return ResponseEntity.ok(login);
    }



}
