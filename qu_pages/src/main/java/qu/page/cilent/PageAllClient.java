package qu.page.cilent;

import com.qu.item.api.AllApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("Item-service")
public interface PageAllClient extends AllApi {

}
