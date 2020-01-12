package qu_Item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import qu_Item.unity.User;

public interface UserAPi {

    @GetMapping("query")
    User queryUserByUserNameAndPassword(@RequestParam("username") String username,
                                        @RequestParam("password") String password);

}