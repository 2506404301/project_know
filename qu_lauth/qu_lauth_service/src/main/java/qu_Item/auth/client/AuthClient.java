package qu_Item.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import qu_Item.api.UserAPi;


@FeignClient("user-service")
public interface AuthClient extends UserAPi {

}
