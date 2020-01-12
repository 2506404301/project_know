package qu.page.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import qu.page.server.ItemModelService;

import java.util.Map;

@Controller
public class pageController {

    @Autowired
    private ItemModelService itemModelService;

    @GetMapping("page/{id}.html")
    public String itemModel(@PathVariable("id") Long id, Model model){
        Map<String, Object> map = itemModelService.queryAtts(id);
        model.addAllAttributes(map);
        return "item";
    }

}
