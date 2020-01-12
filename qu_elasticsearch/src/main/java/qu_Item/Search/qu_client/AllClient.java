package qu_Item.Search.qu_client;

import com.qu.item.api.AllApi;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("Item-service")
public interface AllClient extends AllApi{

}
